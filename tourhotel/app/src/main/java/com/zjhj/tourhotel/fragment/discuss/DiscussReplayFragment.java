package com.zjhj.tourhotel.fragment.discuss;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.discuss.DiscussItemAdapter;
import com.zjhj.tourhotel.base.BaseFrag;
import com.zjhj.tourhotel.interfaces.DiscussOnItemClickListener;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.widget.BestSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class DiscussReplayFragment extends BaseFrag {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    List<MapiDiscussResult> list;
    List<IndexData> mList;

    DiscussItemAdapter mAdapter;


    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;

    /**
     * 是否创建
     */
    protected boolean isCreate = false;

    public DiscussReplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreate=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_discuss_replay, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    //解决viewpager缓存
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreate) {
            //相当于Fragment的onResume
            //在这里处理加载数据等操作
//            refreshData();
        } else {
            //相当于Fragment的onPause
        }
    }


    private void initView(){
        mList = new ArrayList<>();
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DiscussItemAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mAdapter.setOnItemClickListener(new DiscussOnItemClickListener() {
            @Override
            public void onLookClick(View view, int position) {
                MapiDiscussResult discussResult = (MapiDiscussResult) mList.get(position).getData();
                ControllerUtil.go2OrderDetail(discussResult.getOrder_id());
            }

            @Override
            public void onReplayClick(View view, int position) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void load() {
        showLoading();
        ItemApi.commentindex(getActivity(), pageIndex + "", pageNum + "","1", new RequestPageCallback<List<MapiDiscussResult>>() {//已回复
            @Override
            public void success(Integer isNext, List<MapiDiscussResult> success) {
                swipeRefreshLayout.setRefreshing(false);
                hideLoading();
                counts = isNext;
                if (success.isEmpty())
                    return;
                list.clear();
                list.addAll(success);
                initData(list);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                swipeRefreshLayout.setRefreshing(false);
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void loadNext() {
        if (counts == null || counts <= pageIndex) {
            MainToast.showShortToast("没有更多数据了");
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            list.clear();
            mList.clear();
            pageIndex = 1;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    private void initData(List<MapiDiscussResult> data){
        int count = list.size();
        for (MapiDiscussResult mapiDiscussResult : data) {
            mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
            mList.add(new IndexData(count++, "ITEM", mapiDiscussResult));
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
