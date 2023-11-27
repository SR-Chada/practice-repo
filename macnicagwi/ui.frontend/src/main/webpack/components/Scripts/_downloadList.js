import * as utils from '../../site/common/scripts/common.js';
import {MultilevelFilter} from '../Scripts/_multilevelfilter.js';
import * as popupScript from './_popup.js';
import * as formScript from './_forms.js';

class DownloadList extends MultilevelFilter {
    constructor(componentContainer, dataResourcePath) {
        super(componentContainer, dataResourcePath);
        this.itemsTag = ".cmp-downloadlist .cmp-downloadlist__table-body tr";
    }

    init() {
        super.init();
        this.applyFiltersFuntionality();
        this.uncheckFiltersFunctionality();
        this.freeTextSearchFunctionality();
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
            const downloadListContainer = $('<div />').append(data).find('.cmp-downloadlist').html();
            const totalResultsSize = $('<div />').append(data).find('.cmp-downloadlist').attr("data-cmp-listsize");
            const downloadListBody = $('<div />').append(data).find('.cmp-downloadlist .cmp-downloadlist__table-body').html();
            const isDataAvailable = $('<div />').append(data).find('.cmp-downloadlist .cmp-downloadlist__table-body tr').length != 0 ? true: false;
            if(isDataAvailable) {
                this.componentContainer.find(".cmp-downloadlist__no-results-found").hide();
                if(isLoadMore) {
                    $("body").find(this.componentContainer).find(".cmp-downloadlist .cmp-downloadlist__table-body").append(downloadListBody);
                } else {
                    $("body").find(this.componentContainer).find(".cmp-downloadlist").html(downloadListContainer);
                }
            } else {
                $("body").find(this.componentContainer).find(".cmp-downloadlist").html("");
                this.componentContainer.find(".cmp-downloadlist__loadmore-button > .cmp-button").attr("disabled",true);
                this.componentContainer.find(".cmp-downloadlist__no-results-found").show();
            }
            
            $("body").find(this.componentContainer).find(".cmp-downloadlist").attr("data-cmp-listsize", totalResultsSize);
            this.downloadRestrictedAsset();
            
        });
        //api failure
        apiResponse.fail((error)=> {console.log(error)});
    }

    applyFiltersFuntionality() {
        const self = this;
         // apply button click functionality
        this.componentContainer.find(".cmp-multilevelfilter__apply-button").each(function(){
            $(this).on("click",function() {
                const apiResponse = self.callFiltersApi(false, false, self.itemsTag);
                self.updateComponentDom(apiResponse, false);
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
                const apiResponse = self.callFiltersApi(true, false, self.itemsTag);
                self.updateComponentDom(apiResponse, false);
                self.componentContainer.find('.cmp-multilevelfilter__apply-button').prop('disabled',true);
                self.componentContainer.find(".cmp-downloadlist__loadmore-button > .cmp-button").attr("disabled",false);
            });
        });
    }


    freeTextSearchFunctionality() {
        const self = this;
         // search query
        this.componentContainer.find(".cmp-multilevelfilter__text-search > input").keypress(function(event){
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if(keycode == '13'){
                const apiResponse = self.callFiltersApi(false, false, self.itemsTag);
                self.updateComponentDom(apiResponse, false);
                self.componentContainer.find(".cmp-downloadlist__loadmore-button > .cmp-button").attr("disabled",false);
            }
        });

        //search icon click functionality 
        this.componentContainer.find(".cmp-multilevelfilter__text-search .cmp-multilevelfilter__text-search-icon").on("click",function(){
            const apiResponse = self.callFiltersApi(false, false, self.itemsTag);
            self.updateComponentDom(apiResponse, false);
            self.componentContainer.find(".cmp-card-list__loadmore-button > .cmp-button").attr("disabled",false);
        });
    }

    allButtonFunctionality() {
        const self = this;
         //All button functionality
        this.componentContainer.find(".cmp-multilevelfilter--select-all").on("click",function() {
            self.unSelectFilters();
            const apiResponse = self.callFiltersApi(true, false, self.itemsTag);
            self.updateComponentDom(apiResponse, false);
            self.componentContainer.find(".cmp-downloadlist__loadmore-button > .cmp-button").attr("disabled",false);
        });
    }

    loadMoreFunctionality() {
        const self = this;
         //load more button functionality
        this.componentContainer.find(".cmp-downloadlist__loadmore-button > .cmp-button").on("click",function() {

            const currentListSize = self.componentContainer.find(self.itemsTag).length;
            const totalResultsSize = self.componentContainer.find(".cmp-downloadlist").attr("data-cmp-listsize");
            if( currentListSize < totalResultsSize) {
                const apiResponse = self.callFiltersApi(false, true, self.itemsTag);
                self.updateComponentDom(apiResponse, true);
             } else {
                $(this).attr("disabled",true);
            }
        });
    }

    downloadRestrictedAsset() {
        const popupFormComponent = this.componentContainer.find(".cmp-downloadlist--popup-form");
        
        this.componentContainer.find(".cmp-downloadlist .cmp-downloadlist__action").each(function() {
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
    $(".downloadlist").trigger("load");
});

$(".downloadlist").on("load",function() {
    const componentContainer = $(this);
    const dataResourcePath = $(this).find(".cmp-downloadlist").attr("data-resource-path");
    const downloadListClass = new DownloadList(componentContainer, dataResourcePath);
    downloadListClass.init();
    $(window).trigger("resize");
});

