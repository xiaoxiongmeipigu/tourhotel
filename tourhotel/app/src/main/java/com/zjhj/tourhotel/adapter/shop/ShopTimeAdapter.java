package com.zjhj.tourhotel.adapter.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/3.
 */
public class ShopTimeAdapter extends RecyclerView.Adapter<ShopTimeAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiResourceResult> mList;
    Context mContext;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ShopTimeAdapter(Context context, List<MapiResourceResult> list) {
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
        return new ViewHolder(inflater.inflate(R.layout.item_shop_time, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MapiResourceResult resourceResult = mList.get(position);
        holder.delete.setTag(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        holder.nameTv.setText(TextUtils.isEmpty(resourceResult.getNonoperating_time())?"":resourceResult.getNonoperating_time());

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.delete)
        TextView delete;
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
