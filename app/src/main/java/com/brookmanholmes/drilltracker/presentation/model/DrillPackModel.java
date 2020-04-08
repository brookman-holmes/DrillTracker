package com.brookmanholmes.drilltracker.presentation.model;

import androidx.annotation.NonNull;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class DrillPackModel extends Model {
    public String name;
    public String price;
    public String description;
    public String sku;
    public String url;
    public boolean purchased = false;
    public String token;


    @NonNull
    @Override
    public String toString() {
        return "DrillPackModel{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", sku='" + sku + '\'' +
                ", url='" + url + '\'' +
                ", purchased=" + purchased +
                ", token='" + token + '\'' +
                '}';
    }
}
