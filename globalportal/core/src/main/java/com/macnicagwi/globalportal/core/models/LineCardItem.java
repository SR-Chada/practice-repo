package com.macnicagwi.globalportal.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

public class LineCardItem {
	private static final Logger LOG = LoggerFactory.getLogger(LineCardItem.class);
	
	private String category;
	private String logo;
	private String description;
	private String link;
	
	public LineCardItem() {
	//do nothing	
	}
	
	public LineCardItem(Resource resource, TagManager tagManager) {
		ContentFragment lineCard = resource.adaptTo(ContentFragment.class);
		String categoryTagId = lineCard.getElement("categoryTag").getContent();
		LOG.info("Category Tag ID: {} Tag",categoryTagId);
		this.logo= lineCard.getElement("logo").getContent();
		this.description = lineCard.getElement("cardDescription").getContent();
		this.link = lineCard.getElement("ctaLink").getContent();
		Tag categoryTag = tagManager.resolve(categoryTagId);
		this.category = categoryTag==null?StringUtils.EMPTY:categoryTag.getTitle();
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	

}
