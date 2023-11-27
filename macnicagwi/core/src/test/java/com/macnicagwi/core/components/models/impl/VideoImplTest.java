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

import com.macnicagwi.core.components.models.Video;
import com.macnicagwi.core.components.models.impl.VideoImpl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test Video Impl Model, Resource and Its All Methods
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
class VideoImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(VideoImplTest.class);
	private final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).plugin(CORE_COMPONENTS).build();
	private Video video;

	@BeforeEach
    public void setup() throws Exception {
    	context.addModelsForClasses(VideoImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/VideoImplTest.json", "/content");
		context.currentResource("/content/video");
		video = context.request().adaptTo(Video.class);
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(video, "Checking if resource is not null");
		String[] methods = new String[] { "isRestrictedVideo", "isValidRequest", "getType", "getUrl", "getResult", "getHtml", "getEmbeddableResourceType", "getExportedType" };

		for (String methodStr : methods) {
			try {
				Method method = video.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(video), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Video Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
