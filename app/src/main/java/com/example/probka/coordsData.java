package com.example.probka;

import org.json.JSONException;
import org.json.JSONObject;

public class coordsData {
    private String mLat;
    private String mLon;

    public static coordsData fromJson(JSONObject jsonObject) {
        try {
            coordsData coordsD = new coordsData();
            coordsD.mLat = jsonObject.getJSONObject("coord").getString("lat");
            coordsD.mLon = jsonObject.getJSONObject("coord").getString("lon");
            return coordsD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getmLat(){
        return mLat;
    }
    public String getmLon(){
        return mLon;
    }
}
