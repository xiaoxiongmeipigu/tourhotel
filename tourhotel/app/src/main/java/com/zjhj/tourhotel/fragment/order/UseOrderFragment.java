package com.zjhj.tourhotel.fragment.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.order.OrderUseAdapter;
import com.zjhj.tourhotel.adapter.order.OrderWaitAdapter;
import com.zjhj.tourhotel.base.BaseFrag;
import com.zjhj.tourhotel.interfaces.OrderDeelListener;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.widget.BestSwipeRefreshLayout;
import com.zjhj.tourhotel.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UseOrderFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    OrderUseAdapter mAdapter;
    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;

    private List<MapiOrderResult> mList;

    MainAlertDialog passDialog;
    private int pos = -1;

    public UseOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    private void initView() {
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderUseAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        passDialog = new MainAlertDialog(getActivity());
        passDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认使用？");

    }

    private void initListener() {
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
                    comfirm();
                passDialog.dismiss();
            }
        });

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiOrderResult orderResult = mList.get(position);
                ControllerUtil.go2OrderDetail(orderResult.getId());
            }
        });

        mAdapter.setOrderDeelListener(new OrderDeelListener() {
            @Override
            public void left(View view, int position) {
            }

            @Override
            public void right(View view, int position) {
                pos = position;
                passDialog.show();
            }
        });

    }

    public void load() {
        showLoading();
        ItemApi.orderindex(getActivity(),"2", pageIndex + "", pageNum + "",new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
                swipeRefreshLayout.setRefreshing(false);
                hideLoading();
                counts = isNext;
                if (success.isEmpty())
                    return;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
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
            mList.clear();
            pageIndex = 1;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    private void comfirm(){
        MapiOrderResult orderResult =  mList.get(pos);
        showLoading();
        ItemApi.ordermack(getActivity(), orderResult.getId(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(pos);
                mAdapter.notifyDataSetChanged();
                pos = -1;
                MainToast.showShortToast("确认成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if(null!=passDialog&&passDialog.isShowing()){
            passDialog.dismiss();
            passDialog = null;
        }}

}
