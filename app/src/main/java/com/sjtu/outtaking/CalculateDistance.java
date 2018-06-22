package com.sjtu.outtaking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 86506 on 2018/6/22.
 */

public class CalculateDistance {
    private double SLatitude;   //起点纬度
    private double SLongitude;  //起点经度
    private double ELatitude;   //结束纬度
    private double ELongitude;  //结束经度
    private int Distance;    //两地的距离

    public CalculateDistance(double SLAT,double SLON,double ELAT,double ELON){
        this.SLatitude=SLAT;
        this.SLongitude=SLON;
        this.ELatitude=ELAT;
        this.ELongitude=ELON;
    }

    public int getDistance(){
        String url = "http://api.map.baidu.com/routematrix/v2/riding?"
                + "output=json&origins="+SLatitude+","+SLongitude+"&destinations="+ELatitude+","+ELongitude+"&ak=x2Y5ecxTQ45YAEAzgzrFiZt3p1OQOEO0";
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpsConn = myURL.openConnection();//建立连接
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(//传输数据
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    System.out.println(data);
                    //{"status":0,"result":[{"distance":{"text":"425米","value":425},"duration":{"text":"2分钟","value":127}}],"message":"成功"}
                    Distance = Integer.parseInt(data.substring(data.indexOf("\"value\":") + ("\"value\":").length(), data.indexOf("},\"duration\"")));
                }
                else
                    return -1;
                insr.close();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Distance;
    }
}
