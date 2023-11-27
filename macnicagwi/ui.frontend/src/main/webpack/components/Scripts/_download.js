import * as utils from '../../site/common/scripts/common.js';
import * as popupScript from './_popup.js';
import * as formScript from './_forms.js';


$(document).ready(function() {
    $(".download").trigger("load");
});


$(".download").on("load",function (event) {
    function getMaxChildWidth() {
        let maxWidth = 0;
        $(".cmp-download__property--filename").each(function(){
            const componentFileNameWidth = parseInt($(this).width());
            if (componentFileNameWidth > maxWidth) {
                maxWidth = componentFileNameWidth;
            }
        });
        return maxWidth;
    }
    $(".cmp-download__property--filename").css("width", getMaxChildWidth());

    const popupFormComponent = $(this).find(".cmp-download--popup-form");
    const isRestrictedDownload = $(this).find(".cmp-download__action").is("[isRestrictedDownload]");
    const assetResourcePath = $(this).find(".cmp-download__action").attr("resource");

   
        $(this).find(".cmp-download .cmp-download__action").on("click",function() {

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
