package com.macnicagwi.core.components.models;

import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;

/**
 * {@code BannerImageList} Sling Model used for the
 * {@code /apps/macnicagwi/components/content/bannerimagelist} component.
 * 
 * @author Sai
 */

@ConsumerType
public interface BannerImageList extends Component {

	/***
	 * @return Parent page path if configured in the dialogue of component, current
	 *         page path otherwise.
	 */

	public default String getParentPagePath() {
		return null;
	}

	/***
	 * @return List of Banner images.
	 */
	public List<BannerImage> getChildPageBanners();

}
