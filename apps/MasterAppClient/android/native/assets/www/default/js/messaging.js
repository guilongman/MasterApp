
/* JavaScript content from js/messaging.js in folder common */

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

function newMessage(msg) {
    console.log(msg);
}

function subscribeToTopic() {
    cordova.exec(newMessage, Transmission_PacketSendFail, "MqttConnection", "subscribe", []);
}

function getMessage() {
    cordova.exec(newMessage, Transmission_PacketSendFail, "GetMessage", "blah", []);
}