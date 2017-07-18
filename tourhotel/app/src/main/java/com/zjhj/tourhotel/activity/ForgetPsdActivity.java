package com.zjhj.tourhotel.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.api.BasicApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.SMSUtils;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.receiver.SMSBroadcastReceiver;
import com.zjhj.tourhotel.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPsdActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.clear_name)
    ImageView clearName;
    @Bind(R.id.image_et)
    EditText imageEt;
    @Bind(R.id.clear_img)
    ImageView clearImg;
    @Bind(R.id.code_img)
    SimpleDraweeView codeImg;
    @Bind(R.id.code_et)
    EditText codeEt;
    @Bind(R.id.clear_code)
    ImageView clearCode;
    @Bind(R.id.request_code)
    TextView requestCode;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.account_et)
    EditText accountEt;
    @Bind(R.id.clear_account)
    ImageView clearAccount;

    /**
     * 短信验证倒计时--时长
     */
    private int i = 60;
    // 读取短信广播
    private SMSBroadcastReceiver smsBroadcastReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    SMSUtils.EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psd);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {

        center.setText("忘记密码");
        back.setImageResource(R.mipmap.back);

        getImg();

    }

    private void initListener() {

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

        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearName.setVisibility(View.VISIBLE);
                } else {
                    clearName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        codeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearCode.setVisibility(View.VISIBLE);
                } else {
                    clearCode.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearImg.setVisibility(View.VISIBLE);
                } else {
                    clearImg.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getImg() {
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_URL + BasicApi.imgcode);

        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imageUri);
        imagePipeline.evictFromDiskCache(imageUri);
        // combines above two lines
        imagePipeline.evictFromCache(imageUri);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(105), DPUtil.dip2px(45)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(codeImg.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        codeImg.setController(controller);
    }

    @OnClick({R.id.back, R.id.clear_name, R.id.clear_img, R.id.request_img, R.id.clear_code, R.id.request_code
            , R.id.next,R.id.clear_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear_name:
                nameEt.setText("");
                break;
            case R.id.clear_img:
                imageEt.setText("");
                break;
            case R.id.request_img:
                getImg();
                break;
            case R.id.clear_code:
                codeEt.setText("");
                break;
            case R.id.clear_account:
                accountEt.setText("");
                break;
            case R.id.request_code:
                String phoneStr = nameEt.getText().toString();

                if (TextUtils.isEmpty(phoneStr)) {
                    MainToast.showShortToast("请输入手机号");
                    return;
                }

                if (!StringUtil.isMobile(phoneStr)) {
                    MainToast.showShortToast("手机号格式不正确！");
                    return;
                }

                if (TextUtils.isEmpty(imageEt.getText().toString())) {
                    MainToast.showShortToast("请输入图片验证码");
                    return;
                }

                requestCode();
                break;
            case R.id.next:

                if (TextUtils.isEmpty(accountEt.getText())) {
                    MainToast.showShortToast("请输入账号");
                    return;
                }

                if (TextUtils.isEmpty(nameEt.getText().toString())) {
                    MainToast.showShortToast("请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(codeEt.getText().toString())) {
                    MainToast.showShortToast("请输入验证码");
                    return;
                }

                showLoading();
                UserApi.findpassword(this, "GET_CODE",accountEt.getText().toString(), nameEt.getText().toString(), codeEt.getText().toString(), "", "", new RequestCallback<JSONObject>() {
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
                        String code = success.getJSONObject("data").getString("code");
                        if (!TextUtils.isEmpty(code))
                            ControllerUtil.go2SetPsd(code);
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

    /**
     * 向服务器请求验证码
     */
    private void requestCode() {
        SMSUtils.requestCode(this, "FIND_PASSWORD", nameEt.getText().toString(), imageEt.getText().toString());
        // 把按钮变成不可点击，并且显示倒计时（正在获取）
        requestCode.setClickable(false);
        requestCode.setFocusableInTouchMode(false);
        requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_color_divider));
        requestCode.setTextColor(getResources().getColor(R.color.shop_white));
        requestCode.setText("(" + i + "s)后重新获取");
        initHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        i = 30;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }

    private void initHandler() {
        eventHandler = new SMSUtils.EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = -7;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSUtils.registerEventHandler(eventHandler);
    }

    String mark = "";

    /**
     * 处理服务器返回的信息
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -9:
                    if (null != requestCode)
                        requestCode.setText("(" + i + "s)后重新获取");
                    break;
                case -8:
                    if (null != requestCode) {
                        requestCode.setText("获取验证码");
                        requestCode.setClickable(true);
                        requestCode.setTextColor(getResources().getColor(R.color.shop_yellow));
                        requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_white_stroke_yellow));
                        i = 60;
                    }

                    break;
                case -7:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    DebugLog.e("event=" + event);
                    if (result == SMSUtils.RESULT_COMPLETE) {
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE) {
                            mark = (String) data;
                            MainToast.showShortToast("验证码已经发送");
                        }
                    } else if (result == SMSUtils.RESULT_ERROR) {
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE_ERROR) {
                            MainToast.showShortToast((String) data);

                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onDestroy() {
        if (null != eventHandler)
            SMSUtils.unregisterEventHandler(eventHandler);
        super.onDestroy();
    }

}
