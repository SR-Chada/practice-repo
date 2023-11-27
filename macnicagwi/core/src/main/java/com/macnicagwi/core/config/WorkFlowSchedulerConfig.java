package com.macnicagwi.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

import static com.macnicagwi.core.config.WorkFlowSchedulerConfig.CONFIGURATION_DESCRIPTION;
import static com.macnicagwi.core.config.WorkFlowSchedulerConfig.CONFIGURATION_NAME;

@ObjectClassDefinition(
        name = CONFIGURATION_NAME,
        description = CONFIGURATION_DESCRIPTION
)
public @interface WorkFlowSchedulerConfig {

    String CONFIGURATION_NAME = "Work Flow Email Reminder Notification Configuration";
    String CONFIGURATION_DESCRIPTION = "This configuration captures workflow status and sends remainder email";

    @AttributeDefinition(
            name = "Scheduler Name",
            description = "Enter a unique identifier that represents name of the scheduler",
            type = AttributeType.STRING
    )
    String schedulerName() default CONFIGURATION_NAME;

    @AttributeDefinition(
            name = "Enabled",
            description = "Check the box to enable the scheduler",
            type = AttributeType.BOOLEAN
    )
    boolean enabled() default true;

    @AttributeDefinition(
            name = "Cron Expression",
            description = "Cron expression which will decide how the scheduler will run",
            type = AttributeType.STRING
    )
    String cronExpression() default "0 0 * ? * *";

    @AttributeDefinition(
            name = "Time interval days",
            description = "Interval days which will decide remainder emails.",
            type = AttributeType.INTEGER
    )
    String timeInterval() default "1";

    @AttributeDefinition(
            name = "Select Day or Hour format",
            description = "Choose Day or Hour format",
            options = {@Option(label = "Days", value = "D"),
                    @Option(label = "Hour", value = "H")})
    String timeFormat() default "H";
}
