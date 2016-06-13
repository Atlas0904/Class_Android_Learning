package com.as.atlas.teaorder;

/**
 * Created by atlas on 2016/6/13.
 */
public class Drink {
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
}
