package com.macnicagwi.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Macnica Workflow Configuration")
public @interface WorkFlowCaConfig {
    @Property(label = "Approver Groups")
    String approverGroup() default StringUtils.EMPTY;

    @Property(label = "HQ Groups")
    String hqGroup() default StringUtils.EMPTY;
}
