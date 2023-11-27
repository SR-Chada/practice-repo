package com.macnicagwi.globalportal.core.models.impl;

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
class TitleImplTest {

    @Mock
    com.adobe.cq.wcm.core.components.models.Title title;
    private TitleImpl titleImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        titleImpl = Mockito.spy(new TitleImpl());
        titleImpl.title = title;
    }

    @Test
    void getText() {
        titleImpl.getText();

    }

    @Test
    void getType() {
        titleImpl.getType();
    }

    @Test
    void getId() {
        titleImpl.getId();
    }

    @Test
    void getPretitle() {
        titleImpl.getPretitle();
    }

    @Test
    void getBulletPoint() {
        titleImpl.getBulletPoint();
    }

    @Test
    void getUnderline() {
        titleImpl.getUnderline();
    }

    @Test
    void getLink() {
        titleImpl.getLink();
    }

    @Test
    void isLinkDisabled() {
        titleImpl.isLinkDisabled();
    }

    @Test
    void getAppliedCssClasses() {
        titleImpl.getAppliedCssClasses();
    }

    @Test
    void getExportedType() {
        titleImpl.getExportedType();
    }
}