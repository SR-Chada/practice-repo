package com.macnicagwi.globalportal.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import com.macnicagwi.globalportal.core.models.NewsListItem;
import com.macnicagwi.globalportal.core.models.dtos.NewsList;
import com.macnicagwi.globalportal.core.models.impl.NewsListItemImpl;

@Component(service = Servlet.class, property = {
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + "globalportal/components/newslist",
        ServletResolverConstants.SLING_SERVLET_SELECTORS + "=" + "serv",
        ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=" + "json"
})
public class NewsListingServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 7762806638577903486L;
    private static final String TAG = NewsListingServlet.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsListingServlet.class);

    @Reference
    QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            LOGGER.info("Newslist Servlet hits {}");
            ObjectMapper mapper = new ObjectMapper();
            NewsList newsListResponse = getChildPages(request);

            response.getWriter().print(mapper.writeValueAsString(newsListResponse));
        } catch (IOException | RepositoryException ex) {
            LOGGER.error("Unable to write JSON {}", ex.getMessage());
        }
    }

    protected NewsList getChildPages(SlingHttpServletRequest request) throws RepositoryException {
        ValueMap valueMap = request.getResource().getValueMap();
        LOGGER.info("Newslist Servlet valuemap {}",valueMap);
        List<NewsListItem> childPagesList = new ArrayList<>();
        Map<String, String> map = new LinkedHashMap();
        map.put("path", valueMap.get("pagePath", ""));
        map.put("type", NameConstants.NT_PAGE);
        map.put("tagid", valueMap.get("tags", ""));
        map.put("tagid.property", "jcr:content/cq:tags");
        int offset = 0;
        String pageNumber = getQueryParameter(request, "pageNumber");
        String listLimit = valueMap.get("maxItems", "");

        if (StringUtils.isNumeric(pageNumber) && StringUtils.isNumeric(listLimit)) {
            int startIndex = Integer.parseInt(pageNumber) - 1;
            offset = Integer.parseInt(listLimit) * (startIndex);
        }
        map.put("p.limit", listLimit);
        map.put("p.offset", String.valueOf(offset));
        map.put("orderby", "@jcr:content/"+valueMap.get("orderBy", ""));
        map.put("orderby.sort", valueMap.get("sortOrder", ""));
        LOGGER.info("map Servlet query {}",map);
        Session session = request.getResourceResolver().adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult searchResult = query.getResult();

        long totalMatches = searchResult.getTotalMatches();
        NewsList newsList = new NewsList();
        newsList.setTotalResults(totalMatches);
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
        List<Hit> hits = searchResult.getHits();
        if (hits != null) {
            for (Hit hit : hits) {
                try {
                    Resource hitRes = hit.getResource();
                    Page page = pageManager.getContainingPage(hitRes);
                    if (page != null) {                    	
                        NewsListItemImpl item = page.adaptTo(NewsListItemImpl.class);
                        item.setLinkHandler(request.adaptTo(GlobalPortalLinkHandler.class));
                        LOGGER.info("searchResult item ============== {}",item);
                        childPagesList.add(item);
                    }
                } catch (RepositoryException e) {
                    LOGGER.error("Unable to retrieve search results for query.", e);
                }
            }
        }
        newsList.setNewsList(childPagesList);
        return newsList;
    }

    private String getQueryParameter(SlingHttpServletRequest request, String key) {
        LOGGER.debug("Key = {} & Value ={}", key, request.getParameter(key));
        return request.getParameter(key);
    }

}