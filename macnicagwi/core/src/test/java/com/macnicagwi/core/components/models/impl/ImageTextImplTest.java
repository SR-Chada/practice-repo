package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.ImageText;
import com.macnicagwi.core.components.models.impl.ImageTextImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Image Text Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class ImageTextImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTextImplTest.class);
    private final AemContext context = AppAemContext.newAemContext();
    private ImageText imageText;
	 
	@BeforeEach			
    public void setup() throws Exception {
    	context.addModelsForClasses(ImageTextImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/ImageTextImplTest.json", "/content");
        context.currentResource("/content/imagetext");
    	imageText = context.request().adaptTo(ImageText.class);
    }
    
	@Test
	public void testLoadAndGetters() {
		assertNotNull(imageText, "Checking if resource is not null");
		String[] methods = new String[] { "isText", "isButton", "isHeading", "getAssetType", "getAssetPositionLargeScreen", "getAssetPositionMediumScreen", "getAssetPositionSmallScreen" };
		
		for (String methodStr : methods) {
			try {
				Method method = imageText.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(imageText), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Image Text Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
