package com.macnicagwi.core.workflows;

import com.adobe.granite.workflow.collection.ResourceCollectionManager;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.google.gson.Gson;
import com.macnicagwi.core.components.dtos.ContentRootPathDto;
import com.macnicagwi.core.services.ReplicationService;
import com.macnicagwi.core.utils.MacnicaCoreUtils;
import com.macnicagwi.core.utils.ResourceVisitor;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;

@Component(service = WorkflowProcess.class, property = {"chooser.label=" + "Previewing Content"})
public class PreviewProcess implements WorkflowProcess, Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private ResourceCollectionManager rcManager;
    @Reference
    Scheduler scheduler;
    @Reference
    ReplicationService replicationService;
    List<String> paths;
    List<String> childPages;
    boolean isPublish;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        try{
            ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resolverFactory,
                    MACNICA_SUB_SERVICE);
            boolean includeChildren = Boolean.parseBoolean(workItem.getWorkflowData().getMetaDataMap()
                    .get("includeChildren").toString());
            Object obj = workItem.getWorkflowData().getMetaDataMap().get("contentRootPaths");
            ContentRootPathDto ex = new Gson().fromJson(obj.toString(),ContentRootPathDto.class);
            paths = ex.getContentRootPaths();
            if(includeChildren) {
                childPages = new ArrayList<>();
                paths.forEach(it -> {
                    ResourceVisitor resourceVisitor = new ResourceVisitor();
                    resourceVisitor.accept(serviceResolver.getResource(it));
                    childPages.addAll(resourceVisitor.getChildPages().stream().distinct().collect(Collectors.toList()));
                });
                paths.addAll(childPages.stream().distinct().collect(Collectors.toList()));
            }
            logger.trace("paths = {}",ex.getContentRootPaths());
            String absoluteTime = workItem.getWorkflowData().getMetaDataMap().get("absoluteTime").toString();
            logger.trace("absoluteTime = {}",absoluteTime);
            String processArgs = metaDataMap.get("PROCESS_ARGS", "");
            logger.trace("processArgs = {}",processArgs);
            isPublish = processArgs.equalsIgnoreCase("publish");
            scheduleReplication(Long.parseLong(absoluteTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scheduleReplication(long absoluteTime) {
        Date absDate = new Date(absoluteTime);
        logger.trace("Replication Date  = {}",absDate);
        ScheduleOptions scheduleOptions;
       // if (WorkFlowUtils.getTmeDifference(absDate, new Date()) > 2) {
            // scheduleOptions = scheduler.AT(DateUtils.addMinutes(absDate, 1));
            scheduleOptions = scheduler.AT(absDate);
//        } else {
//            scheduleOptions = scheduler.NOW();
//        }
        scheduleOptions.name("Macnicagwi Preview Replication Process ");
        scheduleOptions.canRunConcurrently(true);
        // Scheduling the job
        scheduler.schedule(this, scheduleOptions);
        logger.trace("----------- Job scheduled ----");
        //  paths = metaDataMap.get("contentRootPaths", List.class);
    }

    @Override
    public void run() {
        logger.trace("replicateContent   = {} & isPublish = {}",paths,isPublish);
        replicationService.replicateContent(paths.stream().distinct().collect(Collectors.toList()),isPublish);
    }
}
