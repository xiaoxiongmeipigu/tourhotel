package com.zjhj.commom.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2016/2/17.
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        int length;

        if ((s == null) || (s.length() == 0) || s.equals("null")) {
            return true;
        }
        length = s.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 数组转换字符串
     *
     * @param list
     * @param ch
     * @return
     */
    public static String arrayToString(String[] list, String ch) {
        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            if (sb.length() != 0)
                sb.append(ch);
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * list转String
     *
     * @param list
     * @param ch
     * @return
     */
    public static String listToString(List<String> list, String ch) {
        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            if (sb.length() != 0)
                sb.append(ch);
            sb.append(str);
        }
        return sb.toString();
    }

//    /**
//     * 列表转String
//     *
//     * @param list
//     * @return
//     */
//    public static String listToUrlString(List<String> list) {
//        StringBuilder sb = new StringBuilder();
//
//        for (String str : list) {
//            if (sb.length() != 0)
//                sb.append("&");
//            sb.append(urlEncode(str));
//        }
//        return sb.toString();
//    }
//
//    /**
//     * URL
//     *
//     * @param strIn
//     * @return
//     */
//    public static String urlEncode(String strIn) {
//        if (isEmpty(strIn)) {
//            return "";
//        }
//        String strOut = null;
//        try {
//            strOut = java.net.URLEncoder.encode(strIn, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return strOut;
//    }


    /**
     * 替换、过滤特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "【").replaceAll("】", "】").replaceAll("！", "!");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 价格格式化
     *
     * @param price
     * @return
     */
    public static String formatPrice(Integer price) {
        if (price == null)
            return "0";
        return NumberFormat.getNumberInstance().format(price);
    }

    // 过滤特殊字符
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 格式化，两个小数点
     * @param price
     * @return
     */
    public static String numberFormat(long price){
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(price);
    }

    /**
     * 格式化，两个小数点
     * @param price
     * @return
     */
    public static String numberFormat(double price){
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(price);
    }

    /**
     * 格式化，姓名
     * @param name
     * @return
     */
    public static String nameFormat(String name){
        String str = "";
        if(TextUtils.isEmpty(name))
            str="";
        else{
            if(name.length()<=2)
                str = name;
            else
                str = name.substring(name.length()-2,name.length());
        }
        return str;
    }

    /**
     * 检证手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern.compile("1[3|5|7|8]\\d{9}");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();

    }

    /**
     * 手机号用****号隐藏中间数字
     *
     * @param phone
     * @return
     */
    public static String settingphone(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }


    /**
     * 邮箱用****号隐藏前面的字母
     *
     * @return
     */
    public static String settingemail(String email) {
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
    }

    /**
     * 检证座机号码
     *
     * @param str
     * @return
     */
    public static boolean isTel(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(010|02\\d|0[3-9]\\d{2})?-\\d{6,8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 检证统一社会信用代码
     *
     * @param str
     * @return
     */
    public static boolean isBusiness(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.length() < 1) {
            return false;
        }
        //^([0-9ABCDEFGHJKLMNPQRTUWXY]{2})([0-9]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{9})([0-9Y])$
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{18}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

}
