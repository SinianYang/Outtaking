package com.sjtu.outtaking;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by 86506 on 2018/6/13.
 */

public class MyLocationListener extends BDAbstractLocationListener {
    private BaiduMap mMap;
    private Marker mMarker;
    private boolean isFirst = true;
    private LatLng curLocation;

    public MyLocationListener(BaiduMap mMap, Marker mMarker){
        this.mMap = mMap;
        this.mMarker = mMarker;
    }

    //回调方法，mLocationClient.start的 回调方法，
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //注意 这个 LatLng，它用与设置 标记的位置和 Map的中心点的位置 。

        LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        setCurLocation(latLng);

        if(mMarker == null) {
            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.draggable(true);
            //必须要设置option的icon，而且 这个 icon 必须是BitmapDescriptor的。
            BitmapDescriptor bitmapFactory = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
            options.icon(bitmapFactory);
            //第一次的时候，通过强制类型转换将mMap.addOverlay(options) 变成Marker，因为它返回的其实是Overlay的
            //Overlay和Marker的父类，所以要强制类型转换
            mMarker = (Marker) mMap.addOverlay(options);
        }else{
            mMarker.setPosition(latLng);
        }


        //设置获取的位置为中心点 。这里面的 参数是 LatLng
        if(isFirst) {
            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLng(latLng);
            mMap.setMapStatus(mapStatus);
            mMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
            isFirst = false;
        }
    }

    public LatLng getCurLocation(){
        return this.curLocation;
    }

    public void setCurLocation(LatLng curLocation){
        this.curLocation = curLocation;
    }
}