package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.ManufacturerListing;
import com.macnicagwi.core.components.models.impl.ManufacturerListingImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Manufacturer Listing Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class ManufacturerListingImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerListingImplTest.class);
	private final AemContext context = AppAemContext.newAemContext();
	private ManufacturerListing manufacturerListing;
	 
	@BeforeEach
    public void setup() throws Exception {
    	context.addModelsForClasses(ManufacturerListingImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/ManufacturerListingImplTest.json", "/content");
		context.currentResource("/content/manufacturerlisting");
		manufacturerListing = context.request().adaptTo(ManufacturerListing.class);
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(manufacturerListing, "Checking if resource is not null");
		String[] methods = new String[] { "getSearchPageRootPath", "getFilterType", "getFilterRootPaths", "isFreeTextSearch", "getFreeTextSearchPlaceholder", "getApplyButtonText", "getClearButtonText", "getLoadMoreButtonText", "getSearchResultsBlockSize", "getFilterButtonText", "getNoResultsFoundText", "getResourcePath", "getFacetSearchRequestDto" };

		for (String methodStr : methods) {
			try {
				Method method = manufacturerListing.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(manufacturerListing), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Manufacturer Listing Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
