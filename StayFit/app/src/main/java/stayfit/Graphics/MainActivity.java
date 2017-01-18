package stayfit.Graphics;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnLogSubscribe;
    private Button btnLogLogIn;
    private EditText etLogUserName;
    private EditText etLogPassword;

    private List<User> users;
    private List<DataSample> dataSamples;
    private List<String> DATABASE;

    /* Back result Tools */
    private final int ACTIVITY_RESULT_SUBSCRIBE = 0;
    private final int ACTIVITY_RESULT_EDIT_PROFILE =1000;
    private final int ACTIVITY_RESULT_HOME =1001;

    private String ActualUser;
    private String ActualUserMDP;

    public void getDataBase() {
        DATABASE = new ArrayList<String>();
        AssetManager am = getAssets(); //<-----------------KEEP THIS LINE ALIVE
        Context context = getApplicationContext();
        try {
            //InputStream is = context.openFileInput("DATABASE.txt");
            InputStream is = am.open("DATABASE.txt"); //<-----------------KEEP THIS LINE ALIVE
            Toast.makeText(this, "DATABASE ACCESS FOUND", Toast.LENGTH_LONG).show();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            DATABASE.add(line);

            while (line !=null)
            {
                line =reader.readLine();
                DATABASE.add(line);
            }
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "DATABASE ACCESS NOT FOUND", Toast.LENGTH_LONG).show();
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

    /* Intent OnCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_main);


        /* DataBase List initialisation */
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
//        getDataBase();
        DATABASE= new ArrayList<String>();
        DataBaseRefresh();
        /* Component Initialisation */
        btnLogSubscribe = (Button) findViewById(stayfit.R.id.btnLobSubscribe);
        btnLogLogIn = (Button)findViewById(stayfit.R.id.btnLogLogIn);
        etLogPassword=(EditText)findViewById(stayfit.R.id.etLogPassword);
        etLogUserName=(EditText)findViewById(stayfit.R.id.etLogUsername);


        /*Component control part */
        btnLogSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubscribeActivity.class);

                startActivityForResult(intent, ACTIVITY_RESULT_SUBSCRIBE);
            }
        });

        btnLogLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<users.size();i++)
                {
                    if(users.get(i).Pseudo.equals(etLogUserName.getText().toString()) && users.get(i).MDP.equals(etLogPassword.getText().toString()))
                    {
                        ActualUser =users.get(i).Pseudo;
                        ActualUserMDP = users.get(i).MDP;

                        if(users.get(i).Weight !=0)
                        {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("actualUser", ActualUser);
                            startActivityForResult(intent, ACTIVITY_RESULT_HOME);

                        }
                        else
                        {
                            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                            intent.putExtra("actualUser", ActualUser);
                            startActivityForResult(intent, ACTIVITY_RESULT_EDIT_PROFILE);
                        }
                    }
                }

            }
        });
        for(int i=0; i<DATABASE.size();i++)
        {
            DataBaseInterpret(DATABASE.get(i));
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }


    /* On activity back result */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case 0:
                if (resultCode == RESULT_OK) {
                    Log.i("ActivityResult", "Result_OK");
                    Toast.makeText(this, "New user was created. ", Toast.LENGTH_LONG).show();
                    DataBaseRefresh();
                }
                break;
            case 1000:
                if (resultCode == RESULT_OK) {
                    Log.i("ActivityResult", "Result_OK");
                    Toast.makeText(this, "Have fun with StayFit !", Toast.LENGTH_LONG).show();
                    DataBaseRefresh();

                    if(etLogPassword.getText().toString().equals(ActualUserMDP) && etLogUserName.getText().toString().equals(ActualUser))
                    {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("actualUser", ActualUser);
                        startActivityForResult(intent, ACTIVITY_RESULT_HOME);

                    }
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

}
