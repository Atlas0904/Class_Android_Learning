package com.as.atlas.teaorder;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by atlas on 2016/6/6.
 */
@ParseClassName("Order")
public class Order extends ParseObject {

//    String note;
//    String menuResult;
//    String storeInfo;


//    public Order(String note, String menuResult, String storeInfo) {
//        this.note = note;
//        this.menuResult = menuResult;
//        this.storeInfo = storeInfo;
//    }

    public String getNote() { return getString("note"); }
    public void setNote(String note) { put("note", note); }

    public String getMenuResults() {
        String menuResults = getString("menuResults");
        if (menuResults == null) {
            return "";
        }
        return menuResults;
    }
    public void setMenuResults(String menuResults) { put("menuResults", menuResults); }

    public String getStoreInfo() { return getString("storeInfo"); }
    public void setStoreInfo(String storeInfo) { put("storeInfo", storeInfo); }

    public static ParseQuery<Order> getQuery() { return ParseQuery.getQuery(Order.class); }

    public static void getOrdersFromRemote(final FindCallback<Order> callback) {
        Log.d("Atlas", "getOrdersFromRemote enter");
        getQuery().orderByDescending("createdAt")
                .findInBackground(new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                Log.d("Atlas", "getOrdersFromRemote e:" + e);
                if (e == null) {
                    ParseObject.pinAllInBackground(objects); // 東西存在local 端

                    Log.d("Atlas", "getOrdersFromRemote objects= " + objects);
                }
                Log.d("Atlas", "getOrdersFromRemote objects:" + objects);
                callback.done(objects, e);

            }
        });
    }

    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("note", getNote());
            jsonObject.put("menuResults", getMenuResults());
            jsonObject.put("storeInfo", getStoreInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    public static Order newInstanceWithData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Order order = new Order();
            order.setNote(jsonObject.getString("note"));
            order.setMenuResults(jsonObject.getString("menuResults"));
            order.setStoreInfo(jsonObject.getString("storeInfo"));

            return order;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

}
