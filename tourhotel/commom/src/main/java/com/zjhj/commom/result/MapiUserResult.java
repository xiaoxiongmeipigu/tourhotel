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
    private String is_contract_over;
    private String contract_over_url;
    private String merchant_name;
    private String merchant_feature;
    private String merchant_cover_pic;
    private String share_page_url;

    public String getMerchant_cover_pic() {
        return merchant_cover_pic;
    }

    public void setMerchant_cover_pic(String merchant_cover_pic) {
        this.merchant_cover_pic = merchant_cover_pic;
    }

    public String getMerchant_feature() {
        return merchant_feature;
    }

    public void setMerchant_feature(String merchant_feature) {
        this.merchant_feature = merchant_feature;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getShare_page_url() {
        return share_page_url;
    }

    public void setShare_page_url(String share_page_url) {
        this.share_page_url = share_page_url;
    }

    public String getContract_over_url() {
        return contract_over_url;
    }

    public void setContract_over_url(String contract_over_url) {
        this.contract_over_url = contract_over_url;
    }

    public String getIs_contract_over() {
        return is_contract_over;
    }

    public void setIs_contract_over(String is_contract_over) {
        this.is_contract_over = is_contract_over;
    }

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
