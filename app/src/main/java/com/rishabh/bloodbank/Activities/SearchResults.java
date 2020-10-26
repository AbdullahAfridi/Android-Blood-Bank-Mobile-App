package com.rishabh.bloodbank.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rishabh.bloodbank.Adaptar.SearchAdapter;
import com.rishabh.bloodbank.DataModels.Donor;
import com.rishabh.bloodbank.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    List<Donor> donorList;
    RecyclerView recyclerView;
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        donorList = new ArrayList<>();
        String json;
        String city1, blood_group1;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        city1 = intent.getStringExtra("city1");
        blood_group1 = intent.getStringExtra("blood_group1");
        TextView heading = findViewById(R.id.heading);
        String str = "Donors in " + city1 + " with blood group " + blood_group1;
        heading.setText(str);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Donor>>() {
        }.getType();
        List<Donor> dataModels = gson.fromJson(json, type);
        if (dataModels != null && dataModels.isEmpty()) {
            heading.setText("No results");
        }else if(dataModels!=null){
            donorList.addAll(dataModels);
        }

         recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
         adapter = new SearchAdapter(donorList, SearchResults.this);
        recyclerView.setAdapter(adapter);

    }


}