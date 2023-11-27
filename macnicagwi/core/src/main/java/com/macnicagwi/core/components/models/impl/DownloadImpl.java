package com.macnicagwi.core.components.models.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.Download;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { Download.class,
		ComponentExporter.class }, resourceType = DownloadImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class DownloadImpl extends AbstractComponentImpl implements Download {

	public static final String RESOURCE_TYPE = "macnicagwi/components/content/download";
	private static final String ASSET_RESTRICTED_DOWNLOAD_PROPERTY = "jcr:content/metadata@isRestrictedDownload";

	@Self
	SlingHttpServletRequest request;

	@SlingObject
	@Required
	private ResourceResolver resourceResolver;

	@Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.Download download;

	@ValueMapValue
	@Nullable
	private String downloadFormXFPath;
	

	@ValueMapValue
	@Nullable
	private String isDownloadable;

	@ValueMapValue
	private String fileReference;

	private String fileTitle;

	private String damSha;

	private Asset asset;

	private String resourcePath;

	private boolean isRestrictedDownload;

	@PostConstruct
	public void init() {
		resourcePath = request.getResource().getPath();
		Resource assetResource = resourceResolver.getResource(fileReference);
		if ( assetResource != null) {
			asset = resourceResolver.getResource(fileReference).adaptTo(Asset.class);
			if (asset != null) {
				fileTitle = asset.getMetadataValue(DamConstants.DC_TITLE);
				damSha = asset.getMetadataValue(DamConstants.PN_SHA1);
				isRestrictedDownload = Boolean.parseBoolean(MacnicaComponentUtils.getAssetPropertyValue(asset, ASSET_RESTRICTED_DOWNLOAD_PROPERTY));
			}
		}
	}

	@Override
	public String getResourcePath() {
		return resourcePath;
	}

	@Override
	public Asset getAsset() {
		return asset;
	}

	@Override
	public String getDamSha() {
		if (isRestrictedDownload) {
			return damSha;
		}
		return StringUtils.EMPTY;
	}

	@Override
	public String getDownloadFormXFPath() {
		return downloadFormXFPath;
	}
	
	@Override
	public boolean isRestrictedDownload() {
		return isRestrictedDownload;
	}

	@Override
    public String getUrl() {
        return download.getUrl();
    }


    @Override
    public String getFilename() {
		if (StringUtils.isNotBlank(fileTitle)) {
			return fileTitle;
		}
        return FilenameUtils.removeExtension(download.getFilename());
    }

    @Override
    public String getFormat() {
        return download.getExtension();
    }

    @Override
    public String getSize() {
        return download.getSize();
    }

    @Override
    public boolean displaySize() {
        return download.displaySize();
    }

    @Override
    public boolean displayFormat() {
        return download.displayFormat();
    }

    @Override
    public boolean displayFilename() {
        return download.displayFilename();
    }

	@NotNull
	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

}
