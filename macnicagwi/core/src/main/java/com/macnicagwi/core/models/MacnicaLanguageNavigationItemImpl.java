package com.macnicagwi.core.models;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MacnicaLanguageNavigationItemImpl implements com.adobe.cq.wcm.core.components.models.LanguageNavigationItem{

	 	protected String title;
	    protected Locale locale;
	    protected String country;
	    protected String language;
	    private Page page;
	    protected int level;
	    protected boolean active;
	    private boolean current;
	    protected List<NavigationItem> children = Collections.emptyList();
	    protected Optional<Link<Page>> link;

	    
	    public MacnicaLanguageNavigationItemImpl(Page page, boolean active, boolean current,
				MacnicaLinkHandler linkHandler, int level, List<NavigationItem> children, String title,
				String parentId, Component component) {
					this.title = title;
					this.active = active;
			        this.current = current;
			        this.level = level;
			        this.children = children;
			        this.link = linkHandler.getLink(page);
			        if (this.link.isPresent()) {
			            this.page = link.get().getReference();
			        } else {
			            this.page = page;
			        }
		}

		@Override
	    public String getTitle() { 
	    	return title; 
	    }

	    @Override
	    public Locale getLocale() {
	        if (locale == null) {
	            // looks up jcr:language properties to the root, then considers the page name, falls back to system default
	            // we therefore assume the language structure is correctly configured for the site for this to be accurate
	            locale = page.getLanguage(false);
	        }
	        return locale;
	    }

	    @Override
	    public String getCountry() {
	        if (country == null) {
	            country = page.getLanguage(false).getCountry();
	        }
	        return country;
	    }

	    @Override
	    public String getLanguage() {
	        if (language == null) {
	            // uses hyphens to ensure it's hreflang valid
	            language = page.getLanguage(false).toString().replace('_', '-');
	        }
	        return language;
	    }
	    
	    @Override
	    public int getLevel() {
	        return level;
	    }
	    
	    @Override
	    public boolean isActive() {
	        return active;
	    }

	    @Override
	    public boolean isCurrent() {
	        return current;
	    }

	    @Override
	    public List<NavigationItem> getChildren() {
	        return children;
	    }
	    
	    @Override
	    @JsonIgnore
	    @Nullable
	    public Link<Page> getLink() {
	        return link.orElse(null);
	    }

	    @Override
	    public String getURL() {
	        return link.map(Link::getURL).orElse(null);
	    }
	    	    
}
