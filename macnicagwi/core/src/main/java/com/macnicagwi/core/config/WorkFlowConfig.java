package com.macnicagwi.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import static com.macnicagwi.core.config.WorkFlowConfig.CONFIGURATION_DESCRIPTION;
import static com.macnicagwi.core.config.WorkFlowConfig.CONFIGURATION_NAME;

@ObjectClassDefinition(
        name = CONFIGURATION_NAME,
        description = CONFIGURATION_DESCRIPTION
)
public @interface WorkFlowConfig {
    String CONFIGURATION_NAME = "Macnicagwi Replication Workflow config";
    String CONFIGURATION_DESCRIPTION = "Configuring the approver list and preview url";

    @AttributeDefinition(
            name = "Preview URL",
            description = "Preview URL Link",
            type = AttributeType.STRING
    )
    String previewURL() default "https://prd-preview.macnica.com";

    @AttributeDefinition(
            name = "Publish URL",
            description = "Publish URL Link",
            type = AttributeType.STRING
    )
    String publishURL() default "https://www.macnica.com";
}
