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
public class SpotsAddAdapter extends RecyclerView.Adapter<SpotsAddAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiResourceResult> mList;
    Context mContext;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SpotsAddAdapter(Context context, List<MapiResourceResult> list) {
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
        return new ViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MapiResourceResult resourceResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
        holder.nameTv.setText(TextUtils.isEmpty(resourceResult.getName()) ? "" : resourceResult.getName());

        String pname = TextUtils.isEmpty(resourceResult.getPname())?"":resourceResult.getPname()+" ";
        String cname = TextUtils.isEmpty(resourceResult.getCname())?"":resourceResult.getCname()+" ";
        String aname = TextUtils.isEmpty(resourceResult.getAname())?"":resourceResult.getAname()+" ";
        holder.cityTv.setText(pname+cname+aname);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.city_tv)
        TextView cityTv;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
