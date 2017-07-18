package com.zjhj.tourhotel.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.polites.android.GestureImageView;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/8/3.
 */
public class ShowBigPicActivity extends BaseActivity {

    @Bind(R.id.lay_header)
    RelativeLayout headerLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    private int pos = 0;
    private List<MapiResourceResult> mList;
    DisplayImageOptions options = null;

    boolean isDelete = false;
    boolean deleteReal = true;

    ArrayList<MapiResourceResult> delList;

    MainAlertDialog passDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show_bigpic);
        ButterKnife.bind(this);
        if (null != getIntent().getExtras()) {
            pos = getIntent().getIntExtra("position", 0);
            mList = (List<MapiResourceResult>) getIntent().getSerializableExtra("list");
            isDelete = getIntent().getBooleanExtra("isDelete", false);
            deleteReal = getIntent().getBooleanExtra("deleteReal",true);
        }
        initView();
        initListener();
    }

    private void initView() {

        delList = new ArrayList<>();

        line.setVisibility(View.GONE);
        back.setImageResource(R.mipmap.back_white);
        headerLayout.setBackgroundColor(Color.parseColor("#0D000000"));
        if(isDelete) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(R.mipmap.delete);
        }else
            ivRight.setVisibility(View.GONE);

        viewpager.setAdapter(new ViewPagerAdapter());
        viewpager.setCurrentItem(pos);
        initOptions();

        passDialog = new MainAlertDialog(this);
        passDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认删除该图片吗？");

    }

    private void initListener(){
        passDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDialog.dismiss();
            }
        });

        passDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos>=0)
                    delete();
                passDialog.dismiss();
            }
        });
    }

    private void initOptions() {
        options = new DisplayImageOptions.Builder()
                //				.showImageOnLoading(R.drawable.design_default)
                .showImageForEmptyUri(R.mipmap.img_default)
                .showImageOnFail(R.mipmap.img_default)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.NONE)//EXACTLY
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())//Bitmap.Config.RGB_565
                .bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public void onBackPressed() {
        if(isDelete){
            Intent intent = new Intent();
            intent.putExtra("list",delList);
            setResult(RESULT_OK,intent);
            finish();
        }else
            super.onBackPressed();
    }

    @OnClick({R.id.back,R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                if(isDelete){
                    Intent intent = new Intent();
                    intent.putExtra("list",delList);
                    setResult(RESULT_OK,intent);
                    finish();
                }else
                    finish();
                break;
            case R.id.iv_right:
                pos = viewpager.getCurrentItem();
                passDialog.show();
                break;
        }
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View channelView = null;
            try {
                channelView = LayoutInflater.from(ShowBigPicActivity.this).inflate(
                        R.layout.viewpager_bigpic, container, false);
                //GestureImageView
                final GestureImageView mImageView = (GestureImageView) channelView
                        .findViewById(R.id.pager_img); //手势缩放类
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                final ProgressBar progressBar = (ProgressBar) channelView.findViewById(R.id.image_zoom_progressbar);
                ImageLoader.getInstance().displayImage(
                        TextUtils.isEmpty(mList.get(position).getPic_url())?"":mList.get(position).getPic_url(), mImageView, options, new ImageLoadingListener() {

                            @Override
                            public void onLoadingCancelled(String arg0, View arg1) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingStarted(String arg0, View arg1) {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                        }
                );
                container.addView(channelView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return channelView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void delete(){
        if(deleteReal){
            MapiResourceResult resourceResult = mList.get(pos);
            showLoading();
            ItemApi.hotelpicdel(this, resourceResult.getId(), new RequestCallback() {
                @Override
                public void success(Object success) {
                    hideLoading();
                    MainToast.showShortToast("删除成功");
                    delList.add(mList.get(pos));
                    mList.remove(pos);
                    viewpager.getAdapter().notifyDataSetChanged();
                    pos = -1;
                    if(mList.isEmpty()){
                        Intent intent = new Intent();
                        intent.putExtra("list",delList);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                }
            }, new RequestExceptionCallback() {
                @Override
                public void error(Integer code, String message) {
                    hideLoading();
                    MainToast.showShortToast(message);
                }
            });
        }else{
            delList.add(mList.get(pos));
            mList.remove(pos);
            viewpager.getAdapter().notifyDataSetChanged();
            pos = -1;
            if(mList.isEmpty()){
                Intent intent = new Intent();
                intent.putExtra("list",delList);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //退出当前页面清除内存
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearMemoryCache();
        if(null!=passDialog&&passDialog.isShowing()){
            passDialog.dismiss();
            passDialog = null;
        }
        super.onDestroy();
    }

}
