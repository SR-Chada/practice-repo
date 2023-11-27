package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.commons.Externalizer;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.google.common.collect.ImmutableMap;


/**
 * Utility class to execute test cases related to Link based components
 * 
 * @author Sai
 */
public class MacnicaLinkTestUtils {
	public static final String ROOT = "https://example.org";
	  public static void assertValidLink(@NotNull Link link, @NotNull String linkURL) {
	        assertValidLink(link, linkURL, (SlingHttpServletRequest)null);
	    }

	    public static void assertValidLink(@NotNull Link link, @NotNull String linkURL,
	                                       @Nullable SlingHttpServletRequest request) {
	        assertTrue(link.isValid(), "linkValid");
	        assertEquals(ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
	        if (request != null && StringUtils.isNotEmpty(request.getContextPath()) && !linkURL.startsWith("http")) {
	            linkURL = request.getContextPath().concat(linkURL);
	        }
	        assertEquals(linkURL, link.getURL(), "linkUrl");
	        assertEquals(linkURL.replaceAll("^\\/content\\/links\\/site1\\/(.+)","/content/site1/$1"), link.getMappedURL(), "linkMappedUrl");
	        assertEquals(ImmutableMap.of("href", linkURL), link.getHtmlAttributes(), "linkHtmlAttributes");
	    }

	    public static void assertValidLink(@NotNull Link link, @NotNull String linkURL, @Nullable String linkTarget) {
	        if (linkTarget == null) {
	            assertValidLink(link,  linkURL);
	            return;
	        }
	        assertTrue(link.isValid(), "linkValid");
	        assertEquals(linkURL, link.getURL(), "linkUrl");
	        assertEquals(linkURL.replaceAll("^\\/content\\/links\\/site1\\/(.+)","/content/site1/$1"), link.getMappedURL(), "linkMappedUrl");
	        assertEquals(ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
	        assertEquals(ImmutableMap.of("href", linkURL, "target", linkTarget), link.getHtmlAttributes(), "linkHtmlAttributes");
	    }

	    public static void assertValidLink(@NotNull Link link, @NotNull String linkURL, @NotNull String linkAccessibilityLabel, @NotNull String linkTitleAttribute) {
	        assertTrue(link.isValid(), "linkValid");
	        assertEquals(linkURL, link.getURL(), "linkUrl");
	        assertEquals(linkURL, link.getMappedURL(), "linkMappedUrl");
	        assertEquals(ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
	        assertEquals(ImmutableMap.of("href", linkURL, "aria-label", linkAccessibilityLabel, "title", linkTitleAttribute), link.getHtmlAttributes(), "linkHtmlAttributes");
	    }

	    public static void assertValidLink(@NotNull Link link, @NotNull String linkURL, @NotNull String linkAccessibilityLabel, @NotNull String linkTitleAttribute, @Nullable String linkTarget) {
	        if (linkTarget == null) {
	            assertValidLink(link,  linkURL, linkAccessibilityLabel, linkTitleAttribute);
	            return;
	        }
	        assertTrue(link.isValid(), "linkValid");
	        assertEquals(linkURL, link.getURL(), "linkUrl");
	        assertEquals(linkURL, link.getMappedURL(), "linkMappedUrl");
	        assertEquals(ROOT + linkURL, link.getExternalizedURL(), "linkExternalizedUrl");
	        assertEquals(ImmutableMap.of("href", linkURL, "aria-label", linkAccessibilityLabel, "title", linkTitleAttribute, "target", linkTarget), link.getHtmlAttributes(), "linkHtmlAttributes");
	    }

	    public static void assertInvalidLink(@NotNull Link link) {
	        assertFalse(link.isValid(), "linkValid");
	        assertNull(link.getURL(), "linkURL");
	        assertNotNull(link.getHtmlAttributes(), "linkHtmlAttributes not null");
	        assertTrue(link.getHtmlAttributes().isEmpty(), "linkHtmlAttributes empty");
	    }
	    
	    public static Externalizer getExternalizerService() {
	        Externalizer externalizer = mock(Externalizer.class);
	        lenient().when(externalizer.publishLink(any(ResourceResolver.class), anyString())).then(
	                invocationOnMock -> ROOT + invocationOnMock.getArgument(1));
	        return externalizer;
	    }

}
