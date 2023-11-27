package com.macnicagwi.core.workflows;

import com.adobe.granite.workflow.collection.ResourceCollection;
import com.adobe.granite.workflow.collection.ResourceCollectionManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.workflow.process.ResourceCollectionHelper;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.macnicagwi.core.services.EmailService;
import com.macnicagwi.core.services.MacnicaGroupUsers;
import com.macnicagwi.core.services.MacnicaWorkflows;
import com.macnicagwi.core.services.WorkFlowConfigService;
import com.macnicagwi.core.utils.WorkFlowUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Value;
import java.util.*;

import static com.macnicagwi.core.utils.WorkFlowUtils.*;

@Component(service = WorkflowProcess.class, property = {"chooser.label=" + "Publish confirmation"})
public class PublishConfirmationSender implements WorkflowProcess {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isActivate = true;

    @Reference
    MacnicaGroupUsers macnicaGroupUsers;

    @Reference
    MacnicaWorkflows macnicaWorkflows;

    @Reference
    EmailService emailService;

    @Reference
    WorkFlowConfigService service;
    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private ResourceCollectionManager rcManager;

    public void sendMail(List<Value> emailList,List<Value> ccEmailList, String content) {
        String emailSubject = "Your request has been approved and ";
        emailSubject += isActivate ? "published":"unpublished";
        try {
            logger.trace("Sending  Mail Notification to {}", emailList);
            emailService.sendEmail(emailList, ccEmailList,
                    "", emailSubject, content);
            logger.trace(" Confirmation email is sent to approver group");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendEmail(String initiator, String approverGroup, List<String> payLoadList, String hq) {
        logger.trace("-- Inside  PublishConfirmationSender sendEmail -- ");
        List<Value> usersEmailList = macnicaGroupUsers.getUserEmail(initiator);
        List<Value> approverEmailList = macnicaGroupUsers.getUserDetails(approverGroup);
        List<Value> hqList = macnicaGroupUsers.getUserDetails(hq);
        logger.trace(" usersEmailList size = {} ", usersEmailList != null ? usersEmailList.size() : "Empty");
        String publishConfirmationContent = "This email is to notify you that your request has been approved and ";
        publishConfirmationContent += isActivate ? "published.":"unpublished.";
        publishConfirmationContent += System.lineSeparator() + "Access the file: ";
        StringBuffer joiner = new StringBuffer();
        joiner.append(publishConfirmationContent);
        for (String path : payLoadList) {
            logger.trace("-- mail payload = {}-- ", path);
            joiner.append(service.getPublishURL()).append(fun.apply(path)).append(System.lineSeparator());
        }
        List<Value> ccEmailList = new ArrayList<>();
        if (usersEmailList != null) {
            usersEmailList.forEach(it -> logger.trace("email = {}", it.toString()));
        }
        if (approverEmailList != null) {
            approverEmailList.forEach(it -> {
                logger.trace("approverEmail = {}", it.toString());
                ccEmailList.add(it);
            });
        }
        if (hqList != null) {
            hqList.removeIf(it -> it.toString().equalsIgnoreCase(initiator));
            hqList.forEach(it -> {
                logger.trace("hqList = {}", it.toString());
                ccEmailList.add(it);
            });
        }
        sendMail(usersEmailList,ccEmailList, joiner.toString());
    }

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
        String workflowInitiator = workItem.getWorkflow().getInitiator();
        Map<String, String> approverMap = new HashMap<>();
        List<String> approverValList = new ArrayList<>();
        List<String> contentPathList = new ArrayList<>();
        String hq = null;
        logger.trace(" -- Inside PublishConfirmationSender , Author{}", workflowInitiator);
        try {
            Session session = workflowSession.getSession();
            WorkflowData data = workItem.getWorkflowData();
            String path = null;
            String type = data.getPayloadType();
            logger.error("--- type = {} -------", type);
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

            if (path != null) {
                ResourceResolver resolver = this.resolverFactory.getResourceResolver(Collections.singletonMap("user.jcr.session", session));
                Resource res = resolver.getResource(path);
                if (res.adaptTo(Page.class) != null) {
                    Page page = res.adaptTo(Page.class);
                    if (page.getContentResource().isResourceType("cq/workflow/components/collection/page")) {
                        logger.trace("---isResourceType matched = {} -------", true);
                        List<ResourceCollection> rcCollections = this.rcManager.getCollectionsForNode((Node) session.getItem(path));
                        List<String> paths = ResourceCollectionHelper.getPaths(path, rcCollections);
                        //paths.forEach(it -> logger.trace("path = {}", it));
                        approverMap = getApproverList(paths, resolverFactory);
                        hq = getHQGroup(paths, resolverFactory);
                    } else {
                        logger.error("---isResourceType not matched = {} -------", false);
                        approverMap = getApproverList(Collections.singletonList(path), resolverFactory);
                        hq = getHQGroup(Collections.singletonList(path), resolverFactory);
                    }
                    approverMap.forEach((k, v) -> logger.trace("Approve List Key : {} , Value : {}", k, v));
                } else {
                    if (path.contains("/content/dam/macnicagwi/") || path.contains("/content/cq:tags/"))  {
                        approverMap = getApproverList(Collections.singletonList(path), resolverFactory);
                        approverMap.forEach((k, v) -> logger.trace("Approve List Key : {} , Value : {}", k, v));
                    } else {
                        logger.error("--- Page is empty -----");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        approverMap.forEach((k, v) -> {
            approverValList.add(v);
            contentPathList.add(k);
            logger.trace("Map Key : {} , Value : {}", k, v);
        });
        String actionType = args.get("PROCESS_ARGS", "");
        if(actionType != null) {
            isActivate = !actionType.equalsIgnoreCase(ReplicationActionType.DEACTIVATE.toString());
        }
        approverValList.forEach(it -> logger.trace("approver item = {}", it));
        contentPathList.forEach(it -> logger.trace("contentPath item = {}", it));

        if (!approverValList.isEmpty()) {
            logger.trace("-- approverValList isn't empty -- ");
            String approver = approverValList.stream().filter(StringUtils::isNotBlank).findFirst().get();
            logger.trace("-- finding first  approver --> {}", approver);
            List<String> contentRootPaths = WorkFlowUtils.getPreviewPath(workItem.getWorkflowData().getMetaDataMap());
            sendEmail(workflowInitiator, approver, contentRootPaths, hq);
        }

    }
}