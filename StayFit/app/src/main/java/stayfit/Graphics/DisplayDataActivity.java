package stayfit.Graphics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import stayfit.DataBase.DataSample;
import stayfit.R;

public class DisplayDataActivity extends AppCompatActivity {

    private TextView txtVMoy;
    private TextView txtFootSteps;
    private TextView txtCal;
    private TextView txtDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        DataSample dataSample = null ;
        dataSample = (DataSample)extras.getSerializable("dataSample");
//        Toast.makeText( getApplicationContext(), dataSample.ID, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),dataSample+"",Toast.LENGTH_LONG).show();

        txtVMoy= (TextView)findViewById(R.id.txtVMoy);
        txtFootSteps=(TextView)findViewById(R.id.txtFootSteps);
        txtCal=(TextView)findViewById(R.id.txtCalories);
        txtDist=(TextView)findViewById(R.id.txtDistance);

        //time in seconds
        int dtTime=dataSample.Duration;
        int pathDist=dataSample.Distance;

        float averageSpeed= pathDist/dtTime;

        txtVMoy.setText(averageSpeed+"");
        txtFootSteps.setText(dataSample.Steps+"");
        txtCal.setText(dataSample.Calories+"");
        txtDist.setText(dataSample.Distance+"");
    }
}
