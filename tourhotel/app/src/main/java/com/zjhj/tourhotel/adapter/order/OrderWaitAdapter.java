package com.zjhj.tourhotel.adapter.order;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.OrderDeelListener;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/1.
 */
public class OrderWaitAdapter extends RecyclerView.Adapter<OrderWaitAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiOrderResult> mList;
    Context mContext;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OrderDeelListener orderDeelListener;

    public void setOrderDeelListener(OrderDeelListener orderDeelListener) {
        this.orderDeelListener = orderDeelListener;
    }

    public OrderWaitAdapter(Context context, List<MapiOrderResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_order_wait, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiOrderResult itemResult = mList.get(position);

        final ArrayList<MapiFoodResult> foodResults = (ArrayList<MapiFoodResult>) itemResult.getOrder_detail();
        if (null != foodResults && !foodResults.isEmpty()) {
            holder.recyclerView.removeAllViews();
            holder.recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            FoodItemAdapter mAdapter = new FoodItemAdapter(mContext, foodResults);
            holder.recyclerView.setAdapter(mAdapter);
            mAdapter.setParentPos(position);
            mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (null != onItemClickListener&&position>=0)
                        onItemClickListener.onItemClick(view,position);
                }
            });

            double allOldPrice = 0;

            for (MapiFoodResult foodResult : foodResults) {
                String price = TextUtils.isEmpty(foodResult.getOriginal_single_price()) ? "0" : foodResult.getOriginal_single_price();
                String numStr = TextUtils.isEmpty(foodResult.getNum()) ? "0" : foodResult.getNum();
                double priceDouble = Double.parseDouble(price);
                allOldPrice += priceDouble * Integer.parseInt(numStr);
            }

            String discountRateStr = itemResult.getMi_discount_rate();
            discountRateStr = TextUtils.isEmpty(discountRateStr)?"10":discountRateStr;
            holder.discountRate.setText(discountRateStr+"折");

            double discountRateDouble = Double.parseDouble(discountRateStr);
            double newPriceDouble = allOldPrice * discountRateDouble / 10;
            holder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.oldPrice.setText("原价：¥ "+StringUtil.numberFormat(allOldPrice));
            holder.newPrice.setText("¥ " + StringUtil.numberFormat(newPriceDouble));

        } else {
            holder.recyclerView.setVisibility(View.GONE);
        }

        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        holder.reject.setTag(position);
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != orderDeelListener)
                    orderDeelListener.left(view, (Integer) view.getTag());
            }
        });

        holder.pass.setTag(position);
        holder.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != orderDeelListener)
                    orderDeelListener.right(view, (Integer) view.getTag());
            }
        });

        holder.dateTv.setText("下单时间：" + (TextUtils.isEmpty(itemResult.getAddtime()) ? "" : itemResult.getAddtime()));


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        @Bind(R.id.reject)
        TextView reject;
        @Bind(R.id.pass)
        TextView pass;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.old_price)
        TextView oldPrice;
        @Bind(R.id.discount_rate)
        TextView discountRate;
        @Bind(R.id.new_price)
        TextView newPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
