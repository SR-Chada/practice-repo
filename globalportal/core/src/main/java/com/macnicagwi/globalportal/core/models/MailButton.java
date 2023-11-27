package com.macnicagwi.globalportal.core.models;

import org.apache.commons.lang3.StringUtils;
import org.osgi.annotation.versioning.ProviderType;

import com.adobe.cq.wcm.core.components.models.Component;

@ProviderType
public interface MailButton extends Component {

	public static final String DEFAULT_MACNICA_EMAIL_ADDRESS = "macnica-marketing@macnica.co.jp";

	/**
	 * Returns Email address configured in the component or else the default macnica
	 * marketing email ID
	 * 
	 * @return email ID configured in component or default macnica marketing email
	 *         id.
	 */
	public default String getEmailAddress() {
		return DEFAULT_MACNICA_EMAIL_ADDRESS;

	}

	/**
	 * Returns the button label configured in the component dialogue. Returns empty
	 * string by default.
	 * 
	 * @return button label configured in component dialogue
	 */
	public default String getText() {
		return StringUtils.EMPTY;
	}
}
