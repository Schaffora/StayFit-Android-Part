package stayfit.Graphics;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.appdatasearch.Feature;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AndroidAppUri;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import stayfit.R;

public class OnGoingActivity extends FragmentActivity implements OnMapReadyCallback,SensorEventListener {

    private GoogleMap mMap;
    private TextView txtVOnGoingKalories;
    private TextView txtVOnGoingSpeedAverage;
    private TextView txtVOnGoingDistance;
    private TextView txtVOnGoingSteps;
    private Button btnOnGoingStop;
    private Chronometer chrnmtOnGoingCrono;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //pedometer
    private SensorManager sensorManager;
    private Sensor sensor; //???
    private int footsteps;
    private TextView txtSteps;

    /* Intent OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going);

        Log.d("INFO","asdfasdf");


         /* Component Initialisation*/
        btnOnGoingStop = (Button) findViewById(R.id.btnOnGoingStop);
        chrnmtOnGoingCrono = (Chronometer) findViewById(R.id.chrnmtOnGoingCrono);
        txtVOnGoingDistance = (TextView) findViewById(R.id.txtVOnGoingDistance);
        txtVOnGoingKalories = (TextView) findViewById(R.id.txtVOnGoingKalories);
        txtVOnGoingSpeedAverage = (TextView) findViewById(R.id.txtVOnGoingSpeedAverage);
        txtVOnGoingSteps = (TextView) findViewById(R.id.txtVOnGoingSteps);

        /* Stop Chronometer */
        btnOnGoingStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chrnmtOnGoingCrono.stop();
                finish();
            }
        });

         /* Chronometer Start*/
        chrnmtOnGoingCrono.start();

        /*Obtain the SupportMapFragment and get notified when the map is ready to be used.*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.d("TEST","TESTTESTTEST");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //pedometer
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        footsteps=0;

        txtSteps= (TextView)findViewById(R.id.txtVOnGoingSteps);
        Log.d("INFO","endCreate");

    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        footsteps++;
        txtSteps.setText(footsteps);

        Log.i("STEP!!",footsteps+" and "+event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-33.866, 151.195);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Positon").snippet("Actual Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-33.866, 151.195))
                .add(new LatLng(-33.864, 151.193))
                .add(new LatLng(-33.866, 151.198))
                .add(new LatLng(-33.867, 151.203))
        );
    }


}
