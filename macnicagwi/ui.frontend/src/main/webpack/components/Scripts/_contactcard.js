$(document).ready(function () {
   $(".cmp-contactcard").trigger("load");
   
    
});


function popupCloseIconClickFunction(){
    $(this).parent().parent().parent().find(".cmp-contactcard__popup-container.popup-container").css("display","none");
}

$(".cmp-contactcard").on("load",function (event) {

    $(this).find(".cmp-contactcard__popup-container.popup-container").hide();
    $(this).find(".cmp-contactcard__route-instrcutions--icons").click(function(){
        $(this).parent().parent().find(".cmp-contactcard__popup-container.popup-container").css("display","flex");

    })
    $(".cmp-popup__close-icon").on("click", popupCloseIconClickFunction);
    $(this).find(".cmp-contactcard__read-more-button").click(function() {
        $(this).parent().find(".cmp-contactcard__emails").show();
    });

    function getMaxHeightContactCardAddressValue() {
        maxHeight = 0;
        $(".cmp-contactcard__address--value").each(function(){
            contactCardAddressValueHeight = parseInt($(this).height());
            if (contactCardAddressValueHeight > maxHeight) {
                maxHeight = contactCardAddressValueHeight;
            }
        });
        return maxHeight;
    }
    $(".cmp-contactcard__address--value").css("height", getMaxHeightContactCardAddressValue());

    function getMaxHeightContactCardTitleValue() {
        maxHeightTitle = 0;
        $(".cmp-contactcard__title").each(function(){
            contactCardTitleValueHeight = parseInt($(this).height());
            if (contactCardTitleValueHeight > maxHeightTitle) {
                maxHeightTitle = contactCardTitleValueHeight;
            }
        });
        return maxHeightTitle;
    }

    $(".cmp-contactcard__title").css("height", getMaxHeightContactCardTitleValue());

});

