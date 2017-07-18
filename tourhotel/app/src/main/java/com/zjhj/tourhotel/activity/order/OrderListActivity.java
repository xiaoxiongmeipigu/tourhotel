package com.zjhj.tourhotel.activity.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.TabFragmentAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.fragment.discuss.DiscussReplayFragment;
import com.zjhj.tourhotel.fragment.discuss.DiscussUnReplayFragment;
import com.zjhj.tourhotel.fragment.order.CancelOrderFragment;
import com.zjhj.tourhotel.fragment.order.CompleteOrderFragment;
import com.zjhj.tourhotel.fragment.order.UseOrderFragment;
import com.zjhj.tourhotel.fragment.order.WaitOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<String> list_title = new ArrayList<>();
    private List<Fragment> list = new ArrayList<>();
    TabFragmentAdapter mAdapter;

    WaitOrderFragment waitOrderFragment;
    CancelOrderFragment cancelOrderFragment;
    UseOrderFragment useOrderFragment;
    CompleteOrderFragment completeOrderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("订单管理");

        waitOrderFragment = new WaitOrderFragment();
        cancelOrderFragment = new CancelOrderFragment();
        useOrderFragment = new UseOrderFragment();
        completeOrderFragment = new CompleteOrderFragment();

        list.add(waitOrderFragment);
        list.add(cancelOrderFragment);
        list.add(useOrderFragment);
        list.add(completeOrderFragment);

        list_title.add("待审核");
        list_title.add("申请取消");
        list_title.add("待使用");
        list_title.add("已完成");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(2)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(3)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
