package com.macnicagwi.core.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
class MacnicaEventDetailsServletTest {

	AemContext context = new AemContext();

	MacnicaEventDetailsServlet macnicaEventDetailsServlet = new MacnicaEventDetailsServlet();

	@Test
	void doGet() throws ServletException, IOException {

		context.build()
				.resource("/content/test/macnica", "startDate", "2022-04-15T00:00:00.000+05:30", "endDate",
						"2022-04-15T00:00:00.000+05:30", "startDateOfReg", "2022-04-15T00:00:00.000+05:30",
						"endDateOfReg", "2022-04-15T00:00:00.000+05:30", "status", "properties:style/color")
				.commit();
		context.currentResource("/content/test/macnica");

		MockSlingHttpServletRequest request = context.request();
		MockSlingHttpServletResponse response = context.response();

		macnicaEventDetailsServlet.doGet(request, response);

		assertEquals(
				"{\"startDate\":\"2022-04-15T00:00:00.000+05:30\",\"endDate\":\"2022-04-15T00:00:00.000+05:30\",\"startDateOfReg\":\"2022-04-15T00:00:00.000+05:30\",\"endDateofReg\":\"2022-04-15T00:00:00.000+05:30\",\"status\":\"properties:style/color\"}",
				response.getOutputAsString());

	}

}
