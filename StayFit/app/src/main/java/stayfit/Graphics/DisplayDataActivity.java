package stayfit.Graphics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import stayfit.DataBase.DataSample;
import stayfit.R;

public class DisplayDataActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener {

    private TextView txtVMoy;
    private TextView txtFootSteps;
    private TextView txtCal;
    private TextView txtDist;
    private GoogleMap mapMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        DataSample dataSample = null ;
        dataSample = (DataSample)extras.getSerializable("dataSample");
//        Toast.makeText( getApplicationContext(), dataSample.ID, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),dataSample+"",Toast.LENGTH_LONG).show();

        txtVMoy= (TextView)findViewById(R.id.txtVMoy);
        txtFootSteps=(TextView)findViewById(R.id.txtFootSteps);
        txtCal=(TextView)findViewById(R.id.txtCalories);
        txtDist=(TextView)findViewById(R.id.txtDistance);

        //time in seconds
        int dtTime=dataSample.Duration;
        int pathDist=dataSample.Distance;

        float averageSpeed= pathDist/dtTime;

        txtVMoy.setText(averageSpeed+"");
        txtFootSteps.setText(dataSample.Steps+"");
        txtCal.setText(dataSample.Calories+"");
        txtDist.setText(dataSample.Distance+"");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapMap = googleMap;
        // Add a marker in Sydney and move the camera
        mapMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-33.866, 151.195))
                .add(new LatLng(-33.864, 151.193))
                .add(new LatLng(-33.866, 151.198))
                .add(new LatLng(-33.867, 151.203))
        );
        LatLng sydney = new LatLng(-33.866, 151.195);
        mapMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        mapMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    protected void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed", connectionResult.getErrorMessage());
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Connection suspended", String.valueOf(i));
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
