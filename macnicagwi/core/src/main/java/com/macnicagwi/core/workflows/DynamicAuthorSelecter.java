package com.macnicagwi.core.workflows;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.macnicagwi.core.services.EmailService;
import com.macnicagwi.core.services.MacnicaGroupUsers;
import com.macnicagwi.core.services.WorkFlowConfigService;
import com.macnicagwi.core.utils.WorkFlowUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.macnicagwi.core.utils.WorkFlowUtils.fun;
import static com.macnicagwi.core.workflows.WorkFlowProcess.KEY_APPROVE_LIST;
import static com.macnicagwi.core.workflows.WorkFlowProcess.KEY_AUTHOR_LIST;

@Component(service = ParticipantStepChooser.class, property = { "chooser.label=" + "Dynamic Author Selector" })
public class DynamicAuthorSelecter implements ParticipantStepChooser {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Reference
        MacnicaGroupUsers macnicaGroupUsers;

        @Reference
        EmailService emailService;

    @Reference
    WorkFlowConfigService workFlowConfigService;

        public void sendEmail(String initiator,String authorList, List<String> payLoad, String mailType,String comment) {
                try {
                        logger.trace("-- Inside DynamicAuthorSelecter sendEmail -- ");
                        List<Value> usersEmailList = macnicaGroupUsers.getUserEmail(initiator);
                        List<Value> authorsEmailList = macnicaGroupUsers.getUserDetails(authorList);
                        logger.trace(" usersEmailList size = {} ", usersEmailList != null ? usersEmailList.size() : "Empty");
                        if (usersEmailList != null) {
                                usersEmailList.forEach(it -> logger.trace("email = {}", it.toString()));
                        }
                        boolean isPublish = mailType.equalsIgnoreCase("publish");
                        String publishConfirmationContent = "This email is to notify you that your request has been approved.";
                        String requestRejectionContent = "This email is to notify you that your request has been rejected.";
                        StringBuffer joiner = new StringBuffer();
                        joiner.append(isPublish ? publishConfirmationContent : requestRejectionContent);
                        joiner.append("\r\n").append("Access the file: ");
                        for (String path : payLoad) {
                            joiner.append(workFlowConfigService.getPreviewURL()).append(fun.apply(path)).append("\r\n");
                        }
                        if(comment!=null&&!comment.isEmpty()) {
                            joiner.append("Comments : ").append(comment);
                        }
                        logger.trace("Content : {}", joiner);
                        logger.trace("Sending  Mail Notification to {}", usersEmailList);
                        emailService.sendEmail(usersEmailList, authorsEmailList,
                                "",
                                isPublish ? "Your request has been approved" : "Your request has been rejected",
                                joiner.toString());
                        logger.trace(" Approval/Rejection email is sent");
                } catch (Exception exception) {
                        exception.printStackTrace();
                }

        }

        @Override
        public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
                String workflowInitiator = workItem.getWorkflow().getInitiator();
                logger.trace("-- inside DynamicAuthorSelecter & Author = {} -- ", workflowInitiator);
                //store the paths
                List<String> contentPathList = new ArrayList<>();
                logger.trace("-- Reading  getMetaDataMap --");
                Map<String, String> map = (Map<String, String>) workItem.getWorkflow().getMetaDataMap().get(KEY_APPROVE_LIST);
                String authorList =workItem.getWorkflow().getMetaDataMap().get(KEY_AUTHOR_LIST).toString();
                String comment = workItem.getMetaDataMap().get("comment").toString();
                String processArgs = metaDataMap.get("PROCESS_ARGS", "");
                logger.trace("-- PROCESS_ARGS => {} --", processArgs);
                map.forEach((k, v) -> {
                        contentPathList.add(k);
                        logger.trace("Map Key : {} , Value : {}", k, v);
                });
                contentPathList.forEach(it -> logger.trace("contentPath item = {}", it));
                List<String> contentRootPaths = WorkFlowUtils.getPreviewPath(workItem.getWorkflowData().getMetaDataMap());
                sendEmail(workflowInitiator,authorList, contentRootPaths, processArgs,comment);
                return workflowInitiator;

        }

}
