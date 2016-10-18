package com.projects.raphaelschaffo.examplewithintents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

public class EditProfileActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnProOk;
    private EditText etProBirthDate;
    private EditText etProSize;
    private EditText etProWeight;
    private RadioButton rbtnProMen;
    private RadioButton rbtnProWomen;
    private Switch swProMobileData;

    /* Intent OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /* Component Initialisation*/
        btnProOk = (Button)findViewById(R.id.btnProOk);
        etProBirthDate= (EditText)findViewById(R.id.etProBirthDate);
        etProSize= (EditText)findViewById(R.id.etProSize);
        etProWeight=(EditText)findViewById(R.id.etProWeight);
        rbtnProMen = (RadioButton)findViewById(R.id.rbtnProMen);
        rbtnProWomen =(RadioButton)findViewById(R.id.rbtnProWomen);

        btnProOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etProBirthDate.getText().equals("")&&!etProSize.getText().equals("") && !etProSize.getText().equals(""))
                {
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

            }
        });
    }
}
