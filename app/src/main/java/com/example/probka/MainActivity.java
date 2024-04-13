package com.example.probka;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API = "dc94dfa0079ac3bf956a89f5f6b56d81";
    final String Pollution_URL = "http://api.openweathermap.org/data/2.5/air_pollution";

    final String CityName_URL = "http://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    String latitude, longitude;


    TextView cityName;
    TextView PM10_TextView;
    TextView PM2_TextView;
    TextView CO_TextView;

    TextView Data_Godzina;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    ImageButton LocalizationButton;
    ImageButton HomeButton;
    TextView SmogButton;
    TextView PylkiButton;
    CardView HomeCardView;
    CardView SmogCardView;
    ImageButton PM10ExitButton;
    ImageButton PM2ExitButton;
    ImageButton COExitButton;
    Dialog PM10Dialog;
    Dialog PM2Dialog;
    Dialog CODialog;
    CardView PylkiCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Dialog PM10Dialog = new Dialog(MainActivity.this);
        Dialog PM2Dialog = new Dialog(MainActivity.this);
        Dialog CODialog = new Dialog(MainActivity.this);

        PM10_TextView = (TextView) findViewById(R.id.PM10_TextView);
        PM2_TextView = (TextView) findViewById(R.id.PM2_TextView);
        CO_TextView = (TextView) findViewById(R.id.CO_TextView);
        HomeCardView = (CardView) findViewById(R.id.HomeCardView);
        SmogCardView = (CardView) findViewById(R.id.SmogCardView);
        PylkiCardView = (CardView) findViewById(R.id.PylkiCardView);
        cityName = (TextView) findViewById(R.id.CityName);
        LocalizationButton = findViewById(R.id.Localization);
        Data_Godzina = findViewById(R.id.DateHour);

        PM10Dialog.setContentView(R.layout.pm10_layout);
        PM2Dialog.setContentView(R.layout.pm2_layout);
        CODialog.setContentView(R.layout.pm1_layout);

        LocalizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tu cos dodac, żeby aktualizowała się lokalizacja po kliknięciu w button
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

    @Override
    protected void onResume() {
        super.onResume();
        getPollutionforCurrentLocation();
    }

    private void getPollutionforCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

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
                Toast.makeText(MainActivity.this, "Poprawno pobrano lokalizację.", Toast.LENGTH_SHORT).show();
                getPollutionforCurrentLocation();
            }
            else
            {
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
                        //Toast.makeText(MainActivity.this, "Niepoprawna nazwa miasta.", Toast.LENGTH_SHORT).show();
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
                    }
                }
        );
    }

    private void updateUI(pollutionData pollution)
    {
        CO_TextView.setText(pollution.getmCO());
        PM10_TextView.setText(pollution.getmPM10());
        PM2_TextView.setText(pollution.getmPM2());
        int resourceID = getResources().getIdentifier(pollution.getmIcon(), "drawable", getPackageName());
    }

    private void updateCN(cityNameData cityNameD)
    {
        cityName.setText(cityNameD.getmCityName());
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String date = formatter.format(today);
        Data_Godzina.setText(date);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager != null)
        {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}