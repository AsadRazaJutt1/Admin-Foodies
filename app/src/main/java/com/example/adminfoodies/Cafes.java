package com.example.adminfoodies;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.adminfoodies.Helper.PlacesPOJO;
import com.example.adminfoodies.Helper.RecyclerViewAdapter;
import com.example.adminfoodies.Helper.SearchData;
import com.example.adminfoodies.Helper.StoreModel;

import java.util.List;


public class Cafes extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText editText;
    Button button;
    List<PlacesPOJO.CustomA> results;
    List<StoreModel> storeModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafes);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SearchData x = SearchData.getInstance();
        storeModels = x.getStoreModels();
        results = x.results;
        if (storeModels.size() <= 5 || storeModels.size() == results.size()) {
            RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, storeModels);
            recyclerView.setAdapter(adapterStores);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchData x = SearchData.getInstance();
        storeModels = x.getStoreModels();
        results = x.results;
        if (storeModels.size() <= 5 || storeModels.size() == results.size()) {
            RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, storeModels);
            recyclerView.setAdapter(adapterStores);
        }

    }
}