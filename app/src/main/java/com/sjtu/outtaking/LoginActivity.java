package com.sjtu.outtaking;

import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText username = findViewById(R.id.account);
        final EditText userpwd = findViewById(R.id.pwd);
        final TextView error = findViewById(R.id.error);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            boolean flag = true;
                            if(username.getText().toString().equals("") || userpwd.getText().toString().equals("")){
                                flag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("用户名或密码错误");
                                        error.setVisibility(error.VISIBLE);
                                    }
                                });
                            }
                            if(flag) {
                                //注册驱动
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("登录中...");
                                        error.setVisibility(error.VISIBLE);
                                    }
                                });
                                Class.forName("com.mysql.jdbc.Driver");
                                String url = "jdbc:mysql://45.32.58.255:3306/Outtaking";
                                Connection conn = DriverManager.getConnection(url, "root", "123456");
                                Statement stmt = conn.createStatement();
                                String sql = "select UserPwd,UserId from Users where UserName=\'" + username.getText().toString() + "\';";
                                ResultSet rs = stmt.executeQuery(sql);
                                rs.next();
                                final String string = rs.getString("UserId");
                                if (userpwd.getText().toString().equals(rs.getString("UserPwd"))) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            error.setVisibility(error.INVISIBLE);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("ID", string);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            error.setText("用户名或密码错误");
                                            error.setVisibility(error.VISIBLE);
                                        }
                                    });
                                    //error.setVisibility(error.VISIBLE);
                                }
                                rs.close();
                                stmt.close();
                                conn.close();
                            }
                        }catch(ClassNotFoundException e){
                            Log.v("final","fail to connect"+" "+e.getMessage());
                        }catch(SQLException e){
                            Log.v("final","fail to connect"+" "+e.getMessage());
                        }
                    }
                }.start();
            }
        });
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                if(username.getText().toString().equals("zdyang") && userpwd.getText().toString().equals("123456")) {
//                    error.setVisibility(error.INVISIBLE);
//                    startActivity(intent);
//                }
//                else{
//                    error.setVisibility(error.VISIBLE);
//                }
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}