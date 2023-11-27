export class MultilevelFilter {
    constructor(componentContainer, dataResourcePath) {
        this.componentContainer = componentContainer;
        this.dataResourcePath = dataResourcePath;
        
    }

    init() {
        this.parentFilterFunctionality();
        this.childFilterFunctionality();
        this.closeIconFunctionality();
        this.filterButtonClick();
        this.resizeEvent();
    }

    // parent filter click functionality
    parentFilterFunctionality() {
        
        const self = this;
        this.componentContainer.find(".cmp-multilevelfilter__parent-tag-title").on("click",function(){
            
            //make parent tag active
            self.componentContainer.find(".cmp-multilevelfilter__parent-tag-title").removeClass("cmp-multilevelfilter__parent-tag-title--active");
            $(this).addClass("cmp-multilevelfilter__parent-tag-title--active");

            //make child filters active
            self.componentContainer.find(".cmp-multilevelfilter__child-filters").removeClass("cmp-multilevelfilter__child-filters--active");
            $(this).parent().find(".cmp-multilevelfilter__child-filters").addClass("cmp-multilevelfilter__child-filters--active");

            if($(this).parent().find("> .cmp-multilevelfilter__parent-tag-title").hasClass("cmp-multilevelfilter--select-all")) {
                self.componentContainer.find(".cmp-multilevelfilter__close-icon").hide();
            } else {
                self.componentContainer.find(".cmp-multilevelfilter__close-icon").show();
            }
             $("input:checked").parent().addClass("cmp-multilevelfilter__children-item--active");
        });
    }

   // child filter click functionality
    childFilterFunctionality() {
        const self = this;
        this.componentContainer.find(".cmp-multilevelfilter__children-item").on("click",function(){
            if($(this).find("> input").is(":checked")) {
                $(this).addClass("cmp-multilevelfilter__children-item--active");
                $(this).parent().parent().find(".cmp-multilevelfilter__apply-button").attr("disabled", false);
            } else {
                $(this).removeClass("cmp-multilevelfilter__children-item--active");
            }

            self.displaySelectedFiltersCount($(this));
            const checkbox = $(this).parent().find('.cmp-multilevelfilter__children-item--checkbox');
            checkbox.on('change', function () {
                 self.componentContainer.find('.cmp-multilevelfilter__apply-button').prop('disabled', !checkbox.filter(':checked').length);
            }).trigger('change');
        });
    }

    //close icon click functionality
    closeIconFunctionality() {
        const self = this;
        this.componentContainer.find(".cmp-multilevelfilter__close-icon").on("click",function(){
            self.componentContainer.find(".cmp-multilevelfilter__parent-tag-title").removeClass("cmp-multilevelfilter__parent-tag-title--active");
            self.componentContainer.find(".cmp-multilevelfilter__child-filters").removeClass("cmp-multilevelfilter__child-filters--active");
            self.componentContainer.find(".cmp-multilevelfilter__close-icon").hide();
            self.componentContainer.find(".overlay .cmp-multilevelfilter__parent-filters").hide();
            self.componentContainer.find(".cmp-multilevelfilter__search-filters").removeClass("overlay");
         });
    }

    unSelectFilters() {
        this.componentContainer.find("input:checkbox").attr("checked",false);
        this.componentContainer.find(".cmp-multilevelfilter__children-item").removeClass("cmp-multilevelfilter__children-item--active");
        this.componentContainer.find(".cmp-multilevelfilter__parent-tag-title-active-children").html("");
    }

    /**
     * @param {*} obj reference to child filter li element this object
     * @returns null
     */
    displaySelectedFiltersCount(obj) {
        // Show selected filters count on parent tag
        const selectedFiltersCount = obj.parent().find("input:checked").length;
        const countShowSpan = obj.parents().eq(2).find(
            ".cmp-multilevelfilter__parent-tag-title-active-children");
        if(selectedFiltersCount > 0) {
            countShowSpan.show();
            countShowSpan.text(" - " + selectedFiltersCount);
        } else {
            countShowSpan.hide();
        }
        return;
    }

    /**
     * @param {*} componentContainer reference to "this" object of component
     * @returns selected filters list
     */
    getSelectedFilters() {
        const selectedFiltersList = [];
        this.componentContainer.find("input:checkbox:checked").each(function(){
            selectedFiltersList.push($(this).attr("id"));
        });
        return selectedFiltersList;
    }

    /**
     * @param {*} componentContainer reference to "this" object of component
     * @returns selected filters list values
     */
    getSelectedFiltersTitles() {
        const selectedFiltersTitlesList = [];
        this.componentContainer.find("input:checkbox:checked").each(function(){
            selectedFiltersTitlesList.push($(this).attr("value"));
        });
        return selectedFiltersTitlesList;
    }

    
    /**
     * 
     * @param {*} isSelectAll whether to fetch all results or not
     * @param {*} isLoadMore whether clicked loadmore or not
     * @returns apiResponse of search filters api
     */
    callFiltersApi(isSelectAll, isLoadMore, itemsTag) {
        const searchFilterQuery = this.componentContainer.find(".cmp-multilevelfilter__text-search > input").val();
        const selectedFiltersList = this.getSelectedFilters();

        let url = "";
        if(isSelectAll) {
            url = this.dataResourcePath+".html";
        } else {
            url = this.dataResourcePath + ".html?t=" + selectedFiltersList;
            if(searchFilterQuery!=null && searchFilterQuery!="") {
                url += "&q=" + searchFilterQuery;
            }
        }

        if(isLoadMore) {

            const currentListSize = this.componentContainer.find(itemsTag).length;

            if(url.includes("?")) {
                url += "&f="+ (currentListSize);
            } else {
                url += "?f="+ (currentListSize);
            }
        }

        const apiResponse = $.get({
            method: 'GET',
            url
        });
        return apiResponse;
    }

     /**
     * Used for SearchResults Component
     * @param {*} isSelectAll whether to fetch all results or not
     * @param {*} isLoadMore whether clicked loadmore or not
     * @returns apiResponse of search filters api
     */
      callSearchResultsFiltersApi(isSelectAll, isLoadMore, itemsTag) {
        const selectedFiltersList = this.getSelectedFilters();

        let url = "";
        if(isSelectAll) {
            url = this.dataResourcePath;
        } else {
            url = this.dataResourcePath + "&t=" + selectedFiltersList;
        }

        if(isLoadMore) {

            const currentListSize = this.componentContainer.find(itemsTag).find("li").length;

            if(url.includes("?")) {
                url += "&f="+ (currentListSize);
            } else {
                url += "?f="+ (currentListSize);
            }
        }

        const apiResponse = $.get({
            method: 'GET',
            url
        });
        return apiResponse;
    }


    filterButtonClick(){
        const self = this;
        this.componentContainer.find(".cmp-multilevelfilter__filter-button").on("click", function(){
            self.componentContainer.find(".cmp-multilevelfilter__search-filters").addClass("overlay");
            self.componentContainer.find(".cmp-multilevelfilter__search-filters.overlay").show();
            self.componentContainer.find(".cmp-multilevelfilter__parent-filters").css("display", "flex");
            self.componentContainer.find(".cmp-multilevelfilter__close-icon").show();
            
            self.componentContainer.find(".cmp-multilevelfilter__parent-filters").on("click",function(e) {
            e.stopPropagation();
            })
        });
    }


    resizeEvent(){
        const self = this;
        $(window).on('resize', function () {
            if ($(window).width() < 1025) {
                self.componentContainer.find(".cmp-multilevelfilter__search-filters").addClass("overlay");
                self.componentContainer.find(".cmp-multilevelfilter__search-filters.overlay").hide();
                self.componentContainer.find(".cmp-multilevelfilter__filter-button").show();
                self.componentContainer.find(".cmp-multilevelfilter__filter-button").css("display", "flex");
            }
            else {
                self.componentContainer.find(".cmp-multilevelfilter__search-filters").removeClass("overlay");
                self.componentContainer.find(".cmp-multilevelfilter__search-filters").show();
                self.componentContainer.find(".cmp-multilevelfilter__parent-filters").show();
                self.componentContainer.find(".cmp-multilevelfilter__filter-button").hide(); 
            }
        });
    }

}