package com.example.probka;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    ImageButton HomeButton;
    TextView SmogButton;
    TextView PylkiButton;
    CardView HomeCardView;
    CardView SmogCardView;
    TextView PM10_TextView;
    TextView PM2_TextView;
    TextView PM1_TextView;
    ImageButton PM10ExitButton;
    ImageButton PM2ExitButton;
    ImageButton PM1ExitButton;
    Dialog PM10Dialog;
    Dialog PM2Dialog;
    Dialog PM1Dialog;
    CardView PylkiCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Dialog PM10Dialog=new Dialog(MainActivity.this);
        Dialog PM2Dialog = new Dialog(MainActivity.this);
        Dialog PM1Dialog= new Dialog(MainActivity.this);
        PM10_TextView=(TextView) findViewById(R.id.PM10_TextView);
        PM2_TextView=(TextView)findViewById(R.id.PM2_TextView);
        PM1_TextView= (TextView)findViewById(R.id.PM1_TextView);
        HomeCardView=(CardView) findViewById(R.id.HomeCardView);
        SmogCardView=(CardView) findViewById(R.id.SmogCardView) ;
        PylkiCardView=(CardView) findViewById(R.id.PylkiCardView);
        PM10Dialog.setContentView(R.layout.pm10_layout);
        PM2Dialog.setContentView(R.layout.pm2_layout);
        PM1Dialog.setContentView(R.layout.pm1_layout);

        PM1_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PM1Dialog.show();
                ImageButton PM1ExitButton=(ImageButton) PM1Dialog.findViewById(R.id.PM1ExitButton);
                PM1ExitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PM1Dialog.dismiss();
                    }
                });
            }
        });
        PM2_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PM2Dialog.show();
                ImageButton PM2ExitButton=(ImageButton) PM2Dialog.findViewById(R.id.PM2ExitButton);
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
                    ImageButton PM10ExitButton=(ImageButton)PM10Dialog.findViewById(R.id.PM10ExitButton);
                    PM10ExitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PM10Dialog.dismiss();
                        }
                    });

            }
        });

        PylkiButton=(TextView) findViewById(R.id.PylkiButton);
        PylkiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PylkiCardView.getVisibility()==CardView.VISIBLE)
                {

                }
                else {
                    PylkiCardView.setVisibility(CardView.VISIBLE);
                    if(HomeCardView.getVisibility()==CardView.VISIBLE)
                    {
                        HomeCardView.setVisibility(View.INVISIBLE);
                    }
                    else if(SmogCardView.getVisibility()==CardView.VISIBLE){
                        SmogCardView.setVisibility(View.INVISIBLE);

                    }

                }
            }
        });
        SmogButton=(TextView) findViewById(R.id.SmogButton);
        SmogButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SmogCardView.getVisibility()==CardView.VISIBLE){

                }
                else {
                    SmogCardView.setVisibility(CardView.VISIBLE);
                    if(HomeCardView.getVisibility()==CardView.VISIBLE)
                    {
                        HomeCardView.setVisibility(CardView.INVISIBLE);
                    }
                    else if(PylkiCardView.getVisibility()==CardView.VISIBLE)
                    {
                        PylkiCardView.setVisibility(CardView.INVISIBLE);
                    }
                }
            }
        }));
        HomeButton=(ImageButton) findViewById(R.id.HomeButton);
        HomeButton.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View view)  {
             if(HomeCardView.getVisibility()==CardView.VISIBLE)
             {

             }
             else {
                 HomeCardView.setVisibility(CardView.VISIBLE);
                 if(SmogCardView.getVisibility()==CardView.VISIBLE) {
                     SmogCardView.setVisibility(CardView.INVISIBLE);
                 } else if (PylkiCardView.getVisibility()==CardView.VISIBLE) {
                     PylkiCardView.setVisibility(View.INVISIBLE);
                 }
             }
          }
        }) ;

    }

}