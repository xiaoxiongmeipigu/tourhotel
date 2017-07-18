package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2017/5/18.
 */
public class ItemApi extends BasicApi{

    /**
     * 评价列表
     * @param activity
     * @param page
     * @param limit
     * @param status
     * @param callback
     * @param exceptionCallback
     */
    public static void commentindex(Activity activity, String page, String limit,String status, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        if(!TextUtils.isEmpty(status))
            params.put("status",status);
        MapiUtil.getInstance().call(activity,commentindex,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiDiscussResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiDiscussResult.class);
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
     * 订单详情
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderdetail(Activity activity, String id, final RequestCallback callback,final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,orderdetail,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiOrderResult orderResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiOrderResult.class);
                callback.success(orderResult);

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 评论回复
     * @param activity
     * @param id
     * @param reply
     * @param callback
     * @param exceptionCallback
     */
    public static void commentreply(Activity activity,String id,String reply,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("reply",reply);
        MapiUtil.getInstance().call(activity,commentreply,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店列表
     * @param activity
     * @param status
     *         订单状态 （1、待审核 2、待使用  3、已完成 4、申请取消）不传值 不参与筛选
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void orderindex(Activity activity,String status,String page,String limit,final RequestPageCallback callback,
                                  final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("status",status);
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,orderindex,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiOrderResult.class);
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
     * 审核驳回
     * @param activity
     * @param id
     * @param content
     * @param callback
     * @param exceptionCallback
     */
    public static void orderreject(Activity activity,String id,String content,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("content",content);
        MapiUtil.getInstance().call(activity,orderreject,params,new MapiUtil.MapiSuccessResponse(){
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
     * 审核通过
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderadopt(Activity activity,String id,String type,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("type",type);
        MapiUtil.getInstance().call(activity,orderadopt,params,new MapiUtil.MapiSuccessResponse(){
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
     * 确认使用
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void ordermack(Activity activity,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,ordermack,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店菜品（列表）
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void footindex(Activity activity,String name,String page,String limit,final RequestPageCallback callback,
                                  final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(name))
            params.put("name",name);
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,footindex,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiFoodResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiFoodResult.class);
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
     * 酒店菜品（添加、修改）
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void footedit(Activity activity,String id,String name,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id))
            params.put("id",id);
        params.put("name",name);
        MapiUtil.getInstance().call(activity,footedit,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiFoodResult foodResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiFoodResult.class);
                callback.success(foodResult);

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 酒店菜品删除
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void footfootdel(Activity activity,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,footfootdel,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店菜品（列表）
     * @param activity
     * @param name
     * @param callback
     * @param exceptionCallback
     */
    public static void footwholesite(Activity activity,String name,final RequestCallback callback,
                                 final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("name",name);
        MapiUtil.getInstance().call(activity,footwholesite,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiFoodResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiFoodResult.class);
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
     * 酒店图片管理列表
     * @param activity
     * @param type
     * @param callback
     * @param exceptionCallback
     */
    public static void hotelpicindex(Activity activity,String type,final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("type",type);
        MapiUtil.getInstance().call(activity,hotelpicindex,params,new MapiUtil.MapiSuccessResponse(){
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

    /**
     * 酒店图片删除
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void hotelpicdel(Activity activity,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,hotelpicdel,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店套餐列表
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void footpackage(Activity activity,String page,String limit,final RequestPageCallback callback,
                                 final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,footpackage,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiFoodResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiFoodResult.class);
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
     * 酒店套餐获取菜品
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void foothotelfoot(Activity activity,final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,foothotelfoot,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiFoodResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiFoodResult.class);
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
     * 酒店套餐新增/修改
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void footpackageedit(Activity activity,String id,String name,String original_price,String cover_pic,String dishes,
                                       String meal_pic,String content,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id))
            params.put("id",id);
        params.put("name",name);
        params.put("original_price",original_price);
        params.put("cover_pic",cover_pic);
        if(!TextUtils.isEmpty(dishes))
            params.put("dishes",dishes);
        if(!TextUtils.isEmpty(meal_pic))
            params.put("meal_pic",meal_pic);
        if(!TextUtils.isEmpty(content))
            params.put("content",content);
        MapiUtil.getInstance().call(activity,footpackageedit,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店套餐详情
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void footpackageinfo(Activity activity,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,footpackageinfo,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店套餐删除
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void footpackagedel(Activity activity,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,footpackagedel,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店管理
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void managehotelmanage(Activity activity,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,managehotelmanage,params,new MapiUtil.MapiSuccessResponse(){
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
     * 酒店管理获取景点
     * @param activity
     * @param name
     * @param callback
     * @param exceptionCallback
     */
    public static void managescenicspot(Activity activity,String name,final RequestCallback callback,
                                     final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("name",name);
        MapiUtil.getInstance().call(activity,managescenicspot,params,new MapiUtil.MapiSuccessResponse(){
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

    /**
     * 酒店非营业时间列表
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void managenonoperating(Activity activity,final RequestCallback callback,
                                        final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,managenonoperating,params,new MapiUtil.MapiSuccessResponse(){
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

    /**
     * 酒店营业时间添加
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void manageaddnonoperating(Activity activity,String time,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("time",time);
        MapiUtil.getInstance().call(activity,manageaddnonoperating,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiResourceResult foodResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiResourceResult.class);
                callback.success(foodResult);

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 酒店营业时间删除
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void managetimedel(Activity activity,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        MapiUtil.getInstance().call(activity,managetimedel,params,new MapiUtil.MapiSuccessResponse(){
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
     *
     * 酒店管理编辑
     * */
    public static void managegetmanage(Activity activity,String merchantInfo_id,String name,String cover_pic,String restaurant_cat_id,String discount_rate,String feature,
                                       String customer_consumption,String province_id,String city_id,String area_id,String address,String longitude,String latitude,
                                       String scenic_spot,String desc,String population_max,String accept_booking_time,String booking_time,String pay_type,String dining_time,String tel,
                                       String mobile,String other,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("merchantInfo_id",merchantInfo_id);
        params.put("name",name);
        params.put("cover_pic",cover_pic);
        params.put("restaurant_cat_id",restaurant_cat_id);
        params.put("discount_rate",discount_rate);
        if(!TextUtils.isEmpty(feature))
            params.put("feature",feature);
        params.put("customer_consumption",customer_consumption);
        params.put("province_id",province_id);
        params.put("city_id",city_id);
        params.put("area_id",area_id);
        params.put("address",address);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("scenic_spot",scenic_spot);
        if(!TextUtils.isEmpty(desc))
            params.put("desc",desc);
        if(!TextUtils.isEmpty(population_max))
            params.put("population_max",population_max);
        if(!TextUtils.isEmpty(accept_booking_time))
            params.put("accept_booking_time",accept_booking_time);
        if(!TextUtils.isEmpty(booking_time))
            params.put("booking_time",booking_time);
        if(!TextUtils.isEmpty(pay_type))
            params.put("pay_type",pay_type);
        if(!TextUtils.isEmpty(dining_time))
            params.put("dining_time",dining_time);
        if(!TextUtils.isEmpty(tel))
            params.put("tel",tel);
        if(!TextUtils.isEmpty(mobile))
            params.put("mobile",mobile);
        if(!TextUtils.isEmpty(other))
            params.put("other",other);
        MapiUtil.getInstance().call(activity,managegetmanage,params,new MapiUtil.MapiSuccessResponse(){
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
