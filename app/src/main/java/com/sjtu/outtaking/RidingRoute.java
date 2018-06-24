package com.sjtu.outtaking;

import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 秦皓喆 on 2018/6/23.
 */

public class RidingRoute {

    private List<LatLng> address;
    private int[] order;
    private List<OverlayOptions> lines=new ArrayList<>();
    private int[] color={0xAAFF0000,0xAA00FF00,0xAA0000FF,0xAAFFFF00,0xAA00FFFF,0xAA800080,0xAAFFA500,0xAAFFB6C1,0xAA2F4F4F,0xAA808080,0xAA800000};


    public void setAddress(List<LatLng> address) {
        this.address = address;
    }

    public List<LatLng> getAddress() {
        return address;
    }

    public void setOrder(int[] order) {
        this.order = order;
    }

    public int[] getOrder() {
        return order;
    }

    public List<OverlayOptions> getLines() {
        return lines;
    }

    public void setLines(List<OverlayOptions> lines) {
        this.lines = lines;
    }

    public void PlanRoute(){
        for(int i=0;i<address.size();i++) {
            System.out.println(i);
            List<LatLng> routeplan=new ArrayList<>();
            String url = "http://api.map.baidu.com/direction/v2/riding?"
                    + "origin=" + address.get(order[i]).latitude + "," + address.get(order[i]).longitude + "&destination=" + address.get(order[i+1]).latitude + "," + address.get(order[i+1]).longitude + "&ak=x2Y5ecxTQ45YAEAzgzrFiZt3p1OQOEO0";
            System.out.println(url);
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
                        //Distance = Integer.parseInt(data.substring(data.indexOf("\"value\":") + ("\"value\":").length(), data.indexOf("},\"duration\"")));
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray routes = result.getJSONArray("routes");
                        JSONObject route = routes.getJSONObject(0);
                        JSONArray steps = route.getJSONArray("steps");
                        for (int j = 0; j < steps.length(); j++) {
                            JSONObject step = steps.getJSONObject(j);
                            JSONObject start_location = step.getJSONObject("stepOriginLocation");
                            routeplan.add(new LatLng(start_location.getDouble("lat"), start_location.getDouble("lng")));
                            JSONObject end_location = step.getJSONObject("stepDestinationLocation");
                            routeplan.add(new LatLng(end_location.getDouble("lat"), end_location.getDouble("lng")));
                        }
                    }

                    insr.close();
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OverlayOptions line = new PolylineOptions().width(13).color(color[i]).points(routeplan);
            lines.add(line);
        }
    }


}
