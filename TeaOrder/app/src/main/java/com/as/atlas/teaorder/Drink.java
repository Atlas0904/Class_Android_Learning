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
    public final static String DRINK_MEDIUM_PRICE = "mPrice";
    public final static String DRINK_LARGE_PRICE = "lPrice";
    private static final String DRINK_IMAGE = "image";

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


    public String getName() { return getString(DRINK_NAME); }
    public void setName(String name) { put(DRINK_NAME, name); }

    public int getmPrice(){  return  getInt(DRINK_MEDIUM_PRICE);}
    public void setmPrice(int mPrice) {
        put(DRINK_MEDIUM_PRICE, mPrice);
    }

    public int getlPrice(){  return  getInt(DRINK_LARGE_PRICE);}
    public void setlPrice(int lPrice) {
        put(DRINK_LARGE_PRICE, lPrice);
    }

    public void setImage(ParseFile file) { put(DRINK_IMAGE, file); }
    public ParseFile getImage() { return getParseFile(DRINK_IMAGE); }

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
            jsonObject.put(DRINK_MEDIUM_PRICE, getmPrice());
            jsonObject.put(DRINK_LARGE_PRICE, getlPrice());
            jsonObject.put(DRINK_IMAGE, getImage());

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
