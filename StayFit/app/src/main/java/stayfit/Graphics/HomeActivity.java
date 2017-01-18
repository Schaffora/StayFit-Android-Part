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
    private List<String> DATABASE;

    /* Intent OnCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_home);

        /* DataBase tools*/
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
        String actualUser ="";
        DATABASE = new ArrayList<String>();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            users = (List<User>)extras.getSerializable("users");
            dataSamples = (List<DataSample>)extras.getSerializable("dataSamples");
            actualUser= intent.getStringExtra("actualUser");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final List<User> finalUsers = users;
        final List<DataSample> finalDataSamples = dataSamples;
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
                passDataBaseBundle(intent, finalUsers, finalDataSamples);
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
                passDataBaseBundle(intent, finalUsers, finalDataSamples);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });

        btnHomeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                passDataBaseBundle(intent, finalUsers, finalDataSamples);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });
    }
    private void passDataBaseBundle(Intent intent, List<User> users, List<DataSample> dataSamples) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("users",(Serializable)users);
        bundle.putSerializable("dataSamples",(Serializable)dataSamples);
        intent.putExtras(bundle);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case 100:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "New user was created. ", Toast.LENGTH_LONG).show();
                    //DataBaseRefresh();
                }
                break;
            default:
                break;
        }
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
