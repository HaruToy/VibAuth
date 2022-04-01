package com.example.vibauth;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String TAG = "ACCEL";
    TextView t_light, t_proximity, t_orientation, t_accelation, ta_orientaiion;
    float[] accelerometerValues;
    List<List<Float>> buf = new ArrayList<>();

    List<Long> timestamp=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        t_light = new TextView(this);
        t_proximity = new TextView(this);
        t_accelation = new TextView(this);
        t_orientation = new TextView(this);
        ta_orientaiion = new TextView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(t_light);
        layout.addView(t_proximity);
        layout.addView(t_accelation);
        layout.addView(t_orientation);
        layout.addView(ta_orientaiion);
        setContentView(R.layout.activity_main);

        // 1. Vibrator 객체 생성
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        Button btnVibrate = findViewById(R.id.button1);

        btnVibrate.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {

                // 2. 진동 구현: 1000ms동안 100 강도의 진동
                vibrator.vibrate(VibrationEffect.createOneShot(1000, 1)); // 1초간 진동
                //Log.v(TAG,"~~~~~~~~VIBRATION~~~~~~~~");
                TextView text=findViewById(R.id.text);
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                SensorEventListener mListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        float[] v = event.values;
                        switch(event.sensor.getType()) {
                            case Sensor.TYPE_ACCELEROMETER:

                                text.setText("x = " + v[0] +'\n' + "y = " + v[1] + '\n' + "z = " + v[2] );
                                Log.v(TAG,event.timestamp+" :  x = " + v[0] +' ' + "y = " + v[1] + ' ' + "z = " + v[2]);
                                /*
                                timestamp.add(event.timestamp);
                                List<Float> acc = new ArrayList<>();
                                acc.add(v[0]);
                                acc.add(v[1]);
                                acc.add(v[2]);
                                buf.add(acc);
                                if(buf.size()>=1000) {
                                    for(int i=0;i<buf.size();i++){
                                        Log.v(TAG,timestamp.get(i)+" :  x = " + buf.get(i).get(0) +' ' + "y = " + buf.get(i).get(1) + ' ' + "z = " + buf.get(i).get(2));
                                    }
                                    buf.clear();
                                }
                                */

                                //Log.v(TAG,event.timestamp+" :  x = " + v[0] +' ' + "y = " + v[1] + ' ' + "z = " + v[2]);
                                break;
                        }
                    }
                    //센서 값이 오차가 있을 수 있다. 잘못된값?
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                };
//가속도센서
                sensorManager.registerListener(mListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        1000);

            }
        });





    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
