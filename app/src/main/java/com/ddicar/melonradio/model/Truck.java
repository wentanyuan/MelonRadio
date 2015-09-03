package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen on 15/9/3.
 */
public class Truck {

    public String plate;

    public String brand;

    public String model;

    public double weight;

    public double volume;

    public double length;

    public Truck(JSONObject json) {
        try {
            plate = json.getString("plate");
            brand = json.getString("brand");
            model = json.getString("model");
            weight = json.getDouble("weight");
            volume = json.getDouble("volume");
            length = json.getDouble("length");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String parameters() {
        return length + "米 " + weight + "吨 " + volume + "升";
    }
}
