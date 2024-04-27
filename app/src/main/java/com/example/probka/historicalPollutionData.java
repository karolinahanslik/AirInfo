package com.example.probka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class historicalPollutionData {

    //float[] wPM10 = new float[24];
    String[] wPM10 = new String[24];
    //String wPM10;
    String[] wPM2 = new String[24];
    String[] wCO = new String[24];
    String[] wSO2 = new String[24];
    String[] wNO2 = new String[24];
    String[] wO3 = new String[24];
    //private String mIcon, mCO, mPM10, mPM2, mSO2, mNO2, mO3;
    private double mCondition;


    public static historicalPollutionData fromJson(JSONObject jsonObject) {
        try {
            historicalPollutionData pollutionD = new historicalPollutionData();
            double pmPM10, pmPM2, pmCO, pmSO2, pmNO2, pmO3;
            JSONArray list = jsonObject.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject list_object = list.getJSONObject(i);
                pmPM10 = list_object.getJSONObject("components").getDouble("pm10");
                pollutionD.wPM10[i] = Double.toString(pmPM10);

                pollutionD.mCondition = list_object.getJSONObject("components").getDouble("pm2_5");
                pmPM2 = list_object.getJSONObject("components").getDouble("pm2_5");
                pollutionD.wPM2[i] = Double.toString(pmPM2);

                pmCO = list_object.getJSONObject("components").getDouble("co");
                pollutionD.wCO[i] = Double.toString(pmCO);

                pmSO2 = list_object.getJSONObject("components").getDouble("so2");
                pollutionD.wSO2[i] = Double.toString(pmSO2);

                pmNO2 = list_object.getJSONObject("components").getDouble("no2");
                pollutionD.wNO2[i] = Double.toString(pmNO2);

                pmO3 = list_object.getJSONObject("components").getDouble("o3");
                pollutionD.wO3[i] = Double.toString(pmO3);
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

    public String[] getwCO() {
        return wCO;
    }

    public String[] getwPM10() { return wPM10; }

    public String[] getwPM2() {return wPM2; }

    public String[] getwSO2() {return wSO2; }

    public String[] getwNO2() {return wNO2; }

    public String[] getwO3() {return wO3; }
}
