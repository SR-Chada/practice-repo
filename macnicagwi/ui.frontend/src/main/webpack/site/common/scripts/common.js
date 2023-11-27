/**
 * @param {*} cookieName name of the cookie to be created
 * @param {*} cookieValue value to store in cookie
 * @param {*} expiryTime time of expiry
 * creates new cookie with specified name, value and expiry time
 */
 function createCookie(cookieName, cookieValue, expiryTime) {
    document.cookie = `${site}_${cookieName}=${cookieValue};expires=${expiryTime}; path=/;secure`;
}
/**
 *
 * @param {*} cookieName name of the cookie to be deleted
 * removes the cookie with specified name from browser
 */
function deleteCookie(cookieName) {
    document.cookie =
        site + "_" + cookieName + "=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}
/**
 * @param {*} cookieName
 * @returns value of the cookieName
 * if exists in browser, null otherwise
 */
export function getCookie(cookieName, withOutSite) {
    let cookieValue = null;
    if (withOutSite) {
        cookieName = cookieName + "=";
    } else {
        cookieName = site + "_" + cookieName + "=";
    }
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieEntries = decodedCookie.split(";");
    for (let i = cookieEntries.length - 1; i >= 0; i--) {
        let thisCookie = cookieEntries[i];
        while (thisCookie.charAt(0) == " ") {
            thisCookie = thisCookie.substring(1);
        }
        if (thisCookie.indexOf(cookieName) == 0) {
            cookieValue = thisCookie.substring(cookieName.length, thisCookie.length);
            break;
        }
    }
    return cookieValue;
}
/**
* determines if the script
* is loaded in author mode or not
*/
export function isEditor() {
    const isAuthorMode = getCookie("wcmmode", true) == "edit" ? true : false;
    if (!isAuthorMode) {
        return false;
    }
    if (window.location.search) {
        let queryParams = window.location.search.split('?');
        if (queryParams.length > 0) {
            queryParams = queryParams[1].split('&');
            const isViewAsPublished = queryParams.includes("wcmmode=disabled");
            if(isViewAsPublished) {
                return !isAuthorMode;
            }
        }
    }
    return isAuthorMode;
}

export const HTTPMethods = {
    GET: "GET",
    POST: "POST",
    PUT: "PUT",
    DELETE: "DELETE"
}

/**
 * 
 * @param {*} method HTTP Methods: GET, POST, PUT, DELETE
 * @param {*} dataResourcePath data-resource-path attribute value from component
 * @param {*} body json object (if any)
 * @returns json response
 */
export function getComponentJson(method, dataResourcePath, body) {

    const requestJson = {
        type: method,
        url: dataResourcePath,
        dataType: "json",
        headers: {
            "Content-Type": "application/json",
        }
    }
    if(body != undefined || body != null) {
        requestJson['data'] = body
    }
    return $.ajax(requestJson);

}

export const searchFilterTypes = {
    multiLevelFilter: "multiLevelFilter",
    singleLevelFilter: "singleLevelFilter",
    none: "none"
}

export function getSearchFiltersJson(response, customClass, filterType, isFreeTextSearch, freeTextSearchPlaceholder) {
    const filterTypes = {
        "multiLevelFilter": false,
        "singleLevelFilter": false
    }
    switch(filterType) {
      case "multiLevelFilter": { filterTypes["multiLevelFilter"] = true; break; }
      case "singleLevelFilter": { filterTypes["singleLevelFilter"] = true; break; }
    }
    const searchFiltersJson = {
      "freeTextSearchPlaceholder":freeTextSearchPlaceholder,
      "isFreeTextSearch": isFreeTextSearch,
      "filterTypes":filterTypes,
      "customClass":customClass, 
      "searchFilters":response.searchFilters
    };
    return searchFiltersJson;
}


export const regexPatterns = {
    email: /[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/, // alows "_" in special characters
    phone: /(\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27|260|263)(9[976]\d|8[987530]\d|6[987]\d|5[90]\d|42\d|3[875]\d|2[98654321]\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\d{4,14}$/,
    text: {
        default: /[\s\S]*/,   // allows everything
        name: /^[\w\s.-]+$/, // allows only . - letters space
        address: /^[^$&+;=?[\]@|{}'<>^*~`()%!_]+$/,      // allows only , . # - : / letters numbers
        noSpecialChars: /[^$&+,:;=?[\]@#|{}'<>.^*~`()%!-_/]/ // to avoid all special characters
    },
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/,
    time: /^([01][0-9]|2[0-3]):[0-5][0-9]$/ // represents time in 24hr format
}

export const validateFormRules =  {
    text: {
        minlength: 2,
        regex: regexPatterns.text.default
    },
    textarea: {
        regex: regexPatterns.text.default
    },
    name: {
        minlength: 2,
        regex: regexPatterns.text.name
    },
    email: {
        email: false,
        customemail: regexPatterns.email
    },
    address: {
        regex: regexPatterns.text.address
    },
    tel : {
        regex: regexPatterns.phone
    },
    date: {
        date : true
    }, 
    time: {
        regex: regexPatterns.time
    },
    number: {
        digits: true
    },
    password: {
        regex: regexPatterns.password
    },
    radio: {
        required: true
    },
    checkbox: {
        minlength: 1
    },
    dropdown: {
        required: true
    },
    multiselect: {
        required: true
    }
}

/**
 * 
 * @returns host origin. Example: https://macnica.com 
 */
export function getHostOrigin() {
    return window.location.origin;
}

/**
 * 
 * @param {*} popupFormComponent value of data-download-form attribute
 * @param {*} dataResourcePath value of data-resource-path
 * @param {*} popupScript popup.js file
 * @param {*} formScript form.js file
 * @returns none
 */
 export function downloadAssetFile(popupFormComponent, assetResourcePath, popupScript, formScript) {
    const cookieValue = getCookie("downloadRestrictedAssets",true);
    const host = getHostOrigin();
    if(cookieValue == null) {
        popupScript.executePopup();
        formScript.executeForm(assetResourcePath);
        popupFormComponent.find(".popup-container:first").addClass("active"); // show popup form
    } else {
        if(assetResourcePath != null) {
            // download the file as cookie exists
            window.location = host + assetResourcePath;
        }
    }
    return;    
}