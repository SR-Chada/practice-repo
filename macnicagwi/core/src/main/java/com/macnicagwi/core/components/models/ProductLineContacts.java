package com.macnicagwi.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import java.util.Collections;
import java.util.List;

import com.adobe.cq.wcm.core.components.models.Component;
import com.macnicagwi.core.components.dtos.ProductLineContactCfmDto;

/**
 * {@code ProductLineContacts} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/productlinecontacts} component.
 * @author Sumit
 */
@ConsumerType
public interface ProductLineContacts extends Component {
	
	/**
     * Returns the Product Lines.
     * @return the Product Lines
     */
    default List<String> getProductLines() {
        return Collections.emptyList();
    }

    /**
     * Returns the Product Line Contact Cfm Dto List.
     * @return the Product Line Contact Cfm Dto List
     */
    default List<ProductLineContactCfmDto> getProductLineContactCfmDtos() {
        return Collections.emptyList();
    }

}
