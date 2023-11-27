package com.macnicagwi.globalportal.core.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragmentList;

public interface MilestonesList extends ContentFragmentList {

	public default Map<Integer, List<MilestoneItem>> getMilestoneMap(){
		return Collections.emptyMap();
	}

}
