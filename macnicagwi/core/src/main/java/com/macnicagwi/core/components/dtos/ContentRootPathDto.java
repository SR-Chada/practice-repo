package com.macnicagwi.core.components.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentRootPathDto {

    @SerializedName("contentRootPaths")
    @Expose
    private List<String> contentRootPaths = null;

    public List<String> getContentRootPaths() {
        return contentRootPaths;
    }

    public void setContentRootPaths(List<String> contentRootPaths) {
        this.contentRootPaths = contentRootPaths;
    }

}