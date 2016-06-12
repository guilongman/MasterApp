
function Transmission_PacketSendFail() {
    console.log("Failure on calling Cordova Plugin!");
}

function Transmission_PacketSendSuccess() {
    console.log("Success on calling Cordova Plugin!");
}

function messagingInit(){
    cordova.exec(Transmission_PacketSendSuccess, Transmission_PacketSendFail, "MqttConnection", "start", []);
}

function sendMessage(message) {
    cordova.exec(Transmission_PacketSendSuccess, Transmission_PacketSendFail, "MqttConnection", "publish", [message]);
}
msgCount = 0;
function newMessage(msg) {
	msgCount = msgCount++;
	messenger.recieve("<p>Ahh, legal, já que você entrou na loja, quer que eu te ajude com as compras?</p><button id='btyes' class='btn waves-effect waves-light' id='modalcart' type='submit' name='action' style='float:left'>Sim</button><button class='btn waves-effect waves-light' id='modalcart' style='float:right' type='submit'  name='action'>Não</button>");
	$("#btyes").click(function(){
		$("#pmodalcart").find(".modal-content").load("pages/cart.html");
		$('#pmodalcart').openModal();
	})
}

function subscribeToTopic() {
    cordova.exec(newMessage, Transmission_PacketSendFail, "MqttConnection", "subscribe", []);
}

function getMessage() {
    cordova.exec(newMessage, Transmission_PacketSendFail, "GetMessage", "blah", []);
}