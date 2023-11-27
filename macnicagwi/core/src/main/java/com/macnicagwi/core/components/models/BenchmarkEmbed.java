package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;


/**
 * {@code BenchmarkEmbed} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/benchmarkembed} component.
 * @author Vijay
 */
@ConsumerType
public interface BenchmarkEmbed extends Component {

    /**
     * Return Free Html code
     * @return Free Html code
     */
    String getHtml();
    
}
