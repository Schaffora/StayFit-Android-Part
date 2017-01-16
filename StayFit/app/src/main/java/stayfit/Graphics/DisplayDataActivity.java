package stayfit.Graphics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import stayfit.DataBase.DataSample;
import stayfit.R;

public class DisplayDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        DataSample dataSample = null ;
        dataSample = (DataSample)extras.getSerializable("dataSample");
//        Toast.makeText( getApplicationContext(), dataSample.ID, Toast.LENGTH_LONG).show();
    }
}
