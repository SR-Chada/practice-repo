package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.Heading;
import com.macnicagwi.core.components.models.impl.HeadingImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Heading Impl Model, Resource and Its All Methods.
 * 
 * 
 */
@ExtendWith(AemContextExtension.class)
class HeadingImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadingImplTest.class);
    private final AemContext context = AppAemContext.newAemContext();
	private Heading heading;
	private final AemContext cSSClassNameContext = AppAemContext.newAemContext();

	@BeforeEach			
    public void setup() throws Exception {
    	context.addModelsForClasses(HeadingImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/HeadingImplTest.json", "/content");
        context.currentResource("/content/heading");
    	heading = context.request().adaptTo(Heading.class);
    }
    
	@Test
	public void testLoadAndGetters() {
		assertNotNull(heading, "Checking if resource is not null");
		String[] methods = new String[] { "getText", "getType", "getIcon", "getCssClassName", "getExportedType","getComponentData"};
		
		for (String methodStr : methods) {
			try {
				Method method = heading.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(heading), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Heading Impl Model Test Method {} :: {}", methodStr, e);
			}
		}
	}
	
	
	@Test
	public void testBlankCssClassName() {

		final String expected = "";
		cSSClassNameContext.addModelsForClasses(HeadingImpl.class);
		cSSClassNameContext.load().json(
				"/com/macnicagwi/core/components/models/HeadingImplCssClassTest.json",
				"/content");
		cSSClassNameContext.currentResource("/content/heading");
		Heading heading = cSSClassNameContext.request().adaptTo(Heading.class);
		String actual = heading.getCssClassName();
		assertEquals(expected, actual);

	}
}
