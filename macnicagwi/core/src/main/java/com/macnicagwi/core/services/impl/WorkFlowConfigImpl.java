package com.macnicagwi.core.services.impl;

import com.macnicagwi.core.config.WorkFlowConfig;
import com.macnicagwi.core.services.WorkFlowConfigService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = WorkFlowConfigService.class, immediate = true)
@Designate(ocd = WorkFlowConfig.class)
public class WorkFlowConfigImpl implements WorkFlowConfigService {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowConfigImpl.class);
    private String previewLinkURL;
    private String publishLinkURL;


    @Activate
    protected void activate(WorkFlowConfig configuration) {
        logger.info("----------< Reading the config values >----------");
        this.previewLinkURL = configuration.previewURL();
        this.publishLinkURL = configuration.publishURL();
    }

    @Override
    public String getPreviewURL() {
        return previewLinkURL;
    }

    @Override
    public String getPublishURL() {
        return publishLinkURL;
    }

}
