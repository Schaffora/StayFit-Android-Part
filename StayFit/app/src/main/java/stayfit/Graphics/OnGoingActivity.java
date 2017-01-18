package stayfit.Graphics;

import android.Manifest;
import android.content.Context;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private int CALORIES=0;

    /* Location Tools */
    private int REQUEST_ACCESS = 1;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;
    private List<String> DATABASE;

    /* Sensor */
    private SensorManager sensorManager;
    private Sensor sensor;
    private int FOOT_STEPS;
    private GoogleApiClient mGoogleApiClient;

    /* Database tools*/
    private List<String> lats;
    private List<String> longs;

    /* Intent OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going);

        lats=new ArrayList<String>();
        longs=new ArrayList<String>();
         /* DataBase List initialisation */
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
        String actualUser ="";
        String choosenActivity="";
        //getDataBase();
        DATABASE= new ArrayList<String>();
        DataBaseRefresh();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            actualUser= intent.getStringExtra("actualUser");
            choosenActivity=intent.getStringExtra("ChoosenActivity");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

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
                try {
                    Context context = getApplicationContext();
                    File outputFile = new File(context.getFilesDir(),"DATABASE.txt");
                    OutputStream outStream = new FileOutputStream(outputFile);
                    OutputStreamWriter outputStreamWriter= new OutputStreamWriter(outStream);
                    String ActualUserID="0";

                    //TODO: this shouldn't be used but without it, users seem to be suppressed when we go here
                    for (User user : users) {
                            if(user.Pseudo.equals(finalActualUser))
                            {
                                ActualUserID=Integer.toString(user.ID);
                            }
                            outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + user.Weight + ";" + user.Height + ";" + user.Birthdate + ";" + user.Gender + "]" + "\n");
                    }
                    for(DataSample datasample :dataSamples)
                    {
                        String latsLongs="";
                        for(int i =0; i < datasample.lats.size(); i++)
                        {
                            latsLongs += ";" +datasample.lats.get(i).toString()+ "/"+datasample.longs.get(i).toString();
                        }
                        outputStreamWriter.write("[datasample="+datasample.ID +";"+datasample.USER_ID +";"+datasample.Duration +";"+datasample.Date+";"+datasample.ACTIVITY_ID+";"+datasample.Distance+";"+datasample.Steps+";"+datasample.Calories+latsLongs +"]"+"\n");
                    }

                    long elapsedSeconds = (SystemClock.elapsedRealtime() - chrnmtOnGoingCrono.getBase())/1000;
                    String latsLongs="";
                    for(int i =0; i < lats.size(); i++)
                    {
                            latsLongs += ";" +lats.get(i).toString()+ "/"+longs.get(i).toString();
                    }
                    String activityType="";
                    if(finalChoosenActivity.equals("run"))
                    {
                        activityType="1";
                    }
                    if(finalChoosenActivity.equals("ride"))
                    {
                        activityType="3";
                    }
                    if(finalChoosenActivity.equals("walk"))
                    {
                        activityType="2";
                    }
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    outputStreamWriter.write("[datasample="+dataSamples.size() +";"+ActualUserID +";"+String.valueOf(elapsedSeconds)+";"+date+";"+activityType+";"+COVERED_DISTANCE+";"+FOOT_STEPS+";"+CALORIES+latsLongs +"]"+"\n");

                    outputStreamWriter.close();


                    setResult(RESULT_OK);
                    finish();
                }
                catch (IOException e) {

                }
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
        }

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        longs.add(Double.toString(location.getLongitude()));
        lats.add(Double.toString(location.getLatitude()));
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
            if(COVERED_DISTANCE>0)
            {
                long elapsedSeconds = (SystemClock.elapsedRealtime() - chrnmtOnGoingCrono.getBase())/1000;
                AVERAGE_SPEED=((Number)elapsedSeconds).intValue()/COVERED_DISTANCE;
                txtVOnGoingSpeedAverage.setText("Vitesse moyenne: "+AVERAGE_SPEED);
            }
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
    public void DataBaseRefresh()
    {
        Context context = getApplicationContext();
        try {

            InputStream inputStream = context.openFileInput("DATABASE.txt");
            users.clear();
            dataSamples.clear();
            DATABASE.clear();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DATABASE.add(line);

            while (line !=null)
            {
                try {
                    line =reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DATABASE.add(line);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(int i=0; i<DATABASE.size();i++)
            {
                if (DATABASE.get(i) != null) {
                    DataBaseInterpret(DATABASE.get(i));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void DataBaseInterpret(String line)
    {
        if(line!=null)
        {

            String datas=line.substring(1,line.length()-1);
            List <String> dataTypes = Arrays.asList(datas.split("\\s*=\\s*"));
            List<String> values =Arrays.asList(dataTypes.get(1).split("\\s*;\\s*"));

            if(dataTypes.get(0).equals("user"))
            {
                users.add(new User(Integer.parseInt(values.get(0)),values.get(1),values.get(2),values.get(3),Integer.parseInt(values.get(4)),Integer.parseInt(values.get(5)),values.get(6),values.get(7)));
            }

            if(dataTypes.get(0).equals("datasample"))
            {
                List<String> lats = new ArrayList<String>();
                List<String> longs = new ArrayList<String>();

                for(int i=8; i<values.size(); i++)
                {
                    if(values.get(i) !=null)
                    {
                        String[]LatLong= values.get(i).split("/");
                        lats.add(LatLong[0]);
                        longs.add(LatLong[1]);
                    }

                }
                dataSamples.add(new DataSample(Integer.parseInt(values.get(0)),Integer.parseInt(values.get(1)),Integer.parseInt(values.get(2)),values.get(3),Integer.parseInt(values.get(4)),Integer.parseInt(values.get(5)),Integer.parseInt(values.get(6)),Integer.parseInt(values.get(7)),lats,longs));
            }
            else{}

        }


    }
}
