package com.sjtu.outtaking;


import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

/**
 * Created by 秦皓喆 on 2018/6/23.
 */

public class CalculateDistanceMatrix {
    private int[][] matrix;
    String url1,url2;

    public int[][] getMatrix(Vector<String> addresses,String ll1,String ll2){
                matrix = new int[][]
                {{0,623,1412,799,479,1722,1180,564,894,397},
                {623,0,883,691,370,1193,618,211,1000,606},
                {1412,883,0,1373,966,306,264,939,1522,1327},
                {799,691,1373,0,1010,1683,1108,813,1520,1114},
                {479,370,966,1010,0,1276,745,196,611,416},
                {1716,1193,306,1683,1276,0,574,1249,1628,1433},
                {1180,618,264,1108,745,574,0,707,1333,1138},
                {564,211,939,813,196,1249,707,0,826,547},
                {879,1000,1522,1528,611,1655,1333,826,0,481},
                {397,606,1327,1114,416,1460,1138,547,481,0}};
//        int length=addresses.size()+1;
//        matrix=new int[length][length];
//        if(!ll2.isEmpty()) {
//            url1 = "http://api.map.baidu.com/routematrix/v2/riding?"
//                    + "output=json&origins=" + ll1 + "&destinations=" + ll1+"|"+ll2 + "&ak=x2Y5ecxTQ45YAEAzgzrFiZt3p1OQOEO0";
//            url2 = "http://api.map.baidu.com/routematrix/v2/riding?"
//                    + "output=json&origins=" + ll2 + "&destinations=" + ll1 + "|"+ll2+"&ak=x2Y5ecxTQ45YAEAzgzrFiZt3p1OQOEO0";
//        }
//        else {
//            url1 = "http://api.map.baidu.com/routematrix/v2/riding?"
//                    + "output=json&origins=" + ll1 + "&destinations=" + ll1 + "&ak=x2Y5ecxTQ45YAEAzgzrFiZt3p1OQOEO0";
//        }
//        URL myURL1 = null,myURL2=null;
//        URLConnection httpsConn = null;
//        try {
//            myURL1 = new URL(url1);
//            myURL2 = new URL(url2);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//         try{
//            httpsConn = myURL1.openConnection();//建立连接
//            if (httpsConn != null) {
//                InputStreamReader insr = new InputStreamReader(//传输数据
//                        httpsConn.getInputStream(), "UTF-8");
//                BufferedReader br = new BufferedReader(insr);
//                String data = null;
//                if ((data = br.readLine()) != null) {
//                    System.out.println(data);
//                    //{"status":0,"result":[{"distance":{"text":"425米","value":425},"duration":{"text":"2分钟","value":127}}],"message":"成功"}
//                    JSONObject object=new JSONObject(data);
//                    JSONArray arr=object.getJSONArray("result");
//
//                    for (int i = 0; i < arr.length(); i++) {
//                            JSONObject dst=arr.getJSONObject(i).getJSONObject("distance");
//                            matrix[i / length][i% length]=dst.getInt("value");
//                    }
//
//                }
//                insr.close();
//                br.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//             e.printStackTrace();
//         }
//        if(!ll2.isEmpty()) {
//            try {
//                httpsConn = myURL2.openConnection();//建立连接
//                if (httpsConn != null) {
//                    InputStreamReader insr = new InputStreamReader(//传输数据
//                            httpsConn.getInputStream(), "UTF-8");
//                    BufferedReader br = new BufferedReader(insr);
//                    String data = null;
//                    if ((data = br.readLine()) != null) {
//                        System.out.println(data);
//                        //{"status":0,"result":[{"distance":{"text":"425米","value":425},"duration":{"text":"2分钟","value":127}}],"message":"成功"}
//                        JSONObject object=new JSONObject(data);
//                        JSONArray arr=object.getJSONArray("result");
//
//                        for (int i = 0; i < arr.length(); i++) {
//                            JSONObject dst=arr.getJSONObject(i).getJSONObject("distance");
//                            matrix[5+i / length][i% length]=dst.getInt("value");
//                        }
//                    }
//                    insr.close();
//                    br.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        for(int i=0;i<length;i++){
//            for(int j=0;j<length;j++){
//                System.out.println("MATRIX: " + String.valueOf(matrix[i][j]));
//            }
//        }
        return null;
    }

    public int[][] getMatrix(){
        return this.matrix;
    }
}
