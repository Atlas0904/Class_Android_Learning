package com.as.atlas.teaorder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atlas on 2016/6/6.
 */
public class Order {
    String note;
    String menuResult;
    String storeInfo;

    public Order() {

    }

    public Order(String note, String menuResult, String storeInfo) {
        this.note = note;
        this.menuResult = menuResult;
        this.storeInfo = storeInfo;
    }

    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("note", note);
            jsonObject.put("menuResult", menuResult);
            jsonObject.put("storeInfo", storeInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    public static Order newInstanceWithData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Order order = new Order();
            order.note = jsonObject.getString("note");
            order.menuResult = jsonObject.getString("menuResult");
            order.storeInfo = jsonObject.getString("storeInfo");

            return order;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

}
