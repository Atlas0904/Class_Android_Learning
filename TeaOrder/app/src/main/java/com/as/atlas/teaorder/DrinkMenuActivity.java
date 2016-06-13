package com.as.atlas.teaorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DrinkMenuActivity extends AppCompatActivity {


    // UI
    ListView listViewDrinkList;
    TextView textViewPrice;

    ArrayList<Drink> drinks = new ArrayList<>();
    ArrayList<Drink> drinkOrders = new ArrayList<>();

    // Set data
    private static final String TAG = DrinkMenuActivity.class.getName();
    String[] drinkNames = {"Tea", "Cola", "Milk Tea", "Bubble Milk Tea"};
    int[] mediumCupPrice = {25, 35, 45, 55};
    int[] largeCupPrice = {35, 45, 55, 65};
    int[] imageId = {
            R.drawable.drink_tea,
            R.drawable.drink_cola,
            R.drawable.drink_milk_tea,
            R.drawable.drink_bubble_milk_tea};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        listViewDrinkList = (ListView) findViewById(R.id.listViewDrinkList);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        setDrinkData();
        setupDrinkListView();

        listViewDrinkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
                Drink drink = (Drink) drinkAdapter.getItem(position);
                drinkOrders.add(drink);
                updateTotalPrice(drinkOrders);
            }
        });
    }

    private void updateTotalPrice(ArrayList<Drink> drinkOrders) {
        int total = 0;
        for (Drink drink: drinkOrders) {
            total += drink.mediumCupPrice;
        }
        textViewPrice.setText(String.valueOf(total));
    }

    private void setDrinkData() {
        for (int i = 0 ; i < drinkNames.length; i++) {
            Drink drink = new Drink(drinkNames[i], mediumCupPrice[i], largeCupPrice[i], imageId[i]);
            drinks.add(drink);
        }
    }

    private void setupDrinkListView() {
        DrinkAdapter adapter = new DrinkAdapter(this, drinks);
        listViewDrinkList.setAdapter(adapter);
    }

    public void onClickOKButton(View view) {
        Intent intent = new Intent();

        JSONArray array = new JSONArray();
        for (Drink drink: drinkOrders) {
            JSONObject obj = drink.getJsonData();
            array.put(obj);
        }

        intent.putExtra("result", array.toString());
        setResult(RESULT_OK, intent);
        finish();   // 會回去 call main onActivityResult
    }


    @Override
    protected void onStart() {
        super.onStart();
        log("onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestory");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    public void log(String s) {
        Log.d(TAG, s);
    }
}
