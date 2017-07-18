package com.zjhj.tourhotel.util;


import com.zjhj.commom.result.MapiResourceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brain on 2016/7/29.
 */
public class JGJDataSource {


    public static final String TYPE_XIANHUO = 0x01+"";
    public static final String TYPE_XIANDAN = 0x02+"";
    public static final String TYPE_HUODONG = 0x03+"";
    public static final String TYPE_PINTUAN = 0x04+"";
    public static final String TYPE_DINGZHI = 0x05+"";
    public static final String TYPE_TUIHUO = 0x06+"";
    public static final String TYPE_VIP = 0x07+"";
    public static final String TYPE_MORE = 0x08+"";

    public static List<MapiResourceResult> getArea(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("0","全部"));
        list.add(new MapiResourceResult("0","丽江"));
        list.add(new MapiResourceResult("0","马尔代夫"));
        list.add(new MapiResourceResult("0","九寨沟"));
        list.add(new MapiResourceResult("0","三亚"));
        list.add(new MapiResourceResult("0","普陀山"));
        list.add(new MapiResourceResult("0","乌镇"));
        list.add(new MapiResourceResult("0","九华山"));
        list.add(new MapiResourceResult("0","成都"));
        list.add(new MapiResourceResult("0","五台山"));
        list.add(new MapiResourceResult("0","凤凰古城"));

        return list;
    }

    public static List<MapiResourceResult> getType() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("0", "全部"));
        list.add(new MapiResourceResult("0", "少数名族"));
        list.add(new MapiResourceResult("0", "社会餐厅"));
        list.add(new MapiResourceResult("0", "温泉"));
        list.add(new MapiResourceResult("0", "连锁餐厅"));
        list.add(new MapiResourceResult("0", "农家乐"));
        list.add(new MapiResourceResult("0", "茶餐厅"));
        list.add(new MapiResourceResult("0", "异域风情"));
        return list;
    }

    public static List<MapiResourceResult> getSort() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("1", "智能排序"));
        list.add(new MapiResourceResult("1", "离我最近"));
        list.add(new MapiResourceResult("2", "人气最高"));
        list.add(new MapiResourceResult("3", "好评优先"));
        list.add(new MapiResourceResult("4", "用餐最低标准"));
        return list;
    }

    public static List<MapiResourceResult> getAcceptTiem() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("1", "00:00"));
        list.add(new MapiResourceResult("1", "00:30"));
        list.add(new MapiResourceResult("2", "01:00"));
        list.add(new MapiResourceResult("3", "01:30"));
        list.add(new MapiResourceResult("4", "02:00"));
        list.add(new MapiResourceResult("5", "02:30"));
        list.add(new MapiResourceResult("6", "03:00"));
        list.add(new MapiResourceResult("7", "03:30"));
        list.add(new MapiResourceResult("8", "04:00"));
        list.add(new MapiResourceResult("9", "04:30"));
        list.add(new MapiResourceResult("10", "05:00"));
        list.add(new MapiResourceResult("11", "05:30"));
        list.add(new MapiResourceResult("12", "06:00"));
        list.add(new MapiResourceResult("13", "06:30"));
        list.add(new MapiResourceResult("14", "07:00"));
        list.add(new MapiResourceResult("15", "07:30"));
        list.add(new MapiResourceResult("16", "08:00"));
        list.add(new MapiResourceResult("17", "08:30"));
        list.add(new MapiResourceResult("18", "09:00"));
        list.add(new MapiResourceResult("19", "09:30"));
        list.add(new MapiResourceResult("20", "10:00"));
        list.add(new MapiResourceResult("21", "10:30"));
        list.add(new MapiResourceResult("22", "11:00"));
        list.add(new MapiResourceResult("23", "11:30"));
        list.add(new MapiResourceResult("24", "12:00"));
        list.add(new MapiResourceResult("25", "12:30"));
        list.add(new MapiResourceResult("26", "13:00"));
        list.add(new MapiResourceResult("27", "13:30"));
        list.add(new MapiResourceResult("28", "14:00"));
        list.add(new MapiResourceResult("29", "14:30"));
        list.add(new MapiResourceResult("30", "15:00"));
        list.add(new MapiResourceResult("31", "15:30"));
        list.add(new MapiResourceResult("32", "16:00"));
        list.add(new MapiResourceResult("33", "16:30"));
        list.add(new MapiResourceResult("34", "17:00"));
        list.add(new MapiResourceResult("35", "17:30"));
        list.add(new MapiResourceResult("36", "18:00"));
        list.add(new MapiResourceResult("37", "18:30"));
        list.add(new MapiResourceResult("38", "19:00"));
        list.add(new MapiResourceResult("39", "19:30"));
        list.add(new MapiResourceResult("40", "20:00"));
        list.add(new MapiResourceResult("41", "20:30"));
        list.add(new MapiResourceResult("42", "21:00"));
        list.add(new MapiResourceResult("43", "21:30"));
        list.add(new MapiResourceResult("44", "22:00"));
        list.add(new MapiResourceResult("45", "22:30"));
        list.add(new MapiResourceResult("46", "23:00"));
        list.add(new MapiResourceResult("47", "23:30"));
        return list;
    }

    public static List<MapiResourceResult> getPayType() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("1", "支付宝"));
        list.add(new MapiResourceResult("2", "微信"));
        list.add(new MapiResourceResult("3", "现金"));
        list.add(new MapiResourceResult("4", "银联"));
        return list;
    }

    public static List<MapiResourceResult> getDining() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("1", "早餐"));
        list.add(new MapiResourceResult("2", "午餐"));
        list.add(new MapiResourceResult("3", "晚餐"));
        return list;
    }

}
