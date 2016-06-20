package com.as.atlas.teaorder;

/**
 * Created by atlas on 2016/6/16.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 紀錄訂單
 */
public class DrinkOrder {


    public static final String DRINK_NAME = "drinkName";
    public static final String ICE = "ice";
    public static final String SUGAR = "sugar";
    public static final String NOTE = "note";
    public static final String MEDIUM_CUP_NUM = "mediumCupNum";
    public static final String LARGE_CUP_NUM = "largeCupNum";
    public static final String MEDIUM_CUP_PRICE = "mediumCupPrice";
    public static final String LARGE_CUP_PRICE = "largeCupPrice";



    String drinkName;
    String ice = "正常";
    String sugar = "正常";
    String note = "";
    int mediumCupNum = 0;
    int largeCupNum = 0;
    int mediumCupPrice;
    int largeCupPrice;

    public JSONObject getJsonData() {   // 透過 Json 封裝 data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DRINK_NAME, drinkName);
            jsonObject.put(ICE, ice);
            jsonObject.put(SUGAR, sugar);
            jsonObject.put(NOTE, note);
            jsonObject.put(MEDIUM_CUP_NUM, mediumCupNum);
            jsonObject.put(LARGE_CUP_NUM, largeCupNum);
            jsonObject.put(MEDIUM_CUP_PRICE, mediumCupPrice);
            jsonObject.put(LARGE_CUP_PRICE, largeCupPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static DrinkOrder newInstanceWithData(String data) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            DrinkOrder drinkOrder = new DrinkOrder();
            drinkOrder.drinkName = jsonObject.getString(DRINK_NAME);
            drinkOrder.ice = jsonObject.getString(ICE);
            drinkOrder.sugar = jsonObject.getString(SUGAR);
            drinkOrder.note = jsonObject.getString(NOTE);
            drinkOrder.mediumCupNum = jsonObject.getInt(MEDIUM_CUP_NUM);
            drinkOrder.largeCupNum = jsonObject.getInt(LARGE_CUP_NUM);
            drinkOrder.mediumCupPrice = jsonObject.getInt(MEDIUM_CUP_PRICE);
            drinkOrder.largeCupPrice = jsonObject.getInt(LARGE_CUP_PRICE);

            return drinkOrder;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(DrinkOrder.class.getName(), e.getMessage());
        }
        return null;

    }

}
