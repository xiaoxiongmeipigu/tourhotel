package com.zjhj.tourhotel.activity.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.OptionsPickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiRegionResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.util.JGJBitmapUtils;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.activity.LocationAddrActivity;
import com.zjhj.tourhotel.adapter.shop.ShopDiningAdapter;
import com.zjhj.tourhotel.adapter.shop.ShopPayAdapter;
import com.zjhj.tourhotel.adapter.shop.ShopServiceAdapter;
import com.zjhj.tourhotel.adapter.shop.ShopSpotsAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.base.RequestCode;
import com.zjhj.tourhotel.interfaces.MealDeelListener;
import com.zjhj.tourhotel.util.JGJDataSource;
import com.zjhj.tourhotel.util.LocationUtil;
import com.zjhj.tourhotel.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopEditActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name_tv)
    EditText nameTv;
    @Bind(R.id.cover_pic)
    SimpleDraweeView coverPic;
    @Bind(R.id.restaurant_cat_tv)
    TextView restaurantCatTv;
    @Bind(R.id.discount_rate_tv)
    EditText discountRateTv;
    @Bind(R.id.feature_tv)
    EditText featureTv;
    @Bind(R.id.customer_consumption_tv)
    EditText customerConsumptionTv;
    @Bind(R.id.city_tv)
    TextView cityTv;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.desc_et)
    EditText descEt;
    @Bind(R.id.population_max_et)
    EditText populationMaxEt;
    @Bind(R.id.accept_booking_time_begin_tv)
    TextView acceptBookingTimeBeginTv;
    @Bind(R.id.accept_booking_time_end_tv)
    TextView acceptBookingTimeEndTv;
    @Bind(R.id.booking_time_et)
    EditText bookingTimeEt;
    @Bind(R.id.pay_recyclerView)
    RecyclerView payRecyclerView;
    @Bind(R.id.dining_recyclerView)
    RecyclerView diningRecyclerView;
    @Bind(R.id.tel_et)
    EditText telEt;
    @Bind(R.id.mobile_et)
    EditText mobileEt;
    @Bind(R.id.service_recyclerView)
    RecyclerView serviceRecyclerView;



    String restaurant_cat_id = "";
    String province_id = "";
    String city_id = "";
    String area_id = "";
    PoiItem poiItem;

    ShopSpotsAdapter shopSpotsAdapter;
    List<IndexData> mList;
    List<MapiResourceResult> selSpotsList;

    PhotoDialog photoDialog;
    ArrayList<MapiImageResult> imgs;

    ArrayList<MapiResourceResult> restaurants;
    OptionsPickerView restaurantsOptions;

    OptionsPickerView positionOptions;
    ArrayList<MapiRegionResult> posOptions1Items = new ArrayList<>();
    ArrayList<ArrayList<MapiRegionResult>> posOptions2Items = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<MapiRegionResult>>> posOptions3Items = new ArrayList<>();

    ArrayList<MapiResourceResult> begins;
    OptionsPickerView beginsOptions;

    ArrayList<MapiResourceResult> ends;
    OptionsPickerView endsOptions;


    ShopPayAdapter shopPayAdapter;
    List<IndexData> payList;
    List<MapiResourceResult> selPayList;
    ArrayList<MapiResourceResult> payData;
    OptionsPickerView payOptions;

    ShopDiningAdapter shopDiningAdapter;
    List<IndexData> diningList;
    List<MapiResourceResult> selDiningList;
    ArrayList<MapiResourceResult> diningData;
    OptionsPickerView diningOptions;

    ShopServiceAdapter shopServiceAdapter;
    List<IndexData> serviceList;
    List<MapiResourceResult> selServiceList;
    ArrayList<MapiResourceResult> serviceData;
    OptionsPickerView serviceOptions;

    LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
        initBegin();
        initEnd();
        initPay();
        initDining();
        locationUtil = new LocationUtil(this);
        if (null != userSP && !TextUtils.isEmpty(userSP.getCityJson())) {
            String cityJson = userSP.getCityJson();
            JSONObject jsonObject = JSONObject.parseObject(cityJson);
            Gson gson = new Gson();
            List<MapiRegionResult> result = gson.fromJson(jsonObject.getJSONArray("data").toJSONString(), new TypeToken<List<MapiRegionResult>>() {
            }.getType());
            if (null == result || result.isEmpty()) {
                MainToast.showShortToast("服务器繁忙");
                finish();
                return;
            } else {
                initCityData(result);
            }
        } else {
            loadCity();
        }

    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("酒店管理");
        tvRight.setText("保存");

        selSpotsList = new ArrayList<>();
        mList = new ArrayList<>();
        imgs = new ArrayList<>();
        restaurants = new ArrayList<>();
        begins = new ArrayList<>();
        ends = new ArrayList<>();

        payList = new ArrayList<>();
        payData = new ArrayList<>();
        selPayList = new ArrayList<>();

        diningList = new ArrayList<>();
        diningData = new ArrayList<>();
        selDiningList = new ArrayList<>();

        serviceList = new ArrayList<>();
        serviceData = new ArrayList<>();
        selServiceList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        shopSpotsAdapter = new ShopSpotsAdapter(this, mList);
        recyclerView.setAdapter(shopSpotsAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(OrientationHelper.VERTICAL);
        payRecyclerView.setLayoutManager(linearLayoutManager2);
        shopPayAdapter = new ShopPayAdapter(this, payList);
        payRecyclerView.setAdapter(shopPayAdapter);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(OrientationHelper.VERTICAL);
        diningRecyclerView.setLayoutManager(linearLayoutManager3);
        shopDiningAdapter = new ShopDiningAdapter(this, diningList);
        diningRecyclerView.setAdapter(shopDiningAdapter);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this);
        linearLayoutManager4.setOrientation(OrientationHelper.VERTICAL);
        serviceRecyclerView.setLayoutManager(linearLayoutManager4);
        shopServiceAdapter = new ShopServiceAdapter(this, serviceList);
        serviceRecyclerView.setAdapter(shopServiceAdapter);

        if (photoDialog == null)
            photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
        photoDialog.setImagePath("shop_image.jpg");

        //选项选择器
        restaurantsOptions = new OptionsPickerView(this);

        //城市选项选择器
        positionOptions = new OptionsPickerView(this);

        //开始时间选项选择器
        beginsOptions = new OptionsPickerView(this);
        //开结束时间选项选择器
        endsOptions = new OptionsPickerView(this);

        //支付选项选择器
        payOptions = new OptionsPickerView(this);

        //用餐选项选择器
        diningOptions = new OptionsPickerView(this);

        //便利设施选项选择器
        serviceOptions = new OptionsPickerView(this);

    }

    private void initListener() {

        discountRateTv.addTextChangedListener(new TextWatcher() {//设置小数点后一位
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 1) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 2);
                        discountRateTv.setText(s);
                        discountRateTv.setSelection(s.length());
                    }
                }

                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    discountRateTv.setText(s);
                    discountRateTv.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        discountRateTv.setText(s.subSequence(0, 1));
                        discountRateTv.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        shopServiceAdapter.setOnItemClickListener(new MealDeelListener() {
            @Override
            public void add(View view, int position) {
                serviceOptions.show();
            }

            @Override
            public void minus(View view, int position) {
                MapiResourceResult foodResult = (MapiResourceResult) serviceList.get(position).getData();
                serviceList.remove(position);
                serviceList.remove(position);
                shopServiceAdapter.notifyDataSetChanged();
                if (selServiceList.contains(foodResult))
                    selServiceList.remove(foodResult);
            }
        });

        shopDiningAdapter.setOnItemClickListener(new MealDeelListener() {
            @Override
            public void add(View view, int position) {
                diningOptions.show();
            }

            @Override
            public void minus(View view, int position) {
                MapiResourceResult foodResult = (MapiResourceResult) diningList.get(position).getData();
                diningList.remove(position);
                diningList.remove(position);
                shopDiningAdapter.notifyDataSetChanged();
                Iterator<MapiResourceResult> iterator = selDiningList.iterator();
                while (iterator.hasNext()){
                    MapiResourceResult resourceResult = iterator.next();
                    if(resourceResult.getName().equals(foodResult.getName())){
                        iterator.remove();
                        return;
                    }
                }
            }
        });

        shopSpotsAdapter.setOnItemClickListener(new MealDeelListener() {
            @Override
            public void add(View view, int position) {

                Intent intent = new Intent(ShopEditActivity.this, SpotAddActivity.class);
                startActivityForResult(intent, RequestCode.add_spot);

            }

            @Override
            public void minus(View view, int position) {
                MapiResourceResult foodResult = (MapiResourceResult) mList.get(position).getData();
                mList.remove(position);
                mList.remove(position);
                shopSpotsAdapter.notifyDataSetChanged();
                if (selSpotsList.contains(foodResult))
                    selSpotsList.remove(foodResult);
            }
        });

        shopPayAdapter.setOnItemClickListener(new MealDeelListener() {
            @Override
            public void add(View view, int position) {
                payOptions.show();
            }

            @Override
            public void minus(View view, int position) {
                MapiResourceResult foodResult = (MapiResourceResult) payList.get(position).getData();
                payList.remove(position);
                payList.remove(position);
                shopPayAdapter.notifyDataSetChanged();
                Iterator<MapiResourceResult> iterator = selPayList.iterator();
                while (iterator.hasNext()){
                    MapiResourceResult resourceResult = iterator.next();
                    if(resourceResult.getName().equals(foodResult.getName())){
                        iterator.remove();
                        return;
                    }
                }
                /*if (selPayList.contains(foodResult))
                    selPayList.remove(foodResult);*/
            }
        });

        restaurantsOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiResourceResult resourceResult = restaurants.get(options1);
                restaurant_cat_id = resourceResult.getId();
                restaurantCatTv.setText(TextUtils.isEmpty(resourceResult.getPickerViewText()) ? "" : resourceResult.getPickerViewText());
            }
        });

        beginsOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiResourceResult resourceResult = begins.get(options1);
                acceptBookingTimeBeginTv.setText(TextUtils.isEmpty(resourceResult.getPickerViewText()) ? "" : resourceResult.getPickerViewText());
            }
        });

        endsOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiResourceResult resourceResult = ends.get(options1);
                acceptBookingTimeEndTv.setText(TextUtils.isEmpty(resourceResult.getPickerViewText()) ? "" : resourceResult.getPickerViewText());
            }
        });

        payOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiResourceResult resourceResult = payData.get(options1);
                selPayList.add(resourceResult);
                payList.add(0, new IndexData(payList.size(), "MINUS", resourceResult));
                payList.add(1, new IndexData(payList.size(), "DIVIDER", new Object()));
                shopPayAdapter.notifyDataSetChanged();

            }
        });

        diningOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiResourceResult resourceResult = diningData.get(options1);
                selDiningList.add(resourceResult);
                diningList.add(0, new IndexData(diningList.size(), "MINUS", resourceResult));
                diningList.add(1, new IndexData(diningList.size(), "DIVIDER", new Object()));
                shopDiningAdapter.notifyDataSetChanged();

            }
        });

        serviceOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiResourceResult resourceResult = serviceData.get(options1);
                selServiceList.add(resourceResult);
                serviceList.add(0, new IndexData(serviceList.size(), "MINUS", resourceResult));
                serviceList.add(1, new IndexData(serviceList.size(), "DIVIDER", new Object()));
                shopServiceAdapter.notifyDataSetChanged();

            }
        });

    }

    private void initBegin() {
        List<MapiResourceResult> list = JGJDataSource.getAcceptTiem();
        if (null != list) {
            begins.addAll(list);
            //三级联动效果
            beginsOptions.setPicker(begins);
            //设置选择的三级单位
            beginsOptions.setCyclic(false);
            //设置默认选中的三级项目
            //监听确定选择按钮
            beginsOptions.setSelectOptions(0);
        }
    }

    private void initEnd() {
        List<MapiResourceResult> list = JGJDataSource.getAcceptTiem();
        if (null != list) {
            ends.addAll(list);
            //三级联动效果
            endsOptions.setPicker(ends);
            //设置选择的三级单位
            endsOptions.setCyclic(false);
            //设置默认选中的三级项目
            //监听确定选择按钮
            endsOptions.setSelectOptions(0);
        }
    }

    private void initPay() {
        List<MapiResourceResult> list = JGJDataSource.getPayType();
        if (null != list) {
            payData.addAll(list);
            //三级联动效果
            payOptions.setPicker(payData);
            //设置选择的三级单位
            payOptions.setCyclic(false);
            //设置默认选中的三级项目
            //监听确定选择按钮
            payOptions.setSelectOptions(0);
        }
    }

    private void initDining() {
        List<MapiResourceResult> list = JGJDataSource.getDining();
        if (null != list) {
            diningData.addAll(list);
            //三级联动效果
            diningOptions.setPicker(diningData);
            //设置选择的三级单位
            diningOptions.setCyclic(false);
            //设置默认选中的三级项目
            //监听确定选择按钮
            diningOptions.setSelectOptions(0);
        }
    }

    private void load() {
        showLoading();
        ItemApi.managehotelmanage(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                if (null == success)
                    return;
                String nameStr = success.getJSONObject("data").getString("name");
                nameTv.setText(TextUtils.isEmpty(nameStr) ? "" : nameStr);
                String cover_pic = success.getJSONObject("data").getString("cover_pic");

                MapiImageResult resourceResult = new MapiImageResult();
                resourceResult.setUrl(cover_pic);
                imgs.add(resourceResult);

                //创建将要下载的图片的URI
                Uri imageUri = Uri.parse(cover_pic);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                        .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(coverPic.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>())
                        .build();
                coverPic.setController(controller);

                String restaurantCatStr = success.getJSONObject("data").getString("restaurant_cat");
                restaurant_cat_id = success.getJSONObject("data").getString("restaurant_cat_id");
                restaurantCatTv.setText(TextUtils.isEmpty(restaurantCatStr) ? "" : restaurantCatStr);

                String discount_rate = success.getJSONObject("data").getString("discount_rate");
                discountRateTv.setText(TextUtils.isEmpty(discount_rate) ? "" : discount_rate);

                String feature = success.getJSONObject("data").getString("feature");
                featureTv.setText(TextUtils.isEmpty(feature) ? "" : feature);

                String customer_consumption = success.getJSONObject("data").getString("customer_consumption");
                customerConsumptionTv.setText(TextUtils.isEmpty(customer_consumption) ? "" : customer_consumption);

                String province_name = success.getJSONObject("data").getString("province_name");
                province_id = success.getJSONObject("data").getString("province_id");
                String city_name = success.getJSONObject("data").getString("city_name");
                city_id = success.getJSONObject("data").getString("city_id");
                String area_name = success.getJSONObject("data").getString("area_name");
                area_id = success.getJSONObject("data").getString("area_id");

                cityTv.setText(province_name + " " + city_name + " " + area_name);


                String address = success.getJSONObject("data").getString("address");
                addrTv.setText(TextUtils.isEmpty(address) ? "" : address);

                String longitude = success.getJSONObject("data").getString("longitude");
                String latitude = success.getJSONObject("data").getString("latitude");
                if(!TextUtils.isEmpty(longitude)&&!TextUtils.isEmpty(latitude)){
                    LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(latitude),Double.parseDouble(longitude));
                    poiItem = new PoiItem("",latLonPoint,"",address);
                }

                selSpotsList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("spots").toJSONString(), MapiResourceResult.class);
                initSposts();

                String desc = success.getJSONObject("data").getString("desc");
                descEt.setText(TextUtils.isEmpty(desc) ? "" : desc);

                String population_max = success.getJSONObject("data").getString("population_max");
                populationMaxEt.setText(TextUtils.isEmpty(population_max) ? "" : population_max);

                List<MapiResourceResult> restaurantsList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("restaurantCatList").toJSONString(), MapiResourceResult.class);
                initRestaurants(restaurantsList);


                String begin = success.getJSONObject("data").getJSONObject("accept_booking_time").getString("begin");
                String end = success.getJSONObject("data").getJSONObject("accept_booking_time").getString("end");
                acceptBookingTimeBeginTv.setText(TextUtils.isEmpty(begin) ? "" : begin);
                acceptBookingTimeEndTv.setText(TextUtils.isEmpty(end) ? "" : end);

                String booking_time = success.getJSONObject("data").getString("booking_time");
                bookingTimeEt.setText(TextUtils.isEmpty(booking_time) ? "24" : booking_time);

                selPayList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("pay_type").toJSONString(), MapiResourceResult.class);
                initType();

                selDiningList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("dining_time").toJSONString(), MapiResourceResult.class);
                initDinner();

                String tel = success.getJSONObject("data").getString("tel");
                telEt.setText(TextUtils.isEmpty(tel) ? "" : tel);

                String mobile = success.getJSONObject("data").getString("mobile");
                mobileEt.setText(TextUtils.isEmpty(mobile) ? "" : mobile);

                List<MapiResourceResult> serviceList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("serviceList").toJSONString(), MapiResourceResult.class);
                initService(serviceList);

                selServiceList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("other").toJSONString(), MapiResourceResult.class);
                initOther();

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void loadCity() {
        showLoading();
        CommonApi.defaultregion(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();

                Gson gson = new Gson();
                List<MapiRegionResult> result = gson.fromJson(success.getJSONArray("data").toJSONString(), new TypeToken<List<MapiRegionResult>>() {
                }.getType());
                if (null == result || result.isEmpty()) {
                    MainToast.showShortToast("服务器繁忙");
                    finish();
                    return;
                } else {
                    userSP.saveCityJson(success.toJSONString());
                    initCityData(result);
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    ArrayList<MapiRegionResult> options2Items_01;
    ArrayList<ArrayList<MapiRegionResult>> options3Items_01;
    ArrayList<MapiRegionResult> options3Items_01_01;

    private void initCityData(List<MapiRegionResult> success) {
        for (MapiRegionResult departmentResult : success) {
            //选项1
            posOptions1Items.add(departmentResult);
            options3Items_01 = new ArrayList<>();
            options2Items_01 = new ArrayList<>();
            if (null != departmentResult.getChildren()) {
                for (MapiRegionResult departmentResult2 : departmentResult.getChildren()) {

                    //选项2
                    options2Items_01.add(departmentResult2);
                    options3Items_01_01 = new ArrayList<>();
                    if (null != departmentResult2.getChildren()) {
                        for (MapiRegionResult departmentResult3 : departmentResult2.getChildren()) {
                            //选项3
                            options3Items_01_01.add(departmentResult3);
                        }
                    }
                    options3Items_01.add(options3Items_01_01);
                }
            }
            posOptions3Items.add(options3Items_01);
            posOptions2Items.add(options2Items_01);
        }
        //三级联动效果
        positionOptions.setPicker(posOptions1Items, posOptions2Items, posOptions3Items, true);//posOptions3Items,
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("选择城市");
        positionOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        positionOptions.setSelectOptions(0, 0, 0);
        positionOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                String options1Str = posOptions1Items.get(options1).getPickerViewText();
                province_id = posOptions1Items.get(options1).getId();
                String options2Str = "";
                if (posOptions2Items.get(options1).isEmpty()) {
                    options2Str = "";
                    city_id = "";
                } else {
                    options2Str = " " + posOptions2Items.get(options1).get(option2).getPickerViewText();
                    city_id = posOptions2Items.get(options1).get(option2).getId();
                }

                String options3Str = "";

                if (posOptions3Items.get(options1).isEmpty() || posOptions3Items.get(options1).get(option2).isEmpty()) {
                    options3Str = "";
                    area_id = "";
                } else {
                    options3Str = " " + posOptions3Items.get(options1).get(option2).get(options3).getPickerViewText();
                    area_id = posOptions3Items.get(options1).get(option2).get(options3).getId();
                }

                cityTv.setText(options1Str + options2Str + options3Str);

            }
        });

    }

    private void initSposts() {

        if (null != selSpotsList) {
            for (MapiResourceResult resourceResult : selSpotsList) {
                mList.add(0, new IndexData(mList.size(), "MINUS", resourceResult));
                mList.add(1, new IndexData(mList.size(), "DIVIDER", new Object()));
            }
        }

        mList.add(new IndexData(mList.size(), "ADD", new Object()));
        shopSpotsAdapter.notifyDataSetChanged();

    }

    private void initType() {

        if (null != selPayList) {
            for (MapiResourceResult resourceResult : selPayList) {
                payList.add(0, new IndexData(payList.size(), "MINUS", resourceResult));
                payList.add(1, new IndexData(payList.size(), "DIVIDER", new Object()));
            }
        }

        payList.add(new IndexData(payList.size(), "ADD", new Object()));
        shopPayAdapter.notifyDataSetChanged();

    }

    private void initDinner() {

        if (null != selDiningList) {
            for (MapiResourceResult resourceResult : selDiningList) {
                diningList.add(0, new IndexData(diningList.size(), "MINUS", resourceResult));
                diningList.add(1, new IndexData(diningList.size(), "DIVIDER", new Object()));
            }
        }

        diningList.add(new IndexData(diningList.size(), "ADD", new Object()));
        shopDiningAdapter.notifyDataSetChanged();

    }

    private void initOther() {

        if (null != selServiceList) {
            for (MapiResourceResult resourceResult : selServiceList) {
                serviceList.add(0, new IndexData(serviceList.size(), "MINUS", resourceResult));
                serviceList.add(1, new IndexData(serviceList.size(), "DIVIDER", new Object()));
            }
        }

        serviceList.add(new IndexData(serviceList.size(), "ADD", new Object()));
        shopServiceAdapter.notifyDataSetChanged();

    }

    private void initRestaurants(List<MapiResourceResult> list) {
        if (null != list) {
            restaurants.addAll(list);
            //三级联动效果
            restaurantsOptions.setPicker(restaurants);
            //设置选择的三级单位
            restaurantsOptions.setCyclic(false);
            //设置默认选中的三级项目
            //监听确定选择按钮
            restaurantsOptions.setSelectOptions(0);
        }
    }

    private void initService(List<MapiResourceResult> list) {
        if (null != list) {
            serviceData.addAll(list);
            //三级联动效果
            serviceOptions.setPicker(serviceData);
            //设置选择的三级单位
            serviceOptions.setCyclic(false);
            //设置默认选中的三级项目
            //监听确定选择按钮
            serviceOptions.setSelectOptions(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "shop_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "shop_image.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
                case RequestCode.SEARCH_RESULT:
                    if (null != data) {
                        poiItem = data.getParcelableExtra("item");
                        if (null != poiItem) {
                            String provinceName = TextUtils.isEmpty(poiItem.getProvinceName()) ? "" : poiItem.getProvinceName();
                            String cityName = TextUtils.isEmpty(poiItem.getCityName()) ? "" : poiItem.getCityName();
                            String adName = TextUtils.isEmpty(poiItem.getAdName()) ? "" : poiItem.getAdName();
                            String snippetName = TextUtils.isEmpty(poiItem.getSnippet()) ? "" : poiItem.getSnippet();

                            String addr = provinceName + cityName + adName + snippetName;
                            addrTv.setText(addr);
                        }
                    }
                    break;
                case RequestCode.add_spot:
                    if (null != data) {
                        MapiResourceResult resourceResult = (MapiResourceResult) data.getSerializableExtra("item");
                        selSpotsList.add(resourceResult);
                        mList.add(0, new IndexData(mList.size(), "MINUS", resourceResult));
                        mList.add(1, new IndexData(mList.size(), "DIVIDER", new Object()));
                        shopSpotsAdapter.notifyDataSetChanged();
                    }

                    break;
            }

        }
    }

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {

                    imgs.clear();
                    imgs.add(success);

                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(imgs.get(0).getUrl());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(coverPic.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    coverPic.setController(controller);


                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                DebugLog.i(message);
            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "shop_image.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).withMaxSize(600, 600).start(this);
//        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @OnClick({R.id.back, R.id.tv_right, R.id.cover_pic, R.id.restaurant_cat_ll, R.id.city_ll, R.id.addr_ll, R.id.end_ll, R.id.start_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                if(TextUtils.isEmpty(nameTv.getText())){
                    MainToast.showShortToast("请输入酒店名称");
                    return;
                }
                if(null==imgs||imgs.isEmpty()){
                    MainToast.showShortToast("请上传酒店封面图");
                    return;
                }
                if(TextUtils.isEmpty(restaurant_cat_id)){
                    MainToast.showShortToast("请选择餐厅类型");
                    return;
                }
                if(TextUtils.isEmpty(discountRateTv.getText())){
                    MainToast.showShortToast("请输入酒店折扣");
                    return;
                }

                double discountRate = Double.parseDouble(discountRateTv.getText().toString());

                if(discountRate>10){
                    MainToast.showShortToast("酒店折扣不能超过10");
                    return;
                }
                if(1>discountRate){
                    MainToast.showShortToast("酒店折扣不能小于1");
                    return;
                }
                if(TextUtils.isEmpty(customerConsumptionTv.getText())){
                    MainToast.showShortToast("请输入人均消费");
                    return;
                }
                if(TextUtils.isEmpty(province_id)){
                    MainToast.showShortToast("请选择地区");
                    return;
                }
                if(null==poiItem){
                    MainToast.showShortToast("请选择详细地址");
                    return;
                }

                if(TextUtils.isEmpty(mobileEt.getText())){
                    MainToast.showShortToast("请输入手机号码");
                    return;
                }
                if(!TextUtils.isEmpty(telEt.getText())){
                    if(!StringUtil.isTel(telEt.getText().toString())){
                        MainToast.showShortToast("请输入正确的座机号格式");
                        return;
                    }
                }

                if(!TextUtils.isEmpty(mobileEt.getText())){
                    if(!StringUtil.isMobile(mobileEt.getText().toString())){
                        MainToast.showShortToast("请输入正确的手机号格式");
                        return;
                    }
                }

                modify();
                break;
            case R.id.cover_pic:
                photoDialog.showDialog();
                break;
            case R.id.restaurant_cat_ll:
                if (null != restaurantsOptions)
                    restaurantsOptions.show();
                break;
            case R.id.city_ll:
                positionOptions.show();
                break;
            case R.id.addr_ll:
                if(!locationUtil.isOPen(this)){
                    MainToast.showShortToast("请打开GPS定位");
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent,RequestCode.LOCATION_QUEST); //设置完成后返回到原来的界面
                }else{
                    Intent intent = new Intent(this, LocationAddrActivity.class);
                    startActivityForResult(intent, RequestCode.SEARCH_RESULT);
                }

                break;
            case R.id.start_ll:
                beginsOptions.show();
                break;
            case R.id.end_ll:
                endsOptions.show();
                break;
        }
    }

    private void modify(){
        LatLonPoint point = poiItem.getLatLonPoint();
        String scenic_spot = getScenic_spot();
        String beginTime = acceptBookingTimeBeginTv.getText().toString();
        String endTime = acceptBookingTimeEndTv.getText().toString();
        String pay_type = getPay_type();
        String dining_time = getDining_time();
        String other = getService();
        showLoading();
        ItemApi.managegetmanage(this, userSP.getUserBean().getMerchant_id(), nameTv.getText().toString(), imgs.get(0).getUrl(), restaurant_cat_id,
                discountRateTv.getText().toString(), featureTv.getText().toString(), customerConsumptionTv.getText().toString(), province_id, city_id, area_id,
                addrTv.getText().toString(), point.getLongitude() + "", point.getLatitude() + "", scenic_spot, descEt.getText().toString(), populationMaxEt.getText().toString(), beginTime + "-" + endTime, bookingTimeEt.getText().toString(),
                pay_type, dining_time, telEt.getText().toString(), mobileEt.getText().toString(), other, new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("保存成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
    }

    private String getScenic_spot(){
        String ids = "";
        if(null!=selSpotsList){
            for(MapiResourceResult resourceResult : selSpotsList){
                if(TextUtils.isEmpty(ids))
                    ids += resourceResult.getId();
                else
                    ids += ","+resourceResult.getId();
            }
        }
        return ids;
    }

    private String getPay_type(){
        String ids = "";
        if(null!=selPayList){
            for(MapiResourceResult resourceResult : selPayList){
                if(TextUtils.isEmpty(ids))
                    ids += resourceResult.getName();
                else
                    ids += ","+resourceResult.getName();
            }
        }
        return ids;
    }

    private String getDining_time(){
        String ids = "";
        if(null!=selDiningList){
            for(MapiResourceResult resourceResult : selDiningList){
                if(TextUtils.isEmpty(ids))
                    ids += resourceResult.getName();
                else
                    ids += ","+resourceResult.getName();
            }
        }
        return ids;
    }

    private String getService(){
        String ids = "";
        if(null!=selServiceList){
            for(MapiResourceResult resourceResult : selServiceList){
                if(TextUtils.isEmpty(ids))
                    ids += resourceResult.getId();
                else
                    ids += ","+resourceResult.getId();
            }
        }
        return ids;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }
        if (null != beginsOptions) {
            beginsOptions.dismiss();
            beginsOptions = null;
        }
        if (null != endsOptions) {
            endsOptions.dismiss();
            endsOptions = null;
        }
        if (null != diningOptions) {
            diningOptions.dismiss();
            diningOptions = null;
        }
        if (null != serviceOptions) {
            serviceOptions.dismiss();
            serviceOptions = null;
        }
    }
}
