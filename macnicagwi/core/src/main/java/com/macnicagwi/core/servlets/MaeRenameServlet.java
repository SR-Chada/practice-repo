package com.macnicagwi.core.servlets;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = Servlet.class, property = {
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/mae-rename" })
public class MaeRenameServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 7762806638577903486L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MaeRenameServlet.class);

	@Reference
	QueryBuilder queryBuilder;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		LOGGER.info("Servlet execution started. ");
		Session session = request.getResourceResolver().adaptTo(Session.class);

		// 1. Search for all nodes containing /content/globalportal
		Map<String, String> predicates = new HashMap<>();
		predicates.put("path", "/content/macnicagwi/europe/atd-europe/");
		predicates.put("p.limit", "-1");
		predicates.put("fulltext", "\"/eu/atd-europe/\"");

		Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
		
		
		SearchResult result = query.getResult();
		StringBuilder outputLog = new StringBuilder();
		for (Hit hit : result.getHits()) {
			try {
				String path = hit.getPath();
				Resource resource = hit.getResource();
				resource.getValueMap();
				ResourceResolver rr = resource.getResourceResolver();
				ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
				String resourceType = resource.getResourceType();
				
				
				
				  if(resourceType.contains("relatedsuggestions")) { 
				  String searchIn = properties.get("searchIn",String.class);
				  if(StringUtils.isNoneBlank(searchIn)) {
					  searchIn = searchIn.replace("/eu/atd-europe/", "/europe/atd-europe/");
					  properties.put("searchIn",searchIn); 
					  }
					  
				  
				  String parentPage = properties.get("parentPage",String.class);
				  if(StringUtils.isNoneBlank(parentPage)) {
					  parentPage = parentPage.replace("/eu/atd-europe/", "/europe/atd-europe/");
					  properties.put("parentPage",parentPage); 
					  
				  }
				  String[] pages = properties.get("pages",String[].class);
				  if(pages.length>=0) {
					  for(int i=0;i<pages.length;i++) {
						  pages[i] = pages[i].replace("/eu/atd-europe/", "/europe/atd-europe/");
					  }
					  properties.put("pages",pages); 
				  }
				   rr.commit();
				  }
				  
				  
				  
				  if(resourceType.contains("resizableimage")) { String fileReference =
				  properties.get("fileReference",String.class); String linkURL =
				  properties.get("linkURL",String.class);
				  if(StringUtils.isNoneBlank(fileReference)) { fileReference =
				  fileReference.replace("/eu/atd-europe/", "/europe/atd-europe/");
				  properties.put("fileReference",fileReference); }
				  if(StringUtils.isNoneBlank(linkURL)) { linkURL =
				  linkURL.replace("/eu/atd-europe/", "/europe/atd-europe/");
				  properties.put("linkURL",linkURL);
				  
				  } rr.commit();
				  
				  }
				  
				  
				  
				  if(resourceType.contains("text") ||resourceType.contains("table") ) { String
				  text = properties.get("text",String.class);
				  
				  if(StringUtils.isNoneBlank(text)) { text = text.replace("/eu/atd-europe/",
				  "/europe/atd-europe/"); properties.put("text",text); } rr.commit();
				  
				  }
				 
				
				outputLog.append(""+hit.getPath()+"\t"+resourceType);
				outputLog.append("+\n");
			} catch (Exception e) {
				LOGGER.error("Error in updating url ",e);
				e.printStackTrace();
			}

		}

		// 2. Iterate through relults
		// 3. Replace /content/globalportal with /content/global
		// 4. Save the change
		// 5. Output the containing page path to log
		response.getWriter().write("Servlet executed successfully. Results" + outputLog);
	}

}