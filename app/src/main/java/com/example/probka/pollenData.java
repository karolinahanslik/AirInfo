package com.example.probka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class pollenData {

    private String mTrawa, mPlesn, mDrzewa, mAmbrozja;
    String m_ocenaTrawa, m_ocenaPlesn, m_ocenaDrzewa, m_ocenaAmbrozja;
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
                pollenD.m_ocenaTrawa = trawaJSONObject.getString("Category");

                JSONObject plesnJSONObject = allPollenArray.getJSONObject(2);
                pmPlesn = plesnJSONObject.getDouble("Value");
                pollenD.mPlesn = Double.toString(pmPlesn);
                pollenD.m_ocenaPlesn = plesnJSONObject.getString("Category");

                JSONObject drzewaJSONObject = allPollenArray.getJSONObject(4);
                pmDrzewa = drzewaJSONObject.getDouble("Value");
                pollenD.mDrzewa = Double.toString(pmDrzewa);
                pollenD.m_ocenaDrzewa = drzewaJSONObject.getString("Category");

                JSONObject ambrozjaJSONObject = allPollenArray.getJSONObject(1);
                pmAmbrozja = ambrozjaJSONObject.getDouble("Value");
                pollenD.mAmbrozja = Double.toString(pmAmbrozja);
                pollenD.m_ocenaAmbrozja = ambrozjaJSONObject.getString("Category");
            }
            return pollenD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    //    koniecznosc dostosowania interpretacji
    public String getmTrawa() {
        return mTrawa;
    }

    public String getmPlesn() { return mPlesn; }

    public String getmDrzewa() {return mDrzewa; }

    public String getmAmbrozja() {return mAmbrozja; }

    public String getM_ocenaTrawa() {return m_ocenaTrawa; }

    public String getM_ocenaPlesn() {return m_ocenaPlesn; }

    public String getM_ocenaDrzewa() {return m_ocenaDrzewa; }

    public String getM_ocenaAmbrozja() {return m_ocenaAmbrozja; }
}