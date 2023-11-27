package com.macnicagwi.core.components.models;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;

import lombok.NonNull;


/**
 * Macnica's extended list item implementation for a asset-backed list item.
 * @implSpec Extends  {@link com.adobe.cq.wcm.core.components.models.ListItem}
 * @author Sai
 *
 */

public interface MacnicaAssetListItem extends MacnicaListItem {
	
	/** 
	 * Determines if the asset is downloadable or not based on Macnica GWI custom asset metadata property {@code isDownloadable}.
	 * @return true if the {@code isDownloadable} asset metadata property of the asset is checked. Returns false otherwise.
	 */
	@NonNull
	public  Boolean isDownloadable();
	
    /**
     *  Determines if the asset is restricted for download based on Macnica GWI custom asset metadata property {@code isRestrictedDownload}.
     * This resource is intended to be rendered via the  Macnica GWI Image Component's logic via a Sling include of this resource.
     * @return the (Sling) resource that represents that image to display in the list item. (Typically featured image of a page)
     */
	@NonNull
    public Boolean isRestrictedDownload(); 
	
	/**
	 * Sling Resource of the asset
	 * @return : Asset resource
	 */
	@Nullable
	public Asset getAsset();
	

    /**
     * Returns the URL (Servlet Endpoint) to the asset.
     *
     * @return the asset URL
     */
	public default String getUrl() {
        return null;
    }
    
    /**
     * Returns the Link to the asset's download detail page.
     *
     * @return the asset download detail page Link
     */
	public default Optional<Link<Page>> getDownloadDetailPageUrl() {
        return Optional.empty();
    }
	
	
	/**
     * Returns Asset's Dam Sha.
     * @return Asset's Dam Sha
     */
    public String getDamSha();
    
    /**
     * Returns the filename of the file to be downloaded.
     *
     * @return the filename of the download file
     */
    public default String getFileName() {
        return null;
    }
    
    /**
     * Returns the mime type of the file to be downloaded.
     *
     * @return the mime type of the download file
     */
    default String getFormat() {
        return null;
    }
    
    /**
     * Returns the extension of the file to be downloaded.
     *
     * @return the extension of the download file
     */
    default String getExtension() {
        return null;
    }
    
    /**
     * Returns the JCR description of asset
     *
     * @return JCR description of asset
     */
	default String getDescription() {
		return null;
	}
    /**
     * Returns the localized manufacturer tag title of the asset to be downloaded
     *
     * @return the localized manufacturer tag title of the download file
     */
    default String getManufacturer() {
        return null;
    }
    
    /**
     * Returns the localized productLine tag title of the asset to be downloaded
     *
     * @return the localized productLine tag title of the download file
     */
    default String getProductLine() {
        return null;
    }
    
    /**
     * Returns the localized year tag title of the asset to be downloaded
     *
     * @return the localized year tag title of the download file
     */
    default String getYear() {
        return null;
    }
    
    /**
     * Returns the localized report type tag title of the asset to be downloaded
     *
     * @return the localized report type tag title of the download file
     */
    default String getReportType() {
        return null;
    }
    
    /**
     * Returns the localized quarter tag title of the asset to be downloaded
     *
     * @return the localized quarter tag title of the download file
     */
    default String getQuarter() {
        return null;
    }
    
    /**
     * Returns the localized content type tag title of the asset to be downloaded
     *
     * @return the localized content type tag title of the download file
     */
    default String getContentType() {
        return null;
    }
    
    /**
     * Returns the publish date of the file to be downloaded.
     *
     * @return the publish date metadata property of the asset
     */
    public default String getPublishDate() {
        return null;
    }
    
    /**
     * Returns the Chinese Year of the file to be downloaded.
     *
     * @return the Chinese Year metadata property of the asset
     */
    public default String getChineseYear() {
        return null;
    }
    
    /**
     * Returns the Download Details Page URL of the file to be downloaded.
     *
     * @return the Download Details metadata property of the asset
     */
    public default Link<Page> getDownloadDetailsLink() {
        return null;
    }

    /**
     * Returns the Business Category of the file to be downloaded.
     *
     * @return the Business category metadata property of the asset
     */
    String getBusinessCategory();
    
    

    
    
    
    
 
    


}
