package com.macnicagwi.globalportal.core.models.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.macnicagwi.globalportal.core.models.GlobalPortalLinkHandler;
import com.macnicagwi.globalportal.core.models.NewsListItem;



@Model(adaptables = {SlingHttpServletRequest.class, Page.class}, adapters = {
        NewsListItem.class,}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsListItemImpl implements NewsListItem {

    @Self
    Page currentPage;   
       
    @Self
    private Resource resource;
    
    @Inject
    SlingHttpServletRequest request;
    

    private String pageTitle;
    
    private Date articleDate;
    
    private GlobalPortalLinkHandler linkHandler;

    private String date;
    

    public void setDate(String date) {
        this.date = date;
    }

    private List<String> tags;

    private String pageLink;
    
    private Link<Page> link;
    
    private static final Logger LOG = LoggerFactory.getLogger(NewsListItemImpl.class);

    public void setLinkHandler(GlobalPortalLinkHandler linkHandler) {
    	this.linkHandler=linkHandler;
    }


    @PostConstruct
    protected void init() {
    	
    	
    	LOG.info("Constructing News list item , request {}, resource, {}, LinkHandler {}", request,resource,linkHandler);
        pageTitle = currentPage.getTitle();
        tags = Arrays.stream(currentPage.getTags()).map(Tag::getTitle).collect(Collectors.toList());
        try {
        articleDate = Optional.ofNullable(currentPage.getProperties().get("articleDate", Calendar.class).getTime()).orElse(null);
        date = Optional.ofNullable(getFormattedDate(articleDate)).orElse(StringUtils.EMPTY);
        } catch(Exception e) {
        	LOG.error("Unable to format date of news article",e);
        	date = currentPage.getProperties().get("articleDate", "");
        	
        }
        pageLink = currentPage.getPath();
        
       


    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public String getDate() {
        return date;
    }
    
    @Override
    public Link<Page> getLink() {
        if(linkHandler!=null) {
        link = linkHandler.getLink(currentPage).orElse(null);
        } else {
        	link = null;
        }
    	return link;
    }

    @Override
    public String getPageLink() {
        return pageLink;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    private  String getFormattedDate(Date articleDate) {
    	try {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd,yyyy");
        return sdfDate.format(articleDate);}
    	catch(Exception e) {
    		LOG.error("Unable to format news article date. ", e);
    	}
    	return StringUtils.EMPTY;
    }



}
