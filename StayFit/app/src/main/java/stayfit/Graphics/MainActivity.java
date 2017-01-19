package stayfit.Graphics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import stayfit.DataBase.DataSample;
import stayfit.DataBase.DatabaseAcesser;
import stayfit.DataBase.User;

public class MainActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnLogSubscribe;
    private Button btnLogLogIn;
    private EditText etLogUserName;
    private EditText etLogPassword;

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;

    /* Database acesser */
    private DatabaseAcesser dba;

    /* Back result Tools */
    private final int ACTIVITY_RESULT_SUBSCRIBE = 0;
    private final int ACTIVITY_RESULT_EDIT_PROFILE =1000;
    private final int ACTIVITY_RESULT_HOME =1001;

    private String ActualUser;
    private String ActualUserMDP;

    /* Intent OnCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_main);

        /* DataBase List initialisation */
        dba= new DatabaseAcesser(getApplicationContext());
        users = dba.getUsers();
        dataSamples=dba.getDataSamples();

        /* Component Initialisation */
        btnLogSubscribe = (Button) findViewById(stayfit.R.id.btnLobSubscribe);
        btnLogLogIn = (Button)findViewById(stayfit.R.id.btnLogLogIn);
        etLogPassword=(EditText)findViewById(stayfit.R.id.etLogPassword);
        etLogUserName=(EditText)findViewById(stayfit.R.id.etLogUsername);


        /*Component control part */
        btnLogSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubscribeActivity.class);

                startActivityForResult(intent, ACTIVITY_RESULT_SUBSCRIBE);
            }
        });

        btnLogLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<users.size();i++)
                {
                    if(users.get(i).Pseudo.equals(etLogUserName.getText().toString()) && users.get(i).MDP.equals(etLogPassword.getText().toString()))
                    {
                        ActualUser =users.get(i).Pseudo;
                        ActualUserMDP = users.get(i).MDP;

                        if(users.get(i).Weight !=0)
                        {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("actualUser", ActualUser);
                            startActivityForResult(intent, ACTIVITY_RESULT_HOME);

                        }
                        else
                        {
                            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                            intent.putExtra("actualUser", ActualUser);
                            startActivityForResult(intent, ACTIVITY_RESULT_EDIT_PROFILE);
                        }
                    }
                }

            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    /* On activity back result */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case 0:
                if (resultCode == RESULT_OK) {
                    Log.i("ActivityResult", "Result_OK");
                    dba.DataBaseRefresh();
                    users=dba.getUsers();
                    dataSamples=dba.getDataSamples();
                }
                break;
            case 1000:
                if (resultCode == RESULT_OK) {
                    Log.i("ActivityResult", "Result_OK");
                    Toast.makeText(this, "Have fun with StayFit !", Toast.LENGTH_LONG).show();
                    dba.DataBaseRefresh();
                    users=dba.getUsers();
                    dataSamples=dba.getDataSamples();

                    if(etLogPassword.getText().toString().equals(ActualUserMDP) && etLogUserName.getText().toString().equals(ActualUser))
                    {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("actualUser", ActualUser);
                        startActivityForResult(intent, ACTIVITY_RESULT_HOME);

                    }
                }
                break;
            default:
                break;
        }
    }
}
