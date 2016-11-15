package stayfit.Graphics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import stayfit.DataBase.ActivityType;
import stayfit.DataBase.DataSample;
import stayfit.DataBase.User;
import stayfit.R;

public class ViewDataActivity extends AppCompatActivity {
    private ListView lstDataViewDataSampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(stayfit.R.layout.activity_view_data);
        lstDataViewDataSampleList=(ListView) findViewById(R.id.lstDataViewDataSampleList);

        /* DataBase tools*/
        List<User> users = null;
        List<DataSample> dataSamples = null ;
        List<ActivityType>activityTypes = null;
        String actualUser ="";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            users = (List<User>)extras.getSerializable("users");
            dataSamples = (List<DataSample>)extras.getSerializable("dataSamples");
            activityTypes= (List<ActivityType>)extras.getSerializable("activityTypes");
            actualUser= intent.getStringExtra("actualUser");
            Toast.makeText( getApplicationContext(), "Welcome "+actualUser, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText( getApplicationContext(), "FATAL DB ACCESS ERROR", Toast.LENGTH_LONG).show();
        }

        final List<User> finalUsers = users;
        final List<DataSample> finalDataSamples = dataSamples;
        final List<ActivityType> finalActivityTypes = activityTypes;
        final String finalActualUser = actualUser;

        lstDataViewDataSampleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    }
}
