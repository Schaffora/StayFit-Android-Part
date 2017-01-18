package stayfit.Graphics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stayfit.DataBase.DataSample;
import stayfit.DataBase.User;

public class SubscribeActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnSubscribe;
    private EditText etSubUsername;
    private EditText etSubPassword;
    private EditText etSubConfPassword;
    private EditText etSubbEmailAdress;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;
    private List<String> DATABASE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_subscribe);

        /* Component Initialisation */
        btnSubscribe= (Button)findViewById(stayfit.R.id.btnSubSubscribe);
        etSubbEmailAdress=(EditText)findViewById(stayfit.R.id.etSubEmailAdress);
        etSubConfPassword=(EditText)findViewById(stayfit.R.id.etSubConfPassword);
        etSubPassword=(EditText)findViewById(stayfit.R.id.etSubPassword);
        etSubUsername=(EditText)findViewById(stayfit.R.id.etSubUserName) ;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        /* DataBase List initialisation */
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
        String actualUser ="";
        //getDataBase();
        DATABASE= new ArrayList<String>();
        DataBaseRefresh();


        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etSubUsername.getText().length()!=0 && etSubPassword.getText().length()!=0 && etSubConfPassword.getText().length()!=0 && etSubbEmailAdress.getText().length()!=0){

                    if(etSubConfPassword.getText().toString().equals(etSubPassword.getText().toString()))
                    {

                        boolean emailDisponible = true;
                        boolean pseudoDisponible = true;

                        

                        for (User user : users) {
                            if(user.Pseudo.toString().equals(etSubUsername.getText().toString()))
                            {
                                pseudoDisponible = false;
                            }
                            if(user.Email.toString().equals(etSubbEmailAdress.getText().toString()))
                            {
                                emailDisponible = false;
                            }
                        }

                        if(emailDisponible == true && pseudoDisponible==true)
                        {

                            try {
                                Context context = getApplicationContext();
                                File outputFile = new File(context.getFilesDir(),"DATABASE.txt");
                                OutputStream outStream = new FileOutputStream(outputFile);
                                OutputStreamWriter outputStreamWriter= new OutputStreamWriter(outStream);

                                for (User user : users) {
                                    outputStreamWriter.write("[user="+user.ID +";" +user.Pseudo+";"+user.Email +";"+user.MDP+";"+user.Weight+";"+user.Height +";"+user.Birthdate +";"+user.Gender +"]"+"\n");
                                }
                                int ID =users.size()+1;
                                outputStreamWriter.write("[user="+ID+";"+etSubUsername.getText().toString()+";"+etSubbEmailAdress.getText().toString()+";"+etSubPassword.getText().toString()+";" +"0;" + "0;" +"1.1.1970;"+"male]"+"\n");
                                for(DataSample datasample :dataSamples)
                                {
                                    String latsLongs="";
                                    for(int i =0; i < datasample.lats.size(); i++)
                                    {
                                        latsLongs += ";" +datasample.lats.get(i).toString()+ "/"+datasample.longs.get(i).toString();
                                    }
                                    outputStreamWriter.write("[datasample="+datasample.ID +";"+datasample.USER_ID +";"+datasample.Duration +";"+datasample.Date+";"+datasample.ACTIVITY_ID+";"+datasample.Distance+";"+datasample.Steps+";"+datasample.Calories+latsLongs +"]"+"\n");
                                }
                                outputStreamWriter.close();
                                setResult(RESULT_OK);
                                finish();
                            }
                            catch (IOException e) {
                            }

                        }
                        else
                        {
                            if(emailDisponible == false)
                            {
                                Toast.makeText( getApplicationContext(), "Email already used", Toast.LENGTH_LONG).show();
                            }
                            if(pseudoDisponible==false)
                            {
                                Toast.makeText( getApplicationContext(), "Pseudo already used", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                    else
                    {
                        Toast.makeText( getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText( getApplicationContext(), "Please complete all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
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
