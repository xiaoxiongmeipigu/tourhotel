package com.zjhj.tourhotel.activity.shop;

import android.content.Intent;
import android.os.Bundle;
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

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.food.FoodAddAdapter;
import com.zjhj.tourhotel.adapter.shop.SpotsAddAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;
import com.zjhj.tourhotel.widget.DividerListItemDecoration;
import com.zjhj.tourhotel.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpotAddActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.clear_iv)
    ImageView clearIv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    SpotsAddAdapter mAdapter;
    List<MapiResourceResult> mList;

    String searchStr = "";
    MainAlertDialog passDialog;
    private int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_add);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        mList = new ArrayList<>();

        back.setImageResource(R.mipmap.back_white);
        center.setText("添加景点");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1f), getResources().getColor(R.color.background_gray)));
        mAdapter = new SpotsAddAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        passDialog = new MainAlertDialog(this);
        passDialog.setLeftBtnText("取消").setRightBtnText("确认");

    }

    private void initListener(){
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
                    add();
                passDialog.dismiss();
            }
        });

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pos = position;
                MapiResourceResult resourceResult = mList.get(position);
                passDialog.setTitle("确认添加"+(TextUtils.isEmpty(resourceResult.getName())?"":resourceResult.getName())+"?");
                passDialog.show();
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                    searchStr = newText;
                    refreshData();
                }else
                    clearIv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void load() {
        showLoading();
        ItemApi.managescenicspot(this,searchStr,new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if (success.isEmpty())
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

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    private void add(){
        MapiResourceResult resourceResult = mList.get(pos);
        pos = -1;
        MainToast.showShortToast("新增成功");
        Intent intent = new Intent();
        intent.putExtra("item",resourceResult);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick({R.id.back, R.id.clear_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear_iv:
                searchEt.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != passDialog && passDialog.isShowing()) {
            passDialog.dismiss();
            passDialog = null;
        }

    }


}
