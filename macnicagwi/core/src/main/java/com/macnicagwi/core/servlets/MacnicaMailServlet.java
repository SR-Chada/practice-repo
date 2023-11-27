package com.macnicagwi.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;


@Component(
		  service = { Servlet.class },
		  property = {
		    SLING_SERVLET_PATHS + "=/bin/sendemail"
		  }
		)
public class MacnicaMailServlet extends SlingAllMethodsServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 @Reference
	  private MessageGatewayService messageGatewayService;
	 private final Logger logger = LoggerFactory.getLogger(getClass());

	  @Override
	  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		 logger.info("Test mail servlet triggered.");
	    JSONObject jsonResponse = new JSONObject();
	    boolean sent = false;
	    try {
	      String[] recipients = { "sai.g@1digitals.com" };
	      sendEmail("This is an test email",
	        "This is the test email body", recipients);
	      response.setStatus(200);
	      sent = true;
	    } catch (EmailException e) {
	    	logger.error("Error in sending test mail",e);
	      response.setStatus(500);
	    }
	    try {
	      jsonResponse.put("result", sent ? "done" : "something went wrong");
	    } catch (Exception e) {
	    	logger.error("Error in sending test mail",e);
	    }
	    response.getWriter().write(jsonResponse.toString());
	  }

	  private void sendEmail(String subjectLine, String msgBody, String[] recipients) throws EmailException {
	    Email email = new HtmlEmail();
	    for (String recipient : recipients) {
	      email.addTo(recipient, recipient);
	    }
	    email.setSubject(subjectLine);
	    email.setMsg(msgBody);
	    MessageGateway<Email> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
	    if (messageGateway != null) {
	    	logger.debug("sending out email");
	      messageGateway.send((Email) email);
	    } else {
	    	logger.error("The message gateway could not be retrieved.");
	    }
	  }
}
