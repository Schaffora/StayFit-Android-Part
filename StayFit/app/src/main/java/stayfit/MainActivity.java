package stayfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnLogSubscribe;
    private Button btnLogLogIn;
    private EditText etLogUserName;
    private EditText etLogPassword;

    /* Back result Tools */
    private final int ACTIVITY_RESULT_SUBSCRIBE = 0;
    private final int ACTIVITY_RESULT_EDIT_PROFILE =1000;
    private final int ACTIVITY_RESULT_HOME =1001;

    /* BDD Tools */
    private boolean firstConnect = true;
    private String Username ="admin";
    private String Password="1000";

    /* Intent OnCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_main);

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
                if(firstConnect==true)
                {
                    if(etLogPassword.getText().toString().equals(Password) && etLogUserName.getText().toString().equals(Username))
                    {
                        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                        startActivityForResult(intent, ACTIVITY_RESULT_EDIT_PROFILE);

                    }
                }
                else
                {
                    if(etLogPassword.getText().toString().equals(Password) && etLogUserName.getText().toString().equals(Username))
                    {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivityForResult(intent, ACTIVITY_RESULT_HOME);

                    }
                }
            }
        });
    }
    /* On activity back result */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode){
            case 0:
                if (resultCode == RESULT_OK) {
                    Log.i("ActivityResult", "Result_OK");
                    Toast.makeText(this, "User creation successed", Toast.LENGTH_LONG).show();
                }
                break;
            case 1000:
                if (resultCode == RESULT_OK) {
                    Log.i("ActivityResult", "Result_OK");
                    Toast.makeText(this, "User connection successed", Toast.LENGTH_LONG).show();
                    firstConnect=false;
                    if(etLogPassword.getText().toString().equals(Password) && etLogUserName.getText().toString().equals(Username))
                    {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivityForResult(intent, ACTIVITY_RESULT_HOME);

                    }
                }
                break;
            default:
                break;
        }




    }

}
