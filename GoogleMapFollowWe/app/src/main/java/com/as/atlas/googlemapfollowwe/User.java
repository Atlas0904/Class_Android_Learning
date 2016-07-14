package com.as.atlas.googlemapfollowwe;

/**
 * Created by atlas on 2016/7/13.
 */
public class User {

    public String name;
    public double currentLat;
    public double currentLng;

    public double previousLat;
    public double previousLng;

    ArriveMethod arriveMethod;

    enum ArriveMethod{
        WALKING, BICYCLE, CAR
    }

}
