package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.ASSET_METADATA_PROPERTY;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_CHINESE_YEAR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_CONTENT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_DOWNLOAD_DETAIL_PAGE_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_IS_DOWNLOADABLE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_IS_RESTRICTED_DOWNLOAD;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_MANUFACTURER;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_PRODUCT_LINE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_PUBLISHED_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_QUARTER;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_REPORT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_YEAR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_DOWNLOAD_DETAILS_LINK;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_BUSINESS_CATEGORY;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.commons.mime.MimeTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.macnicagwi.core.components.models.MacnicaAssetListItem;
import com.macnicagwi.core.components.models.MacnicaListItem;
import com.macnicagwi.core.config.DateFormatConfig;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.servlets.MacnicaDownloadServlet;

import lombok.NonNull;

public class MacnicaAssetListItemImpl implements MacnicaAssetListItem {
	

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The asset resource
	 */
	private Resource assetMetadata;

	/**
	 * The path of this list item.
	 */
	private String path;

	/**
	 * The ID of the component that contains this list item.
	 */
	private String parentId;

	/**
	 * Link Handler object reference
	 */
	private MacnicaLinkHandler linkHandler;

	/**
	 * Asset of the List Item
	 */
	private Asset asset;

	/**
	 * Tag manager
	 */
	private TagManager tagManager;
	
	/**
	 * Two letter language code
	 */
	private Locale language;
	private MimeTypeService mimeTypeService;

	/**
	 * Construct a list item for a given Asset.
	 *
	 * @param linkHandler The link handler.
	 * @param page        The current page.
	 * @param parentId    The ID of the list containing this item.
	 * @param component   The component containing this list item.
	 */
	public MacnicaAssetListItemImpl(@NotNull final MacnicaLinkHandler linkHandler, MimeTypeService mimeTypeService, @NotNull final Asset asset, @NotNull final Locale language,
			final String parentId) {
		logger.trace("Initialising MacnicaAssetListItem  for asset {}", asset.getPath());

		this.linkHandler = linkHandler;
		this.asset = asset;
		this.assetMetadata = asset.adaptTo(Resource.class).getChild(ASSET_METADATA_PROPERTY);
		this.parentId = parentId;
		this.tagManager = assetMetadata.getResourceResolver().adaptTo(TagManager.class);
		this.language = language;
		this.mimeTypeService = mimeTypeService;
		
				}

	@Override
	@NonNull
	public Boolean isDownloadable() {
		Boolean isDownloadable = assetMetadata.getValueMap().get(PN_IS_DOWNLOADABLE, Boolean.class);
		return BooleanUtils.isTrue(isDownloadable);
	}

	@Override
	@NonNull
	public Boolean isRestrictedDownload() {
		Boolean isRestricted = assetMetadata.getValueMap().get(PN_IS_RESTRICTED_DOWNLOAD, Boolean.class);
		return BooleanUtils.isTrue(isRestricted);
	}

	@Override
	public Asset getAsset() {
		return asset;
	}

	@Override
	public String getDamSha() {
		return asset.getMetadataValue(DamConstants.PN_SHA1);
	}

	@Override
	public Optional<Link<Page>> getDownloadDetailPageUrl() {
		String pagePath = assetMetadata.getValueMap().get(PN_DOWNLOAD_DETAIL_PAGE_PATH, String.class);
		return linkHandler.getLink(pagePath, StringUtils.EMPTY);
	}

	@Override
	public String getUrl() {
		String fileName = getFileName();
		StringBuilder downloadUrlBuilder = new StringBuilder();
        downloadUrlBuilder.append(asset.getPath())
                .append(".")
                .append(MacnicaDownloadServlet.SELECTOR)
                .append(".");
        downloadUrlBuilder.append(getExtension());
       return linkHandler.getLink(downloadUrlBuilder.toString() + "/" + fileName, null).map(Link::getURL).orElse(null);
	}
	
    /**
     * Returns the extension of the file to be downloaded.
     *
     * @return the extension of the download file
     */
	@Override
    public String getExtension() {
		if (StringUtils.isNotEmpty(getFormat())) {
            return mimeTypeService.getExtension(getFormat());
        }
		return StringUtils.EMPTY;
    }

	@Override
	public String getFileName() {
		String dcTitle= asset.getMetadataValue(DamConstants.DC_TITLE);
		return StringUtils.isBlank(dcTitle)?asset.getName():dcTitle;
	}

	@Override
	public String getDescription() {
		return asset.getMetadataValue(DamConstants.DC_DESCRIPTION);
	}

	@Override
	public String getFormat() {
		Resource assetContent = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT);
		if (assetContent != null) {
			ValueMap valueMap = assetContent.adaptTo(ValueMap.class);
			if (valueMap != null) {
				String format = valueMap.get(JcrConstants.JCR_MIMETYPE, String.class);
				return StringUtils.isBlank(format) ? StringUtils.EMPTY : format;
			}
		}
		return StringUtils.EMPTY;
	}

	@Override
	public String getManufacturer() {
		String manufacturerTag = assetMetadata.getValueMap().get(PN_MANUFACTURER, String.class);
		if(StringUtils.isNotBlank(manufacturerTag)) {		    
		    return tagManager.resolve(manufacturerTag).getTitle(language);
		}
		return StringUtils.EMPTY;
	}
	
	   @Override
	    public String getBusinessCategory() {
	        String buisnessCategoryTag = assetMetadata.getValueMap().get(PN_BUSINESS_CATEGORY, String.class);
	        if(StringUtils.isNotBlank(buisnessCategoryTag)) {           
	            return tagManager.resolve(buisnessCategoryTag).getTitle(language);
	        }
	        return StringUtils.EMPTY;
	        
	    }

	
	@Override
	public String getProductLine() {
		String prouctLineTag = assetMetadata.getValueMap().get(PN_PRODUCT_LINE, String.class);
        if(StringUtils.isNotBlank(prouctLineTag)) {           
            return tagManager.resolve(prouctLineTag).getTitle(language);
        }
        return StringUtils.EMPTY;
	}

	
	@Override
	public String getYear() {
		String yearTag = assetMetadata.getValueMap().get(PN_YEAR, String.class);
        if(StringUtils.isNotBlank(yearTag)) {           
            return tagManager.resolve(yearTag).getTitle(language);
        }
        return StringUtils.EMPTY;
	}


	@Override
	public String getReportType() {
		String reportTypeTag = assetMetadata.getValueMap().get(PN_REPORT_TYPE, String.class);
        if(StringUtils.isNotBlank(reportTypeTag)) {           
            return tagManager.resolve(reportTypeTag).getTitle(language);
        }
        return StringUtils.EMPTY;
	}

	
	@Override
	public String getQuarter() {
		String quarterTag = assetMetadata.getValueMap().get(PN_QUARTER, String.class);
        if(StringUtils.isNotBlank(quarterTag)) {           
            return tagManager.resolve(quarterTag).getTitle(language);
        }
        return StringUtils.EMPTY;
		
	}


	@Override
	public String getContentType() {
		String contentTypeTag = assetMetadata.getValueMap().get(PN_CONTENT_TYPE, String.class);
        if(StringUtils.isNotBlank(contentTypeTag)) {           
            return tagManager.resolve(contentTypeTag).getTitle(language);
        }
        return StringUtils.EMPTY;
	}
	
	@Override
    public String getPublishDate() {
		Calendar publishedDate = assetMetadata.getValueMap().get(PN_PUBLISHED_DATE, Calendar.class);
		Resource assetResource = asset.adaptTo(Resource.class);
		DateFormatConfig dateFormatConfig = assetResource.adaptTo(ConfigurationBuilder.class).as(DateFormatConfig.class);
		Date date = publishedDate.getTime();
		TimeZone timeZone = publishedDate.getTimeZone();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatConfig.dateFormat());
		dateFormat.setTimeZone(timeZone);
		return dateFormat.format(date);
    }
	

	@Override
    public String getChineseYear() {
		return assetMetadata.getValueMap().get(PN_CHINESE_YEAR, String.class);
    }
    

	@Override
    public  Link<Page> getDownloadDetailsLink() {
    	String downloadDetailsPagePath = assetMetadata.getValueMap().get(PN_DOWNLOAD_DETAILS_LINK, String.class);
    	Optional<Link<Page>> downloadDetailsPageLink =linkHandler.getLink(downloadDetailsPagePath, StringUtils.EMPTY);    	
        return downloadDetailsPageLink.orElse(null);
    }
    
	@Override
	public ItemType getItemType() {
	    return MacnicaListItem.ItemType.ASSET;
	}
    

}
