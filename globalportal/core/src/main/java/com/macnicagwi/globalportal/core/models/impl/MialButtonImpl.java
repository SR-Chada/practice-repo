package com.macnicagwi.globalportal.core.models.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.macnicagwi.globalportal.core.models.MailButton;

public class MialButtonImpl extends AbstractComponentImpl implements MailButton {
	
	private static final String JSON_PN_EMAIL_ADDRESS="emailAddress";
	
	@ValueMapValue
	private String emailAddress;
	
	@ValueMapValue
	private String text;
	
	@Override
	@JsonProperty(JSON_PN_EMAIL_ADDRESS)
	public String getEmailAddress() {
		return Optional.ofNullable(emailAddress).orElse(DEFAULT_MACNICA_EMAIL_ADDRESS);
		
	}
	
	@Override
	@JsonProperty(JSON_PN_EMAIL_ADDRESS)
	public String getText() {
		return Optional.ofNullable(text).orElse(StringUtils.EMPTY);
		
	}
	

}
