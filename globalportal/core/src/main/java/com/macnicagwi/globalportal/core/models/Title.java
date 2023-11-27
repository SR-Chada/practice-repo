package com.macnicagwi.globalportal.core.models;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Title extends com.adobe.cq.wcm.core.components.models.Title {

    public String getPretitle();

    public String getBulletPoint();

    public String getUnderline();

}
