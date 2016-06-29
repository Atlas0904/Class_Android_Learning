package com.as.atlas.teaorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class OrderDetailActivity extends AppCompatActivity {

    TextView textViewNote;
    TextView textViewMenuResult;
    TextView textViewStoreInfo;
    ImageView staticMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        if (note != null) Log.d("debug", note);
        if (menuResults != null) Log.d("debug", menuResults);
        if (storeInfo != null) Log.d("debug", storeInfo);

        textViewNote = (TextView) findViewById(R.id.textViewNote);
        textViewMenuResult = (TextView) findViewById(R.id.textViewMenuResult);
        textViewStoreInfo = (TextView) findViewById(R.id.textViewStoreInfo);
        staticMap = (ImageView) findViewById(R.id.imageView);

        if (note != null) {
            textViewNote.setText(note);
        }

        if (storeInfo != null) {
            textViewStoreInfo.setText(storeInfo);
        }

        String text = "";
        if (menuResults != null) {
            try {
                JSONArray jsonArray = new JSONArray(menuResults);
                for (int i = 0 ; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String mediumNumber = String.valueOf(obj.getInt(DrinkOrder.MEDIUM_CUP_NUM));
                    String largeMNumber = String.valueOf(obj.getInt(DrinkOrder.LARGE_CUP_NUM));

                    text += obj.getString(DrinkOrder.DRINK_NAME) + " :大杯" + largeMNumber + "杯" + " 中杯" + mediumNumber + "杯\n";
                    Log.d("Atlas", "text=" + text);
                }

            } catch (JSONException e) {
                Log.d("Atlas", "e:" + e.getMessage());
                e.printStackTrace();
            }
            textViewMenuResult.setText(text);
        }


        String[] storeInfos = storeInfo.split(",");
        if (storeInfos != null && storeInfos.length > 1) {   // 店員, 地址
            String addr = storeInfos[1];
            (new GeoCodingTask(staticMap)).execute(addr);
        }

    }

    private static class GeoCodingTask extends AsyncTask<String, Void, Bitmap> {

        WeakReference<ImageView> imageViewWeakReference;

        public GeoCodingTask(ImageView imageView) {
            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String addr = params[0];
            Log.d("Atlas", "doInBackground() addr:" + addr);
            double[] latlng = Utils.getLatLngFromGoogleMapAPI(addr);
            if (latlng != null) {
                Log.d("Atlas", String.valueOf(latlng[0]));
                Log.d("Atlas", String.valueOf(latlng[1]));
            }
            return Utils.getStaticMap(latlng);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = imageViewWeakReference.get();
            imageView.setImageBitmap(bitmap);
        }
    }
}
