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
class ButtonListImplTest {

    @Mock
    com.adobe.cq.wcm.core.components.models.List list;
    private ButtonListImpl buttonListImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        buttonListImpl = Mockito.spy(new ButtonListImpl());
        buttonListImpl.list = list;
    }


    @Test
    void getListItems() {
        buttonListImpl.getListItems();

    }

    @Test
    void getExportedType() {
        buttonListImpl.getExportedType();
    }
}