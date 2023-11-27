package com.macnicagwi.core.services.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.macnicagwi.core.services.ReplicationService;
import com.macnicagwi.core.utils.MacnicaCoreUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.*;

import static com.day.cq.dam.api.DamConstants.MOUNTPOINT_ASSETS;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;
import static org.osgi.service.event.EventConstants.SERVICE_ID;

@Component(
        service = ReplicationService.class,
        property = {
                SERVICE_ID + "=" + ReplicationServiceImpl.SERVICE_NAME
        }
)
public class ReplicationServiceImpl implements ReplicationService {

    protected static final String SERVICE_NAME = "Macnicagwi Preview Replication Service";

    private static final String TAG = ReplicationServiceImpl.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationServiceImpl.class);
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    private static final String PREVIEW_AGENT = "preview";
    private static final String PUBLISH_AGENT = "publish";

    @Reference
    Replicator replicator;


    @Override
    public void replicateContent(List<String> payload,boolean isPublish) {
        try {
            LOGGER.trace("{}: trying to replicate: {}", TAG, payload);
            // Getting resource resolver
            ResourceResolver resourceResolver = MacnicaCoreUtils.getResourceResolver(resourceResolverFactory, MACNICA_SUB_SERVICE);
            LOGGER.trace("resourceResolver : {}",  resourceResolver);
            // Getting the session
            Session session = resourceResolver.adaptTo(Session.class);
            LOGGER.trace("session : {}",  session);
            // Replicate the page
            replicate(session, payload, isPublish);
            // Get all the assets on the page(s)
            payload.forEach(it -> {
                LOGGER.trace("path : {}",  it);
                Set<String> assetsOnPage = getAssetsOnPage(resourceResolver, it);
                assetsOnPage.forEach(it1 -> {
                    LOGGER.trace("asset path : {}",  it1);
                    replicate(session, Collections.singletonList(it1), isPublish);
                });
//            for (String assetPath : assetsOnPage) {
//                replicate(session, Collections.singletonList(assetPath));
//            }
            });
            LOGGER.trace("{}: replication completed successfully", TAG);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Set<String> getAssetsOnPage(ResourceResolver resourceResolver, String payload) {
        LOGGER.trace("------ Inside getAssetsOnPage() --------- ");
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = Objects.requireNonNull(pageManager).getPage(payload);
        LOGGER.trace("getAssetsOnPage => page = {} ",page);
        if (page == null) {
            return new LinkedHashSet<>();
        }
        Resource resource = page.getContentResource();
        LOGGER.trace("getAssetsOnPage => resource = {}",resource);
        AssetReferenceSearch assetReferenceSearch = new AssetReferenceSearch(resource.adaptTo(Node.class),
                MOUNTPOINT_ASSETS, resourceResolver);
        LOGGER.trace("getAssetsOnPage => assetReferenceSearch = {}",assetReferenceSearch);
        Map<String, Asset> assetMap = assetReferenceSearch.search();
        LOGGER.trace("getAssetsOnPage => assetMap = {}",assetMap);
        return assetMap.keySet();
    }

    private void replicate(Session session, List<String> path,boolean isPublish) {
        LOGGER.trace(" -- Inside replicate method  --- ");
        ReplicationOptions options = new ReplicationOptions();
        options.setFilter(agent -> agent.getId().equals(PREVIEW_AGENT));
        LOGGER.trace("ReplicationOptions = {} ",options);
        try {
            LOGGER.trace("{}: Replicating: {}", TAG, path);
            path.forEach(it -> {
                try {
                    replicator.replicate(session,isPublish ? ReplicationActionType.ACTIVATE :
                            ReplicationActionType.DEACTIVATE,it,options);
                    LOGGER.trace("  replicator.replicate = {} ,{} ,{}",it, isPublish,options);
                } catch (ReplicationException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("{}: replication failed due to: {}", TAG, e.getMessage());
        }
    }
}