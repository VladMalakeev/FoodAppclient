package tk.tracking_app.devicetrackingproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import tk.tracking_app.devicetrackingproject.AppInfo;
import tk.tracking_app.devicetrackingproject.LocationSingleton;
import tk.tracking_app.devicetrackingproject.MyLocationListener;
import tk.tracking_app.devicetrackingproject.R;


public class ShowMenuActivity extends AppCompatActivity implements MyLocationListener{

    final int REQUEST_ID = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu);
        final Context mContext = this;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ID);
            return;
        }

        LocationSingleton singleton = LocationSingleton.getInstance(this);
        singleton.registerListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("phone_preferences",0);
        final String phone = sharedPreferences.getString("phone_number","");
        final LinearLayout mLayout = (LinearLayout) findViewById(R.id.menu_linear_layout);
        final ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.menuScrollView);
        JSONObject jsonObject;
        TextView tv = new TextView(mContext);
        try {
            jsonObject = new JSONObject(AppInfo.getResponse());
            JSONArray owners = jsonObject.getJSONArray("owners");
            JSONArray menu = owners.getJSONObject(AppInfo.getShowMenuNumber()).getJSONArray("menu");

            final int countClicks[] = new int[menu.length()];

            for (int i=0;i<menu.length();i++) {
                final String dish = menu.getJSONObject(i).getString("name_menu");
                String descript = menu.getJSONObject(i).getString("descript");
                String price = menu.getJSONObject(i).getString("price");


                LinearLayout dishGroup = new LinearLayout(mContext);
                final Button btn = new Button(mContext);
                final LinearLayout descriptLayout = new LinearLayout(mContext);
                final TextView showData = new TextView(mContext);

                dishGroup.setOrientation(LinearLayout.VERTICAL);
                descriptLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams dishGroupParams = new LinearLayout.LayoutParams(viewParams);
                mLayout.addView(dishGroup,dishGroupParams);
                dishGroup.addView(btn,viewParams);
                dishGroupParams.setMargins(10,20,10,0);
                LinearLayout.LayoutParams descriptParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                descriptParams.setMargins(10,0,10,10);
                dishGroup.addView(descriptLayout,descriptParams);
                descriptLayout.addView(showData,viewParams);

                btn.setText(dish);
                btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                btn.setBackgroundResource(R.drawable.common_button_style);
                btn.setVisibility(View.VISIBLE);
                descriptLayout.setVisibility(View.GONE);
                final int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countClicks[finalI] == 0) {
                            descriptLayout.setVisibility(View.VISIBLE);
                            //showData.setVisibility(View.VISIBLE);
                            countClicks[finalI]++;
                        } else if(countClicks[finalI] == 1)
                        {
                            //TODO:new activity for location
                            //При втором нажатии делается пост-запрос на другом активити
                            //Intent intent = new Intent(mContext,MapsActivity.class);
                            //startActivity(intent);
                            btn.setText(dish.concat("(Заказ отправляется...)"));
                            RequestQueue queue = Volley.newRequestQueue(mContext);
                            String url = "https://www.foodapp.gq";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    //TODO:Logs
                                    btn.setText(dish.concat("(Заказ отправлен)"));
                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //TODO:Logs
                                            btn.setText(dish.concat("(Ошибка, проверьте соединение)"));
                                        }
                                    }
                            )
                            {
                                @Override
                                protected Map<String, String> getParams()
                                {
                                    Map<String, String>  params = new HashMap<String, String>();
                                    params.put("id", phone);
                                    params.put("lat", String.valueOf(AppInfo.getLocation().getLatitude()));
                                    params.put("lng", String.valueOf(AppInfo.getLocation().getLongitude()));
                                    params.put("owner", AppInfo.getShowMenuOwner());
                                    params.put("dish_name", dish);
                                    params.put("count", "1");

                                    return params;
                                }
                            };
                            queue.add(stringRequest);
                        }
                    }
                });


                descriptLayout.setBackgroundResource(R.drawable.descript_style);
                showData.setText(getString(R.string.show_menu1).concat(descript).concat(getString(R.string.show_menu2)).concat(price));
                showData.setTextSize(15);
                showData.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //descriptLayout.setVisibility(View.GONE);


            }
        } catch (JSONException e) {
            tv.setText("Нет сети\n");
            mLayout.addView(tv);
            Log.e("json", "JSON ошибка");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ID) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                LocationSingleton singleton = LocationSingleton.getInstance(this);
                singleton.registerListener(this);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        LocationSingleton singleton = LocationSingleton.getInstance(this);
        singleton.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void updateLocation(Location location) {
        AppInfo.setLocation(location);
    }
}
