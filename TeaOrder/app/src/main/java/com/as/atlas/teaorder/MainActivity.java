package com.as.atlas.teaorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;

    TextView textView;
    Button button;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    ArrayList<Order> orders = new ArrayList<>();

    String drinkName = "Black Tea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate");
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);

        setupListView();
        setupSpinner();

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
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClickFunction(v);
                    return true;
                    // 一定要 return true 不然 enter 會往下傳 textView 會多空白
                }
                return false;
            }
        });
    }

    private void setupSpinner() {
        String[] data = getResources().getStringArray(R.array.storeInfo);  // R.array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(adapter);
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

    public void onClickFunction(View view) {
        changeTextView();
    }

    public void changeTextView() {

        String note = editText.getText().toString();
        String storeInfo = (String) spinner.getSelectedItem();
        orders.add(new Order(drinkName, note, storeInfo));

        textView.setText(drinkName);
        editText.setText("");
        setupListView();
    }

    public void showDrinkMenu(View view) {  // public then button can call
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);  // 塞 class name 就好, 不用 new
        startActivityForResult(intent, REQUEST_CODE_DRINK_MENU_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                textView.setText(data.getStringExtra("result"));
                Toast.makeText(this, "完成菜單", Toast.LENGTH_LONG).show();
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
