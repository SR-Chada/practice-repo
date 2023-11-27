package com.macnicagwi.core.workflows;

import com.adobe.granite.workflow.collection.ResourceCollection;
import com.adobe.granite.workflow.collection.ResourceCollectionManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.workflow.process.ResourceCollectionHelper;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.macnicagwi.core.config.WorkFlowCaConfig;
import com.macnicagwi.core.utils.MacnicaCoreUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;
import static com.macnicagwi.core.utils.WorkFlowUtils.getHQGroup;

@Component(service = WorkflowProcess.class, property = {"chooser.label=" + "Find Approver list"})
public class WorkFlowProcess implements WorkflowProcess {
    public static final String KEY_APPROVE_LIST = "approverList";
    public static final String KEY_AUTHOR_LIST = "authorList";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private ResourceCollectionManager rcManager;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        try {
            logger.error("--- Work flow triggerd -------");
            Session session = workflowSession.getSession();
            WorkflowData data = workItem.getWorkflowData();
            String path = null;
            String type = data.getPayloadType();
            logger.trace("--- type = {} -------", type);
            if (type.equals("JCR_PATH") && data.getPayload() != null) {
                logger.trace("inside JCR PATH");
                String payloadData = (String) data.getPayload();
                if (session.itemExists(payloadData)) {
                    path = payloadData;
                }
            } else if (data.getPayload() != null && type.equals("JCR_UUID")) {
                logger.trace("inside JCR_UUID");
                Node node = session.getNodeByIdentifier((String) data.getPayload());
                path = node.getPath();
            }
            logger.trace("---payload = {} -------", path);
            if (path != null) {
                Map<String, String> approverMap;
                String hq = null;
                ResourceResolver resolver = this.resolverFactory.getResourceResolver(Collections.singletonMap("user.jcr.session", session));
                Resource res = resolver.getResource(path);
                if (res.adaptTo(Page.class) != null) {
                    Page page = res.adaptTo(Page.class);
                    if (page.getContentResource().isResourceType("cq/workflow/components/collection/page")) {
                        logger.trace("---isResourceType matched = {} -------", true);
                        List<ResourceCollection> rcCollections = this.rcManager.getCollectionsForNode((Node) session.getItem(path));
                        List<String> paths = ResourceCollectionHelper.getPaths(path, rcCollections);
                        //paths.forEach(it -> logger.trace("path = {}", it));
                        approverMap = getApproverList(paths);
                        hq = getHQGroup(paths, resolverFactory);
                    } else {
                        logger.error("---isResourceType not matched = {} -------", false);
                        approverMap = getApproverList(Collections.singletonList(path));
                        hq = getHQGroup(Collections.singletonList(path), resolverFactory);
                    }
                    approverMap.forEach((k, v) -> logger.trace("Approve List Key : {} , Value : {}", k, v));
                    workItem.getWorkflow().getMetaDataMap().put(KEY_APPROVE_LIST, approverMap);
                    workItem.getWorkflow().getMetaDataMap().put(KEY_AUTHOR_LIST, hq);
                } else {
                    if (path.contains("/content/dam/macnicagwi/") || path.contains("/content/cq:tags/")) {
                        approverMap = getApproverList(Collections.singletonList(path));
                        approverMap.forEach((k, v) -> logger.trace("Approve List Key : {} , Value : {}", k, v));
                        workItem.getWorkflow().getMetaDataMap().put(KEY_APPROVE_LIST, approverMap);
                        hq = getHQGroup(Collections.singletonList(path), resolverFactory);
                        workItem.getWorkflow().getMetaDataMap().put(KEY_AUTHOR_LIST, hq);
                    }
                    else {
                        logger.error("--- Page is empty -----");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
            e.printStackTrace();
        }
    }

    /**
     * To get the approver group
     *
     * @param paths    content paths
     * @param resolver resolver
     * @return approver groups
     */
    private Map<String, String> getApproverList(List<String> paths) {
        Map<String, String> aprvrMap = new HashMap<>();
        logger.trace("Inside getApproverList and paths as an Argument= {}", paths);
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resolverFactory,
                MACNICA_SUB_SERVICE);
        logger.trace("resolverFactory = {} , serviceResolver = {}", resolverFactory,serviceResolver);
        for (String path : paths) {
            Resource contentResource = serviceResolver.getResource(path);
            logger.trace("contentResource = {} , path = {}", contentResource,path);
            if(contentResource != null) {
                WorkFlowCaConfig config = contentResource.adaptTo(ConfigurationBuilder.class).as(WorkFlowCaConfig.class);
                logger.trace("config => {}", config.approverGroup());
                aprvrMap.put(path, config.approverGroup());
            }
        }
        return aprvrMap;
    }

}
