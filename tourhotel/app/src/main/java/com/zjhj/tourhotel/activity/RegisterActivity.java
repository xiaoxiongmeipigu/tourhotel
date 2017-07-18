package com.zjhj.tourhotel.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.OptionsPickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.BasicApi;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiRegionResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.util.JGJBitmapUtils;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.SMSUtils;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.base.RequestCode;
import com.zjhj.tourhotel.receiver.SMSBroadcastReceiver;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.util.LocationUtil;
import com.zjhj.tourhotel.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.shop_name_et)
    EditText shopNameEt;
    @Bind(R.id.clear_shop_name)
    ImageView clearShopName;
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.clear_name)
    ImageView clearName;
    @Bind(R.id.code_et)
    EditText codeEt;
    @Bind(R.id.clear_code)
    ImageView clearCode;
    @Bind(R.id.recommend_et)
    EditText recommendEt;
    @Bind(R.id.clear_recommend)
    ImageView clearRecommend;
    @Bind(R.id.licence_ll)
    LinearLayout licenceLl;
    @Bind(R.id.licence_image)
    SimpleDraweeView licenceImage;
    @Bind(R.id.food_ll)
    LinearLayout foodLl;
    @Bind(R.id.food_image)
    SimpleDraweeView foodImage;
    @Bind(R.id.shop_ll)
    LinearLayout shopLl;
    @Bind(R.id.shop_image)
    SimpleDraweeView shopImage;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.clear_phone)
    ImageView clearPhone;
    @Bind(R.id.licence_rl)
    RelativeLayout licenceRl;
    @Bind(R.id.request_code)
    TextView requestCode;
    @Bind(R.id.city_tv)
    TextView cityTv;
    @Bind(R.id.city_ll)
    LinearLayout cityLl;

    PoiItem poiItem;

    PhotoDialog photoDialog;
    ArrayList<MapiImageResult> licenceImgs;
    ArrayList<MapiImageResult> foodImgs;
    ArrayList<MapiImageResult> shopImgs;
    String type = "0";
    @Bind(R.id.gree_iv)
    ImageView greeIv;


    /**
     * 短信验证倒计时--时长
     */
    private int i = 60;
    // 读取短信广播
    private SMSBroadcastReceiver smsBroadcastReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    SMSUtils.EventHandler eventHandler;

    OptionsPickerView positionOptions;
    ArrayList<MapiRegionResult> posOptions1Items = new ArrayList<>();
    ArrayList<ArrayList<MapiRegionResult>> posOptions2Items = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<MapiRegionResult>>> posOptions3Items = new ArrayList<>();

    String province_id = "";
    String city_id = "";
    String area_id = "";

    LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initListener();
        locationUtil = new LocationUtil(this);
        if (null != userSP && !TextUtils.isEmpty(userSP.getCityJson())) {
            String cityJson = userSP.getCityJson();
            JSONObject jsonObject = JSONObject.parseObject(cityJson);
            Gson gson = new Gson();
            List<MapiRegionResult> result = gson.fromJson(jsonObject.getJSONArray("data").toJSONString(), new TypeToken<List<MapiRegionResult>>() {
            }.getType());
            if (null == result || result.isEmpty()) {
                MainToast.showShortToast("服务器繁忙");
                finish();
                return;
            } else {
                initData(result);
            }
        } else {
            load();
        }
    }

    private void initView() {
        back.setImageResource(R.mipmap.back_white);
        center.setText("申请入驻");

        licenceImgs = new ArrayList<>();
        foodImgs = new ArrayList<>();
        shopImgs = new ArrayList<>();

        //选项选择器
        positionOptions = new OptionsPickerView(this);

    }

    private void initListener() {
        shopNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearShopName.setVisibility(View.VISIBLE);
                } else {
                    clearShopName.setVisibility(View.INVISIBLE);
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

        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPhone.setVisibility(View.VISIBLE);
                } else {
                    clearPhone.setVisibility(View.INVISIBLE);
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
                    clearCode.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recommendEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearRecommend.setVisibility(View.VISIBLE);
                } else {
                    clearRecommend.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void load() {
        showLoading();
        CommonApi.defaultregion(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();

                Gson gson = new Gson();
                List<MapiRegionResult> result = gson.fromJson(success.getJSONArray("data").toJSONString(), new TypeToken<List<MapiRegionResult>>() {
                }.getType());
                if (null == result || result.isEmpty()) {
                    MainToast.showShortToast("服务器繁忙");
                    finish();
                    return;
                } else {
                    userSP.saveCityJson(success.toJSONString());
                    initData(result);
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    ArrayList<MapiRegionResult> options2Items_01;
    ArrayList<ArrayList<MapiRegionResult>> options3Items_01;
    ArrayList<MapiRegionResult> options3Items_01_01;

    private void initData(List<MapiRegionResult> success) {
        for (MapiRegionResult departmentResult : success) {
            //选项1
            posOptions1Items.add(departmentResult);
            options3Items_01 = new ArrayList<>();
            options2Items_01 = new ArrayList<>();
            if (null != departmentResult.getChildren()) {
                for (MapiRegionResult departmentResult2 : departmentResult.getChildren()) {

                    //选项2
                    options2Items_01.add(departmentResult2);
                    options3Items_01_01 = new ArrayList<>();
                    if (null != departmentResult2.getChildren()) {
                        for (MapiRegionResult departmentResult3 : departmentResult2.getChildren()) {
                            //选项3
                            options3Items_01_01.add(departmentResult3);
                        }
                    }
                    options3Items_01.add(options3Items_01_01);
                }
            }
            posOptions3Items.add(options3Items_01);
            posOptions2Items.add(options2Items_01);
        }
        //三级联动效果
        positionOptions.setPicker(posOptions1Items, posOptions2Items, posOptions3Items, true);//posOptions3Items,
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("选择城市");
        positionOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        positionOptions.setSelectOptions(0, 0, 0);
        positionOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                String options1Str = posOptions1Items.get(options1).getPickerViewText();
                province_id = posOptions1Items.get(options1).getId();
                String options2Str = "";
                if (posOptions2Items.get(options1).isEmpty()) {
                    options2Str = "";
                    city_id = "";
                } else {
                    options2Str = "-" + posOptions2Items.get(options1).get(option2).getPickerViewText();
                    city_id = posOptions2Items.get(options1).get(option2).getId();
                }

                String options3Str = "";

                if (posOptions3Items.get(options1).isEmpty() || posOptions3Items.get(options1).get(option2).isEmpty()) {
                    options3Str = "";
                    area_id = "";
                } else {
                    options3Str = "-" + posOptions3Items.get(options1).get(option2).get(options3).getPickerViewText();
                    area_id = posOptions3Items.get(options1).get(option2).get(options3).getId();
                }

                cityTv.setText(options1Str + options2Str + options3Str);

            }
        });

    }

    @OnClick({R.id.back, R.id.clear_shop_name, R.id.clear_name, R.id.clear_phone, R.id.clear_code, R.id.request_code, R.id.clear_recommend, R.id.licence_rl, R.id.food_rl, R.id.shop_rl, R.id.submit
            , R.id.addr_ll, R.id.city_ll, R.id.protocol,R.id.gree_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear_shop_name:
                shopNameEt.setText("");
                break;
            case R.id.clear_name:
                nameEt.setText("");
                break;
            case R.id.clear_phone:
                phoneEt.setText("");
                break;
            case R.id.clear_code:
                codeEt.setText("");
                break;
            case R.id.request_code:
                String phoneStr = phoneEt.getText().toString();

                if (TextUtils.isEmpty(phoneStr)) {
                    MainToast.showShortToast("请输入手机号");
                    return;
                }

                if (!StringUtil.isMobile(phoneStr)) {
                    MainToast.showShortToast("手机号格式不正确！");
                    return;
                }

                requestCode();
                break;
            case R.id.clear_recommend:
                recommendEt.setText("");
                break;
            case R.id.licence_rl:
                type = "0";
                if (photoDialog == null)
                    photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                photoDialog.setImagePath("hotel_licence_image.jpg");
                photoDialog.showDialog();
                break;
            case R.id.food_rl:
                type = "2";
                if (photoDialog == null)
                    photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                photoDialog.setImagePath("hotel_licence_image.jpg");
                photoDialog.showDialog();
                break;
            case R.id.shop_rl:
                type = "4";
                if (photoDialog == null)
                    photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                photoDialog.setImagePath("hotel_licence_image.jpg");
                photoDialog.showDialog();
                break;
            case R.id.city_ll:
                positionOptions.show();
                break;
            case R.id.addr_ll:
                if (!locationUtil.isOPen(this)) {
                    MainToast.showShortToast("请打开GPS定位");
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, RequestCode.LOCATION_QUEST); //设置完成后返回到原来的界面
                } else {
                    Intent intent = new Intent(this, LocationAddrActivity.class);
                    startActivityForResult(intent, RequestCode.SEARCH_RESULT);
                }

                break;
            case R.id.submit:
                if (TextUtils.isEmpty(shopNameEt.getText())) {
                    MainToast.showShortToast("请输入酒店名称");
                    return;
                }
                if (TextUtils.isEmpty(nameEt.getText())) {
                    MainToast.showShortToast("请输入联系人姓名");
                    return;
                }
                if (TextUtils.isEmpty(phoneEt.getText())) {
                    MainToast.showShortToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(codeEt.getText())) {
                    MainToast.showShortToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(province_id)) {
                    MainToast.showShortToast("请选择城市");
                    return;
                }
                if (null == poiItem) {
                    MainToast.showShortToast("请选择您的地址");
                    return;
                }
                if (null == licenceImgs || licenceImgs.isEmpty()) {
                    MainToast.showShortToast("请上传营业执照");
                    return;
                }
                if (null == foodImgs || foodImgs.isEmpty()) {
                    MainToast.showShortToast("请上传食品经营许可证");
                    return;
                }
                if (null == shopImgs || shopImgs.isEmpty()) {
                    MainToast.showShortToast("请上传店面封面图");
                    return;
                }

                if(!isGree){
                    MainToast.showShortToast("您未同意游助网商户服务协议，无法提交");
                    return;
                }

                submit();
                break;
            case R.id.protocol:
                ControllerUtil.go2WebView(BasicApi.PROTOCOL_GUIDE_URL, "商户服务协议", "", "", "", false);
                break;
            case R.id.gree_iv:
                if(!isGree){
                    isGree = true;
                    greeIv.setImageResource(R.mipmap.gree_logo);
                }else if(isGree){
                    isGree = false;
                    greeIv.setImageResource(R.mipmap.ungree_logo);
                }
                break;
        }
    }

    boolean isGree = false;

    private void submit() {

        LatLonPoint point = poiItem.getLatLonPoint();

        showLoading();
        UserApi.loginregister(this, shopNameEt.getText().toString().trim(), nameEt.getText().toString().trim(), phoneEt.getText().toString().trim(), codeEt.getText().toString().trim(),
                recommendEt.getText().toString().trim(), point.getLongitude() + "", point.getLatitude() + "", shopImgs.get(0).getUrl(),
                licenceImgs.get(0).getUrl(), foodImgs.get(0).getUrl(), addrTv.getText().toString(),
                province_id, city_id, area_id,
                new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showLongToast("为了保障酒店利益，平台将在3-5个工作日内确认申请，审核结果将以短信方式告知。");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "hotel_licence_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "hotel_licence_image.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
                case RequestCode.SEARCH_RESULT:
                    if (null != data) {
                        poiItem = data.getParcelableExtra("item");
                        if (null != poiItem) {
                            String provinceName = TextUtils.isEmpty(poiItem.getProvinceName()) ? "" : poiItem.getProvinceName();
                            String cityName = TextUtils.isEmpty(poiItem.getCityName()) ? "" : poiItem.getCityName();
                            String adName = TextUtils.isEmpty(poiItem.getAdName()) ? "" : poiItem.getAdName();
                            String snippetName = TextUtils.isEmpty(poiItem.getSnippet()) ? "" : poiItem.getSnippet();

                            String addr = provinceName + cityName + adName + snippetName;
                            addrTv.setText(addr);
                        }
                    }
                    break;
                case RequestCode.LOCATION_QUEST:
                    if (null != locationUtil) {
                        //打开GPS后操作
                    }
                    break;
            }

        }
    }

    /**
     * 向服务器请求验证码
     */
    private void requestCode() {
        SMSUtils.requestCode(this, "REGISTER", phoneEt.getText().toString(), "");
        // 把按钮变成不可点击，并且显示倒计时（正在获取）
        requestCode.setClickable(false);
        requestCode.setFocusableInTouchMode(false);
        requestCode.setFocusable(false);
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
                    requestCode.setText("(" + i + "s)后重新获取");
                    break;
                case -8:
                    requestCode.setText("获取验证码");
                    requestCode.setClickable(true);
                    requestCode.setTextColor(getResources().getColor(R.color.shop_yellow));
                    requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_white_stroke_yellow));
                    i = 60;
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

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {

                    if ("0".equals(type)) {
                        licenceImgs.clear();
                        licenceImgs.add(success);
                        licenceImage.setVisibility(View.VISIBLE);
                        licenceLl.setVisibility(View.GONE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                        //创建将要下载的图片的URI
                        Uri imageUri = Uri.parse(licenceImgs.get(0).getUrl());
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .setOldController(licenceImage.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        licenceImage.setController(controller);
                    } else if ("2".equals(type)) {
                        foodImgs.clear();
                        foodImgs.add(success);
                        foodImage.setVisibility(View.VISIBLE);
                        foodLl.setVisibility(View.GONE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                        //创建将要下载的图片的URI
                        Uri imageUri = Uri.parse(foodImgs.get(0).getUrl());
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .setOldController(foodImage.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        foodImage.setController(controller);
                    } else if ("4".equals(type)) {
                        shopImgs.clear();
                        shopImgs.add(success);
                        shopImage.setVisibility(View.VISIBLE);
                        shopLl.setVisibility(View.GONE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                        //创建将要下载的图片的URI
                        Uri imageUri = Uri.parse(shopImgs.get(0).getUrl());
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .setOldController(shopImage.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        shopImage.setController(controller);
                    }

                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                DebugLog.i(message);
            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "hotel_licence_image.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).withMaxSize(600, 600).start(this);
//        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @Override
    protected void onDestroy() {
        if (null != eventHandler)
            SMSUtils.unregisterEventHandler(eventHandler);
        if (null != photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }
        super.onDestroy();
    }
}
