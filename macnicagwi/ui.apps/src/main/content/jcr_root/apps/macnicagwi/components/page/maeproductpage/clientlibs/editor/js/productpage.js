(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cq-dialog-product-page__editor",
        pageType: "[name='./pageType']",
        productManufacturerRankingGroup: "[data-product-page-dialog-hook='productManufacturerRankingGroup']",
        productLineGroup: "[data-product-page-dialog-hook='productLineGroup']",
        productDetailsGroup: "[data-product-page-dialog-hook='productDetailsGroup']",
    };

    var pageTypes = {
        productManufacturerLandingPage: "productManufacturerLandingPage",
        productLineLandingPage: "productLineLandingPage",
        productDetailsPage: "productDetailsPage"
    }

    var pageType;
    var productManufacturerRankingGroup;
    var productLineGroup;
    var productDetailsGroup;

    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                pageType = $(selectors.pageType);
                productManufacturerRankingGroup = dialogContent.querySelector(selectors.productManufacturerRankingGroup);
                productLineGroup = dialogContent.querySelector(selectors.productLineGroup);
                productDetailsGroup = dialogContent.querySelector(selectors.productDetailsGroup);

                productLineGroup.setAttribute("hidden", true);
                productDetailsGroup.setAttribute("hidden", true);

                if(pageType) {
                    pageType.change(function() {
                        onPageTypeChange(this.value);
                    });
                    onPageTypeChange(pageType.val());
                }
            }
        }
    });

    function onPageTypeChange(value) {
        if(productManufacturerRankingGroup && productLineGroup && productDetailsGroup) {
            if (value == pageTypes.productManufacturerLandingPage) {
                productManufacturerRankingGroup.removeAttribute("hidden");
                productLineGroup.setAttribute("hidden", true);
                productDetailsGroup.setAttribute("hidden", true);
            } else if (value == pageTypes.productLineLandingPage) {
                productManufacturerRankingGroup.setAttribute("hidden", true);
                productLineGroup.removeAttribute("hidden");
                productDetailsGroup.setAttribute("hidden", true);
            } else if (value == pageTypes.productDetailsPage) {
                productManufacturerRankingGroup.setAttribute("hidden", true);
                productLineGroup.removeAttribute("hidden");
                productDetailsGroup.removeAttribute("hidden");
            }
        }
    }

})(jQuery);
