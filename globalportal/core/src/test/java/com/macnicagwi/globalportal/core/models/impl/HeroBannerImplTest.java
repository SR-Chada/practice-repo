package com.macnicagwi.globalportal.core.models.impl;

import com.adobe.cq.wcm.core.components.models.Image;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
class HeroBannerImplTest {

    @Mock
    Image image;
    private HeroBannerImpl heroBannerImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        heroBannerImpl = Mockito.spy(new HeroBannerImpl());
        heroBannerImpl.image = image;
    }

    @Test
    void getSrc() {
        heroBannerImpl.getSrc();
    }

    @Test
    void getSrcset() {
        heroBannerImpl.getSrcset();
    }

    @Test
    void getAlt() {
        heroBannerImpl.getAlt();
    }

    @Test
    void getExportedType() {
        heroBannerImpl.getExportedType();
    }
}