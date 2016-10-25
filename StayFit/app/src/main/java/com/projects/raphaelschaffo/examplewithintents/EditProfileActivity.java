package com.projects.raphaelschaffo.examplewithintents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class EditProfileActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnProOk;
    private DatePicker etProBirthDate;
    private EditText etProSize;
    private EditText etProWeight;
    private RadioButton rbtnProMen;
    private RadioButton rbtnProWomen;
    private Switch swProMobileData;
    private RadioGroup rbtngGender;
    private NumberPicker npSize;
    private NumberPicker npWeightKG;
    private NumberPicker npWeight;

    /* Intent OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /* Component Initialisation*/
        btnProOk = (Button)findViewById(R.id.btnProOk);
        etProBirthDate= (DatePicker) findViewById(R.id.etProBirthDate);
        rbtnProMen = (RadioButton)findViewById(R.id.rbtnProMen);
        rbtnProWomen =(RadioButton)findViewById(R.id.rbtnProWomen);
        rbtngGender= (RadioGroup) findViewById(R.id.rbtngGender);
        npSize = (NumberPicker) findViewById(R.id.npSize);
        npWeight = (NumberPicker) findViewById(R.id.npWeight);
        npWeightKG = (NumberPicker) findViewById(R.id.npWeightKG);
        npWeight.setMaxValue(9);
        npWeightKG.setMaxValue(200);
        npSize.setMaxValue(210);
        btnProOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(rbtnProWomen.isChecked() == true || rbtnProMen.isChecked()==true)
                    {
                        /* Request to create a new User */
                        /*
                        TODO
                        */
                        setResult(RESULT_OK);
                        finish();
                    }

            }
        });
    }
}
