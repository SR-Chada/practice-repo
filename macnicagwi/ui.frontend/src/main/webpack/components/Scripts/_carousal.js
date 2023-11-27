try {
    $(".cmp-carousel__action").on("click", function () {
        var iframes = document.querySelectorAll('iframe');

        if (iframes.length > 0) {
            iframes.forEach(function (iframe) {
                var iframeSrc = iframe.src;
                iframe.src = iframeSrc;
            });
        }
    });
} catch (error) {
    console.log("There is an error in stopping the iframe video: ", error);
}
