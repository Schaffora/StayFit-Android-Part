package stayfit.Graphics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import stayfit.DataBase.DataSample;
import stayfit.DataBase.DatabaseAcesser;
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

    /*Database acesser */
    private DatabaseAcesser dba;

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
        npWeight.setMaxValue(110);
        npSize.setMinValue(50);
        npSize.setMaxValue(210);


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
                            dba.saveUser(npWeight.getValue(),npSize.getValue(),dayvalue,monthvalue,yearvalue,gender,finalActualUser);
                            setResult(RESULT_OK);
                            finish();
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
