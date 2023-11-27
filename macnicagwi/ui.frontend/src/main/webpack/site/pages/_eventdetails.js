/**
 * Checks if the button should be enabled or disabled. 
 * @param {*} pagePropertiesJson  Event registration page metadata properties
 * @returns true: if the event registrations are open. False otherwise. 
 */

 function isRegistrationEnabled(pagePropertiesJson){
    const isEnabled = true;
    const metadata = pagePropertiesJson;
    console.log("metadata", metadata);
    if(!metadata){
        return !isEnabled;
    }
    try {
    const eventEndDate = metadata.endDate? new Date(metadata.endDate) : null;
    const eventRegistrationStartDate = metadata.startDateOfReg ? new Date(metadata.startDateOfReg) : null;
    const eventRegistrationEndDate =  metadata.endDateOfReg ? new Date(metadata.endDateOfReg) : null;
    const eventStatus = metadata.status.split("/")[1]; 
    
    const currentDate = new Date();

    const isEventClosed =  eventEndDate  && currentDate > eventEndDate ? true : false;
    const isEventRegistrationNotStarted = eventRegistrationStartDate && currentDate < eventRegistrationStartDate? true : false;
    const isEventRegistrationClosed = eventRegistrationEndDate && currentDate > eventRegistrationEndDate? true : false;
    const isEventStatusClosed = eventStatus && (eventStatus.toUpperCase() != "ONGOING" && eventStatus.toUpperCase() != "SIGN-UP") ? true : false;

    if(isEventClosed || isEventRegistrationNotStarted || isEventRegistrationClosed || isEventStatusClosed){
        return !isEnabled;
    }
 

    return isEnabled;
}
catch(err) {
    console.log("error", err);
  }
}

/**
 * Execute on document ready
 */
$(function(){
    //check if event registration button exists
    if($(".cmp-button--event-registration-button").length<1){
        return;
    }
    const currentPagePath = window.location.pathname;
    let pagePropertiesPath;
    if(currentPagePath.includes(".html")) {
        pagePropertiesPath =  currentPagePath.replace(".html", ".details.html");
    } else if(currentPagePath.slice(-1) == "/"){
        pagePropertiesPath = currentPagePath.slice(0,-1) + ".details.html";
    }
    let pagePropertiesJson ={};

    //Fetch page metadata to get the registration status, start date and end date
    $.ajax({
        url: pagePropertiesPath,
        headers: {  
            dataType:"json",
            Accept:"application/json"
        },
        success: function(data) {
            pagePropertiesJson = data ? data :pagePropertiesJson;
        },
        async:false
      });
      

    // Disable button if the registration status is closed, or if start date is in future or if end data is past. 
    if(!isRegistrationEnabled(pagePropertiesJson)){
        
        $(".cmp-button--event-registration-button .cmp-button").prop("disabled",true);
        return;
    }

    //if button exists and is enabled, register event listner
    $(".cmp-button--event-registration-button .cmp-button").click(function(){
        $(".event-registration-form-xf .popup-container").closest(".popup-container").addClass("active");
    });    
    
});


