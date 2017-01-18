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
import stayfit.DataBase.DatabaseAcesser;
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

    /* Database acesser */
    private DatabaseAcesser dba;

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

        /* DataBase List initialisation */
        dba= new DatabaseAcesser(getApplicationContext());
        users = dba.getUsers();
        dataSamples=dba.getDataSamples();

        /*Intent bundle */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String actualUser ="";

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
                            dba.createUser(etSubUsername.getText().toString(),etSubbEmailAdress.getText().toString(),etSubPassword.getText().toString());
                            setResult(RESULT_OK);
                            finish();
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
}
