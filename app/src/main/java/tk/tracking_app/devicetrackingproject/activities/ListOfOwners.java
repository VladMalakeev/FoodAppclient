package tk.tracking_app.devicetrackingproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.tracking_app.devicetrackingproject.AppInfo;
import tk.tracking_app.devicetrackingproject.R;

/**
 * Created by asus on 06.11.2017.
 */

public class ListOfOwners extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userslist);
        final Context mContext = this;
        final LinearLayout mLayout = (LinearLayout) findViewById(R.id.owners_linear_layout);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarGetRequest);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.ownersScrollView);

        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.foodapp.gq/android/data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String mResponse) {
                JSONObject jsonObject;
                String[] Result = mResponse.split("enddead");
                AppInfo.setResponse(Result[0]);
                TextView tv = new TextView(mContext);
                Log.d("response", AppInfo.getResponse());
                try {
                    jsonObject = new JSONObject(AppInfo.getResponse());
                    JSONArray owners = jsonObject.getJSONArray("owners");
                    for (int i = 0; i < owners.length(); i++) {
                        final String name = owners.getJSONObject(i).getString("name_owners");
                        Button btn = new Button(mContext);
                        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        btnParams.setMargins(10,10,10,0);
                        btn.setText(name);
                        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        btn.setAllCaps(true);
                        btn.setBackgroundResource(R.drawable.common_button_style);
                        final int finalI = i;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, ShowMenuActivity.class);
                                AppInfo.setShowMenuNumber(finalI);
                                AppInfo.setShowMenuOwner(name);
                                startActivity(intent);
                            }
                        });
                        mLayout.addView(btn,btnParams);
                    }
                } catch (JSONException e) {
                    tv.setText("Нет сети\n");
                    mLayout.addView(tv);
                    Log.e("json", "JSON ошибка");
                }
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("json", "Ошибка доступа к серверу");
                    }
                });
        queue.add(stringRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}