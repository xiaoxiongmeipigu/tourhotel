package com.zjhj.tourhotel.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.SearchResultAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.base.RequestCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationAddrActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener{

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.search_tv)
    TextView searchTv;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.listview)
    ListView listView;

    private SearchResultAdapter searchResultAdapter;
    private List<PoiItem> resultData;
    private GeocodeSearch geocoderSearch;
    private AMap aMap;
    private LatLonPoint searchLatlonPoint;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private boolean isItemClickAction;
    private boolean isfirstinput = true;
    private Marker locationMarker;
    private boolean isInputKeySearch;
    private String inputSearchKey = "";

    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi数据
    private PoiItem firstItem;
    PoiItem currentPoiItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_addr);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        initView();
        init();
        initListener();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        resultData = new ArrayList<>();

        searchResultAdapter = new SearchResultAdapter(this);
        listView.setAdapter(searchResultAdapter);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isItemClickAction && !isInputKeySearch) {
                    geoAddress();
                    startJumpAnimation();
                }
                searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                isInputKeySearch = false;
                isItemClickAction = false;
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter(null);
            }
        });
    }

    private void initListener(){
        listView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 响应逆地理编码
     */
    public void geoAddress() {
//        Log.i("MY", "geoAddress"+ searchLatlonPoint.toString());
        showLoading();
        searchTv.setText("");
        if (searchLatlonPoint != null){
            RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch.getFromLocationAsyn(query);
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        locationMarker.setZIndex(1);

    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (locationMarker != null ) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = locationMarker.getPosition();
            Point point =  aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this,125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            locationMarker.setAnimation(animation);
            //开始动画
            locationMarker.startAnimation();

        } else {
            Log.e("ama","screenMarker is null");
        }
    }

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != searchResultAdapter.getSelectedPosition()) {
                currentPoiItem = (PoiItem) searchResultAdapter.getItem(position);
                LatLng curLatlng = new LatLng(currentPoiItem.getLatLonPoint().getLatitude(), currentPoiItem.getLatLonPoint().getLongitude());

                isItemClickAction = true;

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                searchResultAdapter.setSelectedPosition(position);
                searchResultAdapter.notifyDataSetChanged();
            }
        }
    };

    @OnClick({R.id.back, R.id.search_tv,R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search_tv:
                Intent intent = new Intent(this,SearchAddrActivity.class);
                startActivityForResult(intent, RequestCode.SEARCH_ADDR);
                break;
            case R.id.confirm:
                if(null!=currentPoiItem){
                    Intent intent1 = new Intent();
                    intent1.putExtra("item",currentPoiItem);
                    setResult(RESULT_OK,intent1);
                    finish();
                }else
                    MainToast.showShortToast("请选择地址");

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==RequestCode.SEARCH_ADDR){
                if(null!=data){
                    Tip tip = data.getParcelableExtra("item");
                    if(null!=tip){
                        searchPoi(tip);
                    }
                }
            }
        }
    }

    private void searchPoi(Tip result) {
        isInputKeySearch = true;
        inputSearchKey = result.getName();//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        searchLatlonPoint = result.getPoint();
        firstItem = new PoiItem("tip", searchLatlonPoint, inputSearchKey, result.getAddress());
        firstItem.setCityName(result.getDistrict());
        firstItem.setAdName("");
        resultData.clear();
        currentPoiItem = firstItem;
        searchResultAdapter.setSelectedPosition(0);

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchLatlonPoint.getLatitude(), searchLatlonPoint.getLongitude()), 16f));
        searchTv.setText(inputSearchKey);
        doSearchQuery();
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);

                LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                searchLatlonPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                isInputKeySearch = false;

                searchTv.setText("");

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
//        Log.i("MY", "doSearchQuery");
        currentPage = 0;
        query = new PoiSearch.Query("","", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setCityLimit(true);
        query.setPageSize(20);
        query.setPageNum(currentPage);

        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));//
            poiSearch.searchPOIAsyn();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        hideLoading();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String address = result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
                firstItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                currentPoiItem = firstItem;
                doSearchQuery();
            }
        } else {
            Toast.makeText(LocationAddrActivity.this, "error code is " + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * POI搜索结果回调
     * @param poiResult 搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        updateListview(poiItems);
                    } else {
                        Toast.makeText(LocationAddrActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(LocationAddrActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新列表中的item
     * @param poiItems
     */
    private void updateListview(List<PoiItem> poiItems) {
        resultData.clear();
        searchResultAdapter.setSelectedPosition(0);
        resultData.add(firstItem);
        resultData.addAll(poiItems);
        currentPoiItem = firstItem;
        searchResultAdapter.setData(resultData);
        searchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
