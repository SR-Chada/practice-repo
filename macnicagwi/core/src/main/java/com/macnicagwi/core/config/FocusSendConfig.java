package com.macnicagwi.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="Macnica Subsidiary sites FocusSend configuration")
public @interface FocusSendConfig {
    
    @Property(label="Focus Send")
	String focusSend() default StringUtils.EMPTY;
}
