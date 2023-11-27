package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.components.models.impl.MacnicaLinkImpl.ATTR_ARIA_LABEL;
import static com.macnicagwi.core.components.models.impl.MacnicaLinkImpl.ATTR_TARGET;
import static com.macnicagwi.core.components.models.impl.MacnicaLinkImpl.ATTR_TITLE;
import static com.macnicagwi.core.components.models.impl.MacnicaLinkTestUtils.ROOT;
import static com.macnicagwi.core.components.models.impl.MacnicaLinkTestUtils.assertValidLink;
import static com.macnicagwi.core.components.models.impl.MacnicaLinkTestUtils.assertInvalidLink;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.internal.link.LinkImpl;
import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;



/**
 * Test class for Macnica Link Impl Model.
 * 
 * @author Sai
 */
class MacnicaLinkImplTest {

	private static final String URL = "/url.html";

	@Test
	void testMacnicaLinkImpl() {
		Link<Page> link = new MacnicaLinkImpl<>(URL, URL, ROOT + URL, null, null);
		assertValidLink(link, URL);
        assertNull(link.getReference());
        assertEquals(URL, link.getMappedURL());
	}

    @Test
    void testValidLinkWithTarget() {
        Link<Page> link = new MacnicaLinkImpl(URL, URL, ROOT + URL, null,
                new HashMap<String, String>() {{ put(ATTR_TARGET, "_blank"); }});
        assertValidLink(link, URL, "_blank");
        assertNull(link.getReference());
    }
    
    @Test
    void testValidLinkWithoutTarget() {
        Link link = new MacnicaLinkImpl(URL, URL, ROOT + URL,null, null);

        assertValidLink(link, URL, (String)null);
        assertNull(link.getReference());
    }
    
    @Test
    void testValidLinkWithTargetAndTargetPage() {
        Page page = mock(Page.class);
        Link<Page> link = new LinkImpl<>(URL, URL, ROOT + URL, page,
                new HashMap<String, String>() {{ put(ATTR_TARGET,
                "_blank"); }});
        assertValidLink(link, URL, "_blank");
        assertSame(page, link.getReference());
    }
    
    @Test
    void testValidLinkWithTargetTargetPageAccessibilityLabelAndTitleAttribute() {
        Page page = mock(Page.class);
        Link<Page> link = new LinkImpl<>(URL, URL, ROOT + URL, page, new HashMap<String, String>() {{
            put(ATTR_TARGET, "_blank");
            put(ATTR_ARIA_LABEL,  "Url Label");
            put(ATTR_TITLE, "Url Title");
        }});

        assertValidLink(link, URL, "Url Label", "Url Title", "_blank");
        assertSame(page, link.getReference());
    }
    
    @Test
    void testValidLinkWithTargetPageAccessibilityLabelTitleAttributeAndWithoutTarget() {
        Page page = mock(Page.class);
        Link<Page> link = new LinkImpl<>(URL, URL, ROOT + URL, page, new HashMap<String, String>() {{
            put(ATTR_ARIA_LABEL, "Url Label");
            put(ATTR_TITLE, "Url Title");
        }});

        assertValidLink(link, URL, "Url Label", "Url Title", null);
        assertSame(page, link.getReference());
    }

    @Test
    void testInvalidLink() {
        Link<Page> link = new LinkImpl<>(null, null, null, null, null);
        assertInvalidLink(link);
        assertNull(link.getReference());
        assertNull(link.getMappedURL());
    }

    @Test
    void testValidLikWithFilteredHtmlAttributes() {
        Page page = mock(Page.class);
        String invalidAttribute = "invalidAttribute";
        Link<Page> link = new LinkImpl<>(URL, URL, ROOT + URL, page, ImmutableMap.of(invalidAttribute,
                "invalidValue"));
        assertValidLink(link, URL);
        assertNull(link.getHtmlAttributes().get(invalidAttribute));
    }



    


}
