package stayfit.Graphics;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    /* Component Declaration */
    private Button btnHomeRecord;
    private Button btnHomeViewData;
    private Button btnHomeSetting;

    /* Intent OnCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_home);

        /* Component Initialisation */
        btnHomeRecord=(Button)findViewById(stayfit.R.id.btnHomeRecord);
        btnHomeSetting=(Button)findViewById(stayfit.R.id.btnHomeSettings);
        btnHomeViewData=(Button)findViewById(stayfit.R.id.btnHomeViewData);


        btnHomeRecord.setBackgroundColor(Color.RED);
        btnHomeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OnGoingActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnHomeViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewDataActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnHomeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
