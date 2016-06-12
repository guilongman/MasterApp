package com.MasterAppClient;

import org.apache.cordova.CordovaActivity;

import com.worklight.androidgap.api.WL;
import com.worklight.androidgap.api.WLInitWebFrameworkListener;
import com.worklight.androidgap.api.WLInitWebFrameworkResult;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MasterAppClient extends CordovaActivity implements WLInitWebFrameworkListener {
	private MqttConnection connection = null;
	private MessageReceiver receiver = null;
	private static Context appCtx = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		WL.createInstance(this);

		WL.getInstance().showSplashScreen(this);

		WL.getInstance().initializeWebFramework(getApplicationContext(), this);
		
		appCtx = getApplicationContext();
		
		Window window = this.getWindow();
		 
		// clear FLAG_TRANSLUCENT_STATUS flag: 
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		 
		// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window 
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		 
		// finally change the color 
		window.setStatusBarColor(this.getResources().getColor(R.color.orangeisthenewblack));
	}

	/**
	 * The IBM MobileFirst Platform calls this method after its initialization is complete and web resources are ready to be used.
	 */
 	public void onInitWebFrameworkComplete(WLInitWebFrameworkResult result){
		if (result.getStatusCode() == WLInitWebFrameworkResult.SUCCESS) {
			super.loadUrl(WL.getInstance().getMainHtmlFilePath());
		} else {
			handleWebFrameworkInitFailure(result);
		}

        IntentFilter filter = new IntentFilter();
        filter.addAction("MASTER_APP_REC_MSG");
         
        receiver = new MessageReceiver();
        registerReceiver(receiver, filter);

		/*publishTopic topic = new publishTopic();
		topic.execute("Zambow Foo Bar");
		
		subscribeTopic subtopic = new subscribeTopic();
		subtopic.execute("blah");
		
*/
	}

	private void handleWebFrameworkInitFailure(WLInitWebFrameworkResult result){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setNegativeButton(R.string.close, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				finish();
			}
		});

		alertDialogBuilder.setTitle(R.string.error);
		alertDialogBuilder.setMessage(result.getMessage());
		alertDialogBuilder.setCancelable(false).create().show();
	}
	
    private class publishTopic extends AsyncTask<String, Void, String> {
        @Override 
        protected String doInBackground(String... message) {
            if (connection == null) {
                connection = new MqttConnection("bluemangroup",
                                                "iot.eclipse.org",
                                                "1883",
                                                "MYCLIENT");
            }
            
            connection.connect();

            connection.publish(message[0]);
            
            return "success";
        }
    }

    private class subscribeTopic extends AsyncTask<String, Void, String> {
        @Override 
        protected String doInBackground(String... message) {
            if (connection == null) {
                connection = new MqttConnection("bluemangroup",
                                                "iot.eclipse.org",
                                                "1883",
                                                "MYCLIENT");
            }

            connection.connect();

            connection.subscribeToTopic();;

            return "success";
        }
    }
    
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
           Bundle bdl = intent.getExtras();
           int command = bdl.getInt("cmd");
           String message = bdl.getString("msg");

           if (command == 0) {
               Intent i = new Intent();
               i.setClass(getApplicationContext(), MasterAppClient.class);
               Notification notification = createNotification(context, 
                                                              "MASTERCITY",
                                                              "Assitente MasterCity",
                                                              message,
                                                              0,
                                                              true,
                                                              0,
                                                              i);
    
               NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    
               notificationManager.notify(1, notification);
           } else {
               Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
           }
        }
     }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    
    private Notification createNotification(Context context, 
                                            String ticker, 
                                            String title, 
                                            String msg, 
                                            int badge, 
                                            boolean bridge, 
                                            int priority, 
                                            Intent intent) {
        

        long when = System.currentTimeMillis();
        Notification notification = null;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                                        .setSmallIcon(R.drawable.micon)
                                        .setTicker(ticker)
                                        .setWhen(when)
                                        .setContentTitle(title)
                                        .setContentText(msg)
                                        .setStyle(new Notification.BigTextStyle().bigText(msg))
                                        .setContentIntent(pendingIntent);

        notification = notificationBuilder.build();
        notification.priority = priority;

        notification.number = badge;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    public static Context getAppContext() {
        return appCtx;
    }
}
