package stayfit.Graphics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import stayfit.DataBase.ActivityType;
import stayfit.DataBase.DataSample;
import stayfit.DataBase.User;

public class HomeActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnHomeRecord;
    private Button btnHomeViewData;
    private Button btnHomeSetting;

    /* Intent OnCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_home);

        /* DataBase tools*/
        List<User> users = null;
        List<DataSample> dataSamples = null ;
        List<ActivityType>activityTypes = null;
        String actualUser ="";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            users = (List<User>)extras.getSerializable("users");
            dataSamples = (List<DataSample>)extras.getSerializable("dataSamples");
            activityTypes= (List<ActivityType>)extras.getSerializable("activityTypes");
            actualUser= intent.getStringExtra("actualUser");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final List<User> finalUsers = users;
        final List<DataSample> finalDataSamples = dataSamples;
        final List<ActivityType> finalActivityTypes = activityTypes;
        final String finalActualUser = actualUser;

        /* Component Initialisation */
        btnHomeRecord=(Button)findViewById(stayfit.R.id.btnHomeRecord);
        btnHomeSetting=(Button)findViewById(stayfit.R.id.btnHomeSettings);
        btnHomeViewData=(Button)findViewById(stayfit.R.id.btnHomeViewData);

        /* Gestion des cliques sur les bouttons */
        btnHomeRecord.setBackgroundColor(Color.BLUE);

        btnHomeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OnGoingActivity.class);
                passDataBaseBundle(intent, finalUsers, finalDataSamples, finalActivityTypes);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });



        btnHomeViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewDataActivity.class);
                passDataBaseBundle(intent, finalUsers, finalDataSamples, finalActivityTypes);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });

        btnHomeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                passDataBaseBundle(intent, finalUsers, finalDataSamples, finalActivityTypes);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });
    }
    private void passDataBaseBundle(Intent intent, List<User> users, List<DataSample> dataSamples,List<ActivityType> activityTypes) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("users",(Serializable)users);
        bundle.putSerializable("dataSamples",(Serializable)dataSamples);
        bundle.putSerializable("activityTypes",(Serializable)activityTypes);
        intent.putExtras(bundle);
    }
}
