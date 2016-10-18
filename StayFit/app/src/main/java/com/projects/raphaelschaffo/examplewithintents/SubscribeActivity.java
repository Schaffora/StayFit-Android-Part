package com.projects.raphaelschaffo.examplewithintents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        setContentView(R.layout.activity_subscribe);

        /* Component Initialisation */
        btnSubscribe= (Button)findViewById(R.id.btnSubSubscribe);
        etSubbEmailAdress=(EditText)findViewById(R.id.etSubEmailAdress);
        etSubConfPassword=(EditText)findViewById(R.id.etSubConfPassword);
        etSubPassword=(EditText)findViewById(R.id.etSubPassword);
        etSubUsername=(EditText)findViewById(R.id.etSubUserName) ;

        /* Component Control Part */
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSubUsername.getText().equals("") &&!etSubPassword.getText().equals("")&& !etSubConfPassword.getText().equals("")&&!etSubbEmailAdress.getText().equals("")) {
                    setResult(RESULT_OK);
                    /*Requête pour créer un compte*/
                    /*
                    TODO
                    */
                    finish();
                }
            }
        });
    }
}
