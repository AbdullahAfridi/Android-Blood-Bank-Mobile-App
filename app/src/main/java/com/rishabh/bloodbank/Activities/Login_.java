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

import java.util.HashMap;
import java.util.Map;

public class Login_ extends AppCompatActivity {
    private EditText number,password;
    private Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);


        number =(EditText) findViewById(R.id.number1);
        password=(EditText) findViewById(R.id.password1);
        log=(Button) findViewById(R.id.log);
        TextView textView2=(TextView) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String number1, password1;
           number1 = number.getText().toString();
           password1 = password.getText().toString();

           if(isValid(number1,password1)){
            login(number1, password1);

           }

            }
        });
    }
    private void login(final String number1, final String password1){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("Invalid Credential")){
                            Toast.makeText(Login_.this, response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login_.this,MainActivity.class));
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number1",number1).apply();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city1",response).apply();
                            Login_.this.finish();
                        }
                        else{
                            Toast.makeText(Login_.this, response, Toast.LENGTH_SHORT).show();
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
                params.put("number1", number1);
                params.put("password1", password1);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }
    private boolean isValid(String number1, String password1){

            if(number1.isEmpty()){
        showMessage(" Number is Required");

                return false;
            } else if(password1.isEmpty()){
                showMessage("Password Required");
                return false;
            }
        return true;
    }
    private void showMessage(String msg){

        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}
