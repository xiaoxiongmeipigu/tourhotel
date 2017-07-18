package com.zjhj.tourhotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.util.JpushUtil;
import com.zjhj.tourhotel.view.HomeSliderLayout;
import com.zjhj.tourhotel.widget.MainAlertDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.homeSliderLayout)
    HomeSliderLayout homeSliderLayout;

    MainAlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!userSP.checkLogin()){
            ControllerUtil.go2Login();
            finish();
        }else{
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            initView();
            initListener();
            load();
        }
    }

    private void initView(){

        tvLeft.setText("注销");
        center.setText("游助网酒店管理");
        ivRight.setImageResource(R.mipmap.msg);

        exitDialog = new MainAlertDialog(this);
        exitDialog.setLeftBtnText("确认").setRightBtnText("取消").setTitle("确认注销当前账号？");

        if(null!=userSP.getUserBean()){

            if (userSP.getAlias()) {
                userSP.setAlias(false);
                JpushUtil.getInstance().setAlias("");
                JpushUtil.getInstance().stopPush(AppContext.getInstance());
            }
            JpushUtil.getInstance().verifyInit(this);
            DebugLog.i(JPushInterface.isPushStopped(AppContext.getInstance())+":服务状态");
            if (JPushInterface.isPushStopped(AppContext.getInstance())) {
                JPushInterface.resumePush(AppContext.getInstance());
            }

            DebugLog.i(JPushInterface.getConnectionState(AppContext.getInstance())+":连接状态");

            if (!userSP.getAlias()) {
                DebugLog.i("getAlias===>false");
                JpushUtil.getInstance().setAlias(userSP.getUserBean().getMerchant_id());
            }
        }



    }

    private void initListener(){
        exitDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JpushUtil.getInstance().setAlias("");
                JpushUtil.getInstance().stopPush(AppContext.getInstance());
                userSP.clearUserData();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        exitDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
            }
        });
    }

    private void load(){
        showLoading();
        CommonApi.managebanner(this, new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if(null==success||success.isEmpty())
                    return;
                homeSliderLayout.setSlider(true);
                homeSliderLayout.load(success);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.iv_right, R.id.order_ll, R.id.discuss_ll, R.id.food_ll, R.id.meal_ll, R.id.photo_ll, R.id.info_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                exitDialog.show();
                break;
            case R.id.iv_right:
                ControllerUtil.go2Message();
                break;
            case R.id.order_ll:
                ControllerUtil.go2OrderList();
                break;
            case R.id.discuss_ll:
                ControllerUtil.go2DiscussList();
                break;
            case R.id.food_ll:
                ControllerUtil.go2FoodList();
                break;
            case R.id.meal_ll:
                ControllerUtil.go2MealList();
                break;
            case R.id.photo_ll:
                ControllerUtil.go2Photo();
                break;
            case R.id.info_ll:
                ControllerUtil.go2ShopInfo();
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (null != exitDialog && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }
        super.onDestroy();
    }
}
