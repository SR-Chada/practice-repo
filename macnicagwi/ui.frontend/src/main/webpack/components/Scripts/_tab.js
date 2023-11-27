function popupListItemClickFunction() {
    tabListActiveText = $(this).parent().find(".cmp-tabs__tab--active").text();
    $(this).parent().parent().parent().parent().find(".cmp-tabs__button").text(tabListActiveText);
    $(this).parent().parent().parent().parent().find(".cmp-tabs").removeClass("overlay");
    $(this).parent().parent().parent().parent().find(".cmp-tabs__tablist").hide();
}

function tablistpopup() {
    $(this).parent().find(".cmp-tabs__tablist").css("display", "block");
    $(this).parent().find(".cmp-tabs").addClass("overlay");
    $(this).parent().find(".overlay").css("visibility", "visible");
    $(this).parent().find(".cmp-tabs__popup-close-icon").show();
    $(this).parent().find(".cmp-tabs__tablist").show();
    $(".overlay .cmp-tabs__tab").on("click", popupListItemClickFunction);

} 
function popupCloseIconClickFunction() {
    tabListActiveText = $(this).parent().find(".cmp-tabs__tab--active").text();
    $(this).parent().parent().parent().find(".cmp-tabs__button").text(tabListActiveText);
    $(this).parent().parent().parent().find(".cmp-tabs").removeClass("overlay");
    $(this).parent().parent().parent().find(".cmp-tabs__tablist").hide();
}

 

function tabshowbutton(tabsContainer){
     var tabListActiveText = tabsContainer.find(".cmp-tabs__tab.cmp-tabs__tab--active").text();
     tabsContainer.parent().find(".cmp-tabs__button").text(tabListActiveText);
     if(tabListActiveText != ""){
        tabsContainer.parent().find(".cmp-tabs__button").show();
     }
     
     tabsContainer.parent().find(".cmp-tabs__button").on("click", tablistpopup);
     if(tabsContainer.parent().attr('class').includes('cmp-tabs__tablist--greybackground')){
        tabsContainer.parent().find(".cmp-tabs__content .cmp-tabs__tablist").hide();
     }
     
  }   
    function tabshowlist(tabsContainer){
       tabsContainer.parent().find(".cmp-tabs").removeClass("overlay");
       tabsContainer.parent().find(".cmp-tabs__button").hide();
       if(tabsContainer.parent().attr('class') == "tabs panelcontainer cmp-tabs__tablist--greybackground aem-GridColumn aem-GridColumn--default--12"){
            tabsContainer.parent().find(".cmp-tabs__content .cmp-tabs__tablist").show();
            tabsContainer.parent().find(".cmp-tabs__content .cmp-tabs__tablist").css("display", "flex");
         }
         var tabListActiveText = tabsContainer.find(".cmp-tabs__tab--active").text();
         if(tabListActiveText === ""){
             tabsContainer.parent().find(".cmp-tabs__tablist").hide();
          }
        tabsContainer.find(".cmp-tabs__popup-close-icon").hide();
        tabsContainer.parent().find(".cmp-tabs__button").hide();
}

function tabResizeEvent(tabsContainer){
    $(window).on('resize', function (event) {
        if ($(window).width() < 1025) {
            tabshowbutton(tabsContainer);
            
        }
        else {
            tabshowlist(tabsContainer);
        }
       
    });
}

$(document).ready(function () {
    const MobileTabListButton = document.createElement("button");
    $(MobileTabListButton).addClass("cmp-tabs__button")
        .insertBefore($(".cmp-tabs__tablist--greybackground .cmp-tabs"));
        $(".cmp-tabs").trigger("load");
        $(".cmp-tabs__popup-close-icon").on("click", popupCloseIconClickFunction); 
          
       
});



$(".cmp-tabs").on("load",function () {
    const tabsContainer = $(this);
    var tabListActiveText = tabsContainer.find(".cmp-tabs__tab--active").text();
    $(window).trigger('resize', tabResizeEvent(tabsContainer));
    if(tabListActiveText === ""){
        tabsContainer.parent().find(".cmp-tabs__tablist").hide();
     }
    
    if(tabsContainer.parent().attr('class') === "tabs panelcontainer cmp-tabs__tablist--greybackground"){
       
        tabsContainer.parent().find(".cmp-tabs__button").text(tabListActiveText);
     }
     function windowwidth(){
        if ($(window).width() < 1025) {
            tabshowbutton(tabsContainer);
        }
        else {
            tabshowlist(tabsContainer);
        }
    }
     windowwidth();
     $(window).trigger('resize');

     tabsContainer.find(".cmp-tabs__popup-close-icon").hide();

    $(window).on('resize', function (event) {
        windowwidth();
       
    });
    
});