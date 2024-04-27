package com.example.probka;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API = "KEY1";
    final String API2 = "KEY2";
    final String Pollution_URL = "http://api.openweathermap.org/data/2.5/air_pollution";

    final String CityName_URL = "http://api.openweathermap.org/data/2.5/weather";

    final String Historical_Pollution_URL = "http://api.openweathermap.org/data/2.5/air_pollution/history";

    final String CityKey_URL = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    long currentEpochTime, pastEpochTime;

    private LineChart wykresPM10;
    private List<String> xValues;

    String[] wPM10 = new String[24];
    String[] wPM2 = new String[24];
    String[] wCO = new String[24];
    String[] wSO2 = new String[24];
    String[] wNO2 = new String[24];
    String[] wO3 = new String[24];

    String Location_Provider = LocationManager.GPS_PROVIDER;

    String latitude, longitude;

    String final_keyData;

    //final String Pollen_URL = String.join("", "http://dataservice.accuweather.com/forecasts/v1/daily/1day/", final_keyData);

    RelativeLayout mCityFinder;
    TextView cityName;
    TextView PM10_TextView;
    TextView PM2_TextView;
    TextView CO_TextView;
    TextView SO2_TextView;
    TextView NO2_TextView;
    TextView O3_TextView;
    TextView trawa_TextView;
    TextView plesn_TextView;
    TextView drzewa_TextView;
    TextView ambrozja_TextView;

    TextView Data_Godzina;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    ImageButton HomeButton;
    TextView SmogButton;
    TextView PylkiButton;

    CardView HomeCardView;
    CardView SmogCardView;
    ImageButton PM10ExitButton;
    ImageButton PM2ExitButton;
    ImageButton COExitButton;
    ImageButton SO2ExitButton;
    ImageButton NO2ExitButton;
    ImageButton O3ExitButton;
    Dialog PM10Dialog;
    Dialog PM2Dialog;
    Dialog CODialog;
    Dialog SO2Dialog;
    Dialog NO2Dialog;
    Dialog O3Dialog;
    CardView PylkiCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Dialog PM10Dialog = new Dialog(MainActivity.this);
        Dialog PM2Dialog = new Dialog(MainActivity.this);
        Dialog CODialog = new Dialog(MainActivity.this);
        Dialog NO2Dialog= new Dialog(MainActivity.this);
        Dialog SO2Dialog= new Dialog(MainActivity.this);
        Dialog O3Dialog= new Dialog(MainActivity.this);


        PM10_TextView = (TextView) findViewById(R.id.PM10_TextView);
        PM2_TextView = (TextView) findViewById(R.id.PM2_TextView);
        CO_TextView = (TextView) findViewById(R.id.CO_TextView);
        SO2_TextView=(TextView)  findViewById(R.id.SO2_TextView);
        NO2_TextView=(TextView)  findViewById(R.id.NO2_TextView);
        O3_TextView=(TextView)  findViewById(R.id.O3_TextView);
        HomeCardView = (CardView) findViewById(R.id.HomeCardView);
        SmogCardView = (CardView) findViewById(R.id.SmogCardView);
        PylkiCardView = (CardView) findViewById(R.id.PylkiCardView);
        cityName = (TextView) findViewById(R.id.CityName);
        mCityFinder = findViewById(R.id.Localization);
        Data_Godzina = findViewById(R.id.DateHour);

        wykresPM10 = findViewById(R.id.WykresP10_CardView);

        PM10Dialog.setContentView(R.layout.pm10_layout);
        PM2Dialog.setContentView(R.layout.pm2_layout);
        CODialog.setContentView(R.layout.pm1_layout);
        SO2Dialog.setContentView(R.layout.so2_layout);
        NO2Dialog.setContentView(R.layout.no2_layout);
        O3Dialog.setContentView(R.layout.o3_layout);

        trawa_TextView = findViewById(R.id.TrawaTextView);
        plesn_TextView = findViewById(R.id.PlesnTextView);
        drzewa_TextView = findViewById(R.id.DrzewaTextView);
        ambrozja_TextView = findViewById(R.id.AmbrozjaTextView);

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });
    SO2_TextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SO2Dialog.show();
            ImageButton SO2ExitButton =(ImageButton) SO2Dialog.findViewById(R.id.SO2ExitButton);
            SO2ExitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SO2Dialog.dismiss();
                }
            });
        }
    });
        NO2_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NO2Dialog.show();
                ImageButton NO2ExitButton =(ImageButton) NO2Dialog.findViewById(R.id.NO2ExitButton);
                NO2ExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NO2Dialog.dismiss();
                    }
                });
            }
        });
        O3_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                O3Dialog.show();
                ImageButton O3ExitButton =(ImageButton) O3Dialog.findViewById(R.id.O3ExitButton);
                O3ExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        O3Dialog.dismiss();
                    }
                });
            }
        });
        CO_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CODialog.show();
                ImageButton COExitButton = (ImageButton) CODialog.findViewById(R.id.COExitButton);
                COExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CODialog.dismiss();
                    }
                });
            }
        });
        PM2_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PM2Dialog.show();
                ImageButton PM2ExitButton = (ImageButton) PM2Dialog.findViewById(R.id.PM2ExitButton);
                PM2ExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PM2Dialog.dismiss();
                    }
                });
            }
        });
        PM10_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PM10Dialog.show();
                ImageButton PM10ExitButton = (ImageButton) PM10Dialog.findViewById(R.id.PM10ExitButton);
                PM10ExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PM10Dialog.dismiss();
                    }
                });

            }
        });

        PylkiButton = (TextView) findViewById(R.id.PylkiButton);
        PylkiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PylkiCardView.getVisibility() == CardView.VISIBLE) {

                } else {
                    PylkiCardView.setVisibility(CardView.VISIBLE);
                    if (HomeCardView.getVisibility() == CardView.VISIBLE) {
                        HomeCardView.setVisibility(View.INVISIBLE);
                    } else if (SmogCardView.getVisibility() == CardView.VISIBLE) {
                        SmogCardView.setVisibility(View.INVISIBLE);

                    }

                }
            }
        });
        SmogButton = (TextView) findViewById(R.id.SmogButton);
        SmogButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SmogCardView.getVisibility() == CardView.VISIBLE) {

                } else {
                    SmogCardView.setVisibility(CardView.VISIBLE);
                    if (HomeCardView.getVisibility() == CardView.VISIBLE) {
                        HomeCardView.setVisibility(CardView.INVISIBLE);
                    } else if (PylkiCardView.getVisibility() == CardView.VISIBLE) {
                        PylkiCardView.setVisibility(CardView.INVISIBLE);
                    }
                }
            }
        }));
        HomeButton = (ImageButton) findViewById(R.id.HomeButton);
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HomeCardView.getVisibility() == CardView.VISIBLE) {

                } else {
                    HomeCardView.setVisibility(CardView.VISIBLE);
                    if (SmogCardView.getVisibility() == CardView.VISIBLE) {
                        SmogCardView.setVisibility(CardView.INVISIBLE);
                    } else if (PylkiCardView.getVisibility() == CardView.VISIBLE) {
                        PylkiCardView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getPollutionforCurrentLocation();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        String city = mIntent.getStringExtra("City");
        if (city != null) {
            getPollutionforNewCity(city);

        } else {
            getPollutionforCurrentLocation();
            getCurrentLocationKey();
        }
    }

    private void getPollutionforNewCity(String city)
    {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", API);
        letsdoMoreNetworking(params);
    }

    private void getPollenforAnyCity(String final_keyData)
    {
        RequestParams params = new RequestParams();
        params.put("apikey", API2);
        //params.put("language", "pl-pl");
        params.put("details", "true");
        params.put("metric", "false");
        letsdoEvenMoreNetworking(params);
    }

    private void getCurrentLocationKey() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                String test = String.join(",", Latitude, Longitude);

                RequestParams params = new RequestParams();
                params.put("apikey", API2);
                params.put("q", test);
                letsgetKey(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    private void getPollutionforCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                latitude = Latitude;
                longitude = Longitude;

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", API);
                letsdoSomeNetworking(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

//        ten kod leci za każdym razem, gdy włączamy apkę
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "Poprawnie pobrano lokalizację.", Toast.LENGTH_SHORT).show();
                getPollutionforCurrentLocation();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Nie udało się pobrać lokalizacji.", Toast.LENGTH_SHORT).show();
                //user denied the permission

            }
        }
    }

    private void letsdoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Pollution_URL, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        pollutionData pollutionD = pollutionData.fromJson(response);
                        updateUI(pollutionD);
                        //super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "Nie udało się pobrać danych.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        client.get(CityName_URL, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(MainActivity.this, "Poprawnie zaktualizowano dane.", Toast.LENGTH_SHORT).show();

                        cityNameData cityNameD = cityNameData.fromJson(response);
                        updateCN(cityNameD);
                        //super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "Niepoprawna nazwa miasta.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //tutaj dodac blok o pobieraniu wykresow
    }

    private void letsdoMoreNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(CityName_URL, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        coordsData coordsD = coordsData.fromJson(response);
                        updateLL(coordsD);
                        RequestParams params2 = new RequestParams();
                        params2.put("lat", latitude);
                        params2.put("lon", longitude);
                        params2.put("appid", API);
                        letsdoSomeNetworking(params2);
                        //super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "Niepoprawna nazwa miasta.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void letsgetKey(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(CityKey_URL, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        cityKeyData keyData = cityKeyData.fromJson(response);
                        updateCK(keyData);
                        getPollenforAnyCity(final_keyData);
                        //super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "Niepoprawna nazwa miasta.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void letsdoEvenMoreNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.join("/", "http://dataservice.accuweather.com/forecasts/v1/daily/1day", final_keyData), params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        pollenData plData = pollenData.fromJson(response);
                        updatePOL(plData);
                        //super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "Nie udało się pobrać danych o pyłkach.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void letsgetHData(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Historical_Pollution_URL, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                        historicalPollutionData HPData = historicalPollutionData.fromJson(response);
                        updateWykresy(HPData);
                        //super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "Niepoprawna nazwa miasta.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateUI(pollutionData pollution)
    {
        CO_TextView.setText(pollution.getmCO());
        PM10_TextView.setText(pollution.getmPM10());
        PM2_TextView.setText(pollution.getmPM2());
        SO2_TextView.setText(pollution.getmSO2());
        NO2_TextView.setText(pollution.getmNO2());
        O3_TextView.setText(pollution.getmO3());
        int resourceID = getResources().getIdentifier(pollution.getmIcon(), "drawable", getPackageName());
    }

    private void updatePOL(pollenData pollen)
    {
        trawa_TextView.setText(pollen.getmTrawa());
        plesn_TextView.setText(pollen.getmPlesn());
        drzewa_TextView.setText(pollen.getmDrzewa());
        ambrozja_TextView.setText(pollen.getmAmbrozja());
        //int resourceID = getResources().getIdentifier(pollen.getmIcon(), "drawable", getPackageName());
    }

    private void updateCN(cityNameData cityNameD)
    {
        cityName.setText(cityNameD.getmCityName());
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String date = formatter.format(today);
        Data_Godzina.setText(date);

        //epoch time
        // Pobieranie aktualnej daty
        Date currentDate = new Date();

        // Zamiana daty na czas Epoch (w milisekundach)
        currentEpochTime = System.currentTimeMillis() / 1000L;

        // Data sprzed 24h
        pastEpochTime = currentEpochTime - 82800;

        RequestParams params = new RequestParams();
        params.put("lat", latitude);
        params.put("lon", longitude);
        params.put("appid", API);
        params.put("start", pastEpochTime);
        params.put("end", currentEpochTime);
        letsgetHData(params);
    }

    private void updateLL(coordsData coordsD)
    {
        latitude = coordsD.getmLat();
        longitude = coordsD.getmLon();
        String test = String.join(",", latitude, longitude);

        RequestParams params = new RequestParams();
        params.put("apikey", API2);
        params.put("q", test);
        letsgetKey(params);
    }

    private void updateCK(cityKeyData ckData)
    {
        final_keyData = ckData.getmCityKey();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager != null)
        {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
    private void updateWykresy(historicalPollutionData HPData)
    {
        //String test = HPData.getwPM10(); //dziala
        wPM10 = HPData.getwPM10();

        //dane z API
        xValues = Arrays.asList("23h", "", "", "", "19h", "", "", "", "15h", "", "", "", "11h", "", "", "", "7h", "", "", "", "3h", "", "", "Teraz");

        Description description = new Description();
        description.setText("");
        wykresPM10.setDescription(description);
        wykresPM10.getAxisRight().setDrawLabels(false);

        XAxis xAxis = wykresPM10.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(24);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = wykresPM10.getAxisLeft();
        //yAxis.setAxisMinimum(0f);
        //yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        //yAxis.setLabelCount(10);

        List<Entry> dane_PM10 = new ArrayList<>();
        dane_PM10.add(new Entry(0, 1));
        dane_PM10.add(new Entry(1, 3));
        dane_PM10.add(new Entry(2, 7));
        dane_PM10.add(new Entry(3, 5));
        dane_PM10.add(new Entry(4, 7));
        dane_PM10.add(new Entry(5, 1));
        dane_PM10.add(new Entry(6, 3));
        dane_PM10.add(new Entry(7, 7));
        dane_PM10.add(new Entry(8, 5));
        dane_PM10.add(new Entry(9, 7));
        dane_PM10.add(new Entry(10, 1));
        dane_PM10.add(new Entry(11, 3));
        dane_PM10.add(new Entry(12, 7));
        dane_PM10.add(new Entry(13, 5));
        dane_PM10.add(new Entry(14, 7));
        dane_PM10.add(new Entry(15, 1));
        dane_PM10.add(new Entry(16, 3));
        dane_PM10.add(new Entry(17, 7));
        dane_PM10.add(new Entry(18, 5));
        dane_PM10.add(new Entry(19, 7));
        dane_PM10.add(new Entry(20, 1));
        dane_PM10.add(new Entry(21, 3));
        dane_PM10.add(new Entry(22, 7));
        dane_PM10.add(new Entry(23, 5));
        /*
        dane_PM10.add(new Entry(0,Float.parseFloat(wPM10[0])));
        dane_PM10.add(new Entry(1,Float.parseFloat(wPM10[1])));
        dane_PM10.add(new Entry(2,Float.parseFloat(wPM10[2])));
        dane_PM10.add(new Entry(3,Float.parseFloat(wPM10[3])));
        dane_PM10.add(new Entry(4,Float.parseFloat(wPM10[4])));
        dane_PM10.add(new Entry(5,Float.parseFloat(wPM10[5])));
        dane_PM10.add(new Entry(6,Float.parseFloat(wPM10[6])));
        dane_PM10.add(new Entry(7,Float.parseFloat(wPM10[7])));
        dane_PM10.add(new Entry(8,Float.parseFloat(wPM10[8])));
        dane_PM10.add(new Entry(9,Float.parseFloat(wPM10[9])));
        dane_PM10.add(new Entry(10,Float.parseFloat(wPM10[10])));
        dane_PM10.add(new Entry(11,Float.parseFloat(wPM10[11])));
        dane_PM10.add(new Entry(12,Float.parseFloat(wPM10[12])));
        dane_PM10.add(new Entry(13,Float.parseFloat(wPM10[13])));
        dane_PM10.add(new Entry(14,Float.parseFloat(wPM10[14])));
        dane_PM10.add(new Entry(15,Float.parseFloat(wPM10[15])));
        dane_PM10.add(new Entry(16,Float.parseFloat(wPM10[16])));
        dane_PM10.add(new Entry(17,Float.parseFloat(wPM10[17])));
        dane_PM10.add(new Entry(18,Float.parseFloat(wPM10[18])));
        dane_PM10.add(new Entry(19,Float.parseFloat(wPM10[19])));
        dane_PM10.add(new Entry(20,Float.parseFloat(wPM10[20])));
        dane_PM10.add(new Entry(21,Float.parseFloat(wPM10[21])));
        dane_PM10.add(new Entry(22,Float.parseFloat(wPM10[22])));
        dane_PM10.add(new Entry(23,Float.parseFloat(wPM10[23])));
        */

        LineDataSet wykres_PM10 = new LineDataSet(dane_PM10, "PM10");
        wykres_PM10.setColor(Color.BLACK);

        LineData PM10LD = new LineData(wykres_PM10);

        wykresPM10.setData(PM10LD);

        wykresPM10.invalidate();
        wykresPM10.setBackgroundColor(Color.WHITE);
    }
}