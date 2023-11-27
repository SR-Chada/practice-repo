package com.macnicagwi.core.utils;

import com.day.cq.wcm.api.Page;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.google.gson.Gson;
import com.macnicagwi.core.components.dtos.ContentRootPathDto;
import com.macnicagwi.core.config.WorkFlowCaConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.*;
import java.util.function.Function;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.MACNICA_SUB_SERVICE;

public class WorkFlowUtils {
    private static final Logger logger = LoggerFactory.getLogger(WorkFlowUtils.class);
    private static final String CONTENT_ROOT_PATH = "contentRootPaths";
   public static Function<String,String> fun = (it) -> (it.startsWith("/content/macnicagwi/") && !it.endsWith(".html"))?
            it.concat(".html") : it;
    public WorkFlowUtils() {
    }

    /**
     * To get the approver group
     *
     * @param paths content paths
     * @return approver groups
     */
    public static Map<String, String> getApproverList(List<String> paths, ResourceResolverFactory resolverFactory) {
        Map<String, String> aprvrMap = new HashMap<>();
        logger.info("Inside getApproverList and paths as an Argument= {}", paths);
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resolverFactory,
                MACNICA_SUB_SERVICE);
        for (String path : paths) {
            Resource contentResource = serviceResolver.getResource(path);
            WorkFlowCaConfig config = contentResource.adaptTo(ConfigurationBuilder.class).as(WorkFlowCaConfig.class);
            logger.info("config => {}", config.approverGroup());
            aprvrMap.put(path, config.approverGroup());
        }
        return aprvrMap;
    }

    public static List<String> getPaths(String type, String payload, ResourceResolverFactory resolverFactory, Session session) {
        String path = null;
        try {
            if (type.equals("JCR_PATH") && payload != null) {
                logger.info("inside JCR PATH");
                if (session.itemExists(payload)) {
                    path = payload;
                }
            } else if (payload != null && type.equals("JCR_UUID")) {
                logger.info("inside JCR_UUID");
                Node node = session.getNodeByIdentifier(payload);
                path = node.getPath();
            }
            if (path != null) {
                ResourceResolver resolver = resolverFactory.getResourceResolver(Collections.singletonMap("user.jcr.session", session));
                Resource res = resolver.getResource(path);
                if (res.adaptTo(Page.class) != null) {
                    Page page = res.adaptTo(Page.class);
                    if (page.getContentResource().isResourceType("cq/workflow/components/collection/page")) {
                        logger.info("---isResourceType matched = {} -------", true);
                        ResourceVisitor obj = new ResourceVisitor();
                        obj.accept(resolver.getResource(path + "/jcr:content/vlt:definition/filter"));
                        return obj.getPath();
                    } else {
                        logger.error("---isResourceType not matched = {} -------", false);
                        return Collections.singletonList(path);
                    }
                } else {
                    if (path.contains("/content/dam/macnicagwi/") || path.contains("/content/cq:tags/")){
                        return Collections.singletonList(path);
                    } else {
                        logger.error("--- Page is empty -----");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHQGroup(List<String> paths, ResourceResolverFactory resolverFactory) {
        logger.info("-- Inside getHQGroups --");
        List<String> hqList = new ArrayList<>();
        ResourceResolver serviceResolver = MacnicaCoreUtils.getResourceResolver(resolverFactory,
                MACNICA_SUB_SERVICE);
        for (String path : paths) {
            Resource contentResource = serviceResolver.getResource(path);
            WorkFlowCaConfig config = contentResource.adaptTo(ConfigurationBuilder.class).as(WorkFlowCaConfig.class);
            logger.info("config => {}", config.hqGroup());
            hqList.add(config.hqGroup());
        }
        return hqList.stream().filter(StringUtils::isNotBlank).findFirst().get();
    }

    public static List<String> getPreviewPath(MetaDataMap metaDataMap){
        return getPreviewPaths(metaDataMap.get(CONTENT_ROOT_PATH));
    }

    public static List<String> getPreviewPath(com.day.cq.workflow.metadata.MetaDataMap metaDataMap) {
        return getPreviewPaths(metaDataMap.get(CONTENT_ROOT_PATH));
    }

    private static List<String> getPreviewPaths(Object obj) {
        ContentRootPathDto path;
        try {
            path = new Gson().fromJson(obj.toString(), ContentRootPathDto.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return path.getContentRootPaths();
    }

}
