import * as utils from '../../site/common/scripts/common.js';

export function executePopup() {

    if (!utils.isEditor()) {
        $(".popup").addClass('popup-container');
        $(".cmp-popup__close-icon").show();
        $(".cmp-popup__close-icon, .popup").click(function() {
            
            try {
                    var iframes = document.querySelectorAll('iframe');
                    if (iframes.length > 0) {
                        iframes.forEach(function (iframe) {
                            var iframeSrc = iframe.src;
                            iframe.src = iframeSrc;
                        });
                    }              
            } catch (error) {
                console.log("Error in stopping the video: ", error);
            }
            $(".popup-container").removeClass("active");
        });
        
        $(".cmp-popup").click(function(event) {
            event.stopPropagation();
        })
    } else {
        $(".popup").removeClass('popup-container');
        $(".cmp-popup__close-icon").hide();
    }
    return;
}

$(document).ready(function() {
    executePopup();
})
