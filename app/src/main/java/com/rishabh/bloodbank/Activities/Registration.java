package com.rishabh.bloodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

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

public class Registration extends AppCompatActivity {
    private EditText name, city, number,blood_group,password;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name);
        city = findViewById(R.id.city);
        number = (EditText) findViewById(R.id.number);
        blood_group = (EditText) findViewById(R.id.blood_group);
        password = (EditText) findViewById(R.id.password);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Login_.class);
                startActivity(intent);
            }
        });


        Button reg1= (Button) findViewById(R.id.reg);
        reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1, city1, password1, number1, blood_group1;
                name1 = name.getText().toString();
                city1 = city.getText().toString();
                number1 = number.getText().toString();
                blood_group1 = blood_group.getText().toString();
                password1 = password.getText().toString();
                if(isValid(name1,city1,blood_group1,number1,password1)){
                    register(name1,city1,blood_group1,number1,password1);
                }


            }
        });
    }
    private  void register(final String name1, final String city1, final String blood_group1, final String number1, final String password1){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",city1).apply();
                            Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this,Login_.class));
                            Registration.this.finish();
                        }
                        else{
                            Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("VOLLEY", volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name1", name1);
                params.put("city1", city1);
                params.put("blood_group1", blood_group1);
                params.put("number1", number1);
                params.put("password1", password1);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name1, String city1, String blood_group1,String number1,String password1){
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("O+");
        valid_blood_groups.add("O-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");



        if(name1.isEmpty()){
            showMessage(" City is Empty");
            return false;
        }
        else if(city1.isEmpty()){
            showMessage(" City is Empty");
            return false;
        }  else if(!valid_blood_groups.contains(blood_group1)){
            showMessage(" Blood Group is Invalid Choose from "+valid_blood_groups);
            return false;
        } else if(name1.isEmpty() || number.length()!=10){
            showMessage(" Invalid Mobile Number. Number Should be in form of 0-10 digits");
            return false;
        } else if(password1.isEmpty()){
            showMessage(" Password is Required");
            return false;
        }
        return true;
    }
    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }

    }




