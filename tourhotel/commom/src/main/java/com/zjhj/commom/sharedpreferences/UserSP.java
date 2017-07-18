package com.zjhj.commom.sharedpreferences;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiUserResult;

/**
 * Created by brain on 2016/6/14.
 */
public class UserSP extends AbstractSP {

    private final static String KEY_SP_USER = "house.user";
    private final static String KEY_SP_ADDR = "house.addr";
    private final static String KEY_SP_USER_GUIDE = "house_user_guide";
    private final static String KEY_SP_Resources = "house.resources";
    private final static String KEY_SP_Alias = "house_user_Alias";
    private final static String KEY_SP_CITY_JSON = "tour.cityjson";

    public UserSP(Context context) {
        super(context);
    }

    public void saveUserBean(MapiUserResult userbean) {
        sharedPreferences.edit().putString(KEY_SP_USER, JSONObject.toJSONString(userbean)).commit();
    }

    public void saveUserAddr(MapiCityItemResult mapiCityItemResult) {
        sharedPreferences.edit().putString(KEY_SP_ADDR, JSONObject.toJSONString(mapiCityItemResult)).commit();
    }

    public void saveCityJson(String city){
        sharedPreferences.edit().putString(KEY_SP_CITY_JSON,city).commit();
    }

    public MapiUserResult getUserBean() {
        String userJsonStr = sharedPreferences.getString(KEY_SP_USER, null);
        if (TextUtils.isEmpty(userJsonStr)) {
            return null;
        }
        return JSONObject.parseObject(userJsonStr, MapiUserResult.class);
    }

    public MapiCityItemResult getUserAddr() {
        String userJsonStr = sharedPreferences.getString(KEY_SP_ADDR, null);
        if (TextUtils.isEmpty(userJsonStr)) {
            return null;
        }
        return JSONObject.parseObject(userJsonStr, MapiCityItemResult.class);
    }

    public String getCityJson() {
        String userJsonStr = sharedPreferences.getString(KEY_SP_CITY_JSON, null);
        if (TextUtils.isEmpty(userJsonStr)) {
            return "";
        }
        return userJsonStr;
    }


    public void saveResource(String json){
        sharedPreferences.edit().putString(KEY_SP_Resources, json).commit();
    }

    public String getResource() {
        String resourceJsonStr = sharedPreferences.getString(KEY_SP_Resources, null);
        if (TextUtils.isEmpty(resourceJsonStr)) {
            return null;
        }
        return resourceJsonStr;
    }

    public void setAlias(boolean isAlias){
        sharedPreferences.edit().putBoolean(KEY_SP_Alias, isAlias).commit();
    }

    public boolean getAlias(){
        boolean isAlias = sharedPreferences.getBoolean(KEY_SP_Alias,false);
        return isAlias;
    }

    public boolean checkLogin() {
        return getUserBean() != null && !TextUtils.isEmpty(getUserBean().getMerchant_id());
    }

    public void clearUserData() {
        sharedPreferences.edit().remove(KEY_SP_USER).commit();
        sharedPreferences.edit().remove(KEY_SP_Alias).commit();
//        sharedPreferences.edit().remove(KEY_SP_ADDR).commit();

    }

    /**
     * 保存版本
     *
     * @param value
     */
    public void saveUserGuide(String value) {
        sharedPreferences.edit().putString(KEY_SP_USER_GUIDE, value).commit();
    }

    /**
     * 获取版本
     *
     * @return
     */
    public String getUserGuide() {
        String code = sharedPreferences.getString(KEY_SP_USER_GUIDE, null);
        if (TextUtils.isEmpty(code)) {
            return null;
        }
        return code;
    }

}
