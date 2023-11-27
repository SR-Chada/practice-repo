(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-downloadlist__editor",
        title: "[data-cmp-downloadlist-dialog-hook='title']",
        titleGroup: "[data-cmp-downloadlist-dialog-hook='titleGroup']",
        description: "[data-cmp-downloadlist-dialog-hook='description']",
        descriptionGroup: "[data-cmp-downloadlist-dialog-hook='descriptionGroup']",
        type: "[data-cmp-downloadlist-dialog-hook='type']",
        typeGroup: "[data-cmp-downloadlist-dialog-hook='typeGroup']",
        manufacturer: "[data-cmp-downloadlist-dialog-hook='manufacturer']",
        manufacturerGroup: "[data-cmp-downloadlist-dialog-hook='manufacturerGroup']",
        productLine: "[data-cmp-downloadlist-dialog-hook='productLine']",
        productLineGroup: "[data-cmp-downloadlist-dialog-hook='productLineGroup']",
        publishedDate: "[data-cmp-downloadlist-dialog-hook='publishedDate']",
        publishedDateGroup: "[data-cmp-downloadlist-dialog-hook='publishedDateGroup']",
        year: "[data-cmp-downloadlist-dialog-hook='year']",
        yearGroup: "[data-cmp-downloadlist-dialog-hook='yearGroup']",
        reportType: "[data-cmp-downloadlist-dialog-hook='reportType']",
        reportTypeGroup: "[data-cmp-downloadlist-dialog-hook='reportTypeGroup']",
        quarter: "[data-cmp-downloadlist-dialog-hook='quarter']",
        quarterGroup: "[data-cmp-downloadlist-dialog-hook='quarterGroup']",
        file: "[data-cmp-downloadlist-dialog-hook='file']",
        fileGroup: "[data-cmp-downloadlist-dialog-hook='fileGroup']"
    };

    var title;
    var titleGroup;
    var description;
    var descriptionGroup;
    var type;
    var typeGroup;
    var manufacturer;
    var manufacturerGroup;
    var productLine;
    var productLineGroup;
    var publishedDate;
    var publishedDateGroup;
    var year;
    var yearGroup;
    var reportType;
    var reportTypeGroup;
    var quarter;
    var quarterGroup;
    var file;
    var fileGroup;

    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                title = dialogContent.querySelector(selectors.title);
                titleGroup = dialogContent.querySelector(selectors.titleGroup);
                description = dialogContent.querySelector(selectors.description);
                descriptionGroup = dialogContent.querySelector(selectors.descriptionGroup);
                type = dialogContent.querySelector(selectors.type);
                typeGroup = dialogContent.querySelector(selectors.typeGroup);
                manufacturer = dialogContent.querySelector(selectors.manufacturer);
                manufacturerGroup = dialogContent.querySelector(selectors.manufacturerGroup);
                productLine = dialogContent.querySelector(selectors.productLine);
                productLineGroup = dialogContent.querySelector(selectors.productLineGroup);
                publishedDate = dialogContent.querySelector(selectors.publishedDate);
                publishedDateGroup = dialogContent.querySelector(selectors.publishedDateGroup);
                year = dialogContent.querySelector(selectors.year);
                yearGroup = dialogContent.querySelector(selectors.yearGroup);
                reportType = dialogContent.querySelector(selectors.reportType);
                reportTypeGroup = dialogContent.querySelector(selectors.reportTypeGroup);
                quarter = dialogContent.querySelector(selectors.quarter);
                quarterGroup = dialogContent.querySelector(selectors.quarterGroup);
                file = dialogContent.querySelector(selectors.file);
                fileGroup = dialogContent.querySelector(selectors.fileGroup);

                if (title) {
                    Coral.commons.ready(title, function() {
                        title.on("change", onTitleChange);
                        onTitleChange();
                    });
                }

                if (description) {
                    Coral.commons.ready(description, function() {
                        description.on("change", onDescriptionChange);
                        onDescriptionChange();
                    });
                }

                if (type) {
                    Coral.commons.ready(type, function() {
                        type.on("change", onTypeChange);
                        onTypeChange();
                    });
                }

                if (manufacturer) {
                    Coral.commons.ready(manufacturer, function() {
                        manufacturer.on("change", onManufacturerChange);
                        onManufacturerChange();
                    });
                }

                if (productLine) {
                    Coral.commons.ready(productLine, function() {
                        productLine.on("change", onProductLineChange);
                        onProductLineChange();
                    });
                }

                if (publishedDate) {
                    Coral.commons.ready(publishedDate, function() {
                        publishedDate.on("change", onPublishedDateChange);
                        onPublishedDateChange();
                    });
                }

                if (year) {
                    Coral.commons.ready(year, function() {
                        year.on("change", onYearChange);
                        onYearChange();
                    });
                }

                if (reportType) {
                    Coral.commons.ready(reportType, function() {
                        reportType.on("change", onReportTypeChange);
                        onReportTypeChange();
                    });
                }

                if (quarter) {
                    Coral.commons.ready(quarter, function() {
                        quarter.on("change", onQuarterChange);
                        onQuarterChange();
                    });
                }

                if (file) {
                    Coral.commons.ready(file, function() {
                        file.on("change", onFileChange);
                        onFileChange();
                    });
                }
            }
        }
    });

    function onTitleChange() {
        if (title && titleGroup) {
            if (!title.checked) {
                titleGroup.setAttribute("hidden", true);
            } else {
                titleGroup.removeAttribute("hidden");
            }
        }
    }

    function onDescriptionChange() {
        if (description && descriptionGroup) {
            if (!description.checked) {
                descriptionGroup.setAttribute("hidden", true);
            } else {
                descriptionGroup.removeAttribute("hidden");
            }
        }
    }

    function onTypeChange() {
        if (type && typeGroup) {
            if (!type.checked) {
                typeGroup.setAttribute("hidden", true);
            } else {
                typeGroup.removeAttribute("hidden");
            }
        }
    }

    function onManufacturerChange() {
        if (manufacturer && manufacturerGroup) {
            if (!manufacturer.checked) {
                manufacturerGroup.setAttribute("hidden", true);
            } else {
                manufacturerGroup.removeAttribute("hidden");
            }
        }
    }

    function onProductLineChange() {
        if (productLine && productLineGroup) {
            if (!productLine.checked) {
                productLineGroup.setAttribute("hidden", true);
            } else {
                productLineGroup.removeAttribute("hidden");
            }
        }
    }

    function onPublishedDateChange() {
        if (publishedDate && publishedDateGroup) {
            if (!publishedDate.checked) {
                publishedDateGroup.setAttribute("hidden", true);
            } else {
                publishedDateGroup.removeAttribute("hidden");
            }
        }
    }

    function onYearChange() {
        if (year && yearGroup) {
            if (!year.checked) {
                yearGroup.setAttribute("hidden", true);
            } else {
                yearGroup.removeAttribute("hidden");
            }
        }
    }

    function onReportTypeChange() {
        if (reportType && reportTypeGroup) {
            if (!reportType.checked) {
                reportTypeGroup.setAttribute("hidden", true);
            } else {
                reportTypeGroup.removeAttribute("hidden");
            }
        }
    }

    function onQuarterChange() {
        if (quarter && quarterGroup) {
            if (!quarter.checked) {
                quarterGroup.setAttribute("hidden", true);
            } else {
                quarterGroup.removeAttribute("hidden");
            }
        }
    }

    function onFileChange() {
        if (file && fileGroup) {
            if (!file.checked) {
                fileGroup.setAttribute("hidden", true);
            } else {
                fileGroup.removeAttribute("hidden");
            }
        }
    }

})(jQuery);
