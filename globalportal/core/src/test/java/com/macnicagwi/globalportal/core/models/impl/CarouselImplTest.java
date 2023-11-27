package com.macnicagwi.globalportal.core.models.impl;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
class CarouselImplTest {

    @Mock
    com.adobe.cq.wcm.core.components.models.Carousel carousel;
    private CarouselImpl carouselImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        carouselImpl = Mockito.spy(new CarouselImpl());
        carouselImpl.carousel = carousel;
    }

    @Test
    void getAccessibilityLabel() {
        carouselImpl.getAccessibilityLabel();
    }

    @Test
    void getAutopauseDisabled() {
        carouselImpl.getAutopauseDisabled();
    }

    @Test
    void getAutoplay() {
        carouselImpl.getAutoplay();
    }

    @Test
    void getDelay() {
        carouselImpl.getDelay();
    }

    @Test
    void isControlsPrepended() {
        carouselImpl.isControlsPrepended();
    }

    @Test
    void getBackgroundStyle() {
        carouselImpl.getBackgroundStyle();
    }

    @Test
    void getExportedItems() {
        carouselImpl.getExportedItems();
    }

    @Test
    void getExportedItemsOrder() {
        carouselImpl.getExportedItemsOrder();
    }

    @Test
    void getItems() {
        carouselImpl.getItems();
    }

    @Test
    void getAppliedCssClasses() {
        carouselImpl.getAppliedCssClasses();
    }

    @Test
    void getData() {
        carouselImpl.getData();
    }

    @Test
    void getId() {
        carouselImpl.getId();
    }

    @Test
    void getScrollText() {
        carouselImpl.getScrollText();
    }

    @Test
    void getExportedType() {
        carouselImpl.getExportedType();
    }
}