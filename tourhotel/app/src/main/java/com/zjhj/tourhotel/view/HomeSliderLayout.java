package com.zjhj.tourhotel.view;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.util.CountDownTimerUtil;
import com.zjhj.tourhotel.widget.LoopViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/8/30.
 */
public class HomeSliderLayout extends RelativeLayout {
    @Bind(R.id.index_viewpager)
    LoopViewPager indexViewpager;
    @Bind(R.id.guide_dot)
    LinearLayout guideDot;

    private Context mContext;
    private View view;

    CountDownTimerUtil countDownTimerUtil;
    BaseActivity activity;
    private boolean isSlider = false;
    private boolean isSliderPlay = false;
    List<MapiResourceResult> imgs;
    public void setSlider(boolean isSlider){
        this.isSlider = isSlider;
    }

    public HomeSliderLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
        initListener();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
        initListener();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_slider, this);
        ButterKnife.bind(this, view);
        imgs = new ArrayList<>();
    }

    private void initListener(){

    }

    public void load(List<MapiResourceResult> list) {
        if(null!=countDownTimerUtil) {
            countDownTimerUtil.cancel();
        }
        if (null != list) {//&&list.size()>0
            imgs.clear();
            imgs.addAll(list);
            /*sliderViewList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SimpleDraweeView view = (SimpleDraweeView) LayoutInflater.from(mContext).inflate(R.layout.layout_draweeview, null);
                //创建将要下载的图片的URI
                Uri imageUri = Uri.parse(list.get(i).getImg_url());
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                        .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(250)))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(view.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>())
                        .build();
                view.setController(controller);


                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                sliderViewList.add(view);
            }
            ShopPagerAdapter sliderAdapter = new ShopPagerAdapter(sliderViewList);
            indexViewpager.setAdapter(sliderAdapter);*/

            ImagePagerAdapter sliderAdapter = new ImagePagerAdapter(imgs);
            indexViewpager.setAdapter(sliderAdapter);

            guideDot.removeAllViews();
            for (int i = 0; i < imgs.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPUtil.dip2px(8), DPUtil.dip2px(8));
                params.setMargins(DPUtil.dip2px(9), 0, DPUtil.dip2px(9), DPUtil.dip2px(10));
                imageView.setLayoutParams(params);
                imageView.setBackgroundResource(R.drawable.selector_item_dot);
                guideDot.addView(imageView);
            }
            guideDot.getChildAt(0).setSelected(true);
            indexViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                   /* for (int i = 0; i < sliderViewList.size(); i++) {
                        if (position == i)
                            guideDot.getChildAt(i).setSelected(true);
                        else
                            guideDot.getChildAt(i).setSelected(false);
                    }*/

                    if(null!=guideDot){
                        //这是重点
                        int newPosition = (position - 1 + imgs.size())%imgs.size();
                        //修改全部的position长度
//                    int newPosition = position % sliderViewList.size();

                        for (int i = 0; i < imgs.size(); i++) {
                            if (newPosition == i) {
                                if(null!= guideDot.getChildAt(i))
                                    guideDot.getChildAt(i).setSelected(true);
                            }else {
                                if(null!= guideDot.getChildAt(i))
                                    guideDot.getChildAt(i).setSelected(false);
                            }
                        }
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }

            });
        }
        if(isSlider) {
           /* ViewPagerScroller viewPagerScroller = new ViewPagerScroller(getContext(),new AccelerateDecelerateInterpolator());
            //调整速率
            viewPagerScroller.setScrollDuration(200);
            viewPagerScroller.initViewPagerScroll(indexViewpager);           //初始化ViewPager时,反射修改滑动速度*/
            isSliderPlay = true;
            slideImage();
        }
    }

    private void slideImage(){

        if(null!=countDownTimerUtil)
            countDownTimerUtil.start();
        else{
            countDownTimerUtil = new CountDownTimerUtil(5 * 1000, 1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    if(null==mContext||null==activity||activity.isFinishing()||getVisibility()==GONE||getVisibility()==INVISIBLE) {
                        DebugLog.i("CountDownTimerUtil==cancel");
                        isSliderPlay = false;
                        cancel();
                    }
                }

                @Override
                public void onFinish() {
                    if(null!=imgs&&!imgs.isEmpty()){
                        //这里是设置当前页的下一页
                        indexViewpager.setCurrentItem(indexViewpager.getCurrentItem()+1,true);
                        indexViewpager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(isSliderPlay)
                                    slideImage();
                            }
                        }, 500);
                    }


                }
            }.start();
        }

    }

    private class ImagePagerAdapter extends PagerAdapter {

        private List<MapiResourceResult> imageViewList ;

        public ImagePagerAdapter(List<MapiResourceResult> imageViewList) {
            this.imageViewList = imageViewList;
        }

        @Override
        public int getCount() {
            return null==imageViewList?0:imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimpleDraweeView view = (SimpleDraweeView) LayoutInflater.from(mContext).inflate(R.layout.layout_draweeview,null);

            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(imageViewList.get(position%imageViewList.size()).getImg_url());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(200)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(view.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            view.setController(controller);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ((ViewPager) container).addView(view, 0);
            return view;

        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //第三处修改，移除的索引为集合的长度
//            int newPosition = position % imageViewList.size();
//            container.removeView(imageViewList.get(newPosition));
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(!hasWindowFocus) {
            DebugLog.i("onWindowFocusChanged=>"+hasWindowFocus);
            DebugLog.i("CountDownTimerUtil==cancel");
            if(null!=imgs&&!imgs.isEmpty()){
                isSliderPlay = false;
                countDownTimerUtil.cancel();
                countDownTimerUtil = null;
            }

        }else{
            isSliderPlay = true;
            if(null==countDownTimerUtil){
                if(null!=imgs&&!imgs.isEmpty())
                    slideImage();
            }

        }

    }

}
