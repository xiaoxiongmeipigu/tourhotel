package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.MapiCityResult;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiRegionResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.sharedpreferences.UserSP;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2017/2/24.
 */
public class CommonApi extends BasicApi{

    /**
     * 上传图片
     * @param activity
     * @param file
     * @param callback
     * @param exceptionCallback
     */
    public static void
    uploadImage(Activity activity, File file, final RequestCallback callback, final RequestExceptionCallback exceptionCallback) {
        MapiUtil.getInstance().uploadFile(activity, uploadimg, file, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i(json.toString());
                MapiImageResult imageResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiImageResult.class);
                callback.success(imageResult);
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 城市列表
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void city(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,city,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                Gson gson = new Gson();
                List<MapiCityResult> result = gson.fromJson(json.getJSONArray("data").toJSONString(), new TypeToken<List<MapiCityResult>>(){}.getType());
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
     * 景点列表、餐厅类型
     * @param activity
     * @param city_id
     * @param callback
     * @param exceptionCallback
     */
    public static void defaultunion(Activity activity,String city_id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("city_id",city_id);
        MapiUtil.getInstance().call(activity,defaultunion,params,new MapiUtil.MapiSuccessResponse(){
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
     * 省市区数据
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void defaultregion(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,defaultregion,params,new MapiUtil.MapiSuccessResponse(){
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
     * 上传酒店图片
     * @param activity
     * @param file
     * @param callback
     * @param exceptionCallback
     */
    public static void
    uploadShopImage(Activity activity, File file,String id,String type,final RequestCallback callback, final RequestExceptionCallback exceptionCallback) {
        MapiUtil.getInstance().uploadShopFile(activity, uploadimg, file,id,type, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i(json.toString());
                MapiResourceResult resourceResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiResourceResult.class);
                callback.success(resourceResult);
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 酒店首页banner
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void managebanner(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,managebanner,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiResourceResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

}
