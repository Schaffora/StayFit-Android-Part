package stayfit.Graphics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stayfit.DataBase.DataSample;
import stayfit.DataBase.DatabaseAcesser;
import stayfit.DataBase.User;

public class HomeActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnHomeRecord;
    private Button btnHomeViewData;
    private Button btnHomeSetting;
    private RadioButton rbtnHomeWalk;
    private RadioButton rbtnHomeRun;
    private RadioButton rbtnHomeRide;
    private RadioGroup rbtnActivity;

    /* Back result Tools */
    private final int ACTIVITY_RESULT = 100;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;

    /* Database acesser */
    private DatabaseAcesser dba;

    /* Intent OnCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_home);

       /* DataBase List initialisation */
        dba= new DatabaseAcesser(getApplicationContext());
        users = dba.getUsers();
        dataSamples=dba.getDataSamples();

        /*Intent bundle */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String actualUser ="";

        if (extras != null) {
            actualUser= intent.getStringExtra("actualUser");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final String finalActualUser = actualUser;

        /* Component Initialisation */
        btnHomeRecord=(Button)findViewById(stayfit.R.id.btnHomeRecord);
        btnHomeSetting=(Button)findViewById(stayfit.R.id.btnHomeSettings);
        btnHomeViewData=(Button)findViewById(stayfit.R.id.btnHomeViewData);
        rbtnActivity=(RadioGroup)findViewById(stayfit.R.id.rbtnActivity) ;
        rbtnHomeRide=(RadioButton)findViewById(stayfit.R.id.rbtnHomeRide) ;
        rbtnHomeRun=(RadioButton)findViewById(stayfit.R.id.rbtnHomeRun);
        rbtnHomeWalk=(RadioButton)findViewById(stayfit.R.id.rbtnHomeWalk);

        /* Default Activity */
        rbtnHomeWalk.setChecked(true);


        /* Gestion des cliques sur les bouttons */

        btnHomeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OnGoingActivity.class);
                intent.putExtra("actualUser", finalActualUser);
                if(rbtnHomeWalk.isChecked()==true)
                {
                    intent.putExtra("ChoosenActivity", "walk");
                }
                if(rbtnHomeRun.isChecked()==true)
                {
                    intent.putExtra("ChoosenActivity", "run");
                }
                if(rbtnHomeRide.isChecked()==true)
                {
                    intent.putExtra("ChoosenActivity", "ride");
                }
                startActivityForResult(intent, ACTIVITY_RESULT);
            }
        });



        btnHomeViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewDataActivity.class);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });

        btnHomeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case 100:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "New user was created. ", Toast.LENGTH_LONG).show();
                    dba.DataBaseRefresh();
                    users=dba.getUsers();
                    dataSamples=dba.getDataSamples();
                }
                break;
            default:
                break;
        }
    }
}
