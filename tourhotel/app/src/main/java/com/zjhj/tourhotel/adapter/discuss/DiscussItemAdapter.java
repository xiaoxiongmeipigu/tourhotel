package com.zjhj.tourhotel.adapter.discuss;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.DiscussOnItemClickListener;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;
import com.zjhj.tourhotel.util.ControllerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/30.
 */
public class DiscussItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM = 0;
    private final static int DIVIDER = 1;

    LayoutInflater inflater;
    Context mContext;

    private List<IndexData> mList;

    private DiscussOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DiscussOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DiscussItemAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.item_discuss, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_color_gray, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_color_gray, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "ITEM":
                return ITEM;
            case "DIVIDER":
                return DIVIDER;
            default:
                return DIVIDER;
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        @Bind(R.id.replay)
        TextView replay;
        @Bind(R.id.replay_rl)
        RelativeLayout replayRl;
        @Bind(R.id.shop_tv)
        TextView shopTv;
        @Bind(R.id.replay_ll)
        LinearLayout replayLl;
        @Bind(R.id.order_tv)
        TextView orderTv;
        @Bind(R.id.order_ll)
        LinearLayout orderLl;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setItem(ItemViewHolder holder, int position) {
        MapiDiscussResult mapiDiscussResult = (MapiDiscussResult) mList.get(position).getData();
        final ArrayList<MapiResourceResult> picList = mapiDiscussResult.getPic();
        if (null != picList && !picList.isEmpty()) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            holder.recyclerView.setLayoutManager(manager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            holder.recyclerView.setHasFixedSize(true);
            DiscussChildItemAdapter mAdapter = new DiscussChildItemAdapter(mContext, picList);
            holder.recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ControllerUtil.go2ShowBig(picList, position);
                }
            });
        }else{
            holder.recyclerView.setVisibility(View.GONE);
        }

        holder.nameTv.setText(TextUtils.isEmpty(mapiDiscussResult.getGname()) ? "" : mapiDiscussResult.getGname());
        String dateStr = TextUtils.isEmpty(mapiDiscussResult.getAddtime()) ? "" : mapiDiscussResult.getAddtime();
        holder.dateTv.setText(dateStr);
        holder.orderTv.setText("订单号："+(TextUtils.isEmpty(mapiDiscussResult.getOrder_id())?"":mapiDiscussResult.getOrder_id()));

        holder.orderLl.setTag(position);
        holder.orderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onLookClick(view, (Integer) view.getTag());
            }
        });

        holder.replay.setTag(position);
        holder.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onReplayClick(view, (Integer) view.getTag());
            }
        });

        holder.content.setText(TextUtils.isEmpty(mapiDiscussResult.getContent()) ? "" : mapiDiscussResult.getContent());
        if (!TextUtils.isEmpty(mapiDiscussResult.getReply())) {
            holder.replayLl.setVisibility(View.VISIBLE);
            holder.replayRl.setVisibility(View.GONE);
            String str = "<font color='#1ea1f3'>商家回复: </font>" + mapiDiscussResult.getReply();
            holder.shopTv.setText(Html.fromHtml(str));
        } else {
            holder.replayLl.setVisibility(View.GONE);
            holder.replayRl.setVisibility(View.VISIBLE);
        }



    }


}
