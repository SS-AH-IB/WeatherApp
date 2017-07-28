package com.unideveloper.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by Subhajit Mondal on 24-07-2017.
 */

public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature=data.optString("temperature");
    }
}
