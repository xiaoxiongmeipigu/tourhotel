package com.zjhj.tourhotel.activity.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DateUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.shop.ShopTimeAdapter;
import com.zjhj.tourhotel.adapter.shop.SpotsAddAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;
import com.zjhj.tourhotel.widget.DividerListItemDecoration;
import com.zjhj.tourhotel.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopTimeActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ShopTimeAdapter mAdapter;
    List<MapiResourceResult> mList;

    TimePickerView pvTime;
    int pos = -1;
    MainAlertDialog passDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_tiem);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        mList = new ArrayList<>();
        back.setImageResource(R.mipmap.back_white);
        center.setText("非营业时间");
        tvRight.setText("新增");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1f), getResources().getColor(R.color.background_gray)));
        mAdapter = new ShopTimeAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {

                String name = DateUtil.getInstance().date2YMD_H(date);
                add(name);

            }

        });

        passDialog = new MainAlertDialog(this);
        passDialog.setLeftBtnText("取消").setRightBtnText("确认");

    }

    private void initListener(){
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pos = position;
                MapiResourceResult resourceResult = mList.get(position);
                passDialog.setTitle("确认移除"+(TextUtils.isEmpty(resourceResult.getNonoperating_time())?"":resourceResult.getNonoperating_time())+"?");
                passDialog.show();
            }
        });

        passDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDialog.dismiss();
            }
        });

        passDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos>=0)
                    delete();
                passDialog.dismiss();
            }
        });


    }

    private void load(){
        showLoading();
        ItemApi.managenonoperating(this, new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if(null==success)
                    return;
                mList.clear();
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void add(String time){
       showLoading();
        ItemApi.manageaddnonoperating(this, time, new RequestCallback<MapiResourceResult>() {
            @Override
            public void success(MapiResourceResult success) {
                hideLoading();
                if(null==success)
                    return;
                mList.add(success);
                mAdapter.notifyDataSetChanged();
                MainToast.showShortToast("新增成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void delete(){
        MapiResourceResult resourceResult = mList.get(pos);
        showLoading();
        ItemApi.managetimedel(this, resourceResult.getId(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(pos);
                pos = -1;
                mAdapter.notifyDataSetChanged();
                MainToast.showShortToast("移除成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                pvTime.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=pvTime){
            pvTime.dismiss();
            pvTime = null;
        }
    }
}
