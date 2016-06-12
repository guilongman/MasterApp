package com.MasterAppClient;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


// This inner class is a wrapper on top of MQTT client.
public class MqttConnection implements MqttCallback {
    private static final String TAG = "MastercardCity";
    private static final int KEEPALIVE_INTERVAL = 300;

    private MqttClient mClient;
    private MqttTopic mTopic;
    private String mClientId;
    private String mHost;
    private String mPort;
    private String mConnSpec;
    private String mTopicName;
    private boolean mConnected;
    private String mArrivedMessage;
    private Context mAppContext;

    // Creates a new connection given the broker address and initial topic
    public MqttConnection(String initTopic, String host, String port, String clientID, Context context) {
        Log.d(TAG, "Mastercard City - topic [" + initTopic + "] / host [" + host + ":" + port + "] / clientID [" + clientID + "]");

        mAppContext = context;
        
        // fetch the device ID from the preferences.
        mClientId = clientID;
        mHost = host;
        mPort = port;

        // Create connection spec
        mConnSpec = "tcp://" + mHost + ":" + mPort;
        mTopicName = initTopic;

        // Connection state!
        mConnected = false;

        mArrivedMessage = "";

        // Initialize our Mqtt Client.
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mClient = new MqttClient(mConnSpec, mClientId, persistence);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return (mClient != null ? mClient.isConnected() : false);
    }

    public boolean publish(String message) {
        Log.d(TAG, "Publishing topic! " + message);
        boolean returnValue = mConnected;

        if (mConnected) {
            try {
                if (mTopic != null) {
                    mTopic.publish(message.getBytes(), 0, false);
                } else {
                    mConnected = false;
                }
            } catch (MqttException me) {
                me.printStackTrace();
                if (me.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED ||
                    me.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING ||
                    me.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST) {
                    // Sometimes connectionLost method is not called properly.
                    // Avoid any further operation if connectivity was lost!
                    mConnected = false;
                }
            }
        }
        return returnValue;
    }

    public void disconnect() {
        if (mConnected) {
            try {
                mClient.disconnect();
            } catch (MqttException mqtte) {
                mqtte.printStackTrace();
            }
        }
    }

    public void subscribeToTopic() {
        try {
            mClient.subscribe(mTopicName, 0);
            mClient.setCallback(this);
        } catch (MqttException mqtte) {
            mqtte.printStackTrace();
        }
    }
    public boolean connect() {
        boolean returnValue = false;
        Log.d(TAG, "Trying to establish a socket connection...");
        try {
            // Try to connect
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(0);
            options.setKeepAliveInterval(KEEPALIVE_INTERVAL);
            options.setCleanSession(false);

            if (mClient != null) {
                mClient.connect(options);
                mTopic = mClient.getTopic(mTopicName);

                returnValue = true;
                mConnected = true;

                Log.d(TAG, "Connection established to " + mHost);
            } else {
                mConnected = false;
            }
        } catch (MqttException e) {
            e.printStackTrace();
            // Return success if we're already connected
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_CONNECTED) {
                returnValue = true;
            } else {
                e.printStackTrace();
                Log.d(TAG, "Failed to connect!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to connect!");
        }
        return returnValue;
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "Lost connection to " + mHost + "!");
        mConnected = false;
    }

    @Override
    public void deliveryComplete(MqttDeliveryToken token) {
        Log.d(TAG, "Message delivered!");
    }

    @Override
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
        Log.d(TAG, "Got message: " + message.toString());
        mArrivedMessage = message.toString();

        String[] result = mArrivedMessage.split("@");

        Intent intent = new Intent();
        intent.setAction("MASTER_APP_REC_MSG");

        if (result != null && result.length == 2) {
            int value = Integer.valueOf(result[0]);

            intent.putExtra("cmd", value);
            intent.putExtra("msg", result[1]);
            mAppContext.sendBroadcast(intent);

        } else {
            Log.d(TAG, "Invalid message!");
        }
    }

    public boolean messageArrived() {
        return mArrivedMessage.length() > 0;
    }

    public String getArrivedMessage() {
        return mArrivedMessage;
    }
}