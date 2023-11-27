package com.macnicagwi.globalportal.core.models;

import com.adobe.cq.wcm.core.components.models.Image;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface HeroCarouselSlideModel extends Image {

    public String getLevelOneTitle();
    public String getLevelTwoTitle();
    

}