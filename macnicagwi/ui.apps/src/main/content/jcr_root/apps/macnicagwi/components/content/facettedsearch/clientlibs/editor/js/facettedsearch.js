(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-facettedsearch__editor",
        freeTextSearch: "[data-cmp-facettedsearch-dialog-hook='freeTextSearch']",
        freeTextSearchGroup: "[data-cmp-facettedsearch-dialog-hook='freeTextSearchGroup']",
        filterType: "[name='./filterType']",
        filterRootPaths: "[name='./filterRootPaths']",
        withFilterGroup: "[data-cmp-facettedsearch-dialog-hook='withFilterGroup']",
        withoutFilterGroup: "[data-cmp-facettedsearch-dialog-hook='withoutFilterGroup']"
    };

    var filterTypes = {
        singleLevelFilter: "singleLevelFilter",
        multiLevelFilter: "multiLevelFilter",
        none: "none"
    }

    var freeTextSearch;
    var freeTextSearchGroup;
    var filterType;
    var withFilterGroup;
    var withoutFilterGroup;
    var filterRootPaths;

    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                freeTextSearch = dialogContent.querySelector(selectors.freeTextSearch);
                freeTextSearchGroup = dialogContent.querySelector(selectors.freeTextSearchGroup);
                filterType = $(selectors.filterType);
                withFilterGroup = dialogContent.querySelector(selectors.withFilterGroup);
                withoutFilterGroup = dialogContent.querySelector(selectors.withoutFilterGroup);
                filterRootPaths = dialogContent.querySelector(selectors.filterRootPaths);

                withFilterGroup.setAttribute("hidden", true);
                withoutFilterGroup.setAttribute("hidden", true);

                if (freeTextSearch) {
                    Coral.commons.ready(freeTextSearch, function() {
                        freeTextSearch.on("change", onFreeTextSearchChange);
                        onFreeTextSearchChange();
                    });
                }

                if(filterType) {
                    filterType.change(function() {
                        onFilterTypeChange(this.value);
                    });
                    onFilterTypeChange($(`${selectors.filterType}:checked`).val());
                }
            }
        }
    });

    function onFreeTextSearchChange() {
        if (freeTextSearch && freeTextSearchGroup) {
            if (!freeTextSearch.checked) {
                freeTextSearchGroup.setAttribute("hidden", true);
            } else {
                freeTextSearchGroup.removeAttribute("hidden");
            }
        }
    }

    function onFilterTypeChange(value) {
        if(withFilterGroup && withoutFilterGroup) {
            if (value == filterTypes.none) {
                withFilterGroup.setAttribute("hidden", true);
                withoutFilterGroup.removeAttribute("hidden");
                filterRootPaths.removeAttribute("multiple");
            }
            else {
                withFilterGroup.removeAttribute("hidden");
                withoutFilterGroup.setAttribute("hidden", true);

                if(value == filterTypes.singleLevelFilter) {
                    filterRootPaths.removeAttribute("multiple");
                } else if(value == filterTypes.multiLevelFilter){
                    filterRootPaths.setAttribute("multiple", true);
                }
            }
        }
    }

})(jQuery);
