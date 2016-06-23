package com.as.atlas.teaorder;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atlas on 2016/6/13.
 */

@ParseClassName("Drink")
public class Drink extends ParseObject{

    public final static String DRINK_NAME = "name";
    public final static String DRINK_MEDIUM_CUP_PRICE = "medium_cup_price";
    public final static String DRINK_LARGE_CUP_PRICE = "large_cup_price";

//    String name;
//    int mediumCupPrice;
//    int largeCupPrice;
//    int num;
    int imageId;

//    public Drink(String name, int mediumCupPrice, int largeCupPrice, int num, int imageId) {
//        this.name = name;
//        this.mediumCupPrice = mediumCupPrice;
//        this.largeCupPrice = largeCupPrice;
//        this.num = num;
//        this.imageId = imageId;
//
//    }


    public String getName() { return getString("name"); }
    public void setName(String name) { put("name", name); }

    public int getMediumCupPrice() { return getInt("mediumCupPrice"); }
    public void setMediumCupPrice(int mediumCupPrice) { put("mediumCupPrice", mediumCupPrice); }

    public int getLargeCupPrice() { return getInt("largeCupPrice"); }
    public void setLargeCupPrice(int largeCupPrice) { put("largeCupPrice", largeCupPrice); }

    public void setImage(ParseFile file) { put("image", file); }
    public ParseFile getImage() { return getParseFile("image"); }

    public static ParseQuery<Drink> getQuery() { return ParseQuery.getQuery(Drink.class); }



//    public void addOne() {
//        this.num++;
//    }
//
//    public void addNum(int num) {
//        this.num += num;
//    }
//
//    public void resetNum() {
//        this.num = 0;
//    }

    public JSONObject getJsonData() {   // 透過 Json 封裝 data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DRINK_NAME, getName());
            jsonObject.put(DRINK_MEDIUM_CUP_PRICE, getMediumCupPrice());
            jsonObject.put(DRINK_LARGE_CUP_PRICE, getLargeCupPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

//    public String getOrderedFormat() {
//        return "" + name + "\t數量:" + num + "\n";
//    }
//
//    public String toString() {
//        return "Drink: " +
//                "Name: " + name +
//                "Price(M):" + mediumCupPrice +
//                "Price(L):" + largeCupPrice +
//                "Numbers:" + num +
//                "(imageId:)" + imageId;
//    }
}
