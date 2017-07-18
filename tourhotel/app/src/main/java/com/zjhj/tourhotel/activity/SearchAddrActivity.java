package com.zjhj.tourhotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.SearchAddrAdapter;
import com.zjhj.tourhotel.adapter.SearchResultAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchAddrActivity extends BaseActivity {

    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.clear_iv)
    ImageView clearIv;
    @Bind(R.id.listview)
    ListView listview;

    SearchAddrAdapter mAdapter;
    private List<Tip> autoTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_addr);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        autoTips = new ArrayList<>();
        mAdapter = new SearchAddrAdapter(this);
        mAdapter.setData(autoTips);
        listview.setAdapter(mAdapter);
    }

    private void initListener(){
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                    InputtipsQuery inputquery = new InputtipsQuery(newText,"");//第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
                    Inputtips inputTips = new Inputtips(SearchAddrActivity.this, inputquery);
                    inputquery.setCityLimit(true);//限制在当前城市
                    inputTips.setInputtipsListener(inputtipsListener);
                    inputTips.requestInputtipsAsyn();
                }else
                    clearIv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter.setItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (autoTips != null && autoTips.size() > position) {
                    Tip tip = autoTips.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("item",tip);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> list, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                autoTips.clear();
                mAdapter.notifyDataSetChanged();
                Iterator<Tip> tipsrator = list.iterator();
                while(tipsrator.hasNext()){
                    Tip tip = tipsrator.next();
                    if(null==tip.getPoint()){
                        tipsrator.remove();
                    }
                }
                autoTips.addAll(list);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(SearchAddrActivity.this, "erroCode " + rCode , Toast.LENGTH_SHORT).show();
            }
        }
    };

    @OnClick({R.id.clear_iv, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_iv:
                searchEt.setText("");
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
