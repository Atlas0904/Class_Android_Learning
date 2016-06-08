package com.as.atlas.jsoupdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText editTextInputUrl;
    TextView textViewFetchResult;
    Button buttonFetchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInputUrl = (EditText) findViewById(R.id.editTextInputUrl);
        textViewFetchResult = (TextView) findViewById(R.id.editTextFetchResult);
        buttonFetchData = (Button) findViewById(R.id.buttonFetchData);

        buttonFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editTextInputUrl.getText().toString();
                (new ParseURL()).execute(new String[]{url});
            }
        });

    }

    public class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buffer = new StringBuffer();
            try {
                Log.d("JSwa", "Connecting to ["+params[0]+"]");
                Document doc = Jsoup.connect(params[0]).get();
                Log.d("JSwa", "Connected to ["+params[0]+"]");

                // Get document (HTML page) title
                String title = doc.title();
                Log.d("JSwA", "Title ["+title+"]");

                // Get meta info
                Elements metaElems = doc.select("meta");
                buffer.append("META DATA\n");
                for (Element metaElem : metaElems) {
                    String name = metaElem.attr("name");
                    String content = metaElem.attr("content");
                    buffer.append("name ["+name+"] - content ["+content+"] \n");
                }

                Elements topicList = doc.select("h2.topic");
                buffer.append("Topic list\n");
                for (Element topic : topicList) {
                    String data = topic.text();

                    buffer.append("Data [" + data + "] rn");
                }

            } catch (IOException e) {
                e.printStackTrace();
                buffer.append("Warring:" + e.toString());
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textViewFetchResult.setText(s);
        }
    }
}
