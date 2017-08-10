package com.zjhj.tourhotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.util.JpushUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPsdActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.psd_et)
    EditText psdEt;
    @Bind(R.id.clear_psd)
    ImageView clearPsd;
    @Bind(R.id.psd_two_et)
    EditText psdTwoEt;
    @Bind(R.id.clear_psd_two)
    ImageView clearPsdTwo;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.old_psd_et)
    EditText oldPsdEt;
    @Bind(R.id.clear_old_psd)
    ImageView clearOldPsd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psd);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {

        center.setText("修改密码");
        back.setImageResource(R.mipmap.back);

    }

    private void initListener() {

        oldPsdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearOldPsd.setVisibility(View.VISIBLE);
                } else {
                    clearOldPsd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        psdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPsd.setVisibility(View.VISIBLE);
                } else {
                    clearPsd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        psdTwoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPsdTwo.setVisibility(View.VISIBLE);
                } else {
                    clearPsdTwo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.back,R.id.clear_old_psd, R.id.clear_psd, R.id.clear_psd_two, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
               finish();
                break;
            case R.id.clear_old_psd:
                oldPsdEt.setText("");
                break;
            case R.id.clear_psd:
                psdEt.setText("");
                break;
            case R.id.clear_psd_two:
                psdTwoEt.setText("");
                break;
            case R.id.confirm:

                if (TextUtils.isEmpty(oldPsdEt.getText())) {
                    MainToast.showShortToast("请输入旧密码");
                    return;
                }

                if (TextUtils.isEmpty(psdEt.getText())) {
                    MainToast.showShortToast("请输入新密码");
                    return;
                }

                if (TextUtils.isEmpty(psdTwoEt.getText())) {
                    MainToast.showShortToast("请输入确认密码");
                    return;
                }

                if (!psdEt.getText().toString().equals(psdTwoEt.getText().toString())) {
                    MainToast.showShortToast("两次密码输入不一致");
                    return;
                }

                if (oldPsdEt.getText().toString().equals(psdTwoEt.getText().toString())) {
                    MainToast.showShortToast("新密码不能与旧密码一致");
                    return;
                }

                showLoading();
                UserApi.managemodifypassword(this,oldPsdEt.getText().toString(),psdTwoEt.getText().toString() ,new RequestCallback<JSONObject>() {
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
                        JpushUtil.getInstance().setAlias("");
                        JpushUtil.getInstance().stopPush(AppContext.getInstance());
                        userSP.clearUserData();
                        MainToast.showShortToast("密码修改成功,请重新登录");
                        Intent it = new Intent(ModifyPsdActivity.this, MainActivity.class);
                        it.putExtra("type",4);
                        startActivity(it);
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });

                break;
        }
    }

}
