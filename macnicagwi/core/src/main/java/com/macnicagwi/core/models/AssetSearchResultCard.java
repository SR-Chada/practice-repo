package com.macnicagwi.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.macnicagwi.core.components.dtos.FileDto;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

import lombok.Getter;

/**
 * {@code DownloadListingTableRow} Sling Model 
 * Adaptable to Asset.
 * @author Sumit
 */

@Model(adaptables = { SlingHttpServletRequest.class, Asset.class })
@JsonInclude(Include.NON_NULL)
public class AssetSearchResultCard {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetSearchResultCard.class);
	private static final String ASSET_RESTRICTED_DOWNLOAD_PROPERTY = "jcr:content/metadata@isRestrictedDownload";

	/*Search Result Asset*/
    @Self
    private Asset asset;

    /*Search Result Type*/
    @Getter
    private String type;
    
    /*Search Result Asset Title*/
    @Getter
    private String title;

    /*Search Result Asset File Details*/
    @Getter
    private FileDto file;
    
    @PostConstruct
    public void init() {
    	//To-Do: Replace this logic to inject the necessary attributes from asset props
    	LOGGER.trace("Initialising my model: {}", asset);
        this.type = DamConstants.NT_DAM_ASSET;
        this.title = returnEmptyString(asset.getMetadataValue(DamConstants.DC_TITLE));
        boolean assetDownloadRestricted = Boolean.parseBoolean(MacnicaComponentUtils.getAssetPropertyValue(asset, ASSET_RESTRICTED_DOWNLOAD_PROPERTY));
        if (assetDownloadRestricted) {
            this.file = new FileDto(StringUtils.EMPTY, asset.getMetadataValue(DamConstants.PN_SHA1), FilenameUtils.getExtension(asset.getPath()));
        } else {
            this.file = new FileDto(asset.getPath(), StringUtils.EMPTY, FilenameUtils.getExtension(asset.getPath()));
        }
    }

    private String returnEmptyString(String propVal) {
        if (StringUtils.isNotBlank(propVal)) {
            return propVal;
        }
        return StringUtils.EMPTY;
    }
}
