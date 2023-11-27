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
class HeroCarouselSlideImplTest {

    @Mock
    Image image;
    private HeroCarouselSlideImpl heroCarouselSlideImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        heroCarouselSlideImpl = Mockito.spy(new HeroCarouselSlideImpl());
        heroCarouselSlideImpl.image = image;
    }

    @Test
    void getSrc() {
        heroCarouselSlideImpl.getSrc();
    }

    @Test
    void getSrcset() {
        heroCarouselSlideImpl.getSrcset();
    }

    @Test
    void getImageLink() {
        heroCarouselSlideImpl.getImageLink();
    }

    @Test
    void getAppliedCssClasses() {
        heroCarouselSlideImpl.getAppliedCssClasses();
    }

    @Test
    void getId() {
        heroCarouselSlideImpl.getId();

    }

    @Test
    void getAlt() {
        heroCarouselSlideImpl.getAlt();
    }

    @Test
    void getLevelOneTitle() {
        heroCarouselSlideImpl.getLevelOneTitle();
    }

    @Test
    void getLevelTwoTitle() {
        heroCarouselSlideImpl.getLevelTwoTitle();
    }

    @Test
    void getExportedType() {
        heroCarouselSlideImpl.getExportedType();
    }
}