import * as utils from '../../site/common/scripts/common.js';
import {MultilevelFilter} from '../Scripts/_multilevelfilter.js';

class CardList extends MultilevelFilter {
    constructor(componentContainer, dataResourcePath) {
        super(componentContainer, dataResourcePath);
        this.itemsTag = ".cmp-card-list li";
    }

    init() {
        super.init();
        this.applyFiltersFuntionality();
        this.uncheckFiltersFunctionality();
        this.freeTextSearchFunctionality();
        this.allButtonFunctionality();
        this.loadMoreFunctionality();
    }

    /**
     * 
     * @param {*} apiResponse search filters api response
     * @param {*} isLoadMore whether user clicked load more button or not
     */
    updateComponentDom(apiResponse, isLoadMore) {
        //api success
        apiResponse.done((data)=>{
            const cardlistContainer = $('<div />').append(data).find('.cmp-card-list').html();
            const totalResultsSize = $('<div />').append(data).find('.cmp-card-list').attr("data-cmp-listsize");
            const isDataAvailable = $('<div />').append(data).find('.cmp-card-list li').length != 0 ? true: false;
           
            if(isDataAvailable) {
                this.componentContainer.find(".cmp-card-list__no-results-found").hide();
                if(isLoadMore) {
                    $("body").find(this.componentContainer).find(".cmp-card-list").append(cardlistContainer);
                } else {
                    $("body").find(this.componentContainer).find(".cmp-card-list").html(cardlistContainer);
                }
            } else {
                $("body").find(this.componentContainer).find(".cmp-card-list").html("");
                this.componentContainer.find(".cmp-card-list__loadmore-button > .cmp-button").attr("disabled",true);
                this.componentContainer.find(".cmp-card-list__no-results-found").show();
            }
           
            $("body").find(this.componentContainer).find(".cmp-card-list").attr("data-cmp-listsize", totalResultsSize);
        
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
                self.componentContainer.find(".cmp-card-list__loadmore-button > .cmp-button").attr("disabled",false);
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
                self.componentContainer.find(".cmp-card-list__loadmore-button > .cmp-button").attr("disabled",false);
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
            self.componentContainer.find(".cmp-card-list__loadmore-button > .cmp-button").attr("disabled",false);
        });
    }

    loadMoreFunctionality() {
        const self = this;
         //load more button functionality
        this.componentContainer.find(".cmp-card-list__loadmore-button > .cmp-button").on("click",function() {
            const currentListSize = self.componentContainer.find(self.itemsTag).length;
            const totalResultsSize = self.componentContainer.find(".cmp-card-list").attr("data-cmp-listsize");
            if( currentListSize < totalResultsSize) {
                const apiResponse = self.callFiltersApi(false, true, self.itemsTag);
                self.updateComponentDom(apiResponse, true);
            } else {
                $(this).attr("disabled",true);
            }
        });
    }
}


$(document).ready(function() {
    $(".cardlist").trigger("load");
});

$(".cardlist").on("load",function() {
    const componentContainer = $(this);
    const dataResourcePath = $(this).find(".cmp-card-list").attr("data-cmp-resource");
    const cardListClass = new CardList(componentContainer, dataResourcePath);
    cardListClass.init();
    $(window).trigger("resize");
});

