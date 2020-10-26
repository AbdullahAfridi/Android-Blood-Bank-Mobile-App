package com.rishabh.bloodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rishabh.bloodbank.R;
import com.rishabh.bloodbank.Utils.EndPoints;
import com.rishabh.bloodbank.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText et_blood_group, et_city;
        et_blood_group = findViewById(R.id.et_blood_group);
        et_city = findViewById(R.id.et_city);
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String blood_group1 = et_blood_group.getText().toString();
                String city1 = et_city.getText().toString();
                if(isValid(blood_group1, city1)){
                    get_search_results(blood_group1, city1);
                }
            }
        });
    }


    private void get_search_results(final String blood_group1, final String city1) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, EndPoints.search_donor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json response
                Intent intent = new Intent(SearchActivity.this, SearchResults.class);
                intent.putExtra("city1", city1);
                intent.putExtra("blood_group1", blood_group1);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT)
                        .show();
                Log.d("VOLLEY", volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city1", city1);
                params.put("blood_group1", blood_group1);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private boolean isValid(String blood_group1, String city1){
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");
        valid_blood_groups.add("O+");
        valid_blood_groups.add("O-");
        if(!valid_blood_groups.contains(blood_group1)){
            showMsg("Blood group invalid choose from " + valid_blood_groups);
            return false;
        }else if(city1.isEmpty()){
            showMsg("Enter city");
            return false;
        }
        return true;
    }


    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}