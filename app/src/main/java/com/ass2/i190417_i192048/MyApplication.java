package com.ass2.i190417_i192048;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.ass2.i190417_i192048.ChatApp.ChatMainScreen;
import com.ass2.i190417_i192048.Models.Music;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyApplication extends Application {
    private static final String ONESIGNAL_APP_ID = "96f1721c-2f55-4ea1-9e50-8f3f29fc47e5";

    @Override
    public void onCreate() {
        super.onCreate();


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.setNotificationOpenedHandler(new MyNotificationOpenedHandler(this));
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);



        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
    }

    public class MyNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {

        private Application application;

        public MyNotificationOpenedHandler(Application application) {
            this.application = application;
        }

        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {

            // Get custom datas from notification
            JSONObject data = result.getNotification().getAdditionalData();
            if (data != null) {
                String myCustomData = data.optString("key", null);
            }

            // React to button pressed
            OSNotificationAction.ActionType actionType = result.getAction().getType();
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.getAction().getActionId());

            // Launch new activity using Application object
            startApp();
        }

        private void startApp() {
            Intent intent = new Intent(application, ChatMainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }

    }


}
