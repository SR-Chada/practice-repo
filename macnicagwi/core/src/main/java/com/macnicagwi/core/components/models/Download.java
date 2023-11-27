package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.day.cq.dam.api.Asset;

/**
 * {@code Download} Sling Model used for the
 * {@code /apps/macnicagwi/components/content/download} component.
 * 
 * @author Sumit
 */
@ConsumerType
public interface Download extends com.adobe.cq.wcm.core.components.models.Download {

	/**
	 * Returns Resource Path.
	 * 
	 * @return Resource Path
	 */
	String getResourcePath();

	/**
	 * Returns Asset.
	 * 
	 * @return Asset
	 */
	Asset getAsset();

	/**
	 * Returns Asset's Dam Sha.
	 * 
	 * @return Asset's Dam Sha
	 */
	String getDamSha();

	/**
	 * Returns Download Form Experience Fragment Path.
	 * 
	 * @return Download Form Experience Fragment Path
	 */
	String getDownloadFormXFPath();

	/**
	 * Returns a boolean flag indicating if the asset is restricted asset or not.
	 * 
	 * @return {@code true} if {@code isRestricted} metadata property of the asset is
	 *         checked, {@code false} otherwise.
	 */
	public boolean isRestrictedDownload();

}
