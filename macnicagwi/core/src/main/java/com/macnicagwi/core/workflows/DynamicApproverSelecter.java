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
import java.util.function.Function;

import static com.macnicagwi.core.utils.WorkFlowUtils.fun;
import static com.macnicagwi.core.workflows.WorkFlowProcess.KEY_APPROVE_LIST;

@Component(service = ParticipantStepChooser.class, property = {"chooser.label=" + "Dynamic Approver Selector"})
public class DynamicApproverSelecter implements ParticipantStepChooser {

    @Reference
    MacnicaGroupUsers macnicaGroupUsers;

    @Reference
    EmailService emailService;

    @Reference
    WorkFlowConfigService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        logger.trace("--------Inside DynamicApproverSelecter -------------- ");
        //store the approver list
        List<String> approverValList = new ArrayList<>();
        //store the paths
        List<String> contentPathList = new ArrayList<>();
        String approver = null;
        logger.trace("-- Reading  getMetaDataMap --");
        Map<String, String> approverMap = (Map<String, String>) workItem.getWorkflow().getMetaDataMap().get(KEY_APPROVE_LIST);
        approverMap.forEach((k, v) -> {
            approverValList.add(v);
            contentPathList.add(k);
            logger.trace("Approver Map Key : {} , Value : {}", k, v);
        });

        approverValList.forEach(it -> logger.trace("approver item = {}", it));
        contentPathList.forEach(it -> logger.trace("contentPath item = {}", it));

        if (!approverValList.isEmpty()) {
            logger.trace("-- approverValList isn't empty -- ");
            approver = approverValList.stream().filter(StringUtils::isNotBlank).findFirst().get();
            logger.trace("-- finding first  approver --> {}", approver);
            List<String> contentRootPaths = WorkFlowUtils.getPreviewPath(workItem.getWorkflowData().getMetaDataMap());
            sendEmail(approver, workItem.getWorkflow().getInitiator(), contentRootPaths);
        }
        return approver;
    }

    public void sendEmail(String groupId, String workflowInitiator, List<String> payloadDetails) {
        try {
            logger.trace("-- Inside SEND MAIL -------------");
            List<Value> usersEmailList = macnicaGroupUsers.getUserDetails(groupId);
            logger.trace(" usersEmailList size = {} ", usersEmailList != null ? usersEmailList.size() : "Empty");
            if (usersEmailList != null) {
                usersEmailList.forEach(it -> logger.trace("email = {}", it.toString()));
            }
            String subject = "Request to review and approve";
            String content = "This email is to notify you that the user " + workflowInitiator
                    + " has asked you to review and approve an article." + System.lineSeparator() +
                    "Access the file: ";
            StringBuffer joiner = new StringBuffer();
            joiner.append(content);
            for (String path : payloadDetails) {
                joiner.append(service.getPreviewURL()).append(fun.apply(path)).append(System.lineSeparator());
            }

            logger.trace("Content : {}", joiner);
            logger.trace("Sending  Mail Notification to {}", usersEmailList);
            List<Value> iniatorEmailList = macnicaGroupUsers.getUserEmail(workflowInitiator);
            emailService.sendEmail(usersEmailList, iniatorEmailList,
                    "", subject, joiner.toString());
            logger.trace("{}: workflow status email is sent", usersEmailList);
            logger.trace("-- END SEND MAIL BLOCK----");
        } catch (Exception ex) {
            logger.error("ex", ex);
            ex.printStackTrace();
        }
    }
}
