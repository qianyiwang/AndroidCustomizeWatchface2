package com.example.qianyiwang.androidcustomizewatchface2;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by qianyiwang on 3/21/17.
 */

public class CommandListener extends WearableListenerService {

    public static String START_ACTIVITY_PATH = "/from-phone";

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "WatchListener Bind", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "WatchListener Unbind", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(START_ACTIVITY_PATH)){
            String msg_watch = new String(messageEvent.getData());
            Log.e("MainApp","CMD from a phone:"+msg_watch);
            if(msg_watch.equals("start")){
//                startService(new Intent(getBaseContext(), HeartRateService.class));
            }
            else if(msg_watch.equals("stop")){
//                stopService(new Intent(getBaseContext(), HeartRateService.class));
            }
        }
    }
}
