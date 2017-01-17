package stayfit.Graphics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import java.util.ArrayList;

import stayfit.DataBase.DataSample;
import stayfit.R;

public class DisplayDataActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener {

    private TextView txtVMoy;
    private TextView txtFootSteps;
    private TextView txtCal;
    private TextView txtDist;
    private GoogleMap mapMap;
    private GoogleApiClient mGoogleApiClient;
    private DataSample dataSample;
    private ArrayList<LatLng> coordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        dataSample = (DataSample)extras.getSerializable("dataSample");
//        Toast.makeText( getApplicationContext(), dataSample.ID, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),dataSample+"",Toast.LENGTH_LONG).show();

        txtVMoy= (TextView)findViewById(R.id.txtVMoy);
        txtFootSteps=(TextView)findViewById(R.id.txtFootSteps);
        txtCal=(TextView)findViewById(R.id.txtCalories);
        txtDist=(TextView)findViewById(R.id.txtDistance);

        coordList = new ArrayList<>();

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
        for(int i =0; i < dataSample.lats.size(); i++)
        {
            coordList.add(new LatLng(Double.parseDouble(dataSample.lats.get(i).toString()), Double.parseDouble(dataSample.longs.get(i))));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coordList);
        polylineOptions
                .width(5)
                .color(Color.RED);
        mapMap.addPolyline(polylineOptions);
        LatLng last = new LatLng(coordList.get(coordList.size()-1).latitude, coordList.get(coordList.size()-1).longitude);
        mapMap.moveCamera(CameraUpdateFactory.newLatLngZoom(last, 18));
        mapMap.moveCamera(CameraUpdateFactory.newLatLng(last));
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
