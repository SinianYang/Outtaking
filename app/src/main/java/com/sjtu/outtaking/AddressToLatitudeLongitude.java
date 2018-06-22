package com.sjtu.outtaking;

/**
 * Created by 86506 on 2018/6/22.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wanglei on 2017/6/20.
 * 根据百度地图API，根据地址得到经纬度
 */
public class AddressToLatitudeLongitude {
    private String address = "上海";//地址
    private double Latitude;//纬度
    private double Longitude;//经度

    public AddressToLatitudeLongitude(String addr_str) {
        this.address = addr_str;
    }

    /*
     *根据地址得到地理坐标
     */
    public int getLatAndLngByAddress() {
        String addr = "";
        String lat = "";
        String lng = "";
        try {
            addr = java.net.URLEncoder.encode(address, "UTF-8");//编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?"
                + "address=%s&ak=YHmWeBUYY4WLMGHuPVSxGmgPGDndjSbS&mcode=A4:09:51:BC:43:90:29:22:CC:E3:93:4A:11:5E:5A:55:64:7B:02:77;com.sjtu.outtaking&output=json", addr);
        URL myURL = null;
        URLConnection httpsConn = null;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpsConn = (URLConnection) myURL.openConnection();//建立连接
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(//传输数据
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    System.out.println(data);
                    String status = data.substring(10,11);
                    if(!status.equals("0"))
                        return -1;
                    //这里的data为以下的json格式字符串,因为简单，所以就不使用json解析了，直接字符串处理
                    //{"status":0,"result":{"location":{"lng":118.77807440802562,"lat":32.05723550180587},"precise":0,"confidence":12,"level":"城市"}}
                    lat = data.substring(data.indexOf("\"lat\":") + ("\"lat\":").length(), data.indexOf("},\"precise\""));
                    lng = data.substring(data.indexOf("\"lng\":") + ("\"lng\":").length(), data.indexOf(",\"lat\""));
                }
                insr.close();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.Latitude = Double.parseDouble(lat);
        this.Longitude = Double.parseDouble(lng);
        return 0;
    }

    public Double getLatitude() {
        return this.Latitude;
    }

    public Double getLongitude() {
        return this.Longitude;
    }
}