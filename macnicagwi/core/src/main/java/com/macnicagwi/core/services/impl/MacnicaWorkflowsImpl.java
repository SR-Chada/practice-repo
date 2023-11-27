package com.macnicagwi.core.services.impl;

import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.Workflow;
import com.macnicagwi.core.services.EmailService;
import com.macnicagwi.core.services.MacnicaGroupUsers;
import com.macnicagwi.core.services.MacnicaWorkflows;
import com.macnicagwi.core.services.WorkFlowConfigService;
import com.macnicagwi.core.utils.MacnicaCoreUtils;
import com.macnicagwi.core.utils.WorkFlowUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.Value;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;
import static com.macnicagwi.core.utils.WorkFlowUtils.fun;

@Component(service = MacnicaWorkflows.class, immediate = true)
@ServiceDescription("Macnica Workflow Service")

public class MacnicaWorkflowsImpl implements MacnicaWorkflows {

    private static final String TAG = MacnicaWorkflowsImpl.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaWorkflowsImpl.class);

    private static final String activationWorkflowName = "request_for_activation1";
    private static final String deactivationWorkflowName = "request_for_deactivation1";

    @Reference
    WorkflowService workflowService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    MacnicaGroupUsers macnicaGroupUsers;

    @Reference
    EmailService emailService;
    @Reference
    WorkFlowConfigService service;

    @Override
    public List<Workflow> getActiveWorkflows() {
        LOGGER.trace("--- finding active wflow--- ");
        try {
            // Get the JCR session
            ResourceResolver serviceResolver = MacnicaCoreUtils
                    .getResourceResolver(resourceResolverFactory, MACNICA_SUB_SERVICE);
            LOGGER.trace("--- serviceResolver ---{} ", serviceResolver);
            Session session = serviceResolver.adaptTo(Session.class);
            // Get the workflow session
            WorkflowSession workflowSession = workflowService.getWorkflowSession(session);
            LOGGER.trace("--- workflowSession ---{} ", workflowSession);
            // States by which we want to query the workflows
            String[] states = {"RUNNING"};
            // Get the list of all the workflows by states
            Workflow[] workflows = workflowSession.getWorkflows(states);
            return Arrays.stream(workflows)
                    .filter(it -> it.getId().contains(deactivationWorkflowName)
                            || it.getId().contains(activationWorkflowName))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("{}: exception occurred: {}", TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public int getDays(String startDate, String currentDate, String timeFormat) {
        long timeDifference = 0;
        SimpleDateFormat obj = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try {
            // Use parse method to get date object of both dates
            Date date1 = obj.parse(currentDate);
            Date date2 = obj.parse(startDate);
            // Calucalte time difference in milliseconds
            timeDifference = date1.getTime() - date2.getTime();
            // Calucalte time difference in days
            //daysDifference = (timeDifference / (1000 * 60 * 60 * 24)) % 365;
            if (timeFormat.equalsIgnoreCase("H")) {
                return (int) TimeUnit.MILLISECONDS.toHours(timeDifference);
            }
        }
        // Catch parse exception
        catch (ParseException e) {
            LOGGER.error("{}: exception occurred : {}", TAG, e.getMessage());
        }
        return (int) TimeUnit.MILLISECONDS.toDays(timeDifference);
    }

    @Override
    public void sendRemainderEmail(String approverGroup, String workflowInitiator, List<String> payLoad) {
        List<Value> initiatorEmailList = macnicaGroupUsers.getUserEmail(workflowInitiator);
        List<Value> usersEmailList = macnicaGroupUsers.getUserDetails(approverGroup);
        String content = "This email is to remind you that the " + workflowInitiator
                + " has asked you to review and approve an article.";
        StringBuilder joiner = new StringBuilder();
        joiner.append(content);
        if (payLoad != null && !payLoad.isEmpty()) {
            joiner.append("Access the file: ");
            for (String path : payLoad) {
                LOGGER.trace("-- mail payload = {}-- ", path);
                joiner.append(service.getPreviewURL()).append(fun.apply(path)).append(System.lineSeparator());
            }
        } else {
            LOGGER.trace(" payLoad is empty ");
        }
        if (usersEmailList != null && !usersEmailList.isEmpty()) {
            LOGGER.trace("Sending Remainder Mail Notification to {}", usersEmailList);
            emailService.sendEmail(usersEmailList, initiatorEmailList,
                    "cmsadmin@macnica.co.jp",
                    "Reminder: You have a pending request to review and approve", joiner.toString());
            LOGGER.trace("{}: workflow status email is sent", content);
        }
        {
            LOGGER.trace(" usersEmailList is empty ");
        }
    }

    @Override
    public void mailtoInactiveWorkflows(String timeInterval, String timeFormat) {
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resourceResolverFactory,
                MACNICA_SUB_SERVICE);
        Session session = serviceResolver.adaptTo(Session.class);
        List<String> approverValList = new ArrayList<>();
        List<String> contentPathList = new ArrayList<>();
        try {
            List<Workflow> activeWorkflows = getActiveWorkflows();
            LOGGER.trace("Active WFS:{}", activeWorkflows.size());
            for (Workflow wf : activeWorkflows) {
                LOGGER.trace("Active WFS Name = {}", wf.getId());
                String payload = (String) wf.getWorkflowData().getPayload();
                if (!session.itemExists(payload)) {
                    LOGGER.trace(" -- path is not in the session skip and continue with next item -- ");
                    continue;
                }

                Date timeSinceStarted = wf.getTimeStarted();
                int calculatedDays = getDays(timeSinceStarted.toString(), new Date().toString(), timeFormat);
                LOGGER.trace("Workflow calculatedDays = {},workflow startedfrom = {}, timeIntervalDays = {}",
                        calculatedDays, timeSinceStarted, timeInterval);
                LOGGER.trace("Time Interval :{}", timeInterval);
                if ((calculatedDays >= Integer.parseInt(timeInterval))) {
                    LOGGER.trace("--- Interval format = {} and calculated interval  {} >= {} -----", timeFormat, calculatedDays
                            , Integer.parseInt(timeInterval));
                    // Get the workflow session
                    WorkflowSession workflowSession = workflowService.getWorkflowSession(session);
                    List<String> paths = WorkFlowUtils.getPaths(wf.getWorkflowData().getPayloadType(),
                            payload, resourceResolverFactory, workflowSession.getSession());
                    Map<String, String> map = WorkFlowUtils.getApproverList(paths, resourceResolverFactory);
                    map.forEach((k, v) -> {
                        approverValList.add(v);
                        contentPathList.add(k);
                        LOGGER.trace("Map Key : {} , Value : {}", k, v);
                    });
                    // Sending remainder emails to approver group members associated with author group
                    sendRemainderEmail(approverValList.isEmpty() ? null : approverValList.stream()
                            .filter(StringUtils::isNotBlank).findFirst().get(), wf.getInitiator(), contentPathList);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
