package com.example.james.a1shakedetector;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;
//import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
//import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener, OnClickListener{
    private Button start_btn, stop_btn;
    private boolean running;
    private SensorManager sensorManager;
    private AccelerationData data;
    private TextView accelValues, shakeOutput;
    private EditText threshold;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        data = new AccelerationData(0, 0, 0);

        start_btn = (Button) findViewById(R.id.button_one);
        stop_btn = (Button) findViewById(R.id.button_two);
        accelValues = (TextView) findViewById(R.id.textView2);
        shakeOutput = (TextView) findViewById(R.id.textView4);
        threshold = (EditText) findViewById(R.id.edit_message);
        start_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        start_btn.setEnabled(true);
        stop_btn.setEnabled(false);
        accelValues.setText(data.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        while(running) {
            // Fetch new values
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            // Check to see if there has been a change
            if (x != data.getX() || y != data.getY() || z != data.getZ()) {
                // Update data
                data.setX(x);
                data.setY(y);
                data.setZ(z);

                // Display Accel
                accelValues.setText(data.toString());
            }

            // Update shake status if satisfactory
            if (sqrt(pow(x,2)+pow(y,2)+pow(z,2))
                    >= Integer.parseInt(threshold.getText().toString())) {
                // OUTPUT: SHAKE TextView4
                shakeOutput.setText("SHAKE");
            } else {
                // OUTPUT: NO SHAKE TextView4
                shakeOutput.setText("NO SHAKE");

            }
        }
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    */

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
                Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(this, accelerometer,
                        sensorManager.SENSOR_DELAY_FASTEST);
                break;
            case R.id.button_two:
                start_btn.setEnabled(true);
                stop_btn.setEnabled(false);
                running = true;
                sensorManager.unregisterListener(this);

                break;
            default:
                break;
        }

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
