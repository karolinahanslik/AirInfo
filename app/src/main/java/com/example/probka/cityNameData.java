package com.example.probka;

import org.json.JSONException;
import org.json.JSONObject;

public class cityNameData {

    private String mCityName;

    public static cityNameData fromJson(JSONObject jsonObject) {
        try {
            cityNameData cityNameD = new cityNameData();
            cityNameD.mCityName = jsonObject.getString("name");
            return cityNameD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getmCityName(){
        return mCityName;
    }
}
