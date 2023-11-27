(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-card-list__editor",
        isfreeTextSearchEnabled: "[data-cmp-card-list-dialog-hook='isfreeTextSearchEnabled']",
        freeTextSearchGroup: "[data-cmp-card-list-dialog-hook='freeTextSearchGroup']",
    };

    var isfreeTextSearchEnabled;
    var freeTextSearchGroup;


    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                isfreeTextSearchEnabled = dialogContent.querySelector(selectors.isfreeTextSearchEnabled);
                freeTextSearchGroup = dialogContent.querySelector(selectors.freeTextSearchGroup);

                if (isfreeTextSearchEnabled) {
                    Coral.commons.ready(isfreeTextSearchEnabled, function() {
                        isfreeTextSearchEnabled.on("change", onFreeTextSearchChange);
                        onFreeTextSearchChange();
                    });
                }
            }
        }
    });

    function onFreeTextSearchChange() {
        if (isfreeTextSearchEnabled && freeTextSearchGroup) {
            if (!isfreeTextSearchEnabled.checked) {
                freeTextSearchGroup.setAttribute("hidden", true);
            } else {
                freeTextSearchGroup.removeAttribute("hidden");
            }
        }
    }

})(jQuery);
