package com.example.probka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class pollutionData {

    private String mIcon, mCO, mPM10, mPM2, mSO2, mNO2, mO3;

    private String m_ocenaSO2, m_ocenaNO2, m_ocenaPM10, m_ocenaPM2, m_ocenaO3, m_ocenaCO;
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

                pollutionD.m_ocenaPM10 = updateOcenaPM10(pmPM10);

                pollutionD.mCondition = list_object.getJSONObject("components").getDouble("pm2_5");
                pmPM2 = list_object.getJSONObject("components").getDouble("pm2_5");
                pollutionD.mPM2 = Double.toString(pmPM2);

                pollutionD.m_ocenaPM2 = updateOcenaPM2(pmPM2);

                pmCO = list_object.getJSONObject("components").getDouble("co");
                pollutionD.mCO = Double.toString(pmCO);

                pollutionD.m_ocenaCO = updateOcenaCO(pmCO);

                pmSO2 = list_object.getJSONObject("components").getDouble("so2");
                pollutionD.mSO2 = Double.toString(pmSO2);

                pollutionD.m_ocenaSO2 = updateOcenaSO2(pmSO2);

                pmNO2 = list_object.getJSONObject("components").getDouble("no2");
                pollutionD.mNO2 = Double.toString(pmNO2);

                pollutionD.m_ocenaNO2 = updateOcenaNO2(pmNO2);

                pmO3 = list_object.getJSONObject("components").getDouble("o3");
                pollutionD.mO3 = Double.toString(pmO3);

                pollutionD.m_ocenaO3 = updateOcenaO3(pmO3);
            }
            return pollutionD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

//    koniecznosc dostosowania interpretacji
    private static String updateOcenaSO2(double condition) {
        if (condition >= 0 && condition < 20) {
            return "Dobre";
        } else if (condition >= 20 && condition < 80) {
            return "Poprawne";
        }else if (condition >= 80 && condition < 250){
            return "Umiarkowane";
        }else if (condition >= 250 && condition < 350) {
            return "Złe";
        }else if (condition >= 350) {
            return "Bardzo złe";
        }
        return "Nieznane warunki.";
    }

    private static String updateOcenaNO2(double condition) {
        if (condition >= 0 && condition < 40) {
            return "Dobre";
        } else if (condition >= 40 && condition < 70) {
            return "Poprawne";
        }else if (condition >= 70 && condition < 150){
            return "Umiarkowane";
        }else if (condition >= 150 && condition < 200) {
            return "Złe";
        }else if (condition >= 200) {
            return "Bardzo złe";
        }
        return "Nieznane warunki.";
    }

    private static String updateOcenaPM10(double condition) {
        if (condition >= 0 && condition < 20) {
            return "Dobre";
        } else if (condition >= 20 && condition < 50) {
            return "Poprawne";
        }else if (condition >= 50 && condition < 100){
            return "Umiarkowane";
        }else if (condition >= 100 && condition < 200) {
            return "Złe";
        }else if (condition >= 200) {
            return "Bardzo złe";
        }
        return "Nieznane warunki.";
    }

    private static String updateOcenaPM2(double condition) {
        if (condition >= 0 && condition < 10) {
            return "Dobre";
        } else if (condition >= 10 && condition < 25) {
            return "Poprawne";
        }else if (condition >= 25 && condition < 50){
            return "Umiarkowane";
        }else if (condition >= 50 && condition < 75) {
            return "Złe";
        }else if (condition >= 75) {
            return "Bardzo złe";
        }
        return "Nieznane warunki.";
    }

    private static String updateOcenaO3(double condition) {
        if (condition >= 0 && condition < 60) {
            return "Dobre";
        } else if (condition >= 60 && condition < 100) {
            return "Poprawne";
        }else if (condition >= 100 && condition < 140){
            return "Umiarkowane";
        }else if (condition >= 140 && condition < 180) {
            return "Złe";
        }else if (condition >= 180) {
            return "Bardzo złe";
        }
        return "Nieznane warunki.";
    }

    private static String updateOcenaCO(double condition) {
        if (condition >= 0 && condition < 4400) {
            return "Dobre";
        } else if (condition >= 4400 && condition < 9400) {
            return "Poprawne";
        }else if (condition >= 9400 && condition < 12400){
            return "Umiarkowane";
        }else if (condition >= 12400 && condition < 15400) {
            return "Złe";
        }else if (condition >= 15400) {
            return "Bardzo złe";
        }
        return "Nieznane warunki.";
    }

    public String getmCO() {
        return mCO;
    }

    public String getmPM10() { return mPM10; }

    public String getmPM2() {return mPM2; }

    public String getmSO2() {return mSO2; }

    public String getmNO2() {return mNO2; }

    public String getmO3() {return mO3; }

    public String getM_ocenaSO2() {return m_ocenaSO2; }

    public String getM_ocenaNO2() {return m_ocenaNO2; }

    public String getM_ocenaO3() {return m_ocenaO3; }

    public String getM_ocenaCO() {return m_ocenaCO; }

    public String getM_ocenaPM10() {return m_ocenaPM10; }

    public String getM_ocenaPM2() {return m_ocenaPM2; }
}
