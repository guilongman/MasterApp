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
	$("#pfinally").click(function(){
		messenger.recieve("<p>E aí, posso fechar o pedido pelo masterpass?</p><button id='btyesfin' class='btn waves-effect waves-light' id='modalcart' type='submit' name='action' style='float:left'>Sim</button><button class='btn waves-effect waves-light' id='modalcart' style='float:right' type='submit'  name='action'>Não</button>");
		$("#btyesfin").click(function(){
			messenger.recieve("Beleza! Agora é só apresentar a imagem abaixo na saída:<br></br><img src='images/qrcode.png' alt=''>");

		});
	});
	$("#login-geral").show();
	messagingInit();
	setTimeout(function() {
	    sendMessage("Blue man group IBM Devs!");

	    setTimeout(function() {
	        subscribeToTopic();
	    }, 5000);

	}, 5000);

}
var messenger;
