package com.as.atlas.teaorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                log("onItemClick.");
                DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
                Drink drink = (Drink) drinkAdapter.getItem(position);
                if (drinkOrders.contains(drink)) {
                    drink.addOne();
                    log("drink add one. drink=" + drink);
                } else {
                    drinkOrders.add(drink);
                    log("drink no contain. drink=" + drink);
                }
                setupDrinkListView();
                updateTotalPrice(drinkOrders);
            }
        });

        listViewDrinkList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
                Drink drink = (Drink) drinkAdapter.getItem(position);
                if (drinkOrders.contains(drink)) {
                    drink.resetNum();
                    log("drink num reset. drink=" + drink);
                }
                setupDrinkListView();
                updateTotalPrice(drinkOrders);

                return true;
            }
        });
    }

    private void resetOrderList() {
        for (Drink drink: drinkOrders) {
            drink.resetNum();
        }
        updateTotalPrice(drinkOrders);
        setupDrinkListView();
    }

    private void updateTotalPrice(ArrayList<Drink> drinkOrders) {

        int total = 0;
        for (Drink drink: drinkOrders) {
            total += (drink.mediumCupPrice * drink.num);
        }
        textViewPrice.setText(String.valueOf(total));
    }

    private void setDrinkData() {
        for (int i = 0 ; i < drinkNames.length; i++) {
            Drink drink = new Drink(drinkNames[i], mediumCupPrice[i], largeCupPrice[i], 0, imageId[i]);
            drinks.add(drink);
        }
    }

    private void setupDrinkListView() {
        DrinkAdapter adapter = new DrinkAdapter(this, drinks);
        listViewDrinkList.setAdapter(adapter);
    }

    public void onClickOKButton(View view) {
        showOrderDialog();
    }

    public void showOrderDialog() {
        String totalPrice = textViewPrice.getText().toString();
        String orderList="";
        for (Drink drink : drinkOrders) {
            orderList += drink.getOrderedFormat();
        }

        final String finalOrderList = orderList;
        new AlertDialog.Builder(this)
                .setTitle("您的訂單是：")
                .setMessage("訂單總金額:" + totalPrice +"\n"
                                         + orderList)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "訂購完成", Toast.LENGTH_SHORT).show();
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
                })
                .setNegativeButton("稍後決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "幫你存到購物車囉!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetOrderList();
                        Toast.makeText(getApplicationContext(), "歡迎再看看歐", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
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
