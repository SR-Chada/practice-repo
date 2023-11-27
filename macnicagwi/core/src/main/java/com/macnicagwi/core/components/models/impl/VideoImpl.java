package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.servlets.MacnicaWatchVideoFormServlet.WATCH_VIDEO_REQUEST_COOKIE_NAME;
import static com.macnicagwi.core.servlets.MacnicaWatchVideoFormServlet.WATCH_VIDEO_REQUEST_COOKIE_VALUE;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Embed;
import com.adobe.cq.wcm.core.components.services.embed.UrlProcessor;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.Video;
import com.macnicagwi.core.utils.MacnicaFormUtils;

@Model(
	adaptables = SlingHttpServletRequest.class, 
	adapters = { Video.class,
		ComponentExporter.class }, 
	resourceType = VideoImpl.RESOURCE_TYPE, 
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, 
	extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class VideoImpl extends AbstractComponentImpl implements Video {

	public static final String RESOURCE_TYPE = "macnicagwi/components/content/video";

	@Self
	SlingHttpServletRequest request;

	@Self
    @Via(type = ResourceSuperType.class)
    private Embed embed;

	@ValueMapValue
    private String videourl;

    @ValueMapValue
    private int width;

    @ValueMapValue
    private int height;

    @ValueMapValue
    private String embedCode;

    @ValueMapValue
    private int embedWidth;

    @ValueMapValue
    private int embedHeight;

	@ValueMapValue
	private boolean isRestrictedVideo;

	private boolean isValidRequest = true;

	@PostConstruct
	public void init() {
		if (isRestrictedVideo) {
			isValidRequest = MacnicaFormUtils.validateRequest(request, WATCH_VIDEO_REQUEST_COOKIE_NAME, WATCH_VIDEO_REQUEST_COOKIE_VALUE);
		}
	}

    @Override
    public String getEmbedCode(){
        return embedCode;
    }

    @Override
    public int getEmbedWidth(){
        return embedWidth;
    }

    @Override
    public int getEmbedHeight(){
        return embedHeight;
    }
    
	@Override
    public String getVideoUrl(){
        return videourl; 
    }
    @Override
    public int getWidth() {
        return width;
    }
    @Override
    public int getHeight() {
        return height;
    }
	
	@Override
	public boolean isRestrictedVideo() {
		return isRestrictedVideo;
	}

	@Override
	public boolean isValidRequest() {
		return isValidRequest;
	}

	@Nullable
    @Override
    public Type getType() {
        return embed.getType();
    }

    @Nullable
    @Override
    public String getUrl() {
        return embed.getUrl();
    }

    @Nullable
    @Override
    public UrlProcessor.Result getResult() {
        return embed.getResult();
    }

    @Nullable
    @Override
    public String getHtml() {
        return embed.getHtml();
    }

    @Nullable
    @Override
    public String getEmbeddableResourceType() {
        return embed.getEmbeddableResourceType();
    }
	
	@NotNull
	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

}
