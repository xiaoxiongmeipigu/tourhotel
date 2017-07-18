package com.zjhj.tourhotel.activity.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.order.FoodItemAdapter;
import com.zjhj.tourhotel.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.order_tv)
    TextView orderTv;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.use_date_tv)
    TextView useDateTv;
    @Bind(R.id.taste_tv)
    TextView tasteTv;
    @Bind(R.id.remark_tv)
    TextView remarkTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.old_price_tv)
    TextView oldPriceTv;
    @Bind(R.id.discount_rate_tv)
    TextView discountRateTv;
    @Bind(R.id.all_price_tv)
    TextView allPriceTv;

    FoodItemAdapter mAdapter;
    List<MapiFoodResult> mList;
    private String order_id = "";

    private String discountRateStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        if(null!=getIntent()){
            order_id = getIntent().getStringExtra("id");
        }
        if(!TextUtils.isEmpty(order_id)){
            initView();
            load();
        }

    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("订单详情");

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FoodItemAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    public void load(){
        showLoading();
        ItemApi.orderdetail(this, order_id, new RequestCallback<MapiOrderResult>() {
            @Override
            public void success(MapiOrderResult success) {
                hideLoading();
                if(null==success)
                    return;
                initData(success);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void initData(MapiOrderResult success){

        orderTv.setText("订单编号："+(TextUtils.isEmpty(success.getId())?"":success.getId()));
        dateTv.setText("下单时间："+(TextUtils.isEmpty(success.getAddtime())?"":success.getAddtime()));
        nameTv.setText("导游："+(TextUtils.isEmpty(success.getName())?"":success.getName()));
        phoneTv.setText("电话："+(TextUtils.isEmpty(success.getMobile())?"":success.getMobile()));

        String useDate = TextUtils.isEmpty(success.getUse_date())?"": success.getUse_date();
        String beginTime = TextUtils.isEmpty(success.getUse_begin_time())?"": success.getUse_begin_time();
        String endTime = TextUtils.isEmpty(success.getUse_end_time())?"": success.getUse_end_time();

        useDateTv.setText("用餐时间："+useDate+"  "+(TextUtils.isEmpty(beginTime)?"":beginTime+"-")+endTime);

        tasteTv.setText("口味："+(TextUtils.isEmpty(success.getTaste())?"":success.getTaste()));

        discountRateStr = success.getMi_discount_rate();
        discountRateStr = TextUtils.isEmpty(discountRateStr)?"10":discountRateStr;
        discountRateTv.setText(discountRateStr);

        if(!TextUtils.isEmpty(success.getRemark())){
            remarkTv.setVisibility(View.VISIBLE);
            remarkTv.setText("备注："+success.getRemark());
        }else
            remarkTv.setVisibility(View.GONE);

        if(null!=success.getOrder_detail()&&!success.getOrder_detail().isEmpty()){
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mList.addAll(success.getOrder_detail());
            mAdapter.notifyDataSetChanged();
            notifyPrice();
        }

    }

    private void notifyPrice() {

        double allOldPrice = 0;


        for(MapiFoodResult foodResult : mList){
            String price = TextUtils.isEmpty(foodResult.getOriginal_single_price()) ? "0" : foodResult.getOriginal_single_price();
            String numStr = TextUtils.isEmpty(foodResult.getNum())?"0":foodResult.getNum();
            double priceDouble = Double.parseDouble(price);
            allOldPrice += priceDouble * Integer.parseInt(numStr);
        }
        double discountRateDouble = Double.parseDouble(discountRateStr);
        double newPriceDouble = allOldPrice * discountRateDouble / 10;
        oldPriceTv.setText(StringUtil.numberFormat(allOldPrice));
        allPriceTv.setText("¥ " + StringUtil.numberFormat(newPriceDouble));

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
