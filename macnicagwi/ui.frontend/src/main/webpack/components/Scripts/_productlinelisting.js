/* import * as template from './productlinelisting.hbs'

$(document).ready(function(){
    $(".cmp-productlinelisting").trigger("load");
});

function productLineListingApi(dataResourcePath) {
    return $.ajax({
        type: "GET",
        url: dataResourcePath + ".json",
        dataType: "json",
        headers: {
          "Content-Type": "application/json",
        }
    });
}

$(".cmp-productlinelisting").on("load",function (event) {
    const pageRootPath = $(event.currentTarget).attr("data-resource-path");
     //hide component if servlet resource path is not available. 
    if(pageRootPath == undefined) {
        $(this).hide();
        return;
    }
    const apiResult = productLineListingApi(pageRootPath);
    apiResult.done((response)=>{
        if(response && response.searchResults.length > 0) {
            const productlinelistingHtml = template(response);
           $(this).find('.cmp-productlinelisting__product-line-cards').html(productlinelistingHtml);
        }
    });
    apiResult.fail((error)=>{
        $(this).parent().hide(); 
    });
})
 */