package com.macnicagwi.core.components.models.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Image;
import com.macnicagwi.core.components.models.BannerImage;
import com.macnicagwi.core.components.models.impl.BannerImageImpl;
import com.macnicagwi.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


/**
 * Test Banner Image Impl Model, Resource and Its All Methods.
 * 
 * @author Sumit Kumar Agarwal
 */
@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
class BannerImageImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BannerImageImplTest.class);
	private final AemContext context = AppAemContext.newAemContext();
	private BannerImage bannerImage;

	@Mock
    private Image image;

    @Mock
    private ModelFactory modelFactory;
	 
	@BeforeEach
    public void setup() throws Exception {
    	context.addModelsForClasses(BannerImageImpl.class);
    	context.load().json("/com/macnicagwi/core/components/models/BannerImageImplTest.json", "/content");
		lenient().when(modelFactory.getModelFromWrappedRequest(eq(context.request()), any(Resource.class), eq(Image.class)))
                .thenReturn(image);
        context.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING,
                Integer.MAX_VALUE);
		context.currentResource("/content/bannerimage");
		bannerImage = context.request().adaptTo(BannerImage.class);
		when(image.getSrc()).thenReturn("");
    }

	@Test
	public void testLoadAndGetters() {
		assertNotNull(bannerImage, "Checking if resource is not null");
		String[] methods = new String[] { "getSubText", "getImage", "isEmpty", "isPopup" };

		for (String methodStr : methods) {
			try {
				Method method = bannerImage.getClass().getMethod(methodStr);
				assertNotNull(method.invoke(bannerImage), "Checking All Getters of the model class");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in Banner Image Impl Test Method {} :: {}", methodStr, e);
			}
		}
	}
}
