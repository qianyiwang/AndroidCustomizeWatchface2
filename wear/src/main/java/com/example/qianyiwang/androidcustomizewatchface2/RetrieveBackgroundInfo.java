package com.example.qianyiwang.androidcustomizewatchface2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qianyiwang on 3/22/17.
 */

public class RetrieveBackgroundInfo extends Service{

    final String TAG = "RetrieveBackgroundInfo";
    IntentFilter ifilter;
    Intent batteryStatus;
    double batteryPct_last;
    Sensor mHeartRateSensor;
    SensorManager mSensorManager;
    private Handler mHandlerTime = new Handler();
    int battery_time_count, sensor_time_count;
    boolean sensor_flag;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Background Start", Toast.LENGTH_SHORT).show();
        mHandlerTime.postDelayed(timerRun, 0);
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensor_flag = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerTime.removeCallbacks(timerRun);
        mSensorManager.unregisterListener(listener);
        Toast.makeText(this, "Background Stop", Toast.LENGTH_SHORT).show();

    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_HEART_RATE) {
                GlobalValues.hr_val = (int) sensorEvent.values[0];

            } else
                Log.d("Sensor:", "Unknown sensor type");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private final Runnable timerRun = new Runnable()
    {
        @Override
        public void run() {

            if(battery_time_count == 0){
                updateBattery();
                battery_time_count = 0;
            }
            Log.e(TAG,"hr_sensor_functional"+GlobalValues.hr_sensor_functional);
            if(sensor_time_count == 1 && GlobalValues.hr_sensor_functional){
                updateHeartRateInfo();
                sensor_time_count = 0;
                Log.e("runable", "sensor update");
            }

            mHandlerTime.postDelayed(this, 60000);

//            battery_time_count++;
            sensor_time_count++;
        }
    };

    private void updateBattery(){
        Log.e(TAG, "battery check");
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = getApplication().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        GlobalValues.batteryPct = Math.floor(level / (float)scale * 100);
        if(GlobalValues.batteryPct!=batteryPct_last){
            GlobalValues.batteryLevelChanged = true;
        }
        batteryPct_last = GlobalValues.batteryPct;
    }

    private void updateHeartRateInfo(){
        Log.e(TAG, "sensor check");
        if(sensor_flag){
            mSensorManager.registerListener(listener, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else
            mSensorManager.unregisterListener(listener);

        sensor_flag = !sensor_flag;
    }
}
