import * as utils from './common.js';

var globalHeaderElements;
var globalHeaderNavigation;
var subsidiaryHeaderElements;

function getSearchResults(searchText) {
  const dataSearchResultsPagePath = $(".cmp-experiencefragment--subsidiary-header-xf .search:first > section").attr("data-resource-path");
  const host = utils.getHostOrigin();
  window.location = host + dataSearchResultsPagePath + ".html?q=" + searchText;
}

function relocateSearchAndHide() {
  $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header"
  ).append($(".cmp-experiencefragment--subsidiary-header-xf .search"));
  $(".search").hide();
}

function searchBarFunc() {
  if ($("#subsidiary-search-icon").css("background-image") != "none") {
    relocateSearchAndHide();
  }
  $("#subsidiary-search-icon").click(function () {
    $(".search").slideDown();
    $(".cmp-search__clear").show();
    $(this).hide();
  });

  $(".cmp-search__clear-icon").click(function () {
    $(".search").slideUp();
    $("#subsidiary-search-icon").show();
  });

  $(".cmp-experiencefragment--subsidiary-header-xf .cmp-search__icon:first").click(function(){
    const searchText = $(this).parent().find("input:first").val();
    getSearchResults(searchText);
  });

  $(".cmp-experiencefragment--subsidiary-header-xf .search:first .cmp-search__input").keypress(function(event){
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
      event.preventDefault();
      const searchText = $(this).val();
      getSearchResults(searchText);
    }
  });
}

function subsidiarySideHeaderNav() {
  const subsidiaryHeaderContainer = $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header > .cmp-container"
  );
  const hamburgerSearchContainerHtml =
    "<div class='hamburger-search-container'/>";
  subsidiaryHeaderContainer.append(hamburgerSearchContainerHtml);
  $(".hamburger-search-container").append("<div id='subsidiary-search-icon'/>");
  $(".hamburger-search-container").append(
    "<div id='subsidiary-hamburger-icon'/>"
  );
  const sideHeaderNavSubsidiaryHtml =
    "<div class='side-header-nav_subsidiary'/>";
  $(".side-header-nav-container").append(sideHeaderNavSubsidiaryHtml);
  const downArrowHtml = "<div class='down-arrow_subsidiary'/>";
  $(".side-header-nav_subsidiary").prepend(downArrowHtml);
  $(".side-header-nav_subsidiary").append(
    "<div class='subsidiary-elements-container'/>"
  );
  searchBarFunc();
}

function globalSideHeaderNav() {
  const hamburgerHtml =
    "<div class='hamburger-menu-icon_global'><div id='hamburger-icon'/></div>";
  $("header.experiencefragment").append(hamburgerHtml);
  const sideHeaderNavGlobalHtml = "<div class='side-header-nav_global'/>";
  $(".side-header-nav-container").append(sideHeaderNavGlobalHtml);
  const downArrowHtml = "<div class='down-arrow_global'/>";
  $(".side-header-nav_global").prepend(downArrowHtml);
}

function toggleSideHeaderNavGlobal(languagenavigation) {
  $(".down-arrow_global").hide();
  $(".down-arrow_subsidiary").show();
  $(".side-header-nav_subsidiary .navigation").hide();
  $(".side-header-nav_global .navigation").show();
  $(".side-header-nav_global #header-container > div").not(":first").show();
  $(".side-header-nav_subsidiary").css("position","fixed");
  $(".side-header-nav_global").css("position","relative");
  $(".side-header-nav_global").css("z-index","0");
  $(".side-header-nav_subsidiary").css("z-index","1");
  $(".subsidiary-elements-container > .image").css("position","unset");
  $(".subsidiary-elements-container > .languagenavigation").css("position","unset");
  languagenavigation.hide();
}

function toggleSideHeaderNavSubsidiary(languagenavigation) {
  $(".down-arrow_global").show();
  $(".down-arrow_subsidiary").hide();
  $(".side-header-nav_global .navigation").hide();
  $(".side-header-nav_subsidiary .navigation").show();
  $(".side-header-nav_global #header-container > div").not(":first").hide();
  $(".side-header-nav_subsidiary").css("position","relative");
  $(".side-header-nav_global").css("position","sticky");
  $(".side-header-nav_global").css("z-index","1");
  $(".side-header-nav_subsidiary").css("z-index","0");
  $(".subsidiary-elements-container > .image").css("position","sticky");
  $(".subsidiary-elements-container > .languagenavigation").css("position","fixed");
  //languagenavigation.css("display", "inline-flex");
}

function detachAndAppendGlobalHeader() {
  globalHeaderElements.detach().appendTo(".side-header-nav_global");
  globalHeaderNavigation.detach().appendTo(".side-header-nav_global");
  $(".side-header-nav_global .line-separator").hide();
  $(
    ".side-header-nav-container .side-header-nav_global #header-container #for-investors"
  ).show();
  $(
    ".side-header-nav-container .side-header-nav_global #header-container #select-region"
  ).show();
}

function detachAndAppendSubsidiaryHeader() {
  subsidiaryHeaderElements.detach().appendTo(".subsidiary-elements-container");
  $(".subsidiary-elements-container .language-translator-icon").hide();
  $(".subsidiary-elements-container .search").hide();
  $(".subsidiary-elements-container .hamburger-search-container").hide();
  $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header > .cmp-container"
  ).css("justify-content", "flex-end");
}

function applySideHeaderNav() {
  const sideNavContainerHtml = "<div class='side-header-nav-container'/>";
  const languagenavigation = $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment.cmp-experiencefragment--header .cmp-container .languagenavigation"
  );
  $("body").append(sideNavContainerHtml);
  globalSideHeaderNav();
  subsidiarySideHeaderNav();
  $(".side-header-nav-container").hide();

   /* hide side nav when we click on navigation items */
   function hideSideNav(){
    $(".side-header-nav-container .cmp-navigation__item").click(function() {
       $('.side-header-nav-container').hide();
       //detach global header
       $(".side-header-nav_global #header-container")
        .detach()
        .appendTo(
          "header .cmp-experiencefragment--header .cmp-container .container"
        );
      //detach subsidiary header
       $(".side-header-nav_subsidiary .subsidiary-elements-container")
        .children()
        .detach()
        .prependTo(
          ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header > .cmp-container"
        );
        languagenavigation.hide();
    });
  }

  // global hamburger menu
  $(".hamburger-menu-icon_global").click(function () {
    detachAndAppendGlobalHeader();
    detachAndAppendSubsidiaryHeader();
    toggleSideHeaderNavGlobal(languagenavigation);
    $(".side-header-nav-container").show();
    hideSideNav();
  });

  //subsidiary hamburger menu
  $("#subsidiary-hamburger-icon").click(function () {
    detachAndAppendGlobalHeader();
    detachAndAppendSubsidiaryHeader();
    toggleSideHeaderNavSubsidiary(languagenavigation);
    $(".side-header-nav-container").show();
    hideSideNav();
  });


  $(".down-arrow_subsidiary").click(function () {
    toggleSideHeaderNavSubsidiary(languagenavigation);
  });

  // close side nav 
  $(".side-header-nav-container, .down-arrow_global").click(function () {
    languagenavigation.hide();
    $(".subsidiary-elements-container .language-translator-icon").hide();
    $(".side-header-nav-container").hide();
    //detach global header container
    $(".side-header-nav_global #header-container")
      .detach()
      .appendTo(
        "header .cmp-experiencefragment--header .cmp-container .container"
      );

    //detach global header navigation
    $(".side-header-nav_global .navigation")
      .detach()
      .appendTo("header .cmp-experiencefragment--header > .cmp-container");

    //detach subsidiary header container
    $(".side-header-nav_subsidiary .subsidiary-elements-container")
      .children()
      .detach()
      .prependTo(
        ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header > .cmp-container"
      );

    $(
      ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header .hamburger-search-container"
    ).show();

    $(
      ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header .navigation"
    ).hide();

    relocateSearchAndHide();

    $(
      "header .cmp-experiencefragment--header > .cmp-container .container #header-container button"
    ).hide();
    $("header.experiencefragment #header-container .line-separator").hide();
    $(
      ".cmp-experiencefragment.cmp-experiencefragment--header > .cmp-container > .navigation"
    ).hide();
    $(
      ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header > .cmp-container"
    ).css("justify-content", "space-between");
    });
  $(".side-header-nav_global").click(function (e) {
    e.stopPropagation();
  });
  $(".side-header-nav_subsidiary").click(function (e) {
    e.stopPropagation();
  });
}

function scrollTopFunctionality() {
  var scrollTop = $("#scroll-top");
  $(window).scroll(function() {
    // declare variable
    var topPos = $(this).scrollTop();

    // if user scrolls down - show scroll to top button
    if (topPos > 100) {
      $(scrollTop).css("opacity", "1");

    } else {
      $(scrollTop).css("opacity", "0");
    }

  }); 
  
  $("#scroll-top").click(function() {
    $('html, body').animate({
      scrollTop: 0
    }, 500);
    return false;

  }); 
}



$(document).ready(function () {

  // dropdown hover menu active
  $(".cmp-navigation__item--level-0 > .cmp-navigation__group").each(function(){
    $(this).hover(function(){
      $(this).parent().find("> .cmp-navigation__item-link").addClass("cmp-navigation__item-link--active");
    });
    $(this).mouseleave(function(){
      $(this).parent().find("> .cmp-navigation__item-link").removeClass("cmp-navigation__item-link--active");
    });
  });

  const lineSeparatorHtml = "<div class='line-separator'/>";
  $(lineSeparatorHtml).insertBefore("#header-container .button:last");
   const languageTranslatorHtml = "<div class='language-translator-icon'/>";
  $(languageTranslatorHtml).insertBefore(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-container .languagenavigation"
   );
  
var listItems = $(".cmp-experiencefragment--subsidiary-header-xf .cmp-languagenavigation__group li"); 
listItems.each(function(index, element) {
    if (index > 0) {
      $(element).before("<div class='language-navigation-line-separator'>|</div>");
  }
});
  globalHeaderElements = $("#header-container");
  globalHeaderNavigation = $("header .navigation");
  subsidiaryHeaderElements = $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header .cmp-container"
  ).children();
  applySideHeaderNav();
  const subsidiaryNav = $(
    ".cmp-experiencefragment--subsidiary-header-xf .navigation"
  );
  const stickyOffset = subsidiaryNav.offset().top;

  $(window).scroll(function () {
    if ($(window).width() > 1024) {
      var scroll = $(window).scrollTop();
      if (scroll >= stickyOffset) {
        subsidiaryNav.css("position", "fixed");
        subsidiaryNav.css("top", "0");
        subsidiaryNav.css("left", "0");
        subsidiaryNav.css("z-index", "999");
        subsidiaryNav.addClass("cmp-navigation--transition");
        $(".cmp-experiencefragment--subsidiary-header-xf .cmp-navigation").css("justify-content", "center");
      } else {
        subsidiaryNav.css("position", "");
        subsidiaryNav.css("top", "");
        subsidiaryNav.css("left", "");
        subsidiaryNav.css("z-index", "");
        subsidiaryNav.removeClass("cmp-navigation--transition");
        $(".cmp-experiencefragment--subsidiary-header-xf .cmp-navigation").css("justify-content", "initial");
      }
    }
  });

  const languageTranslatorIcon = $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment.cmp-experiencefragment--header .cmp-container .language-translator-icon"
  );
  const languagenavigation = $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment.cmp-experiencefragment--header .cmp-container .languagenavigation"
  );
  const navigation = $(
    ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment.cmp-experiencefragment--header .cmp-container .navigation"
  );
  $(window).bind('resize'); 
  $(window).on("resize", function () {
    if ($(window).width() < 1025) {
      relocateSearchAndHide();
      
       $("header.experiencefragment #header-container #select-region").show();
      
      languageTranslatorIcon.hide();
      languagenavigation.hide();
      navigation.hide();
     } else {
      subsidiaryHeaderElements.show();
      globalHeaderElements.children().show();
      globalHeaderNavigation.show();
      
       $("header.experiencefragment #header-container #select-region").show();
    
      languageTranslatorIcon.show();
      languagenavigation.show();
      languagenavigation.css("position","unset");
      navigation.show();
      $(".search").insertBefore(
        ".cmp-experiencefragment--subsidiary-header-xf .cmp-experiencefragment--header .cmp-container .navigation"
      );
     }
  });

  scrollTopFunctionality();
});

$(".cmp-search__input").focus(function(event){
  $(".search").css("display","block");
  $(window).unbind('resize'); 
});
