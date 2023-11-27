function pageSectionListingListpopup() {
    $(this).parent().parent().find(".cmp-pagesectionlisting__button").hide();
    $(this).parent().parent().find(".cmp-pagesectionlisting").show();
    $(this).parent().find(".cmp-pagesectionlisting__list-item").css("display", "block");
    $(this).parent().addClass("overlay");
    $(this).parent().find(".cmp-pagesectionlisting__closeicon").show();
} 
function pageSectionListingShowButton(pageSectionListingContainer){
    pageSectionListingContainer.parent().find(".cmp-pagesectionlisting").hide();
    pageSectionListingContainer.find(".cmp-pagesectionlisting__list-item").hide();
    pageSectionListingContainer.find(".cmp-pagesectionlisting__closeicon").hide();
    var pageSectionListingActiveText = pageSectionListingContainer.find(".cmp-pagesectionlisting__list-item--active").text();
    pageSectionListingContainer.parent().find(".cmp-pagesectionlisting__button").text(pageSectionListingActiveText);
    pageSectionListingContainer.parent().find(".cmp-pagesectionlisting__button").show();
    pageSectionListingContainer.parent().find(".cmp-pagesectionlisting__button").on("click", pageSectionListingListpopup);
    
}
function pageSectionListingShowList(pageSectionListingContainer){
    pageSectionListingContainer.parent().removeClass("overlay");
    pageSectionListingContainer.parent().find(".cmp-pagesectionlisting").show();
    pageSectionListingContainer.find(".cmp-pagesectionlisting__list-item").show();
    pageSectionListingContainer.parent().find(".cmp-pagesectionlisting__button").hide();
    pageSectionListingContainer.find(".cmp-pagesectionlisting__closeicon").hide();
}

function closeIconClickFunction() {
    pageSectionListingActiveText = $(this).parent().find(".cmp-pagesectionlisting__list-item--active").text();
    $(this).parent().parent().find(".cmp-pagesectionlisting__button").text(pageSectionListingActiveText);
    $(this).parent().parent().removeClass("overlay");
    $(this).parent().parent().find(".cmp-pagesectionlisting").hide();
    $(this).parent().parent().find(".cmp-pagesectionlisting__button").show();
    $(this).parent().find(".cmp-pagesectionlisting__list-item").hide();
   
}


$(document).ready(function () {

        $(".cmp-pagesectionlisting").trigger("load");
});

function pageSectionListingResizeEvent(pageSectionListingContainer){
    $(window).on('resize', function (event) {
        if ($(window).width() < 1025) {
            pageSectionListingShowButton(pageSectionListingContainer);
            $(".cmp-pagesectionlisting__list-item").parent().removeClass("overlay").on("click", closeIconClickFunction);
        }
        else {
            pageSectionListingShowList(pageSectionListingContainer);
        }
       
    });
}

$(".cmp-pagesectionlisting").on("load",function (event) {
   $('.cmp-pagesectionlisting__list li:first').addClass('cmp-pagesectionlisting__list-item--active');
   $(".cmp-pagesectionlisting__list-item a").each(function(){
        const pageSectionListingListClass = $(this);
        const url = window.location.href;
        //make page section list item active if it is part of address bar url.
        if(url.indexOf($(this).attr("href")) != -1){
            $(this).parent().addClass("cmp-pagesectionlisting__list-item--active");
            $('body, html').animate({ scrollTop: 0 });
           
        }
    });

    $(".cmp-pagesectionlisting__closeicon").on("click", closeIconClickFunction);   
   // $(".cmp-pagesectionlisting__list-item").on("click", closeIconClickFunction); 
   // $(".cmp-pagesectionlisting__list-item").parent().removeClass("overlay").on("click", closeIconClickFunction); 
    
    const activeText = $(".cmp-pagesectionlisting__list-item--active").text();
    const button = document.createElement("button");
    $(button).addClass("cmp-pagesectionlisting__button")
        .html(activeText)
        .insertBefore($(".cmp-pagesectionlisting"));
    $(".cmp-pagesectionlisting__button").hide();
   
    const pageSectionListingContainer = $(this);
    var pageSectionListingActiveText = pageSectionListingContainer.find(".cmp-pagesectionlisting__list .cmp-pagesectionlisting__list-item--active").text();
    
    pageSectionListingContainer.find(".cmp-pagesectionlisting__button").text(pageSectionListingActiveText);
     
     $(window).trigger('resize', pageSectionListingResizeEvent(pageSectionListingContainer));

     $(".cmp-pagesectionlisting").parents().css("overflow", "visible");
     $(".cmp-pagesectionlisting").parents().css("visibility", "visible");
     pageSectionListingContainer.find(".cmp-pagesectionlisting__closeicon").hide();
        
    $(window).scroll(function () {
         
         $(".cmp-pagesectionlisting__scrolltosection").css({'position' : 'sticky', 'top' : '57px'});
     });

     $('.cmp-pagesectionlisting__list-item .cmp-pagesectionlisting__list-item-link').click(function(){
        $(".cmp-pagesectionlisting__list-item-link").parent().removeClass("cmp-pagesectionlisting__list-item--active");
        $(".cmp-pagesectionlisting__list-item").parent().removeClass("overlay");
        $(this).parent().addClass("cmp-pagesectionlisting__list-item--active");
         var target = $($(this).attr('href'));
        if(target.length){
          var scrollTo = target.offset().top - 120;
          $('body, html').animate({scrollTop: scrollTo+'px'}, 800);
        }
      });
    
});