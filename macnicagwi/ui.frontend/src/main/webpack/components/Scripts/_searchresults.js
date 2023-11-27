import * as utils from '../../site/common/scripts/common.js';
import {MultilevelFilter} from '../Scripts/_multilevelfilter.js';
import * as popupScript from './_popup.js';
import * as formScript from './_forms.js';

class SearchResults extends MultilevelFilter {
    constructor(componentContainer, dataResourcePath) {
        super(componentContainer, dataResourcePath);
        this.itemsTag = ".cmp-searchresults .cmp-searchresults__pages.cmp-searchresults__assets > ul";
    }

    init() {
        super.init();
        this.applyFiltersFuntionality();
        this.uncheckFiltersFunctionality();
        this.allButtonFunctionality();
        this.loadMoreFunctionality();
        this.downloadRestrictedAsset();
    }

    /**
     * 
     * @param {*} apiResponse search filters api response
     * @param {*} isLoadMore whether user clicked load more button or not
     */
    updateComponentDom(apiResponse, isLoadMore) {
        //api success
        apiResponse.done((data)=>{
            const searchresultsContainerDiv = $('<div />').append(data).find(".cmp-searchresults .cmp-searchresults__pages.cmp-searchresults__assets").html();
            const searchresultsContainerList = $('<div />').append(data).find(this.itemsTag).html();
            const totalResultsSize = $('<div />').append(data).find('.cmp-searchresults').attr("data-cmp-listsize");
            const isDataAvailable = $('<div />').append(data).find(this.itemsTag).find("li").length != 0 ? true: false;

            if(isDataAvailable) {
                this.componentContainer.find(".cmp-searchresults__no-results-found").hide();
                if(isLoadMore) {
                    $("body").find(this.componentContainer).find(this.itemsTag).append(searchresultsContainerList);
                } else {
                    $("body").find(this.componentContainer).find(".cmp-searchresults .cmp-searchresults__pages.cmp-searchresults__assets").html(searchresultsContainerDiv);
                }
            } else {
                $("body").find(this.componentContainer).find(".cmp-searchresults .cmp-searchresults__pages.cmp-searchresults__assets").html("");
                this.componentContainer.find(".cmp-searchresults__loadmore-button > .cmp-button").attr("disabled",true);
                this.componentContainer.find(".cmp-searchresults__no-results-found").show();
            }
            
            $("body").find(this.componentContainer).find(".cmp-searchresults").attr("data-cmp-listsize", totalResultsSize);
        });
        //api failure
        apiResponse.fail((error)=> {console.log(error);});
    }

    applyFiltersFuntionality() {
        const self = this;
         // apply button click functionality
        this.componentContainer.find(".cmp-multilevelfilter__apply-button").each(function(){
            $(this).on("click",function() {
                const apiResponse = self.callSearchResultsFiltersApi(false, false, self.itemsTag);
                self.updateComponentDom(apiResponse, false);
                self.addShowingResultsForElement();
                //close filters dropdown
                $(this).parents().eq(2).find(".cmp-multilevelfilter__parent-tag-title").removeClass("cmp-multilevelfilter__parent-tag-title--active");
                $(this).parents().eq(2).find(".cmp-multilevelfilter__child-filters").removeClass("cmp-multilevelfilter__child-filters--active");
                self.componentContainer.find(".cmp-multilevelfilter__close-icon").hide();
                self.componentContainer.find(".overlay .cmp-multilevelfilter__parent-filters").hide();
                self.componentContainer.find(".cmp-multilevelfilter__search-filters").removeClass("overlay");
            });
        });
    }

    uncheckFiltersFunctionality() {
        const self = this;
         // uncheck_all button click functionality
        this.componentContainer.find(".cmp-multilevelfilter__clear-button").each(function(){
            $(this).on('click',function() {
                self.unSelectFilters();
                const apiResponse = self.callSearchResultsFiltersApi(true, false, self.itemsTag);
                self.updateComponentDom(apiResponse, false);
                self.componentContainer.find('.cmp-multilevelfilter__apply-button').prop('disabled',true);
                self.componentContainer.find(".cmp-searchresults__loadmore-button > .cmp-button").attr("disabled",false);
                self.componentContainer.find(".cmp-searchresults__showing-results-for").hide();
            });
        });
    }

    allButtonFunctionality() {
        const self = this;
         //All button functionality
        this.componentContainer.find(".cmp-multilevelfilter--select-all").on("click",function() {
            self.unSelectFilters();
            const apiResponse = self.callSearchResultsFiltersApi(true, false, self.itemsTag);
            self.updateComponentDom(apiResponse, false);
            self.componentContainer.find(".cmp-searchresults__loadmore-button > .cmp-button").attr("disabled",false);
            self.componentContainer.find(".cmp-searchresults__showing-results-for").hide();
        });
    }

    /**
     * to show "Filtered On" list (applied filters)
     */
    addShowingResultsForElement() {
        const searchFiltersList = this.getSelectedFiltersTitles();
        this.componentContainer.find(".cmp-searchresults__showing-results-for-values").html(
                searchFiltersList.join(", "));
        this.componentContainer.find(".cmp-searchresults__showing-results-for").show();
    }

    loadMoreFunctionality() {
        const self = this;
         //load more button functionality
        this.componentContainer.find(".cmp-searchresults__loadmore-button > .cmp-button").on("click",function() {
            const currentListSize = self.componentContainer.find(self.itemsTag).find("li").length;
            const totalResultsSize = self.componentContainer.find(".cmp-searchresults").attr("data-cmp-listsize");
            if( currentListSize < totalResultsSize) {
                const apiResponse = self.callSearchResultsFiltersApi(false, true, self.itemsTag);
                self.updateComponentDom(apiResponse, true);
            } else {
                $(this).attr("disabled",true);
            }
        });
    }

    downloadRestrictedAsset() {
        const popupFormComponent = this.componentContainer.find(".cmp-searchresults--popup-form");
        
        this.componentContainer.find(".cmp-searchresults .cmp-download__action").each(function() {
            const isRestrictedDownload = $(this).is("[isRestrictedDownload]");
            const assetResourcePath = $(this).attr("resource");
                  
            $(this).on("click",function(){
            
                    if(isRestrictedDownload) { 
                        utils.downloadAssetFile(
                            popupFormComponent, 
                            assetResourcePath, 
                            popupScript, 
                            formScript
                        );
                    } else {
                        window.location = utils.getHostOrigin() + assetResourcePath;
                    }
                });
            
        });
        
    }
}

$(document).ready(function() {
    $(".searchresults").trigger("load");
});

$(".searchresults").on("load",function() {
     const componentContainer = $(this);
     const dataResourcePath = window.location.href;
     const searchresultsClass = new SearchResults(componentContainer, dataResourcePath);
     searchresultsClass.init();
     $(window).trigger("resize");
     const pagesList = $(".cmp-searchresults__pages-content");
     if(!pagesList || !pagesList.length>0){
        $(".cmp-searchresults__loadmore-button > .cmp-button").attr("disabled",true);
        $(".cmp-searchresults__no-results-found").show();
    }
});


