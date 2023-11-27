package com.macnicagwi.core.servlets;

import static com.macnicagwi.core.servlets.MacnicaDownloadAssetFormServlet.ASSET_DOWNLOAD_REQUEST_COOKIE_NAME;
import static com.macnicagwi.core.servlets.MacnicaDownloadAssetFormServlet.ASSET_DOWNLOAD_REQUEST_COOKIE_VALUE;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletName;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.macnicagwi.core.components.models.Download;
import com.macnicagwi.core.components.models.impl.DownloadImpl;
import com.macnicagwi.core.utils.MacnicaComponentUtils;
import com.macnicagwi.core.utils.MacnicaFormUtils;

/**
 * Macnica Download Component - Asset Download Request Servlet
 * Validates incoming Asset Download Request and if valid request returns asset file
 * @author Sumit
 */
@Component(
	name = "Macnica Download Component - Asset Download Request Servlet", 
	immediate = true, 
	service = { Servlet.class })
@SlingServletResourceTypes(
	resourceTypes = { DownloadImpl.RESOURCE_TYPE }, 
	methods = { HttpConstants.METHOD_GET }, 
	extensions = {ExporterConstants.SLING_MODEL_EXTENSION})
@SlingServletName(
	servletName = "Macnica Download Component - Asset Download Request Servlet")
public class DownloadComponentAssetDownloadRequestServlet extends SlingSafeMethodsServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadComponentAssetDownloadRequestServlet.class);
	private static final String ASSET_METADATA_RESTRICTED_DOWNLOAD_PROPERTY = "jcr:content/metadata@isRestrictedDownload";
	private static final String CORE_ASSET_DOWNLOAD_SERVLET_SELECTOR = "coredownload";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		try {
			Download downloadComponent = request.adaptTo(Download.class);
			Asset asset = downloadComponent.getAsset();
			boolean assetDownloadRestricted = Boolean.parseBoolean(MacnicaComponentUtils.getAssetPropertyValue(asset, ASSET_METADATA_RESTRICTED_DOWNLOAD_PROPERTY));
			
			if (assetDownloadRestricted) {
				boolean isValidRequest = MacnicaFormUtils.validateRequest(request, ASSET_DOWNLOAD_REQUEST_COOKIE_NAME, ASSET_DOWNLOAD_REQUEST_COOKIE_VALUE);
				
				if (!isValidRequest) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			}

    		final RequestDispatcherOptions options = new RequestDispatcherOptions();
    		options.setReplaceSelectors(CORE_ASSET_DOWNLOAD_SERVLET_SELECTOR);
    		options.setForceResourceType(DamConstants.NT_DAM_ASSET);

   			request.getRequestDispatcher(asset.getPath(), options).forward(request, response);     
	    }
		catch (Exception e) {
			LOGGER.error("Exception in Macnica Download Component - Asset Download Request Servlet: ", e);
	    } 
	}

}
