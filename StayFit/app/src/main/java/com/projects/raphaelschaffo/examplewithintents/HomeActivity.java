package com.projects.raphaelschaffo.examplewithintents;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_home);

        /* Component Initialisation */
        btnHomeRecord=(Button)findViewById(R.id.btnHomeRecord);
        btnHomeSetting=(Button)findViewById(R.id.btnHomeSettings);
        btnHomeViewData=(Button)findViewById(R.id.btnHomeViewData);

        btnHomeRecord.setBackgroundColor(Color.RED);
    }
}
