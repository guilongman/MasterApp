function wlCommonInit(){
	/*
	 * Use of WL.Client.connect() API before any connectivity to a MobileFirst Server is required. 
	 * This API should be called only once, before any other WL.Client methods that communicate with the MobileFirst Server.
	 * Don't forget to specify and implement onSuccess and onFailure callback functions for WL.Client.connect(), e.g:
	 *    
	 *    WL.Client.connect({
	 *    		onSuccess: onConnectSuccess,
	 *    		onFailure: onConnectFailure
	 *    });
	 *     
	 */
	
	// Common initialization code goes here
	$("#mainmenu").load("pages/menu.html");
	$("#main").load("pages/chat.html");
<<<<<<< HEAD
	
	messagingInit();

	setTimeout(function() {
	    sendMessage("Blue man group IBM Devs!");

	    setTimeout(function() {
	        subscribeToTopic();
	    }, 5000);

	}, 5000);

	setInterval(function() {
	    getMessage();
	}, 3000);
=======

>>>>>>> 4a6b9e4f6481f58ee24716c9476dc08a754f99b5
}
