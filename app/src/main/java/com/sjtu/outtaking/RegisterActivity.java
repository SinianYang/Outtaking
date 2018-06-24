package com.sjtu.outtaking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextView error = findViewById(R.id.error);
        Button back = findViewById(R.id.back);
        Button register = findViewById(R.id.register);
        final EditText username = findViewById(R.id.account);
        final EditText userpwd = findViewById(R.id.pwd);
        final EditText userphone = findViewById(R.id.phone);
        final EditText useremail = findViewById(R.id.email);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            boolean flag = true;
                            if(username.getText().toString().equals("")){
                                flag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("请完整填写信息");
                                        error.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            if(userpwd.getText().toString().equals("")){
                                flag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("请完整填写信息");
                                        error.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            if(userphone.getText().toString().equals("")){
                                flag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("请完整填写信息");
                                        error.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            if(useremail.getText().toString().equals("")){
                                flag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("请完整填写信息");
                                        error.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                            if(flag) {
                                //注册驱动
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setText("注册中...");
                                        error.setVisibility(error.VISIBLE);
                                    }
                                });
                                Class.forName("com.mysql.jdbc.Driver");
                                String url = "jdbc:mysql://45.32.58.255:3306/Outtaking";
                                Connection conn = DriverManager.getConnection(url, "root", "123456");
                                Statement stmt = conn.createStatement();
                                String sql = "select * from Users where UserName=\'" + username.getText().toString() + "\';";
                                ResultSet rs = stmt.executeQuery(sql);
                                if (!rs.next()) {
                                    sql = "insert into Users(UserName,UserPwd,UserPhone,UserEmail) values(\'" + username.getText().toString() + "\',\'" + userpwd.getText().toString() + "\',\'" + userphone.getText().toString() + "\',\'" + useremail.getText().toString() + "\');";
                                    stmt.executeUpdate(sql);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showMessagebox();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            error.setText("该用户名已被占用");
                                            error.setVisibility(error.VISIBLE);
                                        }
                                    });
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showMessagebox()
    {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("注册成功，前往登录")
                .setPositiveButton("确定", click1).show();
    }

    private DialogInterface.OnClickListener click1=new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface arg0,int arg1)
      {
          TextView error = findViewById(R.id.error);
          error.setVisibility(error.INVISIBLE);
          Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
          startActivity(intent);
      }
    };
}
