package com.zjhj.tourhotel.activity.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.base.RequestCode;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.widget.MyScrollview;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopInfoActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.cover_pic)
    SimpleDraweeView coverPic;
    @Bind(R.id.restaurant_cat_tv)
    TextView restaurantCatTv;
    @Bind(R.id.discount_rate_tv)
    TextView discountRateTv;
    @Bind(R.id.feature_tv)
    TextView featureTv;
    @Bind(R.id.customer_consumption_tv)
    TextView customerConsumptionTv;
    @Bind(R.id.city_tv)
    TextView cityTv;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.spots_tv)
    TextView spotsTv;
    @Bind(R.id.desc_tv)
    TextView descTv;
    @Bind(R.id.population_max_tv)
    TextView populationMaxTv;
    @Bind(R.id.accept_booking_time_tv)
    TextView acceptBookingTimeTv;
    @Bind(R.id.booking_time_tv)
    TextView bookingTimeTv;
    @Bind(R.id.pay_type_tv)
    TextView payTypeTv;
    @Bind(R.id.dining_time_tv)
    TextView diningTimeTv;
    @Bind(R.id.tel_tv)
    TextView telTv;
    @Bind(R.id.mobile_tv)
    TextView mobileTv;
    @Bind(R.id.other_tv)
    TextView otherTv;
    @Bind(R.id.scrollView)
    MyScrollview scrollView;

    List<MapiResourceResult> spots;
    List<MapiResourceResult> payTypes;
    List<MapiResourceResult> dining_time;
    List<MapiResourceResult> others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        ButterKnife.bind(this);
        initView();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("酒店管理");

        spots = new ArrayList<>();
        payTypes = new ArrayList<>();
        dining_time = new ArrayList<>();
        others = new ArrayList<>();

    }

    private void load(){
        showLoading();
        ItemApi.managehotelmanage(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                if(null==success)
                    return;
                String nameStr = success.getJSONObject("data").getString("name");
                nameTv.setText(TextUtils.isEmpty(nameStr)?"":nameStr);
                String cover_pic = success.getJSONObject("data").getString("cover_pic");

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
                restaurantCatTv.setText(TextUtils.isEmpty(restaurantCatStr)?"":restaurantCatStr);

                String discount_rate = success.getJSONObject("data").getString("discount_rate");
                discountRateTv.setText(TextUtils.isEmpty(discount_rate)?"":discount_rate);

                String feature = success.getJSONObject("data").getString("feature");
                featureTv.setText(TextUtils.isEmpty(feature)?"":feature);

                String customer_consumption = success.getJSONObject("data").getString("customer_consumption");
                customerConsumptionTv.setText(TextUtils.isEmpty(customer_consumption)?"":customer_consumption);

                String province_name = success.getJSONObject("data").getString("province_name");
                String city_name = success.getJSONObject("data").getString("city_name");
                String area_name = success.getJSONObject("data").getString("area_name");
                cityTv.setText(province_name+" "+city_name+" "+area_name);

                String address = success.getJSONObject("data").getString("address");
                addrTv.setText(TextUtils.isEmpty(address)?"":address);

                spots = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("spots").toJSONString(),MapiResourceResult.class);
                if(null!=spots)
                    initSposts();

                String desc = success.getJSONObject("data").getString("desc");
                descTv.setText(TextUtils.isEmpty(desc)?"":desc);

                String population_max = success.getJSONObject("data").getString("population_max");
                populationMaxTv.setText(TextUtils.isEmpty(population_max)?"":population_max);

                String begin = success.getJSONObject("data").getJSONObject("accept_booking_time").getString("begin");
                String end = success.getJSONObject("data").getJSONObject("accept_booking_time").getString("end");
                acceptBookingTimeTv.setText((TextUtils.isEmpty(begin)?"":begin+"-")+(TextUtils.isEmpty(end)?"":end));

                String booking_time = success.getJSONObject("data").getString("booking_time");
                bookingTimeTv.setText("支付提前"+(TextUtils.isEmpty(booking_time)?"24":booking_time)+"小时预订");

                payTypes = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("pay_type").toJSONString(),MapiResourceResult.class);
                if(null!=payTypes)
                    initType();

                dining_time = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("dining_time").toJSONString(),MapiResourceResult.class);
                if(null!=dining_time)
                    initDinner();

                String tel =  success.getJSONObject("data").getString("tel");
                telTv.setText(TextUtils.isEmpty(tel)?"":tel);

                String mobile =  success.getJSONObject("data").getString("mobile");
                mobileTv.setText(TextUtils.isEmpty(mobile)?"":mobile);

                others = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("other").toJSONString(),MapiResourceResult.class);
                if(null!=others)
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

    private void initSposts(){
        String str = "";
        for(MapiResourceResult resourceResult: spots){
            if(TextUtils.isEmpty(str))
                str += resourceResult.getName();
            else
                str += ", "+resourceResult.getName();
        }
        spotsTv.setText(str);
    }

    private void initType(){
        String str = "";
        for(MapiResourceResult resourceResult: payTypes){
            if(TextUtils.isEmpty(str))
                str += resourceResult.getName();
            else
                str += "  "+resourceResult.getName();
        }
        payTypeTv.setText(str);
    }

    private void initOther(){
        String str = "";
        for(MapiResourceResult resourceResult: others){
            if(TextUtils.isEmpty(str))
                str += resourceResult.getName();
            else
                str += "  "+resourceResult.getName();
        }
        otherTv.setText(str);
    }

    private void initDinner(){
        String str = "";
        for(MapiResourceResult resourceResult: dining_time){
            if(TextUtils.isEmpty(str))
                str += resourceResult.getName();
            else
                str += "  "+resourceResult.getName();
        }
        diningTimeTv.setText(str);
    }

    @OnClick({R.id.back, R.id.modify, R.id.set_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.modify:

                Intent intent = new Intent(AppContext.getInstance(), ShopEditActivity.class);
                startActivityForResult(intent, RequestCode.edit_shop);
//                ControllerUtil.go2ShopEdit();
                break;
            case R.id.set_time:
                ControllerUtil.go2ShopTime();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_OK==resultCode){
            if(requestCode==RequestCode.edit_shop){
                load();
            }
        }
    }
}
