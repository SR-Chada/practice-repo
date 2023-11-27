package com.macnicagwi.core.models;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


@ExtendWith(AemContextExtension.class)
class MacnicaLinkHandlerTest {
	
    private final AemContext context = AppAemContext.newAemContext();

    private Page page;
    private MacnicaLinkHandler linkHandler;
    
    @BeforeEach
    void setUp() {
        page = context.create().page("/content/links/macnicagwi/en/");
        context.currentPage(page);
        linkHandler = context.request().adaptTo(MacnicaLinkHandler.class);
    }

	@Test
	void testGetLinkResource() {
        Optional<Link<Page>> link = linkHandler.getLink(page);
        String actual = link.get().getURL();
        String expected = page.getPath()+".html";        
        assertEquals(expected, actual);
	}


}
