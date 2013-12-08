$(function() {
	addedPartDone = false;

	$('.new-part-form').on('submit', function(e){
	    if (addedPartDone === true) {
	        addedPartDone = false // reset flag
	        return // let the event bubble up
	    }

	    e.preventDefault()

	    var formData = $(this).serializeArray()
	    var fMap = {}
		var emailReg = "/^([w-.]+@([w-]+.)+[w-]{2,4})?$/";

		$(formData).each(function() {
			fMap[$(this).attr('name')] = $(this).attr('value')
		})

		var hasError = true;

		$.each(fMap, function( key, value ) {
	  		if(value == '') {
		    	hasError = true
	    	} else if(key == email && !emailReg.test(value)) {
	    		$('#email_field .help-block').html("Enter a valid email address to send too.")
	   			hasError = true
	   		}
	    })

	    if(hasError) {
	    	$('.part-form-title .form-error').html("Please correct errors")
	    } else {
	    	addedPartDone = true //set flag
	    	$(this).trigger('submit')
	    }

	    if(hasError == false) {
 			$(".add-part").hide();
 			$(".add-part").append('<img src="/images/template/loading.gif" alt="Loading" id="loading" />');
		} 

	    $.post("~/php/sendEmail.php", {
	    		emailTo: "mk7upurz87@gmail.com",
	    		emailFrom: "MedTechAM@gmail.com",
	    		subject: "Part added",
	    		message: "A new part was added"
	    	},
	    	function(data) {
	    		$(".add-part").slideUp("normal", function() {
	    			$(".add-part").before('<h2>Success</h2><p> an email notification was sent.</p>');
	    		});
	    	}
	    ); 
 	})
})