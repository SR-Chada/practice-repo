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

import com.macnicagwi.core.components.models.Container;
import com.macnicagwi.core.components.models.impl.ContainerImpl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Container Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class ContainerImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerImplTest.class);
	private final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).plugin(CORE_COMPONENTS).build();
	private Container container;

	@BeforeEach
    public void setup() throws Exception {
    	context.addModelsForClasses(ContainerImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/ContainerImplTest.json", "/content");
		context.currentResource("/content/container");
		container = context.request().adaptTo(Container.class);
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(container, "Checking if resource is not null");
		String[] methods = new String[] { "getSuccessFragmentPath", "getErrorFragmentPath", "getMethod", "getAction", "getId", "getName", "getEnctype", "getResourceTypeForDropArea", "getRedirect", "getErrorMessages", "getExportedItemsOrder", "getExportedItems", "getExportedType" };

		for (String methodStr : methods) {
			try {
				Method method = container.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(container), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Container Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
