package com.example.agitou.llex;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public TextView x, y, z, xdata, ydata, zdata, sped;
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private MediaPlayer mp, mp2;
    private SoundPool sp;
    private Button btn1;
    boolean isPlaying = false, flag = false;
    private int SPEED_THRESHOLD = 300, constSpeed = 100, soundId;
    private double bpm, bpmmin = 0, bpmmax = 10;


    private float s1 = 0, s2 = 0, s3 = 0, s4 = 0, s5 = 0, s6 = 0;
    private float c1 = 0.05237f, c2 = 0.06725f, c3 = 0.06725f, c4 = 0.05237f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = sp.load(this, R.raw.clap, 1);


        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);

        xdata = (TextView) findViewById(R.id.xdata);
        ydata = (TextView) findViewById(R.id.ydata);
        zdata = (TextView) findViewById(R.id.zdata);
        sped = (TextView) findViewById(R.id.sped);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mp.seekTo(50000);
                /*if(isPlaying){
                    mp2.pause();
                    mp2.setLooping(false);
                }
                else {
                    mp2.start();
                    mp2.setLooping(true);
                }
                isPlaying = !isPlaying;
                */
            }
        });


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent sensor){
        Sensor mySensor = sensor.sensor;


        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float xn = sensor.values[0];
            float yn = sensor.values[1];
            float zn = sensor.values[2];
            long curTime = System.currentTimeMillis();

            //if ((curTime - lastUpdate) > 10)
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                mp = MediaPlayer.create(this, R.raw.clap);

                xdata.setText(String.format("%d", Math.round(xn)));
                ydata.setText(String.format("%d", Math.round(yn)));
                zdata.setText(String.format("%d", Math.round(zn)));

                float s1 = Math.abs(xn + yn + zn - last_x - last_y - last_z)/ diffTime * 10000;

                float speed = s1 * c1 + s2 * c2 + s3 * c3 + s4 * c4;

                sped.setText(String.format("%d", Math.round(speed)));
                Log.i("Speed:", String.format("%.2f", speed));
                //Log.i("Time:", )

                if (s1 > SPEED_THRESHOLD) {
                    sp.play(soundId, 1, 1, 1, 0, 1);
                }


                last_x = xn;
                last_y = yn;
                last_z = zn;
                s4 = s3;
                s3 = s2;
                s2 = s1;
            }
            /*
            xdata.setText(String.format("%.2f", xn));
            ydata.setText(String.format("%.2f", yn));
            zdata.setText(String.format("%.2f", zn));
            */

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
