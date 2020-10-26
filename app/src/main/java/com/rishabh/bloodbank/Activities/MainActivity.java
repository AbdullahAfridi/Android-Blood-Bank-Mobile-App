package com.rishabh.bloodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rishabh.bloodbank.Adaptar.RequestAdaptar;
import com.rishabh.bloodbank.DataModels.RequestDataModel;
import com.rishabh.bloodbank.R;
import com.rishabh.bloodbank.Utils.EndPoints;
import com.rishabh.bloodbank.Utils.VolleySingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
     RecyclerView recyclerView;
     TextView btn;
     List<RequestDataModel> requestDataModels;
     RequestAdaptar requestAdaptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestDataModels =new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolBar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() ==R.id.search_btn){
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));

                }
                return false;
            }
        });

        recyclerView= findViewById(R.id.recyclerView);
       RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
       recyclerView.setLayoutManager(layoutManager);
       requestAdaptar= new RequestAdaptar(requestDataModels,this);
      recyclerView.setAdapter(requestAdaptar);
        populateHomePage();
         TextView pic_location = findViewById(R.id.location);
         String location = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "no_city_found");
            if(!location.equals("no_city_found")){
                pic_location.setText(location);
            }

        btn = findViewById(R.id.make_req);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MakeRequestActivity.class);
                startActivity(intent);
            }
        });

    }
    public void populateHomePage(){
       final String city1=  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city1", "no_city");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.get_requests,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List <RequestDataModel>>(){}.getType();
                        List<RequestDataModel> dataModels = gson.fromJson(response,type);
                        requestDataModels.addAll(dataModels);
                        requestAdaptar.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("VOLLEY", volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                    params.put("city1",city1);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
