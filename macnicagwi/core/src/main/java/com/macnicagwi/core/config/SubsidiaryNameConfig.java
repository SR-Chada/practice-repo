package com.macnicagwi.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="CA Configuration to provide name of the subsidiary site")
public @interface SubsidiaryNameConfig {
    @Property(label="Subsidiar Name")
    String subsidiaryName() default StringUtils.EMPTY;
    

}
