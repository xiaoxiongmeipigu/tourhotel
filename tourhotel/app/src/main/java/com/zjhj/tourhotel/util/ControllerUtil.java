package com.zjhj.tourhotel.util;

import android.content.Intent;


import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tourhotel.activity.ForgetPsdActivity;
import com.zjhj.tourhotel.activity.LocationAddrActivity;
import com.zjhj.tourhotel.activity.LoginActivity;
import com.zjhj.tourhotel.activity.MainActivity;
import com.zjhj.tourhotel.activity.RegisterActivity;
import com.zjhj.tourhotel.activity.SetPsdActivity;
import com.zjhj.tourhotel.activity.ShowBigPicActivity;
import com.zjhj.tourhotel.activity.discuss.DiscussListActivity;
import com.zjhj.tourhotel.activity.food.FoodAddActivity;
import com.zjhj.tourhotel.activity.food.FoodListActivity;
import com.zjhj.tourhotel.activity.meal.MealAddActivity;
import com.zjhj.tourhotel.activity.meal.MealEditActivity;
import com.zjhj.tourhotel.activity.meal.MealListActivity;
import com.zjhj.tourhotel.activity.msg.MessageActivity;
import com.zjhj.tourhotel.activity.order.OrderDetailActivity;
import com.zjhj.tourhotel.activity.order.OrderListActivity;
import com.zjhj.tourhotel.activity.photo.AddPhotoActivity;
import com.zjhj.tourhotel.activity.photo.PhotoActivity;
import com.zjhj.tourhotel.activity.shop.ShopEditActivity;
import com.zjhj.tourhotel.activity.shop.ShopInfoActivity;
import com.zjhj.tourhotel.activity.shop.ShopTimeActivity;
import com.zjhj.tourhotel.activity.webview.WebviewControlActivity;

import java.util.ArrayList;


/**
 * Created by brain on 2016/6/22.
 */
public class ControllerUtil {

    /**
     * 登录
     */
    public static void go2Login() {
        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 设置密码
     */
    public static void go2SetPsd(String code) {
        Intent intent = new Intent(AppContext.getInstance(), SetPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code",code);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 首页
     */
    public static void go2Main() {
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 注册
     */
    public static void go2Register() {
        Intent intent = new Intent(AppContext.getInstance(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 忘记密码
     */
    public static void go2ForgetPsd() {
        Intent intent = new Intent(AppContext.getInstance(), ForgetPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * h5页面
     */
    public static void go2WebView(String url, String title,String shareTitle,String shareContext,String shareLOGO, boolean isShare) {
        Intent intent = new Intent(AppContext.getInstance(), WebviewControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isShare", isShare);
        intent.putExtra("shareTitle", shareTitle);

        intent.putExtra("shareContext", shareContext);
        intent.putExtra("shareLOGO", shareLOGO);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 地图选址
     */
    public static void go2LocationAddr() {
        Intent intent = new Intent(AppContext.getInstance(), LocationAddrActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 消息
     */
    public static void go2Message() {
        Intent intent = new Intent(AppContext.getInstance(), MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 图片展示
     */
    public static void go2ShowBig(ArrayList<MapiResourceResult> list, int position) {
        Intent intent = new Intent(AppContext.getInstance(), ShowBigPicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("list",list);
        intent.putExtra("position",position);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 评价列表
     */
    public static void go2DiscussList() {
        Intent intent = new Intent(AppContext.getInstance(), DiscussListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 订单详情
     */
    public static void go2OrderDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), OrderDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 评订单列表
     */
    public static void go2OrderList() {
        Intent intent = new Intent(AppContext.getInstance(), OrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 菜品列表
     */
    public static void go2FoodList() {
        Intent intent = new Intent(AppContext.getInstance(), FoodListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 菜品新增
     */
    public static void go2FoodAdd() {
        Intent intent = new Intent(AppContext.getInstance(), FoodAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 图片管理
     */
    public static void go2Photo() {
        Intent intent = new Intent(AppContext.getInstance(), PhotoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 图片管理列表
     */
    public static void go2AddPhoto(String type,String title) {
        Intent intent = new Intent(AppContext.getInstance(), AddPhotoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type",type);
        intent.putExtra("title",title);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 套餐管理
     */
    public static void go2MealList() {
        Intent intent = new Intent(AppContext.getInstance(), MealListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 套餐新增
     */
    public static void go2MealAdd() {
        Intent intent = new Intent(AppContext.getInstance(), MealAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 套餐编辑
     */
    public static void go2MealEdit(String id) {
        Intent intent = new Intent(AppContext.getInstance(), MealEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 酒店管理
     */
    public static void go2ShopInfo() {
        Intent intent = new Intent(AppContext.getInstance(), ShopInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 酒店编辑
     */
    public static void go2ShopEdit() {
        Intent intent = new Intent(AppContext.getInstance(), ShopEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 营业时间
     */
    public static void go2ShopTime() {
        Intent intent = new Intent(AppContext.getInstance(), ShopTimeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

}
