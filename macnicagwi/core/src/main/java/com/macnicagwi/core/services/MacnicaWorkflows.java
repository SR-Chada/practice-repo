package com.macnicagwi.core.services;

import com.day.cq.workflow.exec.Workflow;

import java.util.List;

public interface MacnicaWorkflows {

     List<Workflow> getActiveWorkflows();

     int getDays(String startDate, String currentDate, String timeFormat);

     void sendRemainderEmail(String approverGroup, String workflowInitiator, List<String> payLoad);

     void mailtoInactiveWorkflows(String timeIntervalDays, String timeFormat);

}
