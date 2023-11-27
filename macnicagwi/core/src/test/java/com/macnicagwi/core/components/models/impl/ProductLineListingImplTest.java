package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.ProductLineListing;
import com.macnicagwi.core.components.models.impl.ProductLineListingImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Product Line Listing Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class ProductLineListingImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLineListingImplTest.class);
	private final AemContext context = AppAemContext.newAemContext();
	private ProductLineListing productLineListing;
	 
	@BeforeEach
    public void setup() throws Exception {
    	context.addModelsForClasses(ProductLineListingImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/ProductLineListingImplTest.json", "/content");
		context.currentResource("/content/productlinelisting");
		productLineListing = context.request().adaptTo(ProductLineListing.class);
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(productLineListing, "Checking if resource is not null");
		String[] methods = new String[] { "getSearchPageRootPath", "getLoadMoreButtonText", "getSearchResultsBlockSize", "getNoResultsFoundText", "getResourcePath" };

		for (String methodStr : methods) {
			try {
				Method method = productLineListing.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(productLineListing), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Product Line Listing Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
