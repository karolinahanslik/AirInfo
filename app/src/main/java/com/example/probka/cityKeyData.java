package com.example.probka;

import static java.lang.Math.round;

import org.json.JSONException;
import org.json.JSONObject;

public class cityKeyData {

    private String mKeyData;

    public static cityKeyData fromJson(JSONObject jsonObject) {
        try {
            double pcityKey;

            cityKeyData cityKeyD = new cityKeyData();
            pcityKey = jsonObject.getDouble("Key");
            String pom = Double.toString(pcityKey);
            cityKeyD.mKeyData = pom.substring(0, pom.length() - 2);

            return cityKeyD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getmCityKey(){
        return mKeyData;
    }
}
