package com.macnicagwi.core.schedulers;

import com.macnicagwi.core.config.WorkFlowSchedulerConfig;
import com.macnicagwi.core.services.MacnicaWorkflows;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.EQUAL_SEPARATOR;
import static com.macnicagwi.core.schedulers.WorkflowStatusScheduler.NAME;

@Component(immediate = true, service = Runnable.class, property = {
        Constants.SERVICE_ID + EQUAL_SEPARATOR + NAME
})

@Designate(ocd = WorkFlowSchedulerConfig.class)
public class WorkflowStatusScheduler implements Runnable {

    protected static final String NAME = "Workflow Email Reminder Scheduler";

    private static final String TAG = WorkflowStatusScheduler.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowStatusScheduler.class);

    @Reference
    Scheduler scheduler;

    @Reference
    MacnicaWorkflows macnicaWorkflows;

    private String schedulerName;
    private String timeInterval;
    private String timeFormat;

    @Activate
    protected void activate(WorkFlowSchedulerConfig configuration) {
        LOGGER.debug("------ {}: initializing properties for scheduler ----------", TAG);
        this.schedulerName = configuration.schedulerName();
        timeFormat = configuration.timeFormat();
        LOGGER.debug("{}: name of the scheduler: {}", TAG, schedulerName);
        this.timeInterval = configuration.timeInterval();
        LOGGER.debug("{}: Time Interval Days: {}", TAG, timeInterval);
        removeScheduler(configuration);
        addScheduler(configuration);
    }

    @Modified
    protected void modified(WorkFlowSchedulerConfig configuration) {
        LOGGER.info("{}: modifying scheduler with name: {}", TAG, schedulerName);
        // Remove the scheduler registered with old configuration
        removeScheduler(configuration);
        // Add the scheduler registered with new configuration
        addScheduler(configuration);
    }

    @Deactivate
    protected void deactivate(WorkFlowSchedulerConfig configuration) {
        LOGGER.debug("----- {}: removing scheduler: {} ----", TAG, schedulerName);
        removeScheduler(configuration);
    }

    private void addScheduler(WorkFlowSchedulerConfig configuration) {
        // Check if the scheduler has enable flag set to true
        LOGGER.info("-- Inside addScheduler -- ");
        if (configuration.enabled()) {
            timeFormat = configuration.timeFormat();
            LOGGER.info("{}: scheduler: {} is enabled", TAG, schedulerName);
            // Configure the scheduler to use cron expression and some other properties
            ScheduleOptions scheduleOptions = scheduler.EXPR(configuration.cronExpression());
            scheduleOptions.name(schedulerName);
            scheduleOptions.canRunConcurrently(false);
            // Scheduling the job
            scheduler.schedule(this, scheduleOptions);
            LOGGER.info("{}: scheduler {} is added", TAG, schedulerName);
        } else {
            LOGGER.info("{}: scheduler {} is disabled", TAG, schedulerName);
            removeScheduler(configuration);
        }
    }

    private void removeScheduler(WorkFlowSchedulerConfig configuration) {
        LOGGER.info("------- {}: removing scheduler {} ---------", TAG, schedulerName);
        scheduler.unschedule(configuration.schedulerName());
    }

    @Override
    public void run() {
        LOGGER.info("--------- Reminder Email Notification Scheduler running ---------------------");
        macnicaWorkflows.mailtoInactiveWorkflows(timeInterval, timeFormat);
    }
}
