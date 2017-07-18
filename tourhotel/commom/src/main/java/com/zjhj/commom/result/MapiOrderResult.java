package com.zjhj.commom.result;

import java.util.List;

/**
 * Created by brain on 2017/6/30.
 */
public class MapiOrderResult extends MapiBaseResult{

    private String addtime;
    private String mobile;
    private String use_date;
    private String use_begin_time;
    private String use_end_time;
    private String taste;
    private String remark;
    private String discount_rate;
    private String mi_discount_rate;
    private String total_price;
    private String gname;
    private String content;
    private String revoke_reason;

    public String getRevoke_reason() {
        return revoke_reason;
    }

    public void setRevoke_reason(String revoke_reason) {
        this.revoke_reason = revoke_reason;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    private List<MapiFoodResult> order_detail;

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getMi_discount_rate() {
        return mi_discount_rate;
    }

    public void setMi_discount_rate(String mi_discount_rate) {
        this.mi_discount_rate = mi_discount_rate;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<MapiFoodResult> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(List<MapiFoodResult> order_detail) {
        this.order_detail = order_detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getUse_begin_time() {
        return use_begin_time;
    }

    public void setUse_begin_time(String use_begin_time) {
        this.use_begin_time = use_begin_time;
    }

    public String getUse_date() {
        return use_date;
    }

    public void setUse_date(String use_date) {
        this.use_date = use_date;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }
}
