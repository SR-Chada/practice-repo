package com.macnicagwi.core.config;

import org.apache.commons.lang3.StringUtils;

import org.apache.sling.caconfig.annotation.Property;

import org.apache.sling.caconfig.annotation.Configuration;

@Configuration(label = "Macnica Subsidiary sites Shareaholic configuration")
public @interface ShareaholicConfig {
    @Property(label="Header Script")
    String headerScript() default StringUtils.EMPTY;
}
