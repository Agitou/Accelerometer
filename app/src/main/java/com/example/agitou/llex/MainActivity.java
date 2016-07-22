package com.example.agitou.llex;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /*private Switch switch1;
    private TextView txt1, txt2;
    private CheckBox box;
    private ImageView img;
    */

    //sensor declarations
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private MediaPlayer mp;
    private boolean isPlaying = false;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //useless stuffs
        /*txt1 = (TextView) findViewById(R.id.textView4);
        txt2 = (TextView) findViewById(R.id.textView5);
        switch1 = (Switch) findViewById(R.id.switch1);
        box = (CheckBox) findViewById(R.id.checkBox);
        img = (ImageView) findViewById(R.id.imageView2);*/


        //sensors and stuff
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        /*switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(!isChecked){
                    txt1.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }
                else{
                    txt1.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                }
            }
        });

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(!isChecked){
                    txt2.setVisibility(View.INVISIBLE);
                }
                else{
                    txt2.setVisibility(View.VISIBLE);
                }
            }
        });
        */

    }

    //sensor methods
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        mp = MediaPlayer.create(this, R.raw.clap);
        /*btn = (Button)findViewById(R.id.button3);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    mp.pause();
                }
            }
        });
        */

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float avg = (x + y + x) / 3;

            long curTime = System.currentTimeMillis();

            //if ((curTime - lastUpdate) > 10)
            if (avg > 1) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    mp.start();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
