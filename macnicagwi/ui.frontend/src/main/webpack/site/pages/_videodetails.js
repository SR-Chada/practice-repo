import * as utils from '../common/scripts/common.js';

/**
 * Execute on document ready
 */
$(function(){

    const pageUrl = window.location.href;
    const cookieValue = utils.getCookie("watchRestrictedVideos",true);
    if(cookieValue == null) {
        $(".cmp-button--watch-video-button").show();
         //if button exists and is enabled, open video restricted form on cick
        $(".cmp-button--watch-video-button").click(function(){
            $(".watch-video-form-xf .popup-container form").closest(".popup-container").addClass("active");
        });  
        
        $(".watch-video-form-xf .cmp-popup__close-icon").click(function() {
            $(".watch-video-form-xf .popup-container").removeClass("active");
            window.location = pageUrl;
        });

        $(".watch-video-form-xf .popup").click(function() {
            $(".watch-video-form-xf .popup-container").removeClass("active");
            window.location = pageUrl;
        });
    } else {
        $(".cmp-button--watch-video-button").hide();
    }
});