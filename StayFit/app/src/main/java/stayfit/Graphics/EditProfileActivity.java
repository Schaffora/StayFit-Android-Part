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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import stayfit.DataBase.ActivityType;
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
    private NumberPicker npWeightKG;
    private NumberPicker npWeight;

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
        npWeightKG = (NumberPicker) findViewById(stayfit.R.id.npWeightKG);

        /* Value initilisation */
        npWeight.setMinValue(0);
        npWeight.setMaxValue(9);
        npWeightKG.setMinValue(20);
        npWeightKG.setMaxValue(200);
        npSize.setMinValue(50);
        npSize.setMaxValue(210);


        /*DataBase tools */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        List<User> users = null;
        List<DataSample> dataSamples = null ;
        List<ActivityType>activityTypes = null;
        String actualUser ="";


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
        final List<ActivityType> finalactivities =activityTypes;
        final List<DataSample> finalDataSamples =dataSamples;
        final String finalActualUser = actualUser;

        btnProOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(npSize.getValue()>49 && npWeightKG.getValue()>19)
                    {
                        if(rbtnProWomen.isChecked() == true || rbtnProMen.isChecked()==true)
                        {
                            try {
                                int year = etProBirthDate.getYear();
                                int month = etProBirthDate.getMonth();
                                int day = etProBirthDate.getDayOfMonth();

                                String Str = Integer.toString(year);
                                String yearvalue=Str.substring(2);
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

                                for (User user : finalUsers) {
                                 if(user.Pseudo.toString().equals(finalActualUser.toString()))
                                    {

                                        outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + Integer.toString(npWeightKG.getValue()).toString()+ ";" + Integer.toString(npSize.getValue()).toString() + ";" + dayvalue.toString()+"."+monthvalue.toString()+"."+yearvalue.toString() + ";" + gender.toString() + "]" + "\n");
                                    }
                                    else
                                    {
                                        outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + user.Weight + ";" + user.Height + ";" + user.Birthdate + ";" + user.Gender + "]" + "\n");
                                    }
                                }
                                for (ActivityType activitis : finalactivities)
                                {
                                    outputStreamWriter.write("[activitytype="+activitis.ID +";"+activitis.Name+";"+activitis.Coef+"]"+"\n");
                                }
                                for(DataSample datasample :finalDataSamples)
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
}
