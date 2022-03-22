package com.example.vibauth;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class MainActivity extends Activity {

    TextView t_light, t_proximity, t_orientation, t_accelation, ta_orientaiion;
    float[] accelerometerValues;
    float[] magneticFieldValues;
    int counter = 0;
    private Vibrator vibrator;
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
        //setContentView(layout);
        setContentView(R.layout.activity_main);

        // 1. Vibrator 객체 생성
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        Button btnVibrate = findViewById(R.id.button1);

        btnVibrate.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                // 2. 진동 구현: 1000ms동안 100 강도의 진동
                vibrator.vibrate(VibrationEffect.createOneShot(1000, 1)); // 1초간 진동
                TextView text=findViewById(R.id.text);
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                /*
                Workbook workbook =new HSSFWorkbook();
                Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

                Row row = sheet.createRow(0); // 새로운 행 생성
                Cell cell;
                cell = row.createCell(0);
                cell.setCellValue("time");
                cell = row.createCell(1);
                cell.setCellValue("x");
                cell = row.createCell(2);
                cell.setCellValue("y");
                cell = row.createCell(3);
                cell.setCellValue("z");
                */

                SensorEventListener mListener = new SensorEventListener() {
                    //센서 값이 변하게 되면 이게 호출된다.
                    //event.timestamp
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        float[] v = event.values;
                        if(counter++ % 30 != 0) //향상된 방향센서 센서 갱신 주기를 조절
                            return;
                        switch(event.sensor.getType()) {
                            case Sensor.TYPE_ACCELEROMETER:
                                text.setText("x = " + v[0] +'\n' + "y = " + v[1] + '\n' + "z = " + v[2] );
                                //row = sheet.createRow(i+1);
                                //cell = row.createCell(0);
                                accelerometerValues = v; //향상된 방향 사용하기 위한 코드
                                break;
                                /*
                                 case Sensor.TYPE_LIGHT:
                                t_light.setText("조도 = " + v[0]); //조도센서는 값이 하나밖에 없다.
                                break;
                            case Sensor.TYPE_PROXIMITY:
                                t_proximity.setText("근접 = " + v[0]);
                                break;
                            case Sensor.TYPE_ORIENTATION:
                                t_orientation.setText("headingAngle = " + v[0] +'\n' + "pitchAngle = " + v[1] + '\n' + "rollAngle = " + v[2] );
//x 축 : 북 0도, 동쪽 90도,
                                break;

                            case Sensor.TYPE_MAGNETIC_FIELD:
                                magneticFieldValues = v; //향상된 방향 사용하기 위한 코드
                                break;*/
                        }
                        /*
//향상된 방향값을 받아오기 위해서는 가속도값과 마그네틱값 모두 필요하기 때문에 둘다 수신했을 경우에만 계산한다.
                        if(accelerometerValues != null && magneticFieldValues != null){
                            float[] values = new float[3];
                            float[] mr = new float[9];

                            SensorManager.getRotationMatrix(mr, null, accelerometerValues, magneticFieldValues);
                            SensorManager.getOrientation(mr, values);
                            values[0] = (float) Math.toDegrees(values[0]);
                            values[1] = (float) Math.toDegrees(values[1]);
                            values[2] = (float) Math.toDegrees(values[2]);
                            ta_orientaiion.setText("headingAngle : " + values[0] +'\n' + "pitchAngle : " + values[1] + '\n' + "rollAngle: " + values[2]);
                        }
*/
                    }

                    //센서 값이 오차가 있을 수 있다. 잘못된값?
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                };
                //조도 센서
                sensorManager.registerListener(mListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), //이렇게 받아와야함
                        SensorManager.SENSOR_DELAY_UI);
//SensorManager.SENSOR_DELAY_GAME; 이건 그냥 게임전용 센서

//근접 센서
                sensorManager.registerListener(mListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), //이렇게 받아와야함
                        SensorManager.SENSOR_DELAY_UI); //갱신 주기가 가장 빠름

//가속도센서
                sensorManager.registerListener(mListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_UI);

//방향센서
                sensorManager.registerListener(mListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                        SensorManager.SENSOR_DELAY_UI);

//자기장센서 모니터링
                sensorManager.registerListener(mListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                        sensorManager.SENSOR_DELAY_UI);
            }
        });
       // vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
       // vibrator.vibrate(1000);
        // 1초간 진동




    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
