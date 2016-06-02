package com.as.atlas.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button button;
    EditText editText;
    RadioGroup radioGroup;
    CheckBox checkBox;
    String selectedSexual = "Male";
    String name = "";
    String sexual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.buttonShow);
        editText = (EditText) findViewById(R.id.editText);
        radioGroup =  (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox) findViewById(R.id.hideSexCheckBox);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String s = textView.getText().toString();
                if (checkedId == R.id.MaleRadioButton) {
                    selectedSexual = "Male";
                } else if (checkedId == R.id.FemaleRadioButton) {
                    selectedSexual = "Female";
                }
            }

        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeTextView();
            }
        });


        Log.d("debug", "Hello world.");
    }
    public void onClickFunction(View view) {
        name = editText.getText().toString();
        sexual = selectedSexual;
        changeTextView();
        editText.setText("");
    }
    public void changeTextView() {
        String content = "";

        if ("".equals(name)) return;

        if (checkBox.isChecked()) {
            content = name;
        } else {
            content = name + " Sexual:" + sexual;
        }
        textView.setText(content);
    }
}
