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
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;

    ArrayList<String> drinks = new ArrayList<>();

    String drinkName = "Black Tea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        setupListView();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String s = textView.getText().toString();
                if (checkedId == R.id.blackTeaRadioButton) {
                    drinkName = "Black Tea";
                } else if (checkedId == R.id.greenTeaRadioButton) {
                    drinkName = "Green Tea";
                }
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

    private void setupListView() {
        //String[] data = new String[]{"Green tea","Black tea", "Oolong tea", "White tea", "Pu-erh tea ", "Peach tea"};
        ArrayAdapter<String> adpter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drinks);
        listView.setAdapter(adpter);
    }

    public void onClickFunction(View view) {
        changeTextView();
    }

    public void changeTextView() {
        String note = editText.getText().toString();
        drinks.add(drinkName);

        textView.setText(drinkName);
        editText.setText("");
        setupListView();
    }

}
