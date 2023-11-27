package com.macnicagwi.core.components.models;

import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;
import com.macnicagwi.core.components.models.impl.ContactCardsImpl.OfficeLocation;

/**
 * {@code ContactCards} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/contactcards} component.
 * @author Sumit
 */
@ConsumerType
public interface ContactCards extends Component {

    /**
     * Returns the Office Location Cfm List.
     * @return the the Office Location Cfm List
     */
    List<OfficeLocation> getOfficeLocations();

	/**
     * Returns the Parent Path.
     * @return the Parent Path
     */
    String getParentPath();

    String getOrderBy();

	/**
     * Returns the Location Type.
     * @return the Location Type
     */
	String getLocationType();

	/**
     * Returns the Google Maps Label.
     * @return the Google Maps Label
     */
	String getGoogleMapsLabel();

	/**
     * Returns the Route Instructions Label.
     * @return the Route Instructions Label
     */
	String getRouteInstructionsLabel();

	/**
     * Returns the Route Instructions Popup Label.
     * @return the Route Instructions Popup Label
     */
	String getRouteInstructionsPopupTitle();

	/**
     * Returns the Route Instructions Popup - Public Transport Label.
     * @return the Route Instructions Popup - Public Transport Label
     */
	String getPublicTransportTabLabel();

	/**
     * Returns the Route Instructions Popup - Car Label.
     * @return the Route Instructions Popup - Car Label
     */
	String getCarTabLabel();

	/**
     * Returns the Route Instructions Popup - Motorcycle Label.
     * @return the Route Instructions Popup - Motorcycle Label
     */
	String getMotorcycleTabLabel();

	/**
     * Returns the Business Email Label.
     * @return the Business Email Label
     */
	String getBusinessEmailLabel();

	/**
     * Returns the Technical Email Label.
     * @return the Technical Email Label
     */
	String getTechnicalEmailLabel();

	/**
     * Returns the Support Email Label.
     * @return the Support Email Label
     */
	String getSupportEmailLabel();

	/**
     * Returns the Read More Button Label.
     * @return the Read More Button Label
     */
	String getReadMoreButtonLabel();

}
