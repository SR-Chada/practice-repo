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
class TableImplTest {

    @Mock
    com.adobe.cq.wcm.core.components.models.Text text;
    private TableImpl tableImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        tableImpl = Mockito.spy(new TableImpl());
        tableImpl.text = text;
    }

    @Test
    void getText() {
        tableImpl.getText();
    }

    @Test
    void getRichText() {
        tableImpl.getRichText();
    }

    @Test
    void getAppliedCssClasses() {
        tableImpl.getAppliedCssClasses();
    }

    @Test
    void getExportedType() {
        tableImpl.getExportedType();
    }
}