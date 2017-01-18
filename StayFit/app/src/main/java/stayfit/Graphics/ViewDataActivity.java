package stayfit.Graphics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stayfit.DataBase.DataSample;
import stayfit.DataBase.DatabaseAcesser;
import stayfit.DataBase.User;
import stayfit.R;

public class ViewDataActivity extends AppCompatActivity {
    private ListView lstDataViewDataSampleList;
    private ArrayList<String> DataSampleList;
    private ArrayList<Integer> DataSampleKey;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;

    /* Database acesser */
    private DatabaseAcesser dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        lstDataViewDataSampleList=(ListView) findViewById(R.id.lstDataViewDataSampleList);

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
            Toast.makeText( getApplicationContext(), "Welcome "+actualUser, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final String finalActualUser = actualUser;
        int idUser=0;
        for (User user : users){
            if(user.Pseudo.equals(finalActualUser)){
                idUser=user.ID;
            }
        }
        DataSampleList = new ArrayList<String>();
        DataSampleKey = new ArrayList<Integer>();
        for (DataSample data : dataSamples) {
            if(data.USER_ID==idUser){
                DataSampleList.add("Date :"+data.Date +" Duration :"+ data.Duration);
                DataSampleKey.add(data.ID);
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, DataSampleList);
        lstDataViewDataSampleList.setAdapter(arrayAdapter);


        lstDataViewDataSampleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSample currentDataSample=null;
                for (DataSample data : dataSamples) {
                    if(data.ID==DataSampleKey.get(position)){
                        currentDataSample=data;
                    }
                }
                Intent intent = new Intent(ViewDataActivity.this, DisplayDataActivity.class);

                passDataBaseBundle(intent, currentDataSample);
                intent.putExtra("actualUser", finalActualUser);
                startActivityForResult(intent, 0);
            }
        });
    }
    private void passDataBaseBundle(Intent intent,DataSample dataSample) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataSample",dataSample);
        intent.putExtras(bundle);
    }
}


