(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-formcontainer__editor",
		mail_mailToBusiness: "[name='./mail_mailToBusiness']",
        mail_businessEmailsGroup: "[data-cmp-formcontainer-dialog-hook='mail_businessEmailsGroup']",
        rpc_mail_mailToBusiness: "[name='./rpc_mail_mailToBusiness']",
        rpc_mail_businessEmailsGroup: "[data-cmp-formcontainer-dialog-hook='rpc_mail_businessEmailsGroup']"
    };

    var mail_mailToBusiness;
    var mail_businessEmailsGroup;
	var rpc_mail_mailToBusiness;
    var rpc_mail_businessEmailsGroup;

    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
				mail_mailToBusiness = $(selectors.mail_mailToBusiness);
                mail_businessEmailsGroup = dialogContent.querySelector(selectors.mail_businessEmailsGroup);
                rpc_mail_mailToBusiness = $(selectors.rpc_mail_mailToBusiness);
                rpc_mail_businessEmailsGroup = dialogContent.querySelector(selectors.rpc_mail_businessEmailsGroup);
				
				if(mail_mailToBusiness) {
                    mail_mailToBusiness.change(function() {
                        onMailMailToBusinessChange(this.value);
                    });
                    onMailMailToBusinessChange(mail_mailToBusiness.val());
                }
				
				if(rpc_mail_mailToBusiness) {
                    rpc_mail_mailToBusiness.change(function() {
                        onRPCMailMailToBusinessChange(this.value);
                    });
                    onRPCMailMailToBusinessChange(rpc_mail_mailToBusiness.val());
                }
            }
        }
    });
	
	function onMailMailToBusinessChange(value) {
        if (mail_mailToBusiness && mail_businessEmailsGroup) {
            if (!value) {
                mail_businessEmailsGroup.setAttribute("hidden", true);
            } else {
                mail_businessEmailsGroup.removeAttribute("hidden");
            }
        }
    }

    function onRPCMailMailToBusinessChange(value) {
        if (rpc_mail_mailToBusiness && rpc_mail_businessEmailsGroup) {
            if (!value) {
                rpc_mail_businessEmailsGroup.setAttribute("hidden", true);
            } else {
                rpc_mail_businessEmailsGroup.removeAttribute("hidden");
            }
        }
    }

})(jQuery);
