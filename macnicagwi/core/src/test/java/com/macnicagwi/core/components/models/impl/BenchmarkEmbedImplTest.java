package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macnicagwi.core.components.models.BenchmarkEmbed;
import com.macnicagwi.core.components.models.impl.BenchmarkEmbedImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Benchmark Embed Impl Model, Resource and Its All Methods.
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class BenchmarkEmbedImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkEmbedImplTest.class);
    private final AemContext context = AppAemContext.newAemContext();
	private BenchmarkEmbed benchmarkEmbed;

	@BeforeEach			
    public void setup() throws Exception {
    	context.addModelsForClasses(BenchmarkEmbedImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/BenchmarkEmbedImplTest.json", "/content");
        context.currentResource("/content/benchmarkembed");
    	benchmarkEmbed = context.request().adaptTo(BenchmarkEmbed.class);
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(benchmarkEmbed, "Checking if resource is not null");
		String[] methods = new String[] { "getHtml" };
		
		for (String methodStr : methods) {
			try {
				Method method = benchmarkEmbed.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(benchmarkEmbed), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Benchmark Embed Impl Model Method {} :: {}", methodStr, e);
			}
		}
	}
}
