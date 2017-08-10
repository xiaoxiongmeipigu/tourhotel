package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiMsgResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/6/16.
 */
public class UserApi extends BasicApi{

    /**
     * 登录
     * @param activity
     * @param phone
     * @param pwd
     * @param callback
     * @param exceptionCallback
     */
    public static void login(Activity activity, String phone, String pwd, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("loginname",phone);
        params.put("password",pwd);
        MapiUtil.getInstance().call(activity,loginlogin,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiUserResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiUserResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){

            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 短信验证码登录
     * @param activity
     * @param mobile
     * @param sms_code
     * @param callback
     * @param exceptionCallback
     */
    public static void smsLogin(Activity activity,String mobile,String sms_code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        MapiUtil.getInstance().call(activity,smsLoginUrl,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiUserResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiUserResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){

            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


    /**
     * 获取验证码
     * @param activity
     * @param phone
     * @param callback
     * @param exceptionCallback
     */
    public static void getverify(Activity activity,String scene,String phone,String img_code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        params.put("mobile",phone);
        if(!TextUtils.isEmpty(img_code))
            params.put("img_code",img_code);
        MapiUtil.getInstance().call(activity,getverify,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


    /**
     * 注册
     * @param activity
     * @param merchant_name
     * @param name
     * @param mobile
     * @param sms_code
     * @param parent_code
     * @param longitude
     * @param latitude
     * @param cover_pic
     * @param business
     * @param food
     * @param address
     * @param callback
     * @param exceptionCallback
     */
    public static void loginregister(Activity activity,String merchant_name,String name,String mobile,String sms_code,String parent_code,String longitude,String latitude,String cover_pic,
                                     String business,String food,String address,String province_id,String city_id,String area_id,
                                     String business_number,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("merchant_name",merchant_name);
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        if(!TextUtils.isEmpty(parent_code))
            params.put("parent_code",parent_code);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("cover_pic",cover_pic);
        params.put("business",business);
        params.put("food",food);
        params.put("address",address);
        params.put("province_id",province_id);
        params.put("city_id",city_id);
        params.put("area_id",area_id);
        if(!TextUtils.isEmpty(business_number))
            params.put("business_number",business_number);
        MapiUtil.getInstance().call(activity,loginregister,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 个人消息
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void usermessage(Activity activity, String page, String limit, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,usermessage,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiMsgResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiMsgResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 忘记密码
     * @param activity
     * @param scene
     * @param mobile
     * @param sms_code
     * @param code
     * @param new_password
     * @param callback
     * @param exceptionCallback
     */
    public static void findpassword(Activity activity,String scene,String account,String mobile,String sms_code,String code,String new_password,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        if(!TextUtils.isEmpty(account))
            params.put("account",account);
        if(!TextUtils.isEmpty(mobile))
            params.put("mobile",mobile);
        if(!TextUtils.isEmpty(sms_code))
            params.put("sms_code",sms_code);
        if(!TextUtils.isEmpty(code))
            params.put("code",code);
        if(!TextUtils.isEmpty(new_password))
            params.put("new_password",new_password);
        MapiUtil.getInstance().call(activity,findpassword,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 修改密码
     * @param activity
     * @param old_password
     * @param new_password
     * @param callback
     * @param exceptionCallback
     */
    public static void managemodifypassword(Activity activity,String old_password,String new_password,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("old_password",old_password);
        params.put("new_password",new_password);
        MapiUtil.getInstance().call(activity,managemodifypassword,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


}
