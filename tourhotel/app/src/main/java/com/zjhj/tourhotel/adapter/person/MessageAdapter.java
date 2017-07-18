package com.zjhj.tourhotel.adapter.person;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiMsgResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/12.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiMsgResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MessageAdapter(Context context, List<MapiMsgResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        MapiMsgResult msgResult = mList.get(position);
        holder.dateTv.setText(TextUtils.isEmpty(msgResult.getAddtime())?"":msgResult.getAddtime());
        holder.title.setText(TextUtils.isEmpty(msgResult.getTitle())?"":msgResult.getTitle());
        holder.content.setText(TextUtils.isEmpty(msgResult.getContent())?"":msgResult.getContent());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.content)
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
