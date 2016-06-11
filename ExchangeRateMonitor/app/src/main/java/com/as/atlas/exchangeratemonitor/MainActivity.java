package com.as.atlas.exchangeratemonitor;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MSG_SET_EXCHANGE_MESSAGE = 0;
    private static String urlExRate = "http://rate.bot.com.tw/Pages/Static/UIP003.zh-TW.htm";
    private final static String HTML_TAG_CURRENCY = "td.titleleft";
    private final static String HTML_TAG_TIME = "td[style=width:326px;text-align:left;vertical-align:top;color:#0000FF;font-size:11pt;font-weight:bold;]";
    private final static String TAG = "Atlas";
    private final boolean DEBUG = false;


    private Document doc;
    List<RateItem> rateList;
    String updateTime;

    TextView textView;
    TextView textViewUpdateTime;
    ListView listViewExchangeRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Should use AsyncTask as solution, this is practice for Msg and Thread
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        textView = (TextView) findViewById(R.id.textView);
        if (DEBUG == false) textView.setVisibility(View.GONE);
        listViewExchangeRate = (ListView) findViewById(R.id.listView);

        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = MSG_SET_EXCHANGE_MESSAGE;
            handler.sendMessage(message);
        }
    };


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MSG_SET_EXCHANGE_MESSAGE == msg.what) {
                onConnect();
                setExchangeRateInfo();
                setUpdatedTime();
                updateListViewExchangeRateAdapter();
            }
        }
    };

    private void updateListViewExchangeRateAdapter() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        textViewUpdateTime = (TextView) findViewById(R.id.TextViewUpdateTime);
        textViewUpdateTime.setText(updateTime);

        listViewExchangeRate.setAdapter(new ListViewExchangeRateAdapter(this, rateList));

    }

    public void onConnect() {
        try {
            Log.d(TAG, "onConnect is on-going");
            doc = Jsoup.connect(urlExRate).get();
            Log.d(TAG, "onConnect done. doc:" + doc);
        } catch (IOException e) {
            e.printStackTrace();
            textView.setText(e.toString());
        }
    }

    public void setExchangeRateInfo() {
        if (doc == null) return;
        //String currency = doc.select(HTML_TAG_CURRENCY).text();  // text convert all element to String with " " separate

        rateList = new ArrayList<RateItem>();
        final Elements currencys = doc.select(HTML_TAG_CURRENCY);
        int i = 0;
        for (Element currency : currencys) {
            RateItem rateItem = new RateItem();
            rateItem.setCurrency(currency.text());

            if (i < doc.select("td.decimal").size()) {
                rateItem.setCashBuyRate(convertStringToDouble(doc.select("td.decimal").eq(i).text()));
                rateItem.setCashSoldRate(convertStringToDouble(doc.select("td.decimal").eq(i + 1).text()));
                rateItem.setSpotBuyRate(convertStringToDouble(doc.select("td.decimal").eq(i + 2).text()));
                rateItem.setSpotSoldRate(convertStringToDouble(doc.select("td.decimal").eq(i + 3).text()));
                i += 4;
            }
            rateList.add(rateItem);
            Log.d(TAG, "rateItem= " + rateItem);
        }
        StringBuffer result = new StringBuffer();
        for (int j = 0; j < rateList.size(); j++) {
            result.append(rateList.get(j)).append("\n");
        }
        Log.d(TAG, "result= " + result);
        textView.setText(result);

    }

    private void setUpdatedTime() {
        if (doc == null) {
            Log.d(TAG, "doc is null");
        }

        updateTime = doc.select(HTML_TAG_TIME).text();
        Log.d(TAG, "Current time= " + updateTime);
    }

    private double convertStringToDouble(String s) {
        // What should the default value be
        //double d = Double.MAX_VALUE;
        double d = 0.0;
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException e) {

        }
        return d;
    }
}
