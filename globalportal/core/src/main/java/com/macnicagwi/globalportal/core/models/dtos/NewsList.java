package com.macnicagwi.globalportal.core.models.dtos;

import com.macnicagwi.globalportal.core.models.NewsListItem;
import com.macnicagwi.globalportal.core.models.impl.NewsListItemImpl;

import java.util.ArrayList;
import java.util.List;

public class NewsList {

    private long totalResults;
    private List<NewsListItem> newsList = new ArrayList<>();

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsListItem> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsListItem> newsList) {
        this.newsList = newsList;
    }
}
