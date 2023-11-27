$(function(){
    $(".cmp-breadcrumb__list").prepend("<span class='cmp-breadcrumb__list--homeicon'></span>");
    $(".cmp-breadcrumb__list--homeicon").click(function() {
        const host = window.location.origin;
        const urlPath = $(".cmp-breadcrumb__list > .cmp-breadcrumb__item > a").attr("href");
        window.location = host + urlPath;
    });
    let text = $(".cmp-breadcrumb__list > li:first > a > span").text();
    text = text.replace(text, " ");
    $(".cmp-breadcrumb__list > li:first > a > span").html(text);
})