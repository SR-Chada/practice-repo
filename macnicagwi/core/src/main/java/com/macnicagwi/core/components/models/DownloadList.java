package com.macnicagwi.core.components.models;

import java.util.Collection;
import java.util.Map;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@code DownloadList} Sling Model used for the
 * {@code /apps/macnicagwi/components/content/downloadlist} component.
 * 
 * @author Sai
 */
@ConsumerType
public interface DownloadList extends Component {

	public Collection<MacnicaAssetListItem> getAssets();

	/**
	 * @return true if this component has no list items to display.
	 */
	public boolean isEmpty();

	/**
	 * @return Path to the current Download list component resource
	 */
	@JsonProperty("path")
	String getResourcePath();

	/**
	 * Provides the total number of relevant assets obtained for requested search.
	 * This value should be used to paginate download list results.
	 * 
	 * @return Total number of downloadable assets fetched.
	 */
	Integer getTotalResultsSize();

	/**
	 * Returns Download Form Experience Fragment Path.
	 * 
	 * @return Download Form Experience Fragment Path
	 */
	String getDownloadFormXFPath();

	/**
	 * Determines if asset's jcr:description should be displayed in the download
	 * list component.
	 * 
	 * @return {@code true} if the {@code showDescription} dialogue of the download
	 *         list component is checked, {@code false} otherwise.
	 */
	public Boolean showDescription();

	/**
	 * Determines if asset's dc:title should be displayed in the download list
	 * component.
	 * 
	 * @return {@code true} if the {@code showTitle} dialogue of the download list
	 *         component is checked, {@code false} otherwise.
	 */
	public Boolean showTitle();

	/**
	 * @return Map of download table column names and labels.
	 */
	public Map<String, String> getSelectedProperties();


}
