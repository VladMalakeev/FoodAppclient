package tk.tracking_app.devicetrackingproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import tk.tracking_app.devicetrackingproject.R;


/**
 * Created by asus on 05.11.2017.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context thisContext = this;
        Button mainbtn = (Button) findViewById(R.id.make_order);
        Button secondBtn = (Button) findViewById(R.id.settings);
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, ListOfOwners.class);
                startActivity(intent);
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
