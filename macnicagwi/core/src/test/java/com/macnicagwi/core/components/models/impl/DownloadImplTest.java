package com.macnicagwi.core.components.models.impl;

import static com.adobe.cq.wcm.core.components.testing.mock.ContextPlugins.CORE_COMPONENTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.Download;
import com.macnicagwi.core.components.models.impl.DownloadImpl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Download Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class DownloadImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchImplTest.class);
	private final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).plugin(CORE_COMPONENTS).build();
	private Download download;

	@BeforeEach			
    public void setup() throws Exception {
    	context.addModelsForClasses(DownloadImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/DownloadImplTest.json", "/content");
        context.currentResource("/content/download");
    	download = context.request().adaptTo(Download.class);
    }
    
    @Test
	public void testLoadAndGetters() {
		assertNotNull(download, "Checking if resource is not null");
		String[] methods = new String[] { "getResourcePath", "getDownloadFormFragmentPath", "getUrl", "getFilename", "getSize", "getFormat", "displaySize", "displayFormat", "displayFilename", "getExportedType" };

		for (String methodStr : methods) {
			try {
				Method method = download.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(download), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Download Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}    
}
