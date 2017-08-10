package com.zjhj.tourhotel.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhj.commom.api.BasicApi;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.ShareModule;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.broadcast.LoginBroadcast;
import com.zjhj.tourhotel.broadcast.ReceiverAction;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.util.JpushUtil;
import com.zjhj.tourhotel.view.HomeSliderLayout;
import com.zjhj.tourhotel.widget.MainAlertDialog;
import com.zjhj.tourhotel.widget.ShareDialog;

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
    @Bind(R.id.iv_right_two)
    ImageView ivRightTwo;

    MainAlertDialog exitDialog;

    LoginBroadcast loginBroadcast;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!userSP.checkLogin()) {
            ControllerUtil.go2Login();
            finish();
        } else {
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            initView();
            initListener();
            load();
            registerMessageReceiver();
        }
    }

    private void initView() {

        tvLeft.setText("注销");
        center.setText("游助网酒店管理");
        ivRight.setImageResource(R.mipmap.msg);
        ivRightTwo.setImageResource(R.mipmap.share);
        ivRightTwo.setVisibility(View.VISIBLE);

        exitDialog = new MainAlertDialog(this);
        exitDialog.setLeftBtnText("确认").setRightBtnText("取消").setTitle("确认注销当前账号？");

        if (null != userSP.getUserBean()) {

            if (userSP.getAlias()) {
                userSP.setAlias(false);
                JpushUtil.getInstance().setAlias("");
                JpushUtil.getInstance().stopPush(AppContext.getInstance());
            }
            JpushUtil.getInstance().verifyInit(this);
            DebugLog.i(JPushInterface.isPushStopped(AppContext.getInstance()) + ":服务状态");
            if (JPushInterface.isPushStopped(AppContext.getInstance())) {
                JPushInterface.resumePush(AppContext.getInstance());
            }

            DebugLog.i(JPushInterface.getConnectionState(AppContext.getInstance()) + ":连接状态");

            if (!userSP.getAlias()) {
                DebugLog.i("getAlias===>false");
                JpushUtil.getInstance().setAlias(userSP.getUserBean().getMerchant_id());
            }
        }

        if (shareDialog == null)
            shareDialog = new ShareDialog(this, R.style.image_dialog_theme);

    }

    private void initListener() {
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

        shareDialog.setDialogItemClickListner(new ShareDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                if(null!=userSP.getUserBean()){
                    MapiUserResult userResult = userSP.getUserBean();


                    String SHARE_ORDER_DETAIL = TextUtils.isEmpty(userResult.getShare_page_url())?"":userResult.getShare_page_url();
                    String img_url =  TextUtils.isEmpty(userResult.getMerchant_cover_pic())?"":userResult.getMerchant_cover_pic();
                    String name = TextUtils.isEmpty(userResult.getMerchant_name())?"":userResult.getMerchant_name();
                    String feature = TextUtils.isEmpty(userResult.getMerchant_feature())?"":userResult.getMerchant_feature();
                    switch (position) {
                        case 0://微信好友
                            ShareModule shareModule1 = new ShareModule(MainActivity.this,name , feature, img_url, SHARE_ORDER_DETAIL);
                            shareModule1.startShare(1);
                            break;
                        case 1:
                            ShareModule shareModule2 = new ShareModule(MainActivity.this, name, feature, img_url, SHARE_ORDER_DETAIL);
                            shareModule2.startShare(2);
                            break;
                        case 2:
                            ShareModule shareModule3 = new ShareModule(MainActivity.this, name, feature, img_url, SHARE_ORDER_DETAIL);
                            shareModule3.startShare(3);
                            break;
                        case 3:
                            ShareModule shareModule4 = new ShareModule(MainActivity.this, name, feature, img_url, SHARE_ORDER_DETAIL);
                            shareModule4.startShare(4);
                            break;
                    }

                }
            }
        });


    }

    private void load() {
        showLoading();
        CommonApi.managebanner(this, new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if (null == success || success.isEmpty())
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(null!=intent){
            int type = intent.getIntExtra("type",0);
            if(type==4){
                ControllerUtil.go2Login();
                finish();
            }
        }
    }

    public void registerMessageReceiver() {
        loginBroadcast = new LoginBroadcast();
        IntentFilter filter2 = new IntentFilter();
        filter2.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter2.addAction(ReceiverAction.LOGIN_ACTION);
        registerReceiver(loginBroadcast, filter2);
    }

    @OnClick({R.id.tv_left, R.id.iv_right, R.id.order_ll, R.id.discuss_ll, R.id.food_ll, R.id.meal_ll, R.id.photo_ll, R.id.info_ll,R.id.iv_right_two})
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
            case R.id.iv_right_two:
                if (null != userSP.getUserBean())
                    shareDialog.showDialog();
                else
                    MainToast.showShortToast("暂无信息");
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
        if (null != loginBroadcast)
            unregisterReceiver(loginBroadcast);

        if (null != shareDialog && shareDialog.isShowing()) {
            shareDialog.dismiss();
            shareDialog = null;
        }

        super.onDestroy();
    }
}
