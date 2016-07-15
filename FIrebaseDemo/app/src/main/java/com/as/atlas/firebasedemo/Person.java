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
    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("address")
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "{name="+ name + ", address=" + address + "}";
    }
}
