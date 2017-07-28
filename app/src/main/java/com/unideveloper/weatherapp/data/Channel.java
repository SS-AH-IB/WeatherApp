package com.unideveloper.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by Subhajit Mondal on 24-07-2017.
 */

public class Channel implements JSONPopulator{
    private Item item;
    private Units units;

    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    @Override
    public void populate(JSONObject data) {
        units=new Units();
        units.populate((data.optJSONObject("units")));
        item=new Item();
        item.populate(data.optJSONObject("item"));
    }
}
