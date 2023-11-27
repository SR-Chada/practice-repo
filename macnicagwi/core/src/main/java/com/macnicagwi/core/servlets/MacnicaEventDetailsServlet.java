package com.macnicagwi.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sai This Servlet exposes Macncia Subsidiary sites Event detail page
 *         properties in JSON Format This is to be consumed in the front end for
 *         determining the event registration status.
 */
@Component(immediate = true, service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + MacnicaEventDetailsServlet.RESOURCE_TYPE,		
		"sling.servlet.selectors=" + MacnicaEventDetailsServlet.SELECTOR })
@ServiceDescription("Servlet for Exposing Macnica GWI Subsidiary sites event details in JSON Format")
public class MacnicaEventDetailsServlet extends SlingAllMethodsServlet {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaEventDetailsServlet.class);

	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Event Details page Resource Type
	 */
	public static final String RESOURCE_TYPE = "macnicagwi/components/page/eventpage";

	/**
	 * Event Details page Resource Type
	 */
	public static final String SELECTOR = "details";

	/**
	 * Event details page property names
	 */
	private static final String PN_EVENT_START_DATE = "startDate";
	private static final String PN_EVENT_END_DATE = "endDate";
	private static final String PN_EVENT_REGISTRATION_START_DATE = "startDateOfReg";
	private static final String PN_EVENT_REGISTRATION_END_DATE = "endDateOfReg";
	private static final String PN_EVENT_STATUS = "status";

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		final Resource resource = req.getResource();
		ValueMap eventDetails = resource.adaptTo(ValueMap.class);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		ObjectMapper mapper = new ObjectMapper();
		EventDetailsModel details = new EventDetailsModel();
		// TO-DO: Replace logic with object serialization
		details.setStartDate(eventDetails.get(PN_EVENT_START_DATE, String.class));
		details.setEndDate(eventDetails.get(PN_EVENT_END_DATE, String.class));
		details.setStartDateOfReg(eventDetails.get(PN_EVENT_REGISTRATION_START_DATE, String.class));
		details.setEndDateofReg(eventDetails.get(PN_EVENT_REGISTRATION_END_DATE, String.class));
		details.setStatus(eventDetails.get(PN_EVENT_STATUS, String.class));

		mapper.writeValue(resp.getWriter(), details);
	}

	@Getter
	@Setter
	@JsonInclude(Include.NON_NULL)
	class EventDetailsModel {
		private String startDate;
		private String endDate;
		private String startDateOfReg;
		private String endDateofReg;
		private String status;
	}

}
