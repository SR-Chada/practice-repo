package com.macnicagwi.core.components.models;

import java.util.List;
import java.util.Map;

import com.adobe.cq.wcm.core.components.models.Component;
import com.day.cq.tagging.Tag;

import lombok.Getter;


/**
 * {@code MultiLevelFilter} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/multiLevelfilter} component.
 * @author Sai
 */
public interface MultiLevelFilter extends Component {
	
	public boolean isEmpty();
	public boolean isfreeTextSearchEnabled();
	public String getFilterButtonText();
	public String getApplyButtonText(); 
	public String getClearButtonText(); 
	public String getNoResultsFoundText(); 
	public String getLoadMoreButtonText(); 
	public String getAllButtonText();
	public String getFreeTextSearchPlaceholder();
	
	public Map<MacnicaFilterItem, List<MacnicaFilterItem>> getFilterItemMap();	
	
	
	interface MacnicaFilterItem{
		
		public String getTitle();
		public String getId();
		public String getPath();
		public Tag getTag();
		
	}


	

}
