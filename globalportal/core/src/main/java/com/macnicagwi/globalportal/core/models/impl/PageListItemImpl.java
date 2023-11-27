package com.macnicagwi.globalportal.core.models.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.datalayer.PageData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.adobe.granite.asset.api.*;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import com.macnicagwi.globalportal.core.models.PageListItem;

/**
 * Referenced from:
 * com.adobe.cq.wcm.core.components.internal.models.v2.PageListItemImpl
 * 
 * @author Sai
 *
 */
public final class PageListItemImpl extends AbstractComponentImpl implements PageListItem {

    /**
     * Standard logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageListItemImpl.class);

    private static final String NN_PAGE_ICON = "iconimage";
    
    private static final String PN_TEASER_TITLE="teaserTitle";

    /**
     * Prefix prepended to the item ID.
     */
    private static final String ITEM_ID_PREFIX = "item";

    /**
     * The path of this list item.
     */
    private String path;

    /**
     * Data layer type.
     */
    protected String dataLayerType;

    /**
     * The page for this list item.
     */
    private Page page;

    /**
     * The link for this list item.
     */
    private Optional<Link<Page>> link;
    /**
     * The featured image for this list item.
     */
    private Resource featuredImageResource;

    /**
     * The featured image for this list item.
     */
    private Resource pageIcon;
    /**
     * The unique HTML id for this list item.
     */
    private String id;
    /**
     * The ResourceResolver to fetch the image srcset using filerefernce path.
     */
    private ResourceResolver resolver;


    public PageListItemImpl(@NotNull final GlobalPortalLinkHandler linkHandler, @NotNull final Page page,
            final String parentId, final String parentResourceType, ResourceResolver resolver) {
                try{

                
        LOGGER.trace("Initializing Global portal page list item");
        this.resource = page.getContentResource();
        this.resolver = resolver;
        if (resource != null) {
            this.path = resource.getPath();
        }
        if (component != null) {
            this.dataLayerType = parentResourceType + "/" + ITEM_ID_PREFIX;
        }
        this.link = linkHandler.getLink(page);
        if (this.link.isPresent()) {
            this.page = link.get().getReference();

        } else {
            this.page = page;
        }
        this.id = ComponentUtils.generateId(StringUtils.join(parentId, ComponentUtils.ID_SEPARATOR, ITEM_ID_PREFIX),
                path);
        this.featuredImageResource = ComponentUtils.getFeaturedImage(page);
        if(featuredImageResource !=null) {
            String imagePath = featuredImageResource.getValueMap().get("fileReference").toString();
            if(StringUtils.isNotBlank(imagePath)) {
    
        ModifiableValueMap featuredImage = featuredImageResource.adaptTo(ModifiableValueMap.class);
            featuredImage.put("srcset", getSrcset(imagePath));
            } else {
            	LOGGER.warn("Featured image not configured for page {} but being fetched in teaser list ", page.getPath());
            }
                            } 
                }catch(Exception e){
                    LOGGER.error("Error occurs in Page list generation {}", e.getMessage());
                }
        }
    
    private String[] getSrcset(String imagePath) {
        List<String> renditionList = new ArrayList<String>();
        try {
            AssetManager assetManager = resolver.adaptTo(AssetManager.class);
            Asset asset = assetManager.getAsset(imagePath);
            Rendition rendition = null;
            Iterator itr = asset.listRenditions();

            while (itr.hasNext()) {
                rendition = (Rendition) itr.next();
                renditionList.add(rendition.getPath());
            }

        } catch (Exception e) {
            LOGGER.error("Error occurs in srcset generation {}", e.getMessage());
        }
        return renditionList.toArray(new String[renditionList.size()]);
    }

    @Override
    public Link<Page> getLink() {
        return link.orElse(null);
    }

    @Override
    public String getTitle() {
        return PageListItemImpl.getTitle(this.page);
    }
    
    @Override
    public String getTeaserTitle() {
    	String teaserTitle = StringUtils.EMPTY;  
    	if(this.page!=null && this.page.getProperties()!=null) {
    		teaserTitle = this.page.getProperties().get(PN_TEASER_TITLE, String.class);
    	}
        return Optional.ofNullable(teaserTitle).orElse(StringUtils.EMPTY);
    }

    /**
     * Gets the title of a page list item from a given page. The list item title is
     * derived from the page by selecting the first non-null value from the
     * following:
     * <ul>
     * <li>{@link Page#getNavigationTitle()}</li>
     * <li>{@link Page#getPageTitle()}</li>
     * <li>{@link Page#getTitle()}</li>
     * <li>{@link Page#getName()}</li>
     * </ul>
     *
     * @param page The page for which to get the title.
     * @return The list item title.
     */
    private static String getTitle(@NotNull final Page page) {
        String title = page.getNavigationTitle();
        if (title == null) {
            title = page.getPageTitle();
        }
        if (title == null) {
            title = page.getTitle();
        }
        if (title == null) {
            title = page.getName();
        }
        return title;
    }

    @Override
    public String getDescription() {
        return page.getDescription();
    }

    @Override
    public Resource getFeaturedImage() {
        return featuredImageResource;
    }

    @Override
    public Resource getPageIcon() {
        return page.getContentResource(NN_PAGE_ICON);

    }

    @Override
    public Calendar getLastModified() {
        return page.getLastModified();
    }

    @Override
    public String getPath() {
        return page.getPath();
    }

    @Override
    public String getName() {
        return page.getName();
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    @NotNull
    protected PageData getComponentData() {
        return DataLayerBuilder.extending(super.getComponentData()).asPage().withTitle(this::getTitle)
                .withLinkUrl(() -> link.map(Link::getMappedURL).orElse(null)).build();
    }

    @Override
    public String getPageSubtitle() {
        return page.getProperties().get("subtitle", "");
    }

}
