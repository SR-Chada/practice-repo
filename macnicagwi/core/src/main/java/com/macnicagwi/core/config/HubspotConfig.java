package com.macnicagwi.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Macnica Subsidiary sites Hubspot configuration")
public @interface HubspotConfig {

   
    @Property(label="Script")
    String script() default StringUtils.EMPTY;
}

