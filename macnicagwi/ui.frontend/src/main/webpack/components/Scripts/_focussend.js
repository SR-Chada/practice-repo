import $ from "jquery";
import * as utils from '../../site/common/scripts/common.js'

$(document).ready(function () {
    $(function () {
        const form = document.querySelector('#new_form');
        const cls = $("#cmp-form-focussend-data").text();
        $("#btnSubmit").bind("click", function () {
            if(cls === "true"){
                //Focusend Master Secret - To be confirm
                const clientKey = $('.cmp-form-clientkey').text();
                const eventId = $('.cmp-form-eventid').text();
                const eventType = "web";
                const key = [];
                const value = [];
                var formData = {};
                $('.cmp-form-focussend').each(function () {
                    value.push($(this).val());
                    key.push($(this).attr("name"));
                });
                key.forEach((key, i) => formData[key] = value[i]);
                const childEventId = $('.cmp-form-childeventid').text();
                customEventInfo(
                    clientKey,
                    eventId,
                    childEventId,
                    formData,
                    function (res) {
                        console.log(res);
                        try{
                        const response = JSON.parse(res);
                        if (response != null && response.code === 200) {
                            console.log("It is success",response.code);
                            $('.cmp-form-success .popup-container').addClass('active');
                        } else {
                            console.log("It is error",response.code);
                            $('.cmp-form-error .popup-container').addClass('active');
                        }
                    }catch(error){
                        console.log("error in customEventInfo call back",error);
                    }
                    }
                );
                
            }
        });


    });

});
