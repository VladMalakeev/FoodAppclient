package tk.tracking_app.devicetrackingproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tk.tracking_app.devicetrackingproject.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final SharedPreferences sharedPreferences = getSharedPreferences("phone_preferences",0);
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(sharedPreferences.getString("phone_number",""));
        final Button button = (Button) findViewById(R.id.button_accept);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String phoneNum = editText.getText().toString();
                editor.putString("phone_number",phoneNum);
                editor.apply();
                button.setText("Сохранено");
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Сохранить");
            }
        });
    }
}
