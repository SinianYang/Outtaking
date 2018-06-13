package com.sjtu.outtaking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MapView mMapView = null;
    private LocationManager locationManager;
    private String provider;
    BaiduMap baiduMap;
    boolean ifFrist = true;
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.bmapView);


        // 获取baiduMap对象
        baiduMap = mMapView.getMap();
        // 设置可改变地图位置
        baiduMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(this, "当前不能提供位置信息", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            navigateTo(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView=(ListView)findViewById(R.id.left_drawer);
        String[] mMenuTitles=getResources().getStringArray(R.array.menu_drawer_list);

        final EditText myEditText_site = (EditText) findViewById(R.id.editText_site);
        Button button_site = (Button) findViewById(R.id.button_sitesearch);
        button_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String site_str = myEditText_site.getText().toString();
                System.out.print(site_str);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AddressToLatitudeLongitude at = new AddressToLatitudeLongitude(site_str);
                        System.out.print(site_str);
                        at.getLatAndLngByAddress();
                        getLocationByLL(at.getLatitude(), at.getLongitude());
                    }
                }).start();


            }
        });
        try{
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.drawer_list_item,mMenuTitles);
            listView.setAdapter(arrayAdapter);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

        listView.setOnItemClickListener(new DrawerItemClickListener());


    }

    private void navigateTo(Location location) {
        // 按照经纬度确定地图位置
        if (ifFrist) {
            LatLng ll = new LatLng(location.getLatitude(),
                    -location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 移动到某经纬度
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(5f);
            // 放大
            baiduMap.animateMapStatus(update);

            ifFrist = false;
        }
        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(-location.getLongitude());
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 位置改变则重新定位并显示地图
            navigateTo(arg0);
        }
    };


    private void selectItem(int position){

        switch (position){
            case 0:
                DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawers();
                break;
            case 1:

                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                Intent intent2 = getIntent();
                String data = intent2.getStringExtra("ID");
                intent.putExtra("UserId",data);
                startActivity(intent);
                break;
            case 2:
                Intent intent1 = new Intent(MainActivity.this, CallActivity.class);
                intent1.putExtra("phone_no","12345");

                startActivity(intent1);

                break;
            case 3:

                break;
            case 4:

                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getLocationByLL(double la, double lg)
    {
        //地理坐标的数据结构
        LatLng latLng = new LatLng(la, lg);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomBy(5f);
        // 放大
        baiduMap.animateMapStatus(update);

        ifFrist = false;
    }
}
