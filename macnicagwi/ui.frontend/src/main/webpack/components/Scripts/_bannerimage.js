$(document).ready(function () {
    $(".cmp-bannerimage").trigger("load");
});

$(".cmp-bannerimage").on("load",function () {

    $(this).find(".cmp-image").click(function(){
        $(".popup-container").addClass("active");
    })
    
});


