/*******************************************************************************
 * Copyright 2016 Adobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
(function($, channel, Coral) {
    "use strict";

    var EDIT_DIALOG = ".cmp-form-options__editDialog";
    var TEXTFIELD_REQUIRED = ".cmp-form-options__required";
    var TEXTFIELD_REQUIREDMESSAGE = ".cmp-form-options__requiredmessage";
    var TEXTFIELD_READONLYSELECTED_ALERT = ".cmp-form-options__readonlyselected-alert";

    /**
     * Toggles the display of the given element based on the actual and the expected values.
     * If the actualValue is equal to the expectedValue , then the element is shown,
     * otherwise the element is hidden.
     *
     * @param {HTMLElement} element The html element to show/hide.
     * @param {*} expectedValue The value to test against.
     * @param {*} actualValue The value to test.
     */
    function checkAndDisplay(element, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element.show();
        } else {
            element.hide();
        }
    }

      

    /**
     * Toggles the visibility of the required message input field based on the "required" input field.
     * If the "required" field is set, the required message field is shown,
     * otherwise it is hidden.
     *
     * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
     */
    function handleRequiredMessage(dialog) {
        var component = dialog.find(TEXTFIELD_REQUIRED)[0];
        var requiredMessage = dialog.find(TEXTFIELD_REQUIREDMESSAGE);
        checkAndDisplay(requiredMessage,
            true,
            component.checked);
        component.on("change", function() {
            checkAndDisplay(requiredMessage,
                true,
                component.checked);
        });
    }

   
    /**
     * Initialise the conditional display of the various elements of the dialog.
     *
     * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
     */
    function initialise(dialog) {
        dialog = $(dialog);
        handleRequiredMessage(dialog);

        var readonly = dialog.find(TEXTFIELD_READONLY)[0];
        var required = dialog.find(TEXTFIELD_REQUIRED)[0];
        handleExclusion(readonly,
            required,
            dialog.find(TEXTFIELD_REQUIREDSELECTED_ALERT)[0]);
        handleExclusion(required,
            readonly,
            dialog.find(TEXTFIELD_READONLYSELECTED_ALERT)[0]);
    }

    $(document).on("foundation-contentloaded", function(e) {
        if ($(e.target).find(EDIT_DIALOG).length > 0) {
            Coral.commons.ready(e.target, function(component) {
                initialise(component);
            });
        }
    });

})(jQuery, jQuery(document), Coral);
