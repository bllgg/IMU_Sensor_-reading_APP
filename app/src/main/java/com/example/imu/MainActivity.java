package com.example.imu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private List list_acc, list_gyro, list_mag;
    TextView text, scnd_txt, third_txt;

    SensorEventListener sel_gyro = new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            scnd_txt.setText("GYROSCOPE\nx axis: "+values[0]+"\ny axis: "+values[1]+"\nz axis: "+values[2]);
        }
    };

    SensorEventListener sel_mag = new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            text.setText("MAGNETOMETER\nx axis: "+values[0]+"\ny axis: "+values[1]+"\nz axis: "+values[2]);
        }
    };

    SensorEventListener sel_acc = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            third_txt.setText("ACCELERATION\nx axis: "+values[0]+"\ny axis: "+values[1]+"\nz axis: "+values[2]);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        text = (TextView) findViewById(R.id.main_txt);
        scnd_txt = (TextView) findViewById(R.id.scnd_text);
        third_txt = (TextView) findViewById(R.id.third_text);

        list_mag = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        list_acc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        list_gyro = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);

        if(list_acc.size()>0){
            sensorManager.registerListener(sel_acc, (Sensor) list_acc.get(0), SensorManager.SENSOR_DELAY_NORMAL); // in here we register the sensors to the variables.
        }else{
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }

        if(list_mag.size()>0){
            sensorManager.registerListener(sel_mag, (Sensor) list_mag.get(0), SensorManager.SENSOR_DELAY_UI); // in here we register the sensors to the variables.
        }else{
            Toast.makeText(getBaseContext(), "Error: No Magnetometer.", Toast.LENGTH_LONG).show();
        }

        if(list_gyro.size()>0){
            sensorManager.registerListener(sel_gyro, (Sensor) list_gyro.get(0), SensorManager.SENSOR_DELAY_UI);  // here also we register the sensors to the variables.
        }else{
            Toast.makeText(getBaseContext(), "Error: No Gyroscope.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        if (list_gyro.size()>0){
            sensorManager.unregisterListener(sel_gyro);
        }

        if (list_mag.size()>0){
            sensorManager.unregisterListener(sel_mag);
        }

        if (list_acc.size() > 0){
            sensorManager.unregisterListener((sel_acc));
        }
        super.onStop();
    }
}
