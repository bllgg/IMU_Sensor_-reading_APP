package com.example.imu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String tag = "IMU";
    String pattern = "MM_dd_yyyy_HH:mm:ss";
    DateFormat df;
    Date today;
    String file_name;
    String file_path;
    File myExternalFile;
    String myData = ""; //////////////////////

    private SensorManager sensorManager;
    private Sensor sensor;
    private List list_acc, list_gyro, list_mag;
    TextView text, scnd_txt, third_txt, forth_text;
    private float[] mag_values = new float[3];
    private float[] gyr_values = new float[3];

    double x_acc_value, y_acc_value, z_acc_value;

    SensorEventListener sel_gyro = new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event) {
            gyr_values = event.values;
            scnd_txt.setText("GYROSCOPE\nx axis: "+gyr_values[0]+"\ny axis: "+gyr_values[1]+"\nz axis: "+gyr_values[2]);
        }
    };

    SensorEventListener sel_mag = new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event) {
            mag_values = event.values;
            text.setText("MAGNETOMETER\nx axis: "+mag_values[0]+"\ny axis: "+mag_values[1]+"\nz axis: "+mag_values[2]);
        }
    };

    SensorEventListener sel_acc = new SensorEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;

//            double roll, pitch, yaw;
//            int d_roll, d_pitch, d_yaw;
//            double magLength;
//            double[] normMagVals = new double[3];
            x_acc_value = 1.008 * values[0] - 0.1803;
            y_acc_value = 1.011 * values[1] + 0.492;
            z_acc_value = 0.999 * values[2] - 0.3397;
            third_txt.setText("ACCELERATION\nx axis: "+x_acc_value+"\ny axis: "+y_acc_value+"\nz axis: "+z_acc_value);

//            roll = Math.atan2(y_value, z_value + 0.05*x_value);
//            pitch = Math.atan2(x_value, Math.sqrt(y_value * y_value + z_value * z_value));
//
//            magLength = Math.sqrt(mag_values[0]*mag_values[0] + mag_values[1]*mag_values[1] + mag_values[2]*mag_values[2]);
//            normMagVals[0] = mag_values[0]/magLength;
//            normMagVals[1] = mag_values[1]/magLength;
//            normMagVals[2] = mag_values[2]/magLength;
//
//            yaw = Math.atan2(Math.sin(roll)*normMagVals[2] - Math.cos(roll)*normMagVals[1], Math.cos(pitch)*normMagVals[0] + Math.sin(roll)*Math.sin(pitch)*normMagVals[1] + Math.cos(roll)*Math.sin(pitch)*normMagVals[2]);
//
//            d_pitch = (int) Math.round( pitch * 180 / Math.PI );
//            d_roll = (int) Math.round( roll * 180 / Math.PI );
//            d_yaw = (int) Math.round(yaw * 180 / Math.PI);
//
//            forth_text.setText("ORIENTATION\nRoll: "+d_roll+"\nPitch: "+d_pitch+"\nYaw: "+d_yaw);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date according to the chosen pattern
        df = new SimpleDateFormat(pattern);

        // Get the today date using Calendar object.
        today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String todayAsString = df.format(today);
        file_name = "IMU_Data_"+todayAsString+".csv";
//        file_name = "IMU_Data_.csv";

        Log.i(tag, "Today is: " + file_name);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        text = (TextView) findViewById(R.id.main_txt);
        scnd_txt = (TextView) findViewById(R.id.scnd_text);
        third_txt = (TextView) findViewById(R.id.third_text);
        forth_text = (TextView) findViewById(R.id.forth_text);

        list_mag = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        list_acc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        list_gyro = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);

        if(list_acc.size()>0){
            sensorManager.registerListener(sel_acc, (Sensor) list_acc.get(0), SensorManager.SENSOR_DELAY_FASTEST); // in here we register the sensors to the variables.
        }else{
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }

        if(list_mag.size()>0){
            sensorManager.registerListener(sel_mag, (Sensor) list_mag.get(0), SensorManager.SENSOR_DELAY_FASTEST); // in here we register the sensors to the variables.
        }else{
            Toast.makeText(getBaseContext(), "Error: No Magnetometer.", Toast.LENGTH_LONG).show();
        }

        if(list_gyro.size()>0){
            sensorManager.registerListener(sel_gyro, (Sensor) list_gyro.get(0), SensorManager.SENSOR_DELAY_FASTEST);  // here also we register the sensors to the variables.
        }else{
            Toast.makeText(getBaseContext(), "Error: No Gyroscope.", Toast.LENGTH_LONG).show();
        }

        Thread dlthread = new Thread(){
            @Override
            public void run() {
                while (true){
                    myData = x_acc_value+","+y_acc_value+","+z_acc_value+","+gyr_values[0]+","+gyr_values[1]+","+gyr_values[2]+","+mag_values[0]+","+mag_values[1]+","+mag_values[2]+"\n";
                    Log.i(tag, myData);

                    writeToFile(myData);
                    try {
                        synchronized (this) {
                            this.wait(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        dlthread.start();
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

    private void writeToFile(String message)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file_name,Context.MODE_APPEND));
            outputStreamWriter.write(message);
            outputStreamWriter.close();

        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
