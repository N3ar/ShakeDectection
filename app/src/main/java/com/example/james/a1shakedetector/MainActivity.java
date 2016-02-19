package com.example.james.a1shakedetector;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class MainActivity extends AppCompatActivity implements SensorEventListener, OnClickListener{
    private Button start_btn, stop_btn, b_start_btn;
    private boolean running, b_running;
    private SensorManager sensorManager, b_sensorManager;
    private AccelerationData data;
    private BarometricData pressure;
    private TextView accelValues, shakeOutput, barValues;
    private EditText threshold;
    private Sensor accelerometer, barometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        b_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        barometer = b_sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        data = new AccelerationData(0, 0, 0);
        pressure = new BarometricData(0);

        start_btn = (Button) findViewById(R.id.button_one);
        stop_btn = (Button) findViewById(R.id.button_two);
        b_start_btn = (Button) findViewById(R.id.button_three);
        accelValues = (TextView) findViewById(R.id.textView2);
        shakeOutput = (TextView) findViewById(R.id.textView4);
        threshold = (EditText) findViewById(R.id.edit_message);
        barValues = (TextView) findViewById(R.id.textView6);
        start_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        start_btn.setEnabled(true);
        stop_btn.setEnabled(false);
        b_start_btn.setOnClickListener(this);
        b_start_btn.setEnabled(true);
        accelValues.setText(data.toString());
        barValues.setText(pressure.toString());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            if (accelerometer.equals(event.sensor)) {
                // Fetch new values
                double ax = event.values[0];
                double ay = event.values[1];
                double az = event.values[2];

                // Check to see if there has been a change
                if (ax != data.getX() || ay != data.getY() || az != data.getZ()) {
                    // Update data
                    data.setX(ax);
                    data.setY(ay);
                    data.setZ(az);

                    // Display Accel
                    accelValues.setText(data.toString());
                }

                // Update shake status if satisfactory
                if (sqrt(pow(ax, 2) + pow(ay, 2) + pow(az, 2))
                        >= Integer.parseInt(threshold.getText().toString())) {
                    // OUTPUT: SHAKE TextView4
                    shakeOutput.setText("SHAKE");
                } else {
                    // OUTPUT: NO SHAKE TextView4
                    shakeOutput.setText("NO SHAKE");
                }
            }
        }

        if (b_running) {
            if (barometer.equals(event.sensor)) {
                //Update
                pressure.setP(event.values[0]);

                // Display Pressure
                barValues.setText(pressure.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_FASTEST);
        b_sensorManager.registerListener(this, barometer, b_sensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        b_sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor event, int i) {
        //Do nothing
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_one:
                threshold = (EditText) findViewById(R.id.edit_message);
                if (Integer.parseInt(threshold.getText().toString()) < 8 ||
                        Integer.parseInt(threshold.getText().toString()) > 25) {
                    threshold.setText("Integers 8 to 25 only.");
                    break;
                }
                start_btn.setEnabled(false);
                stop_btn.setEnabled(true);
                running = true;
                sensorManager.registerListener(this, accelerometer,
                        sensorManager.SENSOR_DELAY_FASTEST);
                break;
            case R.id.button_two:
                start_btn.setEnabled(true);
                stop_btn.setEnabled(false);
                running = false;
                sensorManager.unregisterListener(this);
                break;
            case R.id.button_three:
//                b_start_btn.setEnabled(false);
//                b_stop_btn.setEnabled(true);
                b_running = true;
                b_sensorManager.registerListener(this, barometer, b_sensorManager.SENSOR_DELAY_FASTEST);
                break;
//            case R.id.button_four:
//                b_start_btn.setEnabled(true);
//                b_stop_btn.setEnabled(false);
//                b_running = false;
//                b_sensorManager.unregisterListener(this);
//                break;
            default:
                break;
        }
    }
}
