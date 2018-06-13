package com.sjtu.outtaking;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        new Thread(){

            @Override

            public void run(){

                try{


                    Class.forName("com.mysql.jdbc.Driver");

                    String url = "jdbc:mysql://45.32.58.255:3306/Outtaking";

                    Connection conn = DriverManager.getConnection(url,"root","123456");

                    Statement stmt = conn.createStatement();
                    Intent i=getIntent();
                    String id=i.getStringExtra("UserId");
                    String sql="select CustomerName,Restaurant,Food,CTime,Ltime,Address from Orders where UserId="+id+";";

                    final ResultSet rs = stmt.executeQuery(sql);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableLayout tableLayout= (TableLayout) findViewById(R.id.table);
                            try {
                                while(rs.next()) {
                                    TextView textView=new TextView(getApplication());
                                    TextView textView2=new TextView(getApplication());
                                    TextView textView3=new TextView(getApplication());
                                    TextView textView4=new TextView(getApplication());
                                    TextView textView5=new TextView(getApplication());
                                    TextView textView6=new TextView(getApplication());
                                    TableRow tableRow=new TableRow(getApplication());
                                    textView.setText(rs.getString("CustomerName"));
                                    textView.setTextColor(Color.rgb(255, 0, 0));
                                    TextView textpra=findViewById(R.id.textView);
                                    textView.setLayoutParams(textpra.getLayoutParams());
                                    tableRow.addView(textView);
                                    textView2.setText(rs.getString("Restaurant"));
                                    textView2.setTextColor(Color.rgb(255, 0, 0));
                                    textView2.setLayoutParams(textpra.getLayoutParams());
                                    tableRow.addView(textView2);
                                    textView3.setText(rs.getString("Food"));
                                    textView3.setTextColor(Color.rgb(255, 0, 0));
                                    textView3.setLayoutParams(textpra.getLayoutParams());
                                    tableRow.addView(textView3);
                                    textView4.setText(rs.getTime("CTime").toString());
                                    textView4.setTextColor(Color.rgb(255, 0, 0));
                                    textView4.setLayoutParams(textpra.getLayoutParams());
                                    tableRow.addView(textView4);
                                    textView5.setText(rs.getTime("LTime").toString());
                                    textView5.setTextColor(Color.rgb(255, 0, 0));
                                    textView5.setLayoutParams(textpra.getLayoutParams());
                                    tableRow.addView(textView5);
                                    textView6.setText(rs.getString("Address"));
                                    textView6.setTextColor(Color.rgb(255, 0, 0));
                                    textView6.setLayoutParams(textpra.getLayoutParams());
                                    tableRow.addView(textView6);
                                    tableLayout.addView(tableRow);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                }catch(ClassNotFoundException e){

                    Log.v("final","fail to connect"+" "+e.getMessage());

                }catch(SQLException e){

                    Log.v("final","fail to connect"+" "+e.getMessage());

                }

            }

        }.start();
    }
}
