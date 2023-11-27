package com.macnicagwi.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label="Macnica Subsidiary Sites Date Format Configuration")
public @interface DateFormatConfig {
	
	public static final String DATE_PATTERN ="dd MMMM yyyy";
	
	@Property(label="Date Format")
	String dateFormat() default DATE_PATTERN;
	
}
