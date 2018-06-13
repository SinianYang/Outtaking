package com.sjtu.outtaking;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        final TextView textview=new TextView(this);
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
                    String sql="select CustomerName,Restaurant,Food,CTime,Ltime from Orders where UserId="+id+";";

                    final ResultSet rs = stmt.executeQuery(sql);

                    rs.next();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ConstraintLayout   constraintLayout= (ConstraintLayout) findViewById(R.id.order);

                            try {
                                textview.setText(rs.getString("Restaurant"));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            constraintLayout.addView(textview);
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
