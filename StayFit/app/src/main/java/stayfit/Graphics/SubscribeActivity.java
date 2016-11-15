package stayfit.Graphics;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import stayfit.DataBase.ActivityType;
import stayfit.DataBase.DataSample;
import stayfit.DataBase.User;

public class SubscribeActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnSubscribe;
    private EditText etSubUsername;
    private EditText etSubPassword;
    private EditText etSubConfPassword;
    private EditText etSubbEmailAdress;

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

        List<User> users = null;
        List<DataSample> dataSamples = null ;
        List<ActivityType>activityTypes = null;


        if (extras != null) {
            users = (List<User>)extras.getSerializable("users");
            dataSamples = (List<DataSample>)extras.getSerializable("dataSamples");
            activityTypes= (List<ActivityType>)extras.getSerializable("activityTypes");
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL ERROR", Toast.LENGTH_LONG).show();
        }


        final List<User> finalUsers = users;
        final List<ActivityType> finalactivities =activityTypes;
        final List<DataSample> finalDataSamples =dataSamples;
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etSubUsername.getText().length()!=0 && etSubPassword.getText().length()!=0 && etSubConfPassword.getText().length()!=0 && etSubbEmailAdress.getText().length()!=0){

                    if(etSubConfPassword.getText().toString().equals(etSubPassword.getText().toString()))
                    {

                        boolean emailDisponible = true;
                        boolean pseudoDisponible = true;

                        

                        for (User user : finalUsers) {
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

                                //Context context = getApplicationContext();
                                //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("DATABASE.txt", Context.MODE_PRIVATE));
                                for (User user : finalUsers) {
                                    outputStreamWriter.write("[user="+user.ID +";" +user.Pseudo+";"+user.Email +";"+user.MDP+";"+user.Weight+";"+user.Height +";"+user.Birthdate +";"+user.Gender +"]"+"\n");
                                }
                                int ID =finalUsers.size()+1;
                                outputStreamWriter.write("[user="+ID+";"+etSubUsername.getText().toString()+";"+etSubbEmailAdress.getText().toString()+";"+etSubPassword.getText().toString()+";" +"0;" + "0;" +"0;"+"null]"+"\n");
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
                            if(emailDisponible == false)
                            {
                                Toast.makeText( getApplicationContext(), "EMAIL ALRDY USED", Toast.LENGTH_LONG).show();
                            }
                            if(pseudoDisponible==false)
                            {
                                Toast.makeText( getApplicationContext(), "PSEUDO ALRDY USED", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                    else
                    {
                        Toast.makeText( getApplicationContext(), "PASSWORD DON'T MATCH", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText( getApplicationContext(), "COMPLETE ALL FIELDS", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
