package stayfit.Graphics;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
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

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import stayfit.R;

public class OnGoingActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener{

    /* Component declaration */
    private GoogleMap mMap;
    private TextView txtVOnGoingKalories;
    private TextView txtVOnGoingSpeedAverage;
    private TextView txtVOnGoingDistance;
    private TextView txtVOnGoingSteps;
    private Button btnOnGoingStop;
    private Chronometer chrnmtOnGoingCrono;
    private GoogleApiClient client;
    private TextView txtSteps;

    /* Sensors declaration */
    private SensorManager sensorManager;
    private Sensor sensor;
    private Location mLastLocation;

    /* Tools declration */
    private int REQUEST_ACCESS = 1;
    private GoogleApiClient mGoogleApiClient;
    private int footsteps;


    /* On create Activity Method */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going);

         /* Component Initialisation*/
        btnOnGoingStop = (Button) findViewById(R.id.btnOnGoingStop);
        chrnmtOnGoingCrono = (Chronometer) findViewById(R.id.chrnmtOnGoingCrono);
        txtVOnGoingDistance = (TextView) findViewById(R.id.txtVOnGoingDistance);
        txtVOnGoingKalories = (TextView) findViewById(R.id.txtVOnGoingKalories);
        txtVOnGoingSpeedAverage = (TextView) findViewById(R.id.txtVOnGoingSpeedAverage);
        txtVOnGoingSteps = (TextView) findViewById(R.id.txtVOnGoingSteps);
        txtSteps = (TextView) findViewById(R.id.txtVOnGoingSteps);

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

        /* Map initilisation */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* Sensor managing */
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        footsteps = 0;

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation= location;
        LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng).title("Positon").snippet("Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
    }
    @Override
    public final void onSensorChanged(SensorEvent event) {
        footsteps++;
        txtSteps.setText(footsteps + "");

        Log.i("STEP!!", footsteps + "");
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
        mMap = googleMap;

        /*LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng).title("Positon").snippet("Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));*/
        //Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-33.866, 151.195);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Positon").snippet("Actual Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-33.866, 151.195))
                .add(new LatLng(-33.864, 151.193))
                .add(new LatLng(-33.866, 151.198))
                .add(new LatLng(-33.867, 151.203))
        );*/

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Log.i("PERMISSION", "permission accordée");
        } else {
            // Show rationale and request permission.
            Log.i("PERMISSION", "permission refusée");
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS);
            //startLocationUpdates();
            return;
        }
        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        /*LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        Toast.makeText( getApplicationContext(), latlng.toString(), Toast.LENGTH_LONG).show();*/


    }
    /*protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }*/


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed", connectionResult.getErrorMessage());
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Connection suspended", String.valueOf(i));
    }
}
