package stayfit.Graphics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import stayfit.DataBase.DataSample;
import stayfit.DataBase.User;
import stayfit.R;

public class OnGoingActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener {


    /* Components*/
    private TextView txtVOnGoingKalories;
    private TextView txtVOnGoingSpeedAverage;
    private TextView txtVOnGoingDistance;
    private TextView txtVOnGoingSteps;
    private Button btnOnGoingStop;
    private Chronometer chrnmtOnGoingCrono;


    /* Map Tools*/
    private List<Double> mapLatsLongsList;
    private GoogleMap mapMap;
    private int NB_OF_MARKERS=0;
    private int COVERED_DISTANCE=0;
    private double AVERAGE_SPEED=0;

    /* Location Tools */
    private int REQUEST_ACCESS = 1;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;


    /* Sensor */
    private SensorManager sensorManager;
    private Sensor sensor;
    private int FOOT_STEPS;
    private GoogleApiClient mGoogleApiClient;


    /* Intent OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going);

        List<User> users = null;
        List<DataSample> dataSamples = null ;
        String actualUser ="";
        String choosenActivity="";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            users = (List<User>)extras.getSerializable("users");
            dataSamples = (List<DataSample>)extras.getSerializable("dataSamples");
            actualUser= intent.getStringExtra("actualUser");
            choosenActivity=intent.getStringExtra("ChoosenActivity");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final List<User> finalUsers = users;
        final List<DataSample> finalDataSamples = dataSamples;
        final String finalActualUser = actualUser;
        final String finalChoosenActivity=choosenActivity;

        /* List of Latitudes and Longitudes Initialisation */
        mapLatsLongsList = new ArrayList<Double>();

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

        /* Using to create the map */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        /* Podometer Initialisation */
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        FOOT_STEPS = 0;
        txtVOnGoingSteps.setText("Pas : "+FOOT_STEPS);


        /* Create an instance of GoogleAPIClient.*/
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(5000);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        FOOT_STEPS++;
        txtVOnGoingSteps.setText("Pas : " + FOOT_STEPS);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS);
            return;
        }

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mapLatsLongsList.add(location.getLatitude());
        mapLatsLongsList.add(location.getLongitude());
        NB_OF_MARKERS++;



        if(mapLatsLongsList.size()>2)
        {
            int size=mapLatsLongsList.size();
            Polyline line = mapMap.addPolyline(new PolylineOptions()
                    .add(new LatLng( mapLatsLongsList.get(size-4), mapLatsLongsList.get(size-3)), new LatLng( mapLatsLongsList.get(size-2),  mapLatsLongsList.get(size-1)))
                    .width(5)
                    .color(Color.BLUE));
            int distance= getDistanceGPSPoint(mapLatsLongsList.get(size-4),mapLatsLongsList.get(size-3),mapLatsLongsList.get(size-2),mapLatsLongsList.get(size-1));
            COVERED_DISTANCE+=distance;
            txtVOnGoingDistance.setText("Distance " + COVERED_DISTANCE);
            long elapsedSeconds = (SystemClock.elapsedRealtime() - chrnmtOnGoingCrono.getBase())/1000;
            AVERAGE_SPEED=((Number)elapsedSeconds).intValue()/COVERED_DISTANCE;
            txtVOnGoingSpeedAverage.setText("Vitesse moyenne: "+AVERAGE_SPEED);
        }

        mapMap.addMarker(new MarkerOptions().position(latlng).title("Marker "+ Integer.toString(NB_OF_MARKERS)).snippet("Actual Position"));
        mapMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mapMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
    }

    protected void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public int getDistanceGPSPoint(double lat1, double lon1, double lat2, double lon2)
    {
        double rlat1 = Math.PI * lat1/180;
        double rlat2 = Math.PI * lat2/180;
        double rlon1 = Math.PI * lon1/180;
        double rlon2 = Math.PI * lon2/180;
        double theta = lon1-lon2;
        double rtheta = Math.PI * theta/180;
        double dist = Math.sin(rlat1) * Math.sin(rlat2) + Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rtheta);
        dist = Math.acos(dist);
        dist = dist * 180/Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000;
        return (int)dist;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapMap.setMyLocationEnabled(true);
            Log.i("PERMISSION", "permission granted");
        } else {
             /* Show rationale and request permission.*/
            Log.i("PERMISSION", "permission denied");
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed", connectionResult.getErrorMessage());
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Connection suspended", String.valueOf(i));
    }
}
