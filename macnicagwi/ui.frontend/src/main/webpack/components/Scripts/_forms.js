import $ from "jquery";
import validate from "jquery-validation";
import * as utils from '../../site/common/scripts/common.js'

var assetResource;

function addRules(validateJson, inputType, inputName, constraintMsgType) {
  if(constraintMsgType != undefined || constraintMsgType != null) {
    validateJson.rules[inputName] = utils.validateFormRules[constraintMsgType];
  } else {
    validateJson.rules[inputName] = utils.validateFormRules[inputType];
  }
}

function addConstraintMessage(validateJson, inputType, inputName, constraintMsg) {
    Object.keys(validateJson.rules[inputName]).forEach(key=>{
      if(validateJson.messages[inputName] != undefined) {
        validateJson.messages[inputName][key] = constraintMsg;
      } else {
        validateJson.messages[inputName] = { };
        validateJson.messages[inputName][key] = constraintMsg;
      }
      
    });
}

function requiredInput(requiredAttr, requiredMsg, validateJson, input, inputName) {
    validateJson.rules[inputName].required = true;
    if(validateJson.messages[inputName] == undefined) {
      validateJson.messages[inputName] = {};
      validateJson.messages[inputName].required = requiredMsg;
    } else {
      validateJson.messages[inputName].required = requiredMsg;
    }
}

function validateForm(form) {
  var valid = form.validate().checkForm();
    if (valid) {
      form.find('.cmp-form-button').removeAttr('disabled');
    } else {
      form.find('.cmp-form-button').attr('disabled', true);
    }
}

/**
 * 
 * @param {*} assetResourcePath asset url to be sent from any component
 * @param {*} dataDamSha damSha of an asset 
 * @returns none
 */
export function executeForm(assetResourcePath) {
  assetResource = assetResourcePath;
  $.validator.addMethod(
    "regex",
    function(value, element, regexp) {
      const re = new RegExp(regexp);
      return this.optional( element ) || re.test(value);
    }
  );

  $.validator.addMethod(
    "customemail",
    function(value, element, regexp) {
        const re = new RegExp(regexp);
        return this.optional( element ) || re.test(value);
    }
  );


  $(".cmp-form").each(function(i, obj) {

    const validateJson = {
      rules:{}, 
      messages:{}, 
      errorPlacement: function (error, element) {
        const elementType = element.attr("type");
        var parent = element.parent();
        if (elementType == "checkbox" || elementType == "radio") {
          parent = element.parent().parent();
          error.appendTo(parent);
        }
        else {
          error.insertAfter(element)
        }
      },
      highlight: function(element) {
        $(element).parent().find("> label").addClass("parent-label-error");
      },
      unhighlight: function(element) {
          $(element).parent().find("> label").removeClass("parent-label-error");
      },
      submitHandler: ()=>{
        const HttpMethod = $(obj).attr('method');
        const HttpUrl = $(obj).attr('action');
        const formData = $(obj).serialize();
        
        $.ajax({
          type: HttpMethod,
          url: HttpUrl,
          data: formData,
          beforeSend: function(){
            $('body').addClass("loading");
            $('body').addClass("overlay");
          },
          success: function (data) {
            $('body').removeClass("loading");
            $('body').removeClass("overlay");
              $(obj).find(".cmp-form-success .popup-container").addClass("active");
              // for asset downloads
              if(assetResource !=null)  {
                const host = utils.getHostOrigin();
                window.location = host + assetResource; // downloads the file in browser
              }
              //reset form after submission
              $(".cmp-form").each(function(){
                this.reset();
              });
          },
          error: function (data) {
            $('body').removeClass("loading");
            $('body').removeClass("overlay");
            console.log("form data error: ", data);
              $(obj).find(".cmp-form-error .popup-container").addClass("active");
              //reset form after submission
              $(".cmp-form").each(function(){
                this.reset();
              });
          },
        });
    }};

    $(".cmp-form-button:submit").parent().css("text-align","center");
    //Text fields
    $(obj).find("input").each(
      function(index){  
          var input = $(this);
          const inputType = input.attr("type");
          const inputName = input.attr("name");
          const requiredAttr = input.attr("required");
          const constraintMsg = input.parent().attr("data-cmp-constraint-message");
          const constraintMsgType = input.parent().attr("data-cmp-constraint-message-type");

          if(inputName != ":formstart" || inputName != "_charset_") {


            if(utils.validateFormRules[inputType]) {
              addRules(validateJson, inputType, inputName, constraintMsgType);
              
              if(constraintMsg != undefined || constraintMsg != null){
                addConstraintMessage(validateJson, inputType, inputName, constraintMsg)
              }

              if (requiredAttr != undefined || requiredAttr != null) {
                var requiredMsg = input.parent().attr("data-cmp-required-message");
                if(inputType == "radio" || inputType == "checkbox") {
                  requiredMsg = input.parent().parent().attr("data-cmp-required-message");
                  requiredInput(requiredAttr,requiredMsg, validateJson, input, inputName);
                } else {
                  requiredInput(requiredAttr,requiredMsg, validateJson, input, inputName);
                }
              }
            } 
          }
    });
    // Textarea fields
    $(obj).find("textarea").each(function() {
      var input = $(this);
      const inputName = input.attr("name");
      const requiredAttr = input.attr("required"); 
      const constraintMsg = input.parent().attr("data-cmp-constraint-message");
      const constraintMsgType = input.parent().attr("data-cmp-constraint-message-type");

      addRules(validateJson, "textarea", inputName, constraintMsgType);

      if(constraintMsg != undefined || constraintMsg != null){
        addConstraintMessage(validateJson, "textarea", inputName, constraintMsg)
      }
     
      if (requiredAttr != undefined || requiredAttr != null) {
        const requiredMsg = input.parent().attr("data-cmp-required-message");
        requiredInput(requiredAttr,requiredMsg, validateJson, input, inputName);
      }
    });


    $(obj).find("select").each(function() {
      var input = $(this);
      const inputName = input.attr("name");
      const requiredAttr = input.attr("required"); 
      const constraintMsg = input.parent().attr("data-cmp-constraint-message");
      const constraintMsgType = input.parent().attr("data-cmp-constraint-message-type");

      addRules(validateJson, inputName, inputName, constraintMsgType);

      if(constraintMsg != undefined || constraintMsg != null){
        addConstraintMessage(validateJson, inputName, inputName, constraintMsg)
      }
      if (requiredAttr != undefined || requiredAttr != null) {
        const requiredMsg = input.parent().attr("data-cmp-required-message");
        requiredInput(requiredAttr,requiredMsg , validateJson, input, inputName);
      }
    });

    $(obj).validate(validateJson);

    $(obj).on('blur keyup change', 'input', function(event) {
      validateForm($(obj));
    });

    $(obj).on('blur keyup change', 'textarea', function(event) {
      validateForm($(obj));
    });

    // submit button validity
    validateForm($(obj));
  });

  return;
}

$(function() {
  executeForm(null);
});