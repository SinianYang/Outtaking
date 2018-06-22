package com.sjtu.outtaking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DistanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        final EditText startAddress = findViewById(R.id.startAddress);
        final EditText endAddress = findViewById(R.id.endAddress);
        final TextView disance = findViewById(R.id.distance);
        final TextView errormsg = findViewById(R.id.error_address);
        Button calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(startAddress.getText().toString().equals("") || endAddress.getText().toString().equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    errormsg.setVisibility(View.VISIBLE);
                                    disance.setText("");
                                }
                            });
                        }else {
                            AddressToLatitudeLongitude at1 = new AddressToLatitudeLongitude(startAddress.getText().toString());
                            int status1 = at1.getLatAndLngByAddress();
                            AddressToLatitudeLongitude at2 = new AddressToLatitudeLongitude(endAddress.getText().toString());
                            int status2 = at2.getLatAndLngByAddress();
                            if (status1 != -1 && status2 != -1) {
                                double sll, sln, ell, eln;
                                sll = at1.getLatitude();
                                sln = at1.getLongitude();
                                ell = at2.getLatitude();
                                eln = at2.getLongitude();
                                CalculateDistance cd = new CalculateDistance(sll, sln, ell, eln);
                                int dis = cd.getDistance();

                                float tmp;
                                final String res;
                                if (dis >= 1000) {
                                    tmp = dis * 1f / 1000;
                                    res = tmp + " 公里";
                                } else {
                                    res = dis + " 米";
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        errormsg.setVisibility(View.INVISIBLE);
                                        disance.setText(res);
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        errormsg.setVisibility(View.VISIBLE);
                                        disance.setText("");
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
