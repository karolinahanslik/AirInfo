package com.example.probka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class pollenData {

    private String mIcon, mTrawa, mPlesn, mDrzewa, mAmbrozja;
    private double mCondition;

    public static pollenData fromJson(JSONObject jsonObject) {
        try {
            pollenData pollenD = new pollenData();
            double pmTrawa, pmPlesn, pmDrzewa, pmAmbrozja;
            JSONArray list = jsonObject.getJSONArray("DailyForecasts");
            for (int i = 0; i < list.length(); i++) {
                JSONObject list_object = list.getJSONObject(i);
                JSONArray allPollenArray = list_object.getJSONArray("AirAndPollen");

                JSONObject trawaJSONObject = allPollenArray.getJSONObject(1);
                pmTrawa = trawaJSONObject.getDouble("Value");
                pollenD.mTrawa = Double.toString(pmTrawa);

                JSONObject plesnJSONObject = allPollenArray.getJSONObject(2);
                pmPlesn = plesnJSONObject.getDouble("Value");
                pollenD.mPlesn = Double.toString(pmPlesn);

                JSONObject drzewaJSONObject = allPollenArray.getJSONObject(4);
                pmDrzewa = drzewaJSONObject.getDouble("Value");
                pollenD.mDrzewa = Double.toString(pmDrzewa);

                JSONObject ambrozjaJSONObject = allPollenArray.getJSONObject(1);
                pmAmbrozja = ambrozjaJSONObject.getDouble("Value");
                pollenD.mAmbrozja = Double.toString(pmAmbrozja);
            }
            return pollenD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    //    koniecznosc dostosowania interpretacji
    private static String updatePollenIcon(double condition) {
        if (condition >= 0 && condition <= 15) {
            return "good";
        } else if (condition > 15 && condition <= 30) {
            return "okay";
        }else if(condition > 30 && condition <=50){
            return "not_good";
        }else if (condition > 50) {
            return "bad";
        }
        return "Nieznane warunki.";
    }

    public String getmTrawa() {
        return mTrawa;
    }

//    public String getmIcon() {
//        return mIcon;
//    }

    public String getmPlesn() { return mPlesn; }

    public String getmDrzewa() {return mDrzewa; }

    public String getmAmbrozja() {return mAmbrozja; }
}