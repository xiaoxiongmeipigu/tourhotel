package com.zjhj.tourhotel.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.account_et)
    EditText accountEt;
    @Bind(R.id.clear_account)
    ImageView clearAccount;
    @Bind(R.id.psd_et)
    EditText psdEt;
    @Bind(R.id.clear_psd)
    ImageView clearPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {

    }

    private void initListener(){
        accountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearAccount.setVisibility(View.VISIBLE);
                } else {
                    clearAccount.setVisibility(View.INVISIBLE);
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

    }

    @OnClick({R.id.clear_account, R.id.clear_psd, R.id.login, R.id.register, R.id.forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_account:
                accountEt.setText("");
                break;
            case R.id.clear_psd:
                psdEt.setText("");
                break;
            case R.id.login:
                if(TextUtils.isEmpty(accountEt.getText())){
                    MainToast.showShortToast("请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(psdEt.getText())){
                    MainToast.showShortToast("请输入密码");
                    return;
                }
                login();
                break;
            case R.id.register:
                ControllerUtil.go2Register();
                break;
            case R.id.forget:
                ControllerUtil.go2ForgetPsd();
                break;
        }
    }

    private void login(){
        showLoading();
        UserApi.login(this, accountEt.getText().toString(), psdEt.getText().toString(), new RequestCallback<MapiUserResult>() {
            @Override
            public void success(MapiUserResult success) {
                hideLoading();
                MainToast.showShortToast("登录成功");
                userSP.saveUserBean(success);
                ControllerUtil.go2Main();
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

}
