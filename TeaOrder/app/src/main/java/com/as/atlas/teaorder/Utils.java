package com.as.atlas.teaorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by atlas on 2016/6/20.
 */
public class Utils {
    public static final String UTILS_ORDER_LIST_HISTORY = "history";
    private static final int OUTSTREAM_BUFFER_SIZE = 1024;
    private static final String ENCODE_UTF8 = "utf-8";
    private static final String PREFIX_GOOGLE_MAP_API_FOR_ADDRESS = "http://maps.google.com.tw/maps/api/geocode/json?address=";
    private static final String RESPONSE_STATUS = "status";
    private static final String RESPONSE_STATUS_OK = "OK";
    private static final String RESPONSE_RESULTS = "results";
    private static final String RESPONSE_GEOMETRY = "geometry";
    private static final String RESPONSE_LOCATION = "location";
    private static final String RESPONSE_LOCATION_LAT = "lat";
    private static final String RESPONSE_LOCATION_LNG = "lng";
    private static final String PREFIX_GOOGLE_MAP_API_FOR_STATIC_MAP = "http://maps.google.com/maps/api/staticmap?center=";
    private static final String POSTFIX_GOOGLE_MAP_API_FOR_STATIC_MAP = "&size=640x400&zoom=17";

    public static void writeFile(Context context, String filename, String content) {
        try {
            FileOutputStream fos = (FileOutputStream) context.openFileOutput(filename, Context.MODE_APPEND);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            byte[] buffer = new byte[1024];
            fis.read(buffer, 0, buffer.length);
            fis.close();
            String string = new String(buffer);
            return string;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] urlToByte(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[OUTSTREAM_BUFFER_SIZE];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            return byteArrayOutputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static double[] getLatLngFromGoogleMapAPI(String addr) {

        try {
            addr = URLEncoder.encode(addr, ENCODE_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiUrl = PREFIX_GOOGLE_MAP_API_FOR_ADDRESS + addr;
        byte[] bytes = Utils.urlToByte(apiUrl);

        if (bytes == null)  return null;

        try {
            JSONObject obj = new JSONObject(new String(bytes));

            if (obj.getString(RESPONSE_STATUS).equals(RESPONSE_STATUS_OK)) {
                JSONObject location = obj.getJSONArray(RESPONSE_RESULTS)
                        .getJSONObject(0)
                        .getJSONObject(RESPONSE_GEOMETRY)
                        .getJSONObject(RESPONSE_LOCATION);

                double lat = location.getDouble(RESPONSE_LOCATION_LAT);
                double lng = location.getDouble(RESPONSE_LOCATION_LNG);

                return new double[] {lat, lng};
            }

        } catch (JSONException e) {
            Log.d("Atlas", "getLatLngFromGoogleMapAPI() e:" + e.getMessage());
            e.printStackTrace();
        }


        return null;
    }

    public static Bitmap getStaticMap(double[] latlng) {
        String center = String.valueOf(latlng[0]) + "," + String.valueOf(latlng[1]);
        String staticMapUrl = PREFIX_GOOGLE_MAP_API_FOR_STATIC_MAP + center + POSTFIX_GOOGLE_MAP_API_FOR_STATIC_MAP;

        byte[] bytes = Utils.urlToByte(staticMapUrl);
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }

        return null;
    }

}
