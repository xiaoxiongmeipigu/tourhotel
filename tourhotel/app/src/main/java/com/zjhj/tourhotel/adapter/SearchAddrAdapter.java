package com.zjhj.tourhotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名： com.amap.searchdemo
 * <p>
 * 创建时间：2016/10/19
 * 项目名称：SearchDemo
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 * <p>
 * 类说明：
 */
public class SearchAddrAdapter extends BaseAdapter {

    private List<Tip> data;
    private Context context;

    private int selectedPosition = 0;

    private RecyOnItemClickListener itemClickListener;

    public void setItemClickListener(RecyOnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public SearchAddrAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<Tip> data) {
        this.data = data;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_holder_addr, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindView(position);

        return convertView;
    }


    class ViewHolder {
        TextView textTitle;
        TextView textSubTitle;
        LinearLayout rootView;
        public ViewHolder(View view) {
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textSubTitle = (TextView) view.findViewById(R.id.text_title_sub);
            rootView = (LinearLayout) view.findViewById(R.id.root_view);
        }

        public void bindView(int position) {
            if (position >= data.size())
                return;

            Tip poiItem = data.get(position);

            rootView.setTag(position);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null!=itemClickListener)
                        itemClickListener.onItemClick(view, (Integer) view.getTag());
                }
            });

            textTitle.setText(poiItem.getName());
            textSubTitle.setText(poiItem.getAddress());
            textSubTitle.setVisibility((position == 0 && poiItem.getPoiID().equals("regeo")) ? View.GONE : View.VISIBLE);
        }
    }
}
