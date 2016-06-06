package com.as.atlas.teaorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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

}
