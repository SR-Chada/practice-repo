package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.Search;
import com.macnicagwi.core.components.models.impl.SearchImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Search Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class SearchImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchImplTest.class);
	private final AemContext context = AppAemContext.newAemContext();
	private Search search;
	 
	@BeforeEach
    public void setup() throws Exception {
    	context.addModelsForClasses(SearchImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/SearchImplTest.json", "/content");
		context.currentResource("/content/search");
		search = context.request().adaptTo(Search.class);
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(search, "Checking if resource is not null");
		String[] methods = new String[] { "getSearchPageRootPath", "getSearchAssetRootPath", "getLoadMoreButtonText", "getSearchResultsBlockSize", "getNoResultsFoundText", "getResourcePath", "getSearchResultsPagePath", "getSearchPlaceholder", "getDownloadFormFragmentPath" };

		for (String methodStr : methods) {
			try {
				Method method = search.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(search), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Search Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
