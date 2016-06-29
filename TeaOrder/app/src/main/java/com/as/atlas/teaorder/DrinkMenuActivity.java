package com.as.atlas.teaorder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity
        implements DrinkOrderDialogFragment.OnDrinkOrderListener {


    // UI
    ListView listViewDrinkList;
    TextView textViewPrice;

    List<Drink> drinks = new ArrayList<>();
    ArrayList<DrinkOrder> drinkOrders = new ArrayList<>();

    // Set data
    private static final String TAG = DrinkMenuActivity.class.getName();
    String[] names = {"冬瓜紅茶", "玫瑰鹽奶蓋紅茶", "珍珠紅茶拿鐵", "紅茶拿鐵"};
    int[] mediumCupPrice = {25, 35, 45, 55};
    int[] largeCupPrice = {35, 45, 55, 65};
    int[] imageId = {
            R.drawable.drink_tea,
            R.drawable.drink_cola,
            R.drawable.drink_milk_tea,
            R.drawable.drink_bubble_milk_tea};

    @Override
    public void onDrinkOrderFinished(DrinkOrder drinkOrder) {

        for (int i = 0; i < drinkOrders.size(); i++) {
            if (drinkOrders.get(i).drinkName.equals(drinkOrder.drinkName)) {
                drinkOrders.set(i, drinkOrder);
                updateTotalPrice();
            }
        }
        drinkOrders.add(drinkOrder);
        updateTotalPrice();

    }

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
//                DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
//                Drink drink = (Drink) drinkAdapter.getItem(position);
//                if (drinkOrders.contains(drink)) {
//                    drink.addOne();
//                    log("drink add one. drink=" + drink);
//                } else {
//                    drinkOrders.add(drink);
//                    log("drink no contain. drink=" + drink);
//                }
//                setupDrinkListView();
//                updateTotalPrice(drinkOrders);

                Drink drink = (Drink) parent.getAdapter().getItem(position);
                showDetailCheckMenu(drink);

            }
        });

        listViewDrinkList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
                Drink drink = (Drink) drinkAdapter.getItem(position);
                if (drinkOrders.contains(drink)) {
//                    drink.resetNum();
                    log("drink num reset. drink=" + drink);
                }
                setupDrinkListView();
                updateTotalPrice();

                return true;
            }
        });
    }

    private void showDetailCheckMenu(Drink drink) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        DrinkOrder drinkOrder = new DrinkOrder();
        boolean found = false;

        for (DrinkOrder order: drinkOrders) {
            if (order.drinkName.equals(drink.getName())) {
                drinkOrder = order;
                found = true;
                break;
            }
        }

        if (!found) {
            drinkOrder.mediumCupPrice = drink.getMediumCupPrice();
            drinkOrder.largeCupPrice = drink.getLargeCupPrice();
            drinkOrder.drinkName = drink.getName();
            Log.d("Atlas", "showDetailCheckMenu() drink:" + drink + " drinkOrder:" + drinkOrder);
        }

        DrinkOrderDialogFragment drinkOrderDialogFragment = DrinkOrderDialogFragment.newInstance(drinkOrder);
        drinkOrderDialogFragment.show(ft, "DrinkOrderDialog");

//        // 1st demo
//        ft.replace(R.id.drinkMenuRelativeLayout, dialogFragment); // 因為有可能重複call
//        ft.addToBackStack(null);
//
//        ft.commit();

    }

//    private void resetOrderList() {
//        for (Drink drink: drinkOrders) {
//            drink.resetNum();
//        }
//        updateTotalPrice(drinkOrders);
//        setupDrinkListView();
//    }

    private void updateTotalPrice() {
        int total = 0;
        for (DrinkOrder drinkOrder: drinkOrders) {
            total += (drinkOrder.mediumCupPrice * drinkOrder.mediumCupNum + drinkOrder.largeCupPrice * drinkOrder.largeCupNum);
            Log.d("Atlas", "updateTotalPrice() drinkOrder:" + drinkOrder);
        }
        Log.d("Atlas", "total price:" + total);
        textViewPrice.setText(String.valueOf(total));
    }

    private void setDrinkData() {

        Drink.getQuery().findInBackground(new FindCallback<Drink>() {
            @Override
            public void done(final List<Drink> objects, ParseException e) {
            if(e != null)
            {
                Drink.getQuery().fromLocalDatastore().findInBackground(new FindCallback<Drink>() {
                    @Override
                    public void done(List<Drink> objects, ParseException e) {
                        drinks = objects;
                        setupDrinkListView();
                    }
                });
            }
            else{
                ParseObject.unpinAllInBackground("Drink", new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject.pinAllInBackground(objects, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                drinks = objects;
                                setupDrinkListView();
                            }
                        });
                    }
                });
            }
            }
        });
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
//        for (Drink drink : drinkOrders) {
//            orderList += drink.getOrderedFormat();
//        }
//
//        final String finalOrderList = orderList;
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
                        for (DrinkOrder drink: drinkOrders) {
                            JSONObject obj = drink.getJsonData();
                            array.put(obj);
                        }

                        intent.putExtra("result", array.toString());
                        log(array.toString());
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
                        //resetOrderList();
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
