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
import java.util.Random;
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
import org.w3c.dom.Text;

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

    private LineChart wykresPM10, wykresCO, wykresPM2, wykresSO2, wykresNO2, wykresO3;
    private List<String> xValues;
String pylenie;
String pylenieText;
    String[] wPM10 = new String[24];
    String[] wPM2 = new String[24];
    String[] wCO = new String[24];
    String[] wSO2 = new String[24];
    String[] wNO2 = new String[24];
    String[] wO3 = new String[24];

    TextView ocenaSO2, ocenaNO2, ocenaPM10, ocenaPM2, ocenaO3, ocenaCO;

    TextView ocenaTrawa, ocenaPlesn, ocenaDrzewa, ocenaAmbrozja;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    String latitude, longitude;

    String final_keyData;

   // final String Pollen_URL = String.join("", "http://dataservice.accuweather.com/forecasts/v1/daily/1day/", final_keyData);
    String[] porady= new String[]{"Aby zadbać o układ oddechowy unikaj palenia papierosów", "Aby zadbać o układ oddechowy, dbaj o jakość powietrza w domu", "Aby dbać o układ oddechowy ćwicz regularnie","Aby dbac o układ oddechowy zebezpiecz się maską przed smogiem"};
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
    TextView SmogTextView;
    TextView PoradyTextView;
    TextView PylkiTextView;
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
    String smogText;

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
        SmogTextView=(TextView) findViewById(R.id.SmogTextView);
        PylkiTextView=(TextView) findViewById(R.id.PylkiTextView);
        PoradyTextView=(TextView) findViewById(R.id.PoradyTextView);
        HomeCardView = (CardView) findViewById(R.id.HomeCardView);
        SmogCardView = (CardView) findViewById(R.id.SmogCardView);
        PylkiCardView = (CardView) findViewById(R.id.PylkiCardView);
        cityName = (TextView) findViewById(R.id.CityName);
        mCityFinder = findViewById(R.id.Localization);
        Data_Godzina = findViewById(R.id.DateHour);

        wykresPM10 = findViewById(R.id.WykresP10_CardView);
        wykresCO = findViewById(R.id.WykresCOCardView);
        wykresPM2 = findViewById(R.id.WykresPM2CardView);
        wykresSO2 = findViewById(R.id.WykresSO2CardView);
        wykresNO2 = findViewById(R.id.WykresNO2CardView);
        wykresO3 = findViewById(R.id.WykresO3CradView);

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

        ocenaSO2 = findViewById(R.id.SO2_Norma);

        ocenaNO2 = findViewById(R.id.NO2_Norma);
        ocenaO3 = findViewById(R.id.O3_Norma);
        ocenaPM10 = findViewById(R.id.PM10_Norma);
        ocenaPM2 = findViewById(R.id.PM2_Norma);
        ocenaCO = findViewById(R.id.CO_Norma);

        ocenaTrawa = findViewById(R.id.stezenieTrawy);
        ocenaPlesn = findViewById(R.id.stezeniePlesn);
        ocenaDrzewa = findViewById(R.id.stezenieDrzewa);
        ocenaAmbrozja = findViewById(R.id.stezenieAmbrozja);


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
        Random random = new Random();

        int liczba = random.nextInt(4) ;
        PoradyTextView.setText(porady[liczba]);
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
        params.put("language", "pl-pl");
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
                //user denied the permission current

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
                        Toast.makeText(MainActivity.this, "Niepoprawna nazwa miasta. Wyszukano wartości dla obecnej lokalizacji.", Toast.LENGTH_SHORT).show();
                        getPollutionforCurrentLocation();
                        getCurrentLocationKey();
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

        ocenaCO.setText(pollution.getM_ocenaCO());
        if(ocenaCO.getText()=="Dobre")
        {
            ocenaCO.setTextColor(Color.parseColor("#66FF33"));
        } else if (ocenaCO.getText()=="Poprawne") {
            ocenaCO.setTextColor(Color.parseColor("#99CC33"));
        } else if (ocenaCO.getText()=="Umiarkowane") {
            ocenaCO.setTextColor(Color.parseColor("#CCCC33"));
        } else if (ocenaCO.getText()=="Złe") {
            ocenaCO.setTextColor(Color.parseColor("#FF3300"));
        } else if (ocenaCO.getText()=="Bardzo złe") {
            ocenaCO.setTextColor(Color.parseColor("#990000"));
        }

        ocenaSO2.setText(pollution.getM_ocenaSO2());
        if(ocenaSO2.getText()=="Dobre")
        {
            ocenaSO2.setTextColor(Color.parseColor("#66FF33"));
        } else if (ocenaSO2.getText()=="Poprawne") {
            ocenaSO2.setTextColor(Color.parseColor("#99CC33"));
        } else if (ocenaSO2.getText()=="Umiarkowane") {
            ocenaSO2.setTextColor(Color.parseColor("#CCCC33"));
        } else if (ocenaSO2.getText()=="Złe") {
            ocenaSO2.setTextColor(Color.parseColor("#FF3300"));
        } else if (ocenaSO2.getText()=="Bardzo złe") {
            ocenaSO2.setTextColor(Color.parseColor("#990000"));
        }
        ocenaNO2.setText(pollution.getM_ocenaNO2());

        if(ocenaNO2.getText()=="Dobre")
        {
            ocenaNO2.setTextColor(Color.parseColor("#66FF33"));
        } else if (ocenaNO2.getText()=="Poprawne") {
            ocenaNO2.setTextColor(Color.parseColor("#99CC33"));
        } else if (ocenaNO2.getText()=="Umiarkowane") {
            ocenaNO2.setTextColor(Color.parseColor("#CCCC33"));
        } else if (ocenaNO2.getText()=="Złe") {
            ocenaNO2.setTextColor(Color.parseColor("#FF3300"));
        } else if (ocenaNO2.getText()=="Bardzo złe") {
            ocenaNO2.setTextColor(Color.parseColor("#990000"));
        }

        ocenaO3.setText(pollution.getM_ocenaO3());

        if(ocenaO3.getText()=="Dobre")
        {
            ocenaO3.setTextColor(Color.parseColor("#66FF33"));
        } else if (ocenaO3.getText()=="Poprawne") {
            ocenaO3.setTextColor(Color.parseColor("#99CC33"));
        } else if (ocenaO3.getText()=="Umiarkowane") {
            ocenaO3.setTextColor(Color.parseColor("#CCCC33"));
        } else if (ocenaO3.getText()=="Złe") {
            ocenaNO2.setTextColor(Color.parseColor("#FF3300"));
        } else if (ocenaNO2.getText()=="Bardzo złe") {
            ocenaNO2.setTextColor(Color.parseColor("#990000"));
        }
        ocenaPM10.setText(pollution.getM_ocenaPM10());
        if(ocenaPM10.getText()=="Dobre")
        {
            ocenaPM10.setTextColor(Color.parseColor("#66FF33"));
        } else if (ocenaPM10.getText()=="Poprawne") {
            ocenaPM10.setTextColor(Color.parseColor("#99CC33"));
        } else if (ocenaPM10.getText()=="Umiarkowane") {
            ocenaPM10.setTextColor(Color.parseColor("#CCCC33"));
        } else if (ocenaPM10.getText()=="Złe") {
            ocenaPM10.setTextColor(Color.parseColor("#FF3300"));
        } else if (ocenaPM10.getText()=="Bardzo złe") {
            ocenaPM10.setTextColor(Color.parseColor("#990000"));
        }
        ocenaPM2.setText(pollution.getM_ocenaPM2());
        if(ocenaPM2.getText()=="Dobre")
        {
            ocenaPM2.setTextColor(Color.parseColor("#66FF33"));
        } else if (ocenaPM2.getText()=="Poprawne") {
            ocenaPM2.setTextColor(Color.parseColor("#99CC33"));
        } else if (ocenaPM2.getText()=="Umiarkowane") {
            ocenaPM2.setTextColor(Color.parseColor("#CCCC33"));
        } else if (ocenaPM2.getText()=="Złe") {
            ocenaPM2.setTextColor(Color.parseColor("#FF3300"));
        } else if (ocenaPM2.getText()=="Bardzo złe") {
            ocenaPM2.setTextColor(Color.parseColor("#990000"));
        }
        if((pollution.getM_ocenaPM2()=="Złe" || pollution.getM_ocenaPM2()=="Bardzo złe") &&(pollution.getM_ocenaPM10()=="Złe"||pollution.getM_ocenaPM10()=="Bardzo złe") )
        {
            smogText="Dzisiaj w "+cityName.getText()+" jest zła jakość powietrza, nie wychodź z domu jeśli to możliwe";
            SmogTextView.setText(smogText);
        } else if ((pollution.getM_ocenaPM2()=="Dobre"||pollution.getM_ocenaPM2()=="Poprawne"||pollution.getM_ocenaPM2()=="Umiarkowane")&&(pollution.getM_ocenaPM10()=="Dobre"||pollution.getM_ocenaPM10()=="Poprawne"||pollution.getM_ocenaPM10()=="Umiarkowane")) {
            smogText="Dzisiaj w "+cityName.getText()+" jest dobra jakość powietrza";
            SmogTextView.setText(smogText);
        }
        else {
            smogText="Dzisiaj w "+cityName.getText()+" jest średnia jakość powietrza";
            SmogTextView.setText(smogText);
        }

    }

    private void updatePOL(pollenData pollen)
    {

        trawa_TextView.setText(pollen.getmTrawa());
        plesn_TextView.setText(pollen.getmPlesn());
        drzewa_TextView.setText(pollen.getmDrzewa());
        ambrozja_TextView.setText(pollen.getmAmbrozja());

        ocenaTrawa.setText(pollen.getM_ocenaTrawa());
        if(pollen.getM_ocenaTrawa().contains("Dobre"))
        {
            ocenaTrawa.setTextColor(Color.parseColor("#66FF33"));
        } else if (pollen.getM_ocenaTrawa().contains("Średnie")) {
            ocenaTrawa.setTextColor(Color.parseColor("#FF9933"));
        } else if (pollen.getM_ocenaTrawa().contains("Szkodliwe"))  {
            ocenaTrawa.setTextColor(Color.parseColor("#CC0000"));
        }
        ocenaPlesn.setText(pollen.getM_ocenaPlesn());
        if(pollen.getM_ocenaPlesn().contains("Dobre"))
        {
            ocenaPlesn.setTextColor(Color.parseColor("#66FF33"));
        } else if (pollen.getM_ocenaPlesn().contains("Średnie")) {
            ocenaPlesn.setTextColor(Color.parseColor("#FF9933"));
        } else if (pollen.getM_ocenaPlesn().contains("Szkodliwe"))  {
            ocenaPlesn.setTextColor(Color.parseColor("#CC0000"));
        }
        ocenaDrzewa.setText(pollen.getM_ocenaDrzewa());
        if(pollen.getM_ocenaDrzewa().contains("Dobre"))
        {
            ocenaDrzewa.setTextColor(Color.parseColor("#66FF33"));
        } else if (pollen.getM_ocenaDrzewa().contains("Średnie")) {
            ocenaDrzewa.setTextColor(Color.parseColor("#FF9933"));
        } else if (pollen.getM_ocenaDrzewa().contains("Szkodliwe"))  {
            ocenaDrzewa.setTextColor(Color.parseColor("#CC0000"));
        }
        ocenaAmbrozja.setText(pollen.getM_ocenaAmbrozja());
        if(pollen.getM_ocenaAmbrozja().contains("Dobre"))
        {
            ocenaAmbrozja.setTextColor(Color.parseColor("#66FF33"));
        } else if (pollen.getM_ocenaAmbrozja().contains("Średnie")) {
            ocenaAmbrozja.setTextColor(Color.parseColor("#FF9933"));
        } else if (pollen.getM_ocenaAmbrozja().contains("Szkodliwe"))  {
            ocenaAmbrozja.setTextColor(Color.parseColor("#CC0000"));
        }
        pylenie="";
        if(pollen.getM_ocenaAmbrozja().contains("Średnie")||pollen.getM_ocenaAmbrozja().contains("Szkodliwe"))
        {
            pylenie="ambrozja";

        }
        if(pollen.getM_ocenaDrzewa().contains("Szkodliwe")||pollen.getM_ocenaDrzewa().contains("Średnie"))
        {
            if(pylenie=="")
            {
                pylenie="drzewa";
            }
            else{
                pylenie=pylenie+", drzewa";
            }

        }
        if(pollen.getM_ocenaPlesn().contains("Średnie")||pollen.getM_ocenaPlesn().contains("Szkodliwe")){
            if(pylenie=="")
            {
                pylenie="pleśń";
            }
            else{
                pylenie=pylenie+", pleśń";
            }

        }
        if(pollen.getM_ocenaTrawa().contains("Średnie")||pollen.getM_ocenaTrawa().contains("Szkodliwe"))
        {
            if(pylenie=="")
            {
                pylenie="trawa";
            }
            pylenie=pylenie+", trawa";
        }
        pylenieText="Dzisiaj w "+cityName.getText()+" pylą: "+pylenie;
        PylkiTextView.setText(pylenieText);
    }

    private void updateCN(cityNameData cityNameD)
    {
        cityName.setText(cityNameD.getmCityName());
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = formatter.format(today);
        Data_Godzina.setText(date);

        //epoch time
        // Pobieranie aktualnej daty
        Date currentDate = new Date();

        // Zamiana daty na czas Epoch (w milisekundach)
        currentEpochTime = System.currentTimeMillis() / 1000L;

        // Data sprzed 24h
        pastEpochTime = currentEpochTime - 86400;

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

        xValues = Arrays.asList("23h", "", "", "", "19h", "", "", "", "15h", "", "", "", "11h", "", "", "", "7h", "", "", "", "3h", "", "", "Teraz");

        //----------------------------PM_10----------------------------
        wPM10 = HPData.getwPM10();

        Description descriptionPM10 = new Description();
        descriptionPM10.setText("");
        wykresPM10.setDescription(descriptionPM10);
        wykresPM10.getAxisRight().setDrawLabels(false);

        XAxis xAxisPM10 = wykresPM10.getXAxis();
        xAxisPM10.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisPM10.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxisPM10.setLabelCount(24);
        xAxisPM10.setGranularity(1f);
        xAxisPM10.setTextColor(Color.BLACK);

        YAxis yAxisPM10 = wykresPM10.getAxisLeft();
        yAxisPM10.setAxisLineWidth(2f);
        yAxisPM10.setAxisLineColor(Color.BLACK);

        List<Entry> dane_PM10 = new ArrayList<>();
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

        LineDataSet wykres_PM10 = new LineDataSet(dane_PM10, "PM10");
        wykres_PM10.setColor(Color.BLACK);
        LineData PM10LD = new LineData(wykres_PM10);
        wykresPM10.setData(PM10LD);
        wykresPM10.invalidate();
        wykresPM10.setBackgroundColor(Color.WHITE);

        //----------------------------CO----------------------------
        wCO = HPData.getwCO();

        Description descriptionCO = new Description();
        descriptionCO.setText("");
        wykresCO.setDescription(descriptionCO);
        wykresCO.getAxisRight().setDrawLabels(false);

        XAxis xAxisCO = wykresCO.getXAxis();
        xAxisCO.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisCO.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxisCO.setLabelCount(24);
        xAxisCO.setGranularity(1f);
        xAxisCO.setTextColor(Color.BLACK);

        YAxis yAxisCO = wykresCO.getAxisLeft();
        yAxisCO.setAxisLineWidth(2f);
        yAxisCO.setAxisLineColor(Color.BLACK);

        List<Entry> dane_CO = new ArrayList<>();
        dane_CO.add(new Entry(0,Float.parseFloat(wCO[0])));
        dane_CO.add(new Entry(1,Float.parseFloat(wCO[1])));
        dane_CO.add(new Entry(2,Float.parseFloat(wCO[2])));
        dane_CO.add(new Entry(3,Float.parseFloat(wCO[3])));
        dane_CO.add(new Entry(4,Float.parseFloat(wCO[4])));
        dane_CO.add(new Entry(5,Float.parseFloat(wCO[5])));
        dane_CO.add(new Entry(6,Float.parseFloat(wCO[6])));
        dane_CO.add(new Entry(7,Float.parseFloat(wCO[7])));
        dane_CO.add(new Entry(8,Float.parseFloat(wCO[8])));
        dane_CO.add(new Entry(9,Float.parseFloat(wCO[9])));
        dane_CO.add(new Entry(10,Float.parseFloat(wCO[10])));
        dane_CO.add(new Entry(11,Float.parseFloat(wCO[11])));
        dane_CO.add(new Entry(12,Float.parseFloat(wCO[12])));
        dane_CO.add(new Entry(13,Float.parseFloat(wCO[13])));
        dane_CO.add(new Entry(14,Float.parseFloat(wCO[14])));
        dane_CO.add(new Entry(15,Float.parseFloat(wCO[15])));
        dane_CO.add(new Entry(16,Float.parseFloat(wCO[16])));
        dane_CO.add(new Entry(17,Float.parseFloat(wCO[17])));
        dane_CO.add(new Entry(18,Float.parseFloat(wCO[18])));
        dane_CO.add(new Entry(19,Float.parseFloat(wCO[19])));
        dane_CO.add(new Entry(20,Float.parseFloat(wCO[20])));
        dane_CO.add(new Entry(21,Float.parseFloat(wCO[21])));
        dane_CO.add(new Entry(22,Float.parseFloat(wCO[22])));
        dane_CO.add(new Entry(23,Float.parseFloat(wCO[23])));

        LineDataSet wykres_CO = new LineDataSet(dane_CO, "CO");
        wykres_CO.setColor(Color.BLACK);
        LineData COLD = new LineData(wykres_CO);
        wykresCO.setData(COLD);
        wykresCO.invalidate();
        wykresCO.setBackgroundColor(Color.WHITE);

        //----------------------------PM2,5----------------------------
        wPM2 = HPData.getwPM2();

        Description descriptionPM2 = new Description();
        descriptionPM2.setText("");
        wykresPM2.setDescription(descriptionPM2);
        wykresPM2.getAxisRight().setDrawLabels(false);

        XAxis xAxisPM2 = wykresPM2.getXAxis();
        xAxisPM2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisPM2.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxisPM2.setLabelCount(24);
        xAxisPM2.setGranularity(1f);
        xAxisPM2.setTextColor(Color.BLACK);

        YAxis yAxisPM2 = wykresPM2.getAxisLeft();
        yAxisPM2.setAxisLineWidth(2f);
        yAxisPM2.setAxisLineColor(Color.BLACK);

        List<Entry> dane_PM2 = new ArrayList<>();
        dane_PM2.add(new Entry(0,Float.parseFloat(wPM2[0])));
        dane_PM2.add(new Entry(1,Float.parseFloat(wPM2[1])));
        dane_PM2.add(new Entry(2,Float.parseFloat(wPM2[2])));
        dane_PM2.add(new Entry(3,Float.parseFloat(wPM2[3])));
        dane_PM2.add(new Entry(4,Float.parseFloat(wPM2[4])));
        dane_PM2.add(new Entry(5,Float.parseFloat(wPM2[5])));
        dane_PM2.add(new Entry(6,Float.parseFloat(wPM2[6])));
        dane_PM2.add(new Entry(7,Float.parseFloat(wPM2[7])));
        dane_PM2.add(new Entry(8,Float.parseFloat(wPM2[8])));
        dane_PM2.add(new Entry(9,Float.parseFloat(wPM2[9])));
        dane_PM2.add(new Entry(10,Float.parseFloat(wPM2[10])));
        dane_PM2.add(new Entry(11,Float.parseFloat(wPM2[11])));
        dane_PM2.add(new Entry(12,Float.parseFloat(wPM2[12])));
        dane_PM2.add(new Entry(13,Float.parseFloat(wPM2[13])));
        dane_PM2.add(new Entry(14,Float.parseFloat(wPM2[14])));
        dane_PM2.add(new Entry(15,Float.parseFloat(wPM2[15])));
        dane_PM2.add(new Entry(16,Float.parseFloat(wPM2[16])));
        dane_PM2.add(new Entry(17,Float.parseFloat(wPM2[17])));
        dane_PM2.add(new Entry(18,Float.parseFloat(wPM2[18])));
        dane_PM2.add(new Entry(19,Float.parseFloat(wPM2[19])));
        dane_PM2.add(new Entry(20,Float.parseFloat(wPM2[20])));
        dane_PM2.add(new Entry(21,Float.parseFloat(wPM2[21])));
        dane_PM2.add(new Entry(22,Float.parseFloat(wPM2[22])));
        dane_PM2.add(new Entry(23,Float.parseFloat(wPM2[23])));

        LineDataSet wykres_PM2 = new LineDataSet(dane_PM2, "PM2,5");
        wykres_PM2.setColor(Color.BLACK);
        LineData PM2LD = new LineData(wykres_PM2);
        wykresPM2.setData(PM2LD);
        wykresPM2.invalidate();
        wykresPM2.setBackgroundColor(Color.WHITE);

        //----------------------------SO2----------------------------
        wSO2 = HPData.getwSO2();

        Description descriptionSO2 = new Description();
        descriptionSO2.setText("");
        wykresSO2.setDescription(descriptionSO2);
        wykresSO2.getAxisRight().setDrawLabels(false);

        XAxis xAxisSO2 = wykresSO2.getXAxis();
        xAxisSO2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisSO2.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxisSO2.setLabelCount(24);
        xAxisSO2.setGranularity(1f);
        xAxisSO2.setTextColor(Color.BLACK);

        YAxis yAxisSO2 = wykresSO2.getAxisLeft();
        yAxisSO2.setAxisLineWidth(2f);
        yAxisSO2.setAxisLineColor(Color.BLACK);

        List<Entry> dane_SO2 = new ArrayList<>();
        dane_SO2.add(new Entry(0,Float.parseFloat(wSO2[0])));
        dane_SO2.add(new Entry(1,Float.parseFloat(wSO2[1])));
        dane_SO2.add(new Entry(2,Float.parseFloat(wSO2[2])));
        dane_SO2.add(new Entry(3,Float.parseFloat(wSO2[3])));
        dane_SO2.add(new Entry(4,Float.parseFloat(wSO2[4])));
        dane_SO2.add(new Entry(5,Float.parseFloat(wSO2[5])));
        dane_SO2.add(new Entry(6,Float.parseFloat(wSO2[6])));
        dane_SO2.add(new Entry(7,Float.parseFloat(wSO2[7])));
        dane_SO2.add(new Entry(8,Float.parseFloat(wSO2[8])));
        dane_SO2.add(new Entry(9,Float.parseFloat(wSO2[9])));
        dane_SO2.add(new Entry(10,Float.parseFloat(wSO2[10])));
        dane_SO2.add(new Entry(11,Float.parseFloat(wSO2[11])));
        dane_SO2.add(new Entry(12,Float.parseFloat(wSO2[12])));
        dane_SO2.add(new Entry(13,Float.parseFloat(wSO2[13])));
        dane_SO2.add(new Entry(14,Float.parseFloat(wSO2[14])));
        dane_SO2.add(new Entry(15,Float.parseFloat(wSO2[15])));
        dane_SO2.add(new Entry(16,Float.parseFloat(wSO2[16])));
        dane_SO2.add(new Entry(17,Float.parseFloat(wSO2[17])));
        dane_SO2.add(new Entry(18,Float.parseFloat(wSO2[18])));
        dane_SO2.add(new Entry(19,Float.parseFloat(wSO2[19])));
        dane_SO2.add(new Entry(20,Float.parseFloat(wSO2[20])));
        dane_SO2.add(new Entry(21,Float.parseFloat(wSO2[21])));
        dane_SO2.add(new Entry(22,Float.parseFloat(wSO2[22])));
        dane_SO2.add(new Entry(23,Float.parseFloat(wSO2[23])));

        LineDataSet wykres_SO2 = new LineDataSet(dane_SO2, "SO2");
        wykres_SO2.setColor(Color.BLACK);
        LineData SO2LD = new LineData(wykres_SO2);
        wykresSO2.setData(SO2LD);
        wykresSO2.invalidate();
        wykresSO2.setBackgroundColor(Color.WHITE);

        //----------------------------NO2----------------------------
        wNO2 = HPData.getwNO2();

        Description descriptionNO2 = new Description();
        descriptionNO2.setText("");
        wykresNO2.setDescription(descriptionNO2);
        wykresNO2.getAxisRight().setDrawLabels(false);

        XAxis xAxisNO2 = wykresNO2.getXAxis();
        xAxisNO2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisNO2.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxisNO2.setLabelCount(24);
        xAxisNO2.setGranularity(1f);
        xAxisNO2.setTextColor(Color.BLACK);

        YAxis yAxisNO2 = wykresNO2.getAxisLeft();
        yAxisNO2.setAxisLineWidth(2f);
        yAxisNO2.setAxisLineColor(Color.BLACK);

        List<Entry> dane_NO2 = new ArrayList<>();
        dane_NO2.add(new Entry(0,Float.parseFloat(wNO2[0])));
        dane_NO2.add(new Entry(1,Float.parseFloat(wNO2[1])));
        dane_NO2.add(new Entry(2,Float.parseFloat(wNO2[2])));
        dane_NO2.add(new Entry(3,Float.parseFloat(wNO2[3])));
        dane_NO2.add(new Entry(4,Float.parseFloat(wNO2[4])));
        dane_NO2.add(new Entry(5,Float.parseFloat(wNO2[5])));
        dane_NO2.add(new Entry(6,Float.parseFloat(wNO2[6])));
        dane_NO2.add(new Entry(7,Float.parseFloat(wNO2[7])));
        dane_NO2.add(new Entry(8,Float.parseFloat(wNO2[8])));
        dane_NO2.add(new Entry(9,Float.parseFloat(wNO2[9])));
        dane_NO2.add(new Entry(10,Float.parseFloat(wNO2[10])));
        dane_NO2.add(new Entry(11,Float.parseFloat(wNO2[11])));
        dane_NO2.add(new Entry(12,Float.parseFloat(wNO2[12])));
        dane_NO2.add(new Entry(13,Float.parseFloat(wNO2[13])));
        dane_NO2.add(new Entry(14,Float.parseFloat(wNO2[14])));
        dane_NO2.add(new Entry(15,Float.parseFloat(wNO2[15])));
        dane_NO2.add(new Entry(16,Float.parseFloat(wNO2[16])));
        dane_NO2.add(new Entry(17,Float.parseFloat(wNO2[17])));
        dane_NO2.add(new Entry(18,Float.parseFloat(wNO2[18])));
        dane_NO2.add(new Entry(19,Float.parseFloat(wNO2[19])));
        dane_NO2.add(new Entry(20,Float.parseFloat(wNO2[20])));
        dane_NO2.add(new Entry(21,Float.parseFloat(wNO2[21])));
        dane_NO2.add(new Entry(22,Float.parseFloat(wNO2[22])));
        dane_NO2.add(new Entry(23,Float.parseFloat(wNO2[23])));

        LineDataSet wykres_NO2 = new LineDataSet(dane_NO2, "NO2");
        wykres_NO2.setColor(Color.BLACK);
        LineData NO2LD = new LineData(wykres_NO2);
        wykresNO2.setData(NO2LD);
        wykresNO2.invalidate();
        wykresNO2.setBackgroundColor(Color.WHITE);

        //----------------------------O3----------------------------
        wO3 = HPData.getwO3();

        Description descriptionO3 = new Description();
        descriptionO3.setText("");
        wykresO3.setDescription(descriptionO3);
        wykresO3.getAxisRight().setDrawLabels(false);

        XAxis xAxisO3 = wykresO3.getXAxis();
        xAxisO3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisO3.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxisO3.setLabelCount(24);
        xAxisO3.setGranularity(1f);
        xAxisO3.setTextColor(Color.BLACK);

        YAxis yAxisO3 = wykresO3.getAxisLeft();
        yAxisO3.setAxisLineWidth(2f);
        yAxisO3.setAxisLineColor(Color.BLACK);

        List<Entry> dane_O3 = new ArrayList<>();
        dane_O3.add(new Entry(0,Float.parseFloat(wO3[0])));
        dane_O3.add(new Entry(1,Float.parseFloat(wO3[1])));
        dane_O3.add(new Entry(2,Float.parseFloat(wO3[2])));
        dane_O3.add(new Entry(3,Float.parseFloat(wO3[3])));
        dane_O3.add(new Entry(4,Float.parseFloat(wO3[4])));
        dane_O3.add(new Entry(5,Float.parseFloat(wO3[5])));
        dane_O3.add(new Entry(6,Float.parseFloat(wO3[6])));
        dane_O3.add(new Entry(7,Float.parseFloat(wO3[7])));
        dane_O3.add(new Entry(8,Float.parseFloat(wO3[8])));
        dane_O3.add(new Entry(9,Float.parseFloat(wO3[9])));
        dane_O3.add(new Entry(10,Float.parseFloat(wO3[10])));
        dane_O3.add(new Entry(11,Float.parseFloat(wO3[11])));
        dane_O3.add(new Entry(12,Float.parseFloat(wO3[12])));
        dane_O3.add(new Entry(13,Float.parseFloat(wO3[13])));
        dane_O3.add(new Entry(14,Float.parseFloat(wO3[14])));
        dane_O3.add(new Entry(15,Float.parseFloat(wO3[15])));
        dane_O3.add(new Entry(16,Float.parseFloat(wO3[16])));
        dane_O3.add(new Entry(17,Float.parseFloat(wO3[17])));
        dane_O3.add(new Entry(18,Float.parseFloat(wO3[18])));
        dane_O3.add(new Entry(19,Float.parseFloat(wO3[19])));
        dane_O3.add(new Entry(20,Float.parseFloat(wO3[20])));
        dane_O3.add(new Entry(21,Float.parseFloat(wO3[21])));
        dane_O3.add(new Entry(22,Float.parseFloat(wO3[22])));
        dane_O3.add(new Entry(23,Float.parseFloat(wO3[23])));

        LineDataSet wykres_O3 = new LineDataSet(dane_O3, "O3");
        wykres_O3.setColor(Color.BLACK);
        LineData O3LD = new LineData(wykres_O3);
        wykresO3.setData(O3LD);
        wykresO3.invalidate();
        wykresO3.setBackgroundColor(Color.WHITE);
    }
}