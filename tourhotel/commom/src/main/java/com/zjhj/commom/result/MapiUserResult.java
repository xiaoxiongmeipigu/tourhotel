package com.zjhj.commom.result;

import java.io.Serializable;

/**
 *Created by brain on 2016/6/14
 */
public class MapiUserResult extends MapiBaseResult {
    private String token;
    private String guide_id;
    private String mobile;
    private String merchant_id;
    private String expiry_time;
    private String parent_code;
    private String username;
    private String business;
    private String hygiene;
    private String cat;
    private String food;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getHygiene() {
        return hygiene;
    }

    public void setHygiene(String hygiene) {
        this.hygiene = hygiene;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(String guide_id) {
        this.guide_id = guide_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
