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
    private List list, list_acc;
    TextView text, scnd_txt;

    SensorEventListener sel = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            text.setText("x axis: "+values[0]+"\ny axis: "+values[1]+"\nz axis: "+values[2]);
        }
    };

    SensorEventListener sel_acc = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            scnd_txt.setText("x: "+values[0]+"\ny: "+values[1]+"\nz: "+values[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        text = (TextView) findViewById(R.id.main_txt);
        scnd_txt = (TextView) findViewById(R.id.scnd_text);

        list = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        list_acc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if(list_acc.size()>0){
            sensorManager.registerListener(sel_acc, (Sensor) list_acc.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }

        if(list.size()>0){
            sensorManager.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        if (list.size()>0){
            sensorManager.unregisterListener(sel);
        }

        if (list_acc.size() > 0){
            sensorManager.unregisterListener((sel_acc));
        }
        super.onStop();
    }
}
