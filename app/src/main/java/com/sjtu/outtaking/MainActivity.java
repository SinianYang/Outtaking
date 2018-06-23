    package com.sjtu.outtaking;

    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Environment;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.KeyEvent;
    import android.view.View;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.TextView;

    import com.baidu.location.LocationClient;
    import com.baidu.location.LocationClientOption;
    import com.baidu.mapapi.SDKInitializer;
    import com.baidu.mapapi.map.BaiduMap;
    import com.baidu.mapapi.map.BitmapDescriptor;
    import com.baidu.mapapi.map.BitmapDescriptorFactory;
    import com.baidu.mapapi.map.MapStatusUpdate;
    import com.baidu.mapapi.map.MapStatusUpdateFactory;
    import com.baidu.mapapi.map.MapView;
    import com.baidu.mapapi.map.Marker;
    import com.baidu.mapapi.map.MarkerOptions;
    import com.baidu.mapapi.model.LatLng;

    import java.io.FileNotFoundException;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.util.Vector;
    import java.util.logging.Logger;


    public class MainActivity extends AppCompatActivity {

        private MapView mMapView = null;
        private LocationClient mLocationClient;
        BaiduMap mMap;
        Marker mMarker;
        Marker myLocationMarker;
        MyLocationListener myListener;
        LatLng curLocation;
        int[] resulttour;
        int curOrderIndex = 1;
        Vector<String> OrderPhones;
        Vector<String> OrderAddresses;

        //为了不每次获取当前位置的时候都创建标记，将标记设置为成员变量
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
            mMap = mMapView.getMap();

            ListView listView=(ListView)findViewById(R.id.left_drawer);
            String[] mMenuTitles=getResources().getStringArray(R.array.menu_drawer_list);
            try{
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.drawer_list_item,mMenuTitles);
                listView.setAdapter(arrayAdapter);
            }catch (Exception e){
                e.printStackTrace(System.out);
            }
            listView.setOnItemClickListener(new DrawerItemClickListener());

            final EditText etContent = (EditText) findViewById(R.id.etContent);
            Button btnSeek = (Button) findViewById(R.id.btnSeek);
            btnSeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String content = etContent.getText().toString();
                            AddressToLatitudeLongitude at = new AddressToLatitudeLongitude(content);
                            at.getLatAndLngByAddress();
                            getLocationByLL(at.getLatitude(), at.getLongitude());
                        }
                    }).start();
                }
            });

            myListener =  new MyLocationListener(mMap, myLocationMarker);

            //注册定位客户端，并且注册定位监听器
            mLocationClient = new LocationClient(this);
            mLocationClient.registerLocationListener(myListener);

            //创建定位客户端的 选项 ，在这里面设置 定位的类型，CoorType，以及是否打开Gps，间隔事件ScanSpan
            LocationClientOption options = new LocationClientOption();
            options.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
            options.setCoorType("bd09ll");//latitue longtitute
            options.setOpenGps(true);
            options.setIsNeedAddress(true);
            options.setProdName("yangzhendan");
            options.setScanSpan(1000);
            mLocationClient.setLocOption(options);
            mLocationClient.start();//注意这里 要用到 start而不是mLocationClient.requestLocation()

            Button btnLoad =findViewById(R.id.btnLoad);
            final TextView error = findViewById(R.id.error);
            btnLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(){
                        @Override
                        public void run() {
                            try{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        error.setVisibility(error.INVISIBLE);
                                    }
                                });
                                Class.forName("com.mysql.jdbc.Driver");
                                String url = "jdbc:mysql://45.32.58.255:3306/Outtaking";
                                Connection conn = DriverManager.getConnection(url,"root","123456");
                                Statement stmt = conn.createStatement();
                                Intent i=getIntent();
                                String id=i.getStringExtra("ID");
                                String sql="select Address,Phone from Orders where UserId="+id+";";
                                ResultSet rs = stmt.executeQuery(sql);
                                Vector<String> addresses=new Vector<>();
                                String ll1=new String();
                                String ll2=new String();
                                int count=0;
                                getCurLocation();
                                ll1+=curLocation.latitude+","+curLocation.longitude+"|";
                                OrderPhones = new Vector<>();
                                OrderAddresses = new Vector<>();
                                while(rs.next()){
                                    OrderPhones.add(rs.getString("Phone"));
                                    OrderAddresses.add(rs.getString("Address"));
                                    count++;
                                    String address=rs.getString("Address");
                                    addresses.add(address);
                                    AddressToLatitudeLongitude at = new AddressToLatitudeLongitude(address);
                                    at.getLatAndLngByAddress();
                                    if(count<=4)
                                        ll1+=at.getLatitude()+","+at.getLongitude()+"|";
                                    else
                                        ll2+=at.getLatitude()+","+at.getLongitude()+"|";
                                    Mark(at.getLatitude(),at.getLongitude());
                                }
                                ll1=ll1.substring(0,ll1.length()-1);
                                if(!ll2.isEmpty())
                                    ll2 =ll2.substring(0,ll2.length()-1);
                                Log.d("LL1",ll1);
                                Log.d("LL2",ll2);
                                CalculateDistanceMatrix calculateDistanceMatrix=new CalculateDistanceMatrix();
                                calculateDistanceMatrix.getMatrix(addresses,ll1,ll2);
                                int[][] matrix = calculateDistanceMatrix.getMatrix();
                                ACO aco = new ACO();
                                aco.init(matrix, 1000);
                                aco.run(200);
                                aco.ReportResult();
                                resulttour = aco.resulttour;

                                for(int k = 0; k < OrderPhones.size(); k++){
                                    System.out.println(OrderPhones.get(k));
                                    System.out.println(OrderAddresses.get(k));
                                }
                                stmt.close();
                                rs.close();
                                conn.close();
                            }catch(ClassNotFoundException e){
                                Log.v("final","fail to connect"+" "+e.getMessage());
                            }catch(SQLException e){
                                Log.v("final","fail to connect"+" "+e.getMessage());
                            }
                        }

                    }.start();
                }
            });

            Button btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(OrderAddresses == null || OrderAddresses.size() == 0 || resulttour == null){
                                error.setText("请先导入订单");
                                error.setVisibility(error.VISIBLE);
                            }else{
                                if(curOrderIndex < resulttour.length - 2){
                                    curOrderIndex++;
                                    error.setVisibility(error.INVISIBLE);
                                }else{
                                    error.setText("没有更多订单");
                                    error.setVisibility(error.VISIBLE);
                                }
                            }
                        }
                    });
                }
            });
        }

        public void Mark(double la, double lg)
        {
            //地理坐标的数据结构
            LatLng latLng = new LatLng(la, lg);
            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.draggable(true);
            //必须要设置option的icon，而且 这个 icon 必须是BitmapDescriptor的。
            BitmapDescriptor bitmapFactory = BitmapDescriptorFactory.fromResource(R.drawable.location_marker_green);
            options.icon(bitmapFactory);
            //第一次的时候，通过强制类型转换将mMap.addOverlay(options) 变成Marker，因为它返回的其实是Overlay的
            //Overlay和Marker的父类，所以要强制类型转换
            Marker marker =(Marker)mMap.addOverlay(options);
        }

        public void getCurLocation(){
            curLocation = myListener.getCurLocation();
            System.out.println("CurLocation: " + curLocation);
        }

        public void clearMark(){
            mMap.clear();
        }

        /*
         *根据经纬度前往
         */
        public void getLocationByLL(double la, double lg)
        {
            //地理坐标的数据结构
            LatLng latLng = new LatLng(la, lg);
            if(mMarker == null) {
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.draggable(true);
                //必须要设置option的icon，而且 这个 icon 必须是BitmapDescriptor的。
                BitmapDescriptor bitmapFactory = BitmapDescriptorFactory.fromResource(R.drawable.location_marker_red);
                options.icon(bitmapFactory);
                //第一次的时候，通过强制类型转换将mMap.addOverlay(options) 变成Marker，因为它返回的其实是Overlay的
                //Overlay和Marker的父类，所以要强制类型转换
                mMarker = (Marker) mMap.addOverlay(options);
            }else{
                mMarker.setPosition(latLng);
            }
            //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
            mMap.setMapStatus(msu);
        }

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
                    String phone_no;
                    if(OrderPhones == null){
                        phone_no = "";
                    }else {
                        phone_no = OrderPhones.get(resulttour[curOrderIndex] - 1);
                    }
                    intent1.putExtra("phone_no",phone_no);
                    startActivity(intent1);
                    break;
                case 3:
                    Intent intent3 = new Intent(MainActivity.this,DistanceActivity.class);
                    startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 = new Intent();
                    String dh_address;
                    if(OrderAddresses == null){
                        dh_address = "上海交通大学闵行校区菁菁堂";
                    }
                    else{
                        dh_address = OrderAddresses.get(resulttour[curOrderIndex] - 1);
                    }
                    System.out.println("导航地址: " + dh_address);
                    intent4.setData(Uri.parse("baidumap://map/navi?query="+dh_address));
                    startActivity(intent4);
                    break;
                case 5:
                    System.exit(0);
                    break;
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mMapView.onDestroy();
            mLocationClient.unRegisterLocationListener(myListener);
            //千万别忘了这个stop，如果不stop那么会继续执行
            mLocationClient.stop();
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

    }