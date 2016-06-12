package com.MasterAppClient;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

public class GetMessage extends CordovaPlugin {
    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String message = MqttConnection.getConn().getArrivedMessage();
        String[] result = message.split("@");

        if (result != null && result.length == 2) {
            int value = Integer.valueOf(result[0]);

            if (value == 1) {
                callbackContext.success(result[1]);
                MqttConnection.getConn().clearMessage();
            }
        }

        return true;
    }
   
    public GetMessage() {}
}