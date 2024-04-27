package com.example.probka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class pollutionData {

    private String mIcon, mCO, mPM10, mPM2, mSO2, mNO2, mO3;
    private double mCondition;

    public static pollutionData fromJson(JSONObject jsonObject) {
        try {
            pollutionData pollutionD = new pollutionData();
            double pmPM10, pmPM2, pmCO, pmSO2, pmNO2, pmO3;
            JSONArray list = jsonObject.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject list_object = list.getJSONObject(i);
                pmPM10 = list_object.getJSONObject("components").getDouble("pm10");
                pollutionD.mPM10 = Double.toString(pmPM10);

                pollutionD.mCondition = list_object.getJSONObject("components").getDouble("pm2_5");
                pmPM2 = list_object.getJSONObject("components").getDouble("pm2_5");
                pollutionD.mPM2 = Double.toString(pmPM2);

                pollutionD.mIcon = updatePollutionIcon(pollutionD.mCondition);

                pmCO = list_object.getJSONObject("components").getDouble("co");
                pollutionD.mCO = Double.toString(pmCO);

                pmSO2 = list_object.getJSONObject("components").getDouble("so2");
                pollutionD.mSO2 = Double.toString(pmSO2);

                pmNO2 = list_object.getJSONObject("components").getDouble("no2");
                pollutionD.mNO2 = Double.toString(pmNO2);

                pmO3 = list_object.getJSONObject("components").getDouble("o3");
                pollutionD.mO3 = Double.toString(pmO3);
            }
            return pollutionD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

//    koniecznosc dostosowania interpretacji
    private static String updatePollutionIcon(double condition) {
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

    public String getmCO() {
        return mCO;
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmPM10() { return mPM10; }

    public String getmPM2() {return mPM2; }

    public String getmSO2() {return mSO2; }

    public String getmNO2() {return mNO2; }

    public String getmO3() {return mO3; }
}
