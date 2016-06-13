package com.as.atlas.teaorder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atlas on 2016/6/13.
 */
public class Drink {

    public final static String DRINK_NAME = "name";
    public final static String DRINK_MEDIUM_CUP_PRICE = "medium_cup_price";
    public final static String DRINK_LARGE_CUP_PRICE = "large_cup_price";

    String name;
    int mediumCupPrice;
    int largeCupPrice;
    int imageId;

    public Drink(String name, int mediumCupPrice, int largeCupPrice, int imageId) {
        this.name = name;
        this.mediumCupPrice = mediumCupPrice;
        this.largeCupPrice = largeCupPrice;
        this.imageId = imageId;
    }

    public JSONObject getJsonData() {   // 透過 Json 封裝 data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DRINK_NAME, name);
            jsonObject.put(DRINK_MEDIUM_CUP_PRICE, mediumCupPrice);
            jsonObject.put(DRINK_LARGE_CUP_PRICE, largeCupPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
