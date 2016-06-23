package com.as.atlas.teaorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;
    public static final String SHARE_PREFERENCES_EDIT_VIEW = "edit_view";
    public static final String SHARE_PREFERENCES_SPINNER = "spinner";

    TextView textView;
    Button button;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    List<Order> orders = new ArrayList<>();
    String drinkName = "Black Tea";
    String menuResults = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate");
        super.onCreate(savedInstanceState);

        // Test parse server
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("Test123", "NTU class 267");
        testObject.saveInBackground();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TestObject");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e== null) {
                    for (ParseObject object: objects) {
                        Toast.makeText(MainActivity.this, object.getString("foo"), Toast.LENGTH_LONG).show();
                        Log.d("Atlas", "object:" + object.getString("foo"));
                    }
                }
            }
        });

        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);

        sharedPreferences = getSharedPreferences("setting", Context.MODE_APPEND);
        editor = sharedPreferences.edit();


        setupOrdersData();
        setupListView();
        setupSpinner();

        editText.setText(sharedPreferences.getString(SHARE_PREFERENCES_EDIT_VIEW, ""));   // 2nd parameter default
        int spinnerPosition = adapter.getPosition(sharedPreferences.getString(SHARE_PREFERENCES_SPINNER,""));
        spinner.setSelection(spinnerPosition);



        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton r = (RadioButton) findViewById(checkedId);
                drinkName = r.getText().toString();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();
                editor.putString(SHARE_PREFERENCES_EDIT_VIEW, text);
                editor.apply();   // apply 才會寫進去

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClickFunction(v);
                    return true;
                    // 一定要 return true 不然 enter 會往下傳 textView 會多空白
                }
                return false;
            }
        });

        textView.setText(sharedPreferences.getString(SHARE_PREFERENCES_EDIT_VIEW,""));
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("textView", s.toString());
                editor.apply();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(SHARE_PREFERENCES_SPINNER, parent.getSelectedItem().toString());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setupSpinner() {
        String[] data = getResources().getStringArray(R.array.storeInfo);  // R.array
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(adapter);

        // add HW4 here
    }

    private void setupListView() {
//        String[] data = new String[]{"Green tea","Black tea", "Oolong tea", "White tea", "Pu-erh tea ", "Peach tea"};
//        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        // Customize list view
        /*
        List<Map<String, String>> data = new ArrayList<>();
        for (int i=0; i < orders.size(); i++) {
            Order order = orders.get(i);
            Map<String, String> item = new HashMap<>();
            item.put("note", order.note);
            item.put("drinkName", order.drinkName);
            data.add(item);
        }
        String[] from = {"note", "drinkName"};
        int[] to = {R.id.noteTextView, R.id.drinkNameTextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_view_order_item, from, to);
        */

        OrderAdapter adapter = new OrderAdapter(this, orders);
        listView.setAdapter(adapter);
    }

    private void setupOrdersData() {
//        String content = Utils.readFile(this, "history");
//        String[] datas = content.split("\n");
//        for (int i = 0; i < datas.length; i++) {
//            Order order = Order.newInstanceWithData(datas[i]);
//            if (order != null) {
//                orders.add(order);
//            }
//        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();


        // 實作 callback 並且注意帶 Order type
        final FindCallback<Order> callback = new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if (e == null) {
                    orders = objects;
                    setupListView();
                }
            }
        };

        if (info != null && info.isConnected()) {
            Order.getOrdersFromRemote(new FindCallback<Order>() {
                @Override
                public void done(List<Order> objects, ParseException e) {
                    if (e != null) {
                        Toast.makeText(MainActivity.this, "Sync failed", Toast.LENGTH_LONG).show();
                        Order.getQuery().fromLocalDatastore().findInBackground(callback);
                    }
                    else {
                        callback.done(objects, e);
                    }
                }
            });
        } else {
            Order.getQuery().fromLocalDatastore().findInBackground(callback);
        }

    }

    public void onClickFunction(View view) {
        changeTextView();
    }

    public void changeTextView() {

        Order order;
        String note = editText.getText().toString();
        String storeInfo = (String) spinner.getSelectedItem();
        //order = new Order(note, menuResult, storeInfo);
        order = new Order();
        order.setNote(note);
        order.setMenuResults(menuResults);
        order.setStoreInfo(storeInfo);
        order.pinInBackground();  // 上傳前存到 local: 用 pinInBackGroud
        order.saveEventually();

        orders.add(order);

        Utils.writeFile(this, "history", order.getJsonObject().toString());

        //textView.setText(menuResult);
        //editText.setText(note);
        menuResults = "";
        setupListView();
    }


    public void showDrinkMenu(View view) {  // public then button can call
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);  // 塞 class name 就好, 不用 new
        startActivityForResult(intent, REQUEST_CODE_DRINK_MENU_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //從其他 Activity回來
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //textView.setText(data.getStringExtra("result"));
                menuResults = data.getStringExtra("result");
                log(menuResults);
                Toast.makeText(this, "完成菜單", Toast.LENGTH_LONG).show();
                changeTextView();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "取消菜單", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Life cycle
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
