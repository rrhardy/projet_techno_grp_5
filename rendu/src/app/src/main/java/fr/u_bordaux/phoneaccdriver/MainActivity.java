package fr.u_bordaux.phoneaccdriver;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import BusDriver.TriPointMessage;
import Drivers.Client;
import android.widget.EditText;
import android.os.StrictMode;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, SensorEventListener{

    private Button connectButton;
    private Client clientBus;
    private SensorManager sensorManager;
    private boolean registred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.connectButton = (Button) findViewById(R.id.Connect);
        this.connectButton.setOnTouchListener(this);
        this.connectButton.setOnClickListener(this);
        this.clientBus = null;
        this.sensorManager = (SensorManager)  getSystemService(SENSOR_SERVICE);
        this.sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        this.registred = false;
        Log.println(Log.DEBUG,"onCreate", "OK");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.Connect){
            if(this.clientBus == null){
                EditText adrInput = (EditText) findViewById(R.id.adrIp);
                Log.println(Log.DEBUG,"onClick","Adr: "+adrInput.getText().toString());
                String adr = adrInput.getText().toString();
                this.clientBus = new Client(adrInput.getText().toString(),"Accelerometer","accDriver");

                if(this.clientBus.register()){
                    //display connexion details
                    Log.println(Log.DEBUG,"onClick","Registrer OK id "+this.clientBus.getId());
                    this.registred = true;
                    this.connectButton.setText("Deconnexion");
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(registred){
                TriPointMessage tpm = new TriPointMessage(event.values[0],event.values[1],event.values[2]);
                this.clientBus.sendMessage(tpm);
                /*try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.println(Log.INFO,"SensorChanged","Sleep interruped");
                }*/
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
