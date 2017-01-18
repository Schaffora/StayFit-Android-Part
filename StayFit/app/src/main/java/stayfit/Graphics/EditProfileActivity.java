package stayfit.Graphics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditProfileActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnProOk;
    private DatePicker etProBirthDate;
    private RadioButton rbtnProMen;
    private RadioButton rbtnProWomen;
    private RadioGroup rbtngGender;
    private NumberPicker npSize;
    private NumberPicker npWeight;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;
    private List<String> DATABASE;

    /* Intent OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_edit_profile);

        /* Component Initialisation*/
        btnProOk = (Button)findViewById(stayfit.R.id.btnProOk);
        etProBirthDate= (DatePicker) findViewById(stayfit.R.id.etProBirthDate);
        rbtnProMen = (RadioButton)findViewById(stayfit.R.id.rbtnProMen);
        rbtnProWomen =(RadioButton)findViewById(stayfit.R.id.rbtnProWomen);
        rbtngGender= (RadioGroup) findViewById(stayfit.R.id.rbtngGender);
        npSize = (NumberPicker) findViewById(stayfit.R.id.npSize);
        npWeight = (NumberPicker) findViewById(stayfit.R.id.npWeight);

        /* Value initilisation */
        npWeight.setMinValue(20);
        npWeight.setMaxValue(200);
        npSize.setMinValue(50);
        npSize.setMaxValue(210);


        /*DataBase tools */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

          /* DataBase List initialisation */
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
        String actualUser ="";
        //getDataBase();
        DATABASE= new ArrayList<String>();
        DataBaseRefresh();


        if (extras != null) {
            actualUser= intent.getStringExtra("actualUser");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final String finalActualUser = actualUser;

        for (User user : users) {

            if(user.Pseudo.equals(finalActualUser))
            {
                npWeight.setValue(user.Weight);
                npSize.setValue(user.Height);
                if(user.Gender.equals("male"))
                {
                    rbtnProMen.setChecked(true);
                    rbtnProWomen.setChecked(false);
                }
                else
                {
                    rbtnProMen.setChecked(false);
                    rbtnProWomen.setChecked(true);
                }
                String[] value=user.Birthdate.split("\\.");
                etProBirthDate.updateDate(Integer.parseInt(value[2]),Integer.parseInt(value[1]),Integer.parseInt(value[0]));
            }
        }

        btnProOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(npSize.getValue()>49 && npWeight.getValue()>19)
                    {
                        if(rbtnProWomen.isChecked() == true || rbtnProMen.isChecked()==true)
                        {
                            try {
                                int year = etProBirthDate.getYear();
                                int month = etProBirthDate.getMonth();
                                int day = etProBirthDate.getDayOfMonth();

                                String yearvalue=Integer.toString(year);
                                String monthvalue=Integer.toString(month);
                                String dayvalue=Integer.toString(day);

                                String gender ="";
                                if(rbtnProWomen.isChecked()==true)
                                {
                                    gender="female";
                                }
                                else {
                                    gender="male";
                                }
                                Context context = getApplicationContext();
                                File outputFile = new File(context.getFilesDir(),"DATABASE.txt");
                                OutputStream outStream = new FileOutputStream(outputFile);
                                OutputStreamWriter outputStreamWriter= new OutputStreamWriter(outStream);

                                for (User user : users) {
                                 if(user.Pseudo.equals(finalActualUser))
                                    {
                                        outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + Integer.toString(npWeight.getValue()).toString()+ ";" + Integer.toString(npSize.getValue()).toString() + ";" + dayvalue.toString()+"."+monthvalue.toString()+"."+yearvalue.toString() + ";" + gender.toString() + "]" + "\n");
                                    }
                                    else
                                    {
                                        outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + user.Weight + ";" + user.Height + ";" + user.Birthdate + ";" + user.Gender + "]" + "\n");
                                    }
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
                                outputStreamWriter.close();
                                outStream.close();
                                setResult(RESULT_OK);
                                finish();
                            }
                            catch (IOException e) {
                            }
                        }
                        else
                        {
                            Toast.makeText( getApplicationContext(), "Please chose a Gender", Toast.LENGTH_LONG).show();
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
