

var currentPosition = 0;
var currentMargin = 0;
var slidesPerPage = 0;
var slidesCount = 0;
var slides = 0;
var containerWidth = $(window).width();
var previous, next;


function othersuggestionspreviousarrow() {
    if (currentPosition != 0) {
        $(this).parent().find(".cmp-othersuggestionscard__list").css('margin-left',currentMargin + (100 / slidesPerPage) + '%');
        currentMargin += (100 / slidesPerPage);
        currentPosition--;
    };
    if (currentPosition === 0) {
        previous.addClass('inactive');
    }
    if (currentPosition < slidesCount) {
        next.removeClass('inactive');
    }
}

function othersuggestionsnextarrow() {
    if (currentPosition != slidesCount) {
        $(".cmp-othersuggestionscard__list").css('margin-left',currentMargin - (100 / slidesPerPage) + '%');
        currentMargin -= (100 / slidesPerPage);
        currentPosition++;
    };
    if (currentPosition == slidesCount) {
        next.addClass('inactive');
    }
    if (currentPosition > 0) {
        previous.removeClass('inactive');
    }
}


function setParams(width) {

    if (width > 481) {
        slidesPerPage = 3;
    }
    
    slidesCount = slides - slidesPerPage;
    if (currentPosition > slidesCount) {
        currentPosition -= slidesPerPage;
    };
    currentMargin = - currentPosition * (100 / slidesPerPage);
    $(this).parent().find(".cmp-othersuggestionscard__list").css('margin-left',currentMargin + '%');
    if (currentPosition > 0) {
        previous.removeClass('inactive');
    }
    if (currentPosition < slidesCount) {
        next.removeClass('inactive');
    }
    if (currentPosition >= slidesCount) {
        next.addClass('inactive');
    }

}

$(window).on('resize', function () {
    if ($(window).width() < 480) {
        $(".cmp-othersuggestionscard__indicators").show();
        $(".cmp-othersuggestionscard__list .cmp-othersuggestionscard__content").hide();
        $(".cmp-othersuggestionscard__list .cmp-othersuggestionscard__content.0").show();
    } else {
        $(".cmp-othersuggestionscard__content").show();
        $(".cmp-othersuggestionscard__indicators").hide();
    }
});

$(document).ready(function(){
    $(".cmp-othersuggestionscard").trigger("load");
});

$(".cmp-othersuggestionscard").on("load",function (e) {
    slides = $(this).find('.cmp-othersuggestionscard__content').length;
    slidesCount = slides - slidesPerPage;
    previous= $(this).find(".cmp-othersuggestionscard__previous-arrow");
    next = $(this).find(".cmp-othersuggestionscard__next-arrow");
    next.on("click", othersuggestionsnextarrow);
    previous.on("click", othersuggestionspreviousarrow);

    setParams(containerWidth);

        
$(this).each(function(index, n) {
    const slidess = $(this).find(".cmp-othersuggestionscard__content").length;
   for (var i = 0; i < slides; i++) {
        const text = this.querySelectorAll(`.${$(this).attr("class")}__content`);
        $(text[i]).addClass(`${i}`);
        $(this).find(".cmp-othersuggestionscard__indicators").append(`<li class='cmp-othersuggestionscard__indicator'>${i}</li>`);
    }
    
    $(this).find(".cmp-othersuggestionscard__indicator").first().addClass("cmp-othersuggestionscard__indicator--active");
  $(this).find(".cmp-othersuggestionscard__indicators").hide();
   
});
  
$(window).trigger('resize');


$(".cmp-othersuggestionscard__indicator").click(function() {
   
    $(this).parent().find(".cmp-othersuggestionscard__indicator").removeClass("cmp-othersuggestionscard__indicator--active");
    $(this).addClass("cmp-othersuggestionscard__indicator--active");
    $(this).parent().parent().find(".cmp-othersuggestionscard__content").hide();
  const indicatorsLengthNum = $(this).text();
  $(this).parent().parent().find('.cmp-othersuggestionscard__content.'+indicatorsLengthNum).show();
});
    
});

