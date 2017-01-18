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
import stayfit.DataBase.User;
import stayfit.R;

public class ViewDataActivity extends AppCompatActivity {
    private ListView lstDataViewDataSampleList;
    private ArrayList<String> DataSampleList;
    private ArrayList<Integer> DataSampleKey;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;
    private List<String> DATABASE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        lstDataViewDataSampleList=(ListView) findViewById(R.id.lstDataViewDataSampleList);

        /* DataBase List initialisation */
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
        String actualUser ="";
        //getDataBase();
        DATABASE= new ArrayList<String>();
        DataBaseRefresh();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

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


