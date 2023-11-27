package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import com.macnicagwi.core.components.models.impl.MacnicaPageListItemImpl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class MacnicaPageListItemImplTest {

	
	private final AemContext context = AppAemContext.newAemContext();

    private Page page;
    private MacnicaLinkHandler linkHandler;
    
    @Test
    void setUp() {
        page = context.create().page("/content/links/macnicagwi/en/","/conf/macnicagwi/settings/wcm/templates/news-page-content","jcr:title","en","description","page description");
        
        context.currentPage(page);
        linkHandler = context.request().adaptTo(MacnicaLinkHandler.class);
        MacnicaPageListItemImpl macnicaPageListItemImpl  = new MacnicaPageListItemImpl(linkHandler,page,"parentId");
        assertNotNull(macnicaPageListItemImpl);
        assertNotNull(macnicaPageListItemImpl.getPath());
        assertNotNull(macnicaPageListItemImpl.getTitle());
        
     }

}
