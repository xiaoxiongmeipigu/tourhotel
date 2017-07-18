package com.zjhj.tourhotel.activity.discuss;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.TabFragmentAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.fragment.discuss.DiscussReplayFragment;
import com.zjhj.tourhotel.fragment.discuss.DiscussUnReplayFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscussListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;

    private List<String> list_title = new ArrayList<>();
    private List<Fragment> list = new ArrayList<>();
    TabFragmentAdapter mAdapter;

    DiscussUnReplayFragment discussUnReplayFragment;
    DiscussReplayFragment discussReplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        back.setImageResource(R.mipmap.back_white);
        center.setText("评价管理");

        discussUnReplayFragment = new DiscussUnReplayFragment();
        discussReplayFragment = new DiscussReplayFragment();

        list.add(discussUnReplayFragment);
        list.add(discussReplayFragment);
//        list.add(newDiscussFragment);

        list_title.add("未回复");
        list_title.add("已回复");
//        list_title.add("最新");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }


}
