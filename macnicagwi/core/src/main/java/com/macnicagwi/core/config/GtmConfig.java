package com.macnicagwi.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="Macnica Subsidiary sites GTM configuration")
public @interface GtmConfig {
	
	@Property(label="Head script")
	String headScript() default StringUtils.EMPTY;
	
	@Property(label="Body script")
	String bodyScript() default StringUtils.EMPTY;
	
	

}
