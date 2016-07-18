package com.as.atlas.firebasedemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by atlas on 2016/7/14.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Person {
    //name and address string
    private String name;
    private String address;

    public Person() {
      /*Blank default constructor essential for Firebase*/
    }

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //Getters and setters
    public String getName() { return name; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return "{name="+ name + ", address=" + address + "}";
    }
}
