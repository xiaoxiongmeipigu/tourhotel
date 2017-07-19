package com.zjhj.tourhotel.activity.food;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.adapter.food.FoodListAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.interfaces.OrderDeelListener;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.widget.BestSwipeRefreshLayout;
import com.zjhj.tourhotel.widget.CancelAlertDialog;
import com.zjhj.tourhotel.widget.DividerListItemDecoration;
import com.zjhj.tourhotel.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.clear_iv)
    ImageView clearIv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tv_right)
    TextView tvRight;

    FoodListAdapter mAdapter;

    private Integer pageIndex = 1;
    private Integer pageNum = 20;
    private Integer counts;

    private List<MapiFoodResult> mList;

    MainAlertDialog passDialog;
    private int pos = -1;
    CancelAlertDialog cancelAlertDialog;

    String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("菜品管理");
        tvRight.setText("新增");

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), getResources().getColor(R.color.background_gray)));
        mAdapter = new FoodListAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        passDialog = new MainAlertDialog(this);
        passDialog.setLeftBtnText("取消").setRightBtnText("确认");

        cancelAlertDialog = new CancelAlertDialog(this);
        cancelAlertDialog.setLeftBtnText("取消").setRightBtnText("提交").setTitle("修改").setHint("");
        cancelAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cancelAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void initListener() {

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    String keyWord = searchEt.getText().toString().trim();
                    searchStr = keyWord;
                    refreshData();
                }
                return true;
            }
        });


        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
//                searchStr = newText;
                if (newText.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                } else
                    clearIv.setVisibility(View.INVISIBLE);
//                refreshData();
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                if (pos >= 0)
                    left();
                passDialog.dismiss();
            }
        });

        cancelAlertDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlertDialog.dismiss();
            }
        });

        cancelAlertDialog.setRightClickListener(new View.OnClickListener() {//提交
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(cancelAlertDialog.getInfo())) {
                    MainToast.showShortToast("请输入菜品");
                    return;
                }
                if (pos >= 0)
                    right(cancelAlertDialog.getInfo());
                cancelAlertDialog.dismiss();
            }
        });


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

        mAdapter.setOrderDeelListener(new OrderDeelListener() {
            @Override
            public void left(View view, int position) {
                pos = position;
                MapiFoodResult foodResult = mList.get(position);
                passDialog.setTitle("确认下架" + (TextUtils.isEmpty(foodResult.getName()) ? "" : foodResult.getName()) + "?");
                passDialog.show();
            }

            @Override
            public void right(View view, int position) {
                pos = position;
                MapiFoodResult foodResult = mList.get(position);
                cancelAlertDialog.setInfo(TextUtils.isEmpty(foodResult.getName()) ? "" : foodResult.getName());
                cancelAlertDialog.show();
            }
        });

    }

    public void load() {
        showLoading();
        ItemApi.footindex(this, searchStr, pageIndex + "", pageNum + "", new RequestPageCallback<List<MapiFoodResult>>() {
            @Override
            public void success(Integer isNext, List<MapiFoodResult> success) {
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

    private void left() {//下架
        MapiFoodResult foodResult = mList.get(pos);
        showLoading();
        ItemApi.footfootdel(this, foodResult.getId(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(pos);
                mAdapter.notifyDataSetChanged();
                pos = -1;
                MainToast.showShortToast("下架成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void right(String content) {//修改
        MapiFoodResult foodResult = mList.get(pos);
        showLoading();
        ItemApi.footedit(this, foodResult.getId(), content, new RequestCallback<MapiFoodResult>() {
            @Override
            public void success(MapiFoodResult success) {
                hideLoading();
                if (null != success) {
                    MapiFoodResult foodResult1 = mList.get(pos);
                    foodResult1.setName(success.getName());
                    mAdapter.notifyDataSetChanged();
                }
                pos = -1;
                MainToast.showShortToast("修改成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back, R.id.clear_iv,R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear_iv:
                searchEt.setText("");
                searchStr = "";
                refreshData();
                break;
            case R.id.tv_right:
                ControllerUtil.go2FoodAdd();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != passDialog && passDialog.isShowing()) {
            passDialog.dismiss();
            passDialog = null;
        }

        if (null != cancelAlertDialog && cancelAlertDialog.isShowing()) {
            cancelAlertDialog.dismiss();
            cancelAlertDialog = null;
        }

    }
}
