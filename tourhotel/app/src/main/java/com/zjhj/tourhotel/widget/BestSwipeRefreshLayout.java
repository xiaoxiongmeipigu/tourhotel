package com.zjhj.tourhotel.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.zjhj.tourhotel.R;

/**
 * Created by brain on 2016/6/23.
 */
public class BestSwipeRefreshLayout extends SwipeRefreshLayout{
    private BestRefreshListener bestRefreshListener;

    public BestSwipeRefreshLayout(Context context) {
        super(context);
        initView();
        initListener();
    }

    public BestSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListener();
    }

    public void setBestRefreshListener(BestRefreshListener bestRefreshListener) {
        this.bestRefreshListener = bestRefreshListener;
    }

    private void initView() {
        setColorSchemeResources(R.color.shop_blue);
        setProgressBackgroundColorSchemeResource(android.R.color.white);
        setSize(SwipeRefreshLayout.LARGE);
    }

    public void initListener() {
        this.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                bestRefreshListener.onBestRefresh();
            }
        });
    }

    public interface BestRefreshListener {
        void onBestRefresh();
    }

}
