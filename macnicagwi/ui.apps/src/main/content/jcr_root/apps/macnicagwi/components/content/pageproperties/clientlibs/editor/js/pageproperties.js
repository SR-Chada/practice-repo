(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-pageproperties__editor",
        newsDate: "[data-cmp-pageproperties-dialog-hook='newsDate']",
        newsDateGroup: "[data-cmp-pageproperties-dialog-hook='newsDateGroup']",
        eventStartDate: "[data-cmp-pageproperties-dialog-hook='eventStartDate']",
        eventStartDateGroup: "[data-cmp-pageproperties-dialog-hook='eventStartDateGroup']",
        eventEndDate: "[data-cmp-pageproperties-dialog-hook='eventEndDate']",
        eventEndDateGroup: "[data-cmp-pageproperties-dialog-hook='eventEndDateGroup']",
        eventLocation: "[data-cmp-pageproperties-dialog-hook='eventLocation']",
        eventLocationGroup: "[data-cmp-pageproperties-dialog-hook='eventLocationGroup']",
        technicalArticleDate: "[data-cmp-pageproperties-dialog-hook='technicalArticleDate']",
        technicalArticleDateGroup: "[data-cmp-pageproperties-dialog-hook='technicalArticleDateGroup']",
        authorName: "[data-cmp-pageproperties-dialog-hook='authorName']",
        authorNameGroup: "[data-cmp-pageproperties-dialog-hook='authorNameGroup']",
        articleSource: "[data-cmp-pageproperties-dialog-hook='articleSource']",
        articleSourceGroup: "[data-cmp-pageproperties-dialog-hook='articleSourceGroup']"
    };

    var newsDate;
    var newsDateGroup;
    var eventStartDate;
    var eventStartDateGroup;
    var eventEndDate;
    var eventEndDateGroup;
    var eventLocation;
    var eventLocationGroup;
    var technicalArticleDate;
    var technicalArticleDateGroup;
    var authorName;
    var authorNameGroup;
    var articleSource;
    var articleSourceGroup;

    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                newsDate = dialogContent.querySelector(selectors.newsDate);
                newsDateGroup = dialogContent.querySelector(selectors.newsDateGroup);
                eventStartDate = dialogContent.querySelector(selectors.eventStartDate);
                eventStartDateGroup = dialogContent.querySelector(selectors.eventStartDateGroup);
                eventEndDate = dialogContent.querySelector(selectors.eventEndDate);
                eventEndDateGroup = dialogContent.querySelector(selectors.eventEndDateGroup);
                eventLocation = dialogContent.querySelector(selectors.eventLocation);
                eventLocationGroup = dialogContent.querySelector(selectors.eventLocationGroup);
                technicalArticleDate = dialogContent.querySelector(selectors.technicalArticleDate);
                technicalArticleDateGroup = dialogContent.querySelector(selectors.technicalArticleDateGroup);
                authorName = dialogContent.querySelector(selectors.authorName);
                authorNameGroup = dialogContent.querySelector(selectors.authorNameGroup);
                articleSource = dialogContent.querySelector(selectors.articleSource);
                articleSourceGroup = dialogContent.querySelector(selectors.articleSourceGroup);

                if (newsDate) {
                    Coral.commons.ready(newsDate, function() {
                        newsDate.on("change", onNewsDateChange);
                        onNewsDateChange();
                    });
                }

                if (authorName) {
                    Coral.commons.ready(authorName, function() {
                        authorName.on("change", authorNameChange);
                        authorName();
                    });
                }

                if (articleSource) {
                    Coral.commons.ready(articleSource, function() {
                        articleSource.on("change", articleSourceChange);
                        articleSource();
                    });
                }

                if (eventStartDate) {
                    Coral.commons.ready(eventStartDate, function() {
                        eventStartDate.on("change", onEventStartDateChange);
                        onEventStartDateChange();
                    });
                }

                if (eventEndDate) {
                    Coral.commons.ready(eventEndDate, function() {
                        eventEndDate.on("change", onEventEndDateChange);
                        onEventEndDateChange();
                    });
                }

                if (eventLocation) {
                    Coral.commons.ready(eventLocation, function() {
                        eventLocation.on("change", onEventLocationChange);
                        onEventLocationChange();
                    });
                }

                if (technicalArticleDate) {
                    Coral.commons.ready(technicalArticleDate, function() {
                        technicalArticleDate.on("change", onTechnicalArticleDateChange);
                        onTechnicalArticleDateChange();
                    });
                }
            }
        }
    });

    function onNewsDateChange() {
        if (newsDate && newsDateGroup) {
            if (!newsDate.checked) {
                newsDateGroup.setAttribute("hidden", true);
            } else {
                newsDateGroup.removeAttribute("hidden");
            }
        }
    }

    function onEventStartDateChange() {
        if (eventStartDate && eventStartDateGroup) {
            if (!eventStartDate.checked) {
                eventStartDateGroup.setAttribute("hidden", true);
            } else {
                eventStartDateGroup.removeAttribute("hidden");
            }
        }
    }

    function onEventEndDateChange() {
        if (eventEndDate && eventEndDateGroup) {
            if (!eventEndDate.checked) {
                eventEndDateGroup.setAttribute("hidden", true);
            } else {
                eventEndDateGroup.removeAttribute("hidden");
            }
        }
    }

    function onEventLocationChange() {
        if (eventLocation && eventLocationGroup) {
            if (!eventLocation.checked) {
                eventLocationGroup.setAttribute("hidden", true);
            } else {
                eventLocationGroup.removeAttribute("hidden");
            }
        }
    }

    function onTechnicalArticleDateChange() {
        if (technicalArticleDate && technicalArticleDateGroup) {
            if (!technicalArticleDate.checked) {
                technicalArticleDateGroup.setAttribute("hidden", true);
            } else {
                technicalArticleDateGroup.removeAttribute("hidden");
            }
        }
    }

    function authorNameChange() {
        if (authorName && authorNameGroup) {
            if (!authorName.checked) {
                authorNameGroup.setAttribute("hidden", true);
            } else {
                authorNameGroup.removeAttribute("hidden");
            }
        }
    }

    function articleSourceChange() {
        if (articleSource && articleSourceGroup) {
            if (!articleSource.checked) {
                articleSourceGroup.setAttribute("hidden", true);
            } else {
                articleSourceGroup.removeAttribute("hidden");
            }
        }
    }
    

})(jQuery);
