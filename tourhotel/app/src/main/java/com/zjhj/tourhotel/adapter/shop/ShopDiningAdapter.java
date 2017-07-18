package com.zjhj.tourhotel.adapter.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.MealDeelListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/7.
 */
public class ShopDiningAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int ADD = 0;
    private final static int MINUS = 1;
    private final static int DIVIDER = 2;

    LayoutInflater inflater;
    Context mContext;

    private List<IndexData> mList;

    private MealDeelListener mealDeelListener;

    public void setOnItemClickListener(MealDeelListener mealDeelListener) {
        this.mealDeelListener = mealDeelListener;
    }

    public ShopDiningAdapter(Context context, List<IndexData> list) {
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
            case ADD:
                return new AddViewHolder(inflater.inflate(R.layout.item_meal_add, parent, false));
            case MINUS:
                return new MinusViewHolder(inflater.inflate(R.layout.item_meal_minus, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_color_gray_hight_one, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_color_gray_hight_one, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddViewHolder) {
            setAdd((AddViewHolder) holder, position);
        } else if (holder instanceof MinusViewHolder) {
            setMinus((MinusViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "ADD":
                return ADD;
            case "MINUS":
                return MINUS;
            case "DIVIDER":
                return DIVIDER;
            default:
                return DIVIDER;
        }
    }


    class AddViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.add)
        ImageView add;

        public AddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MinusViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.minus)
        ImageView minus;

        public MinusViewHolder(View itemView) {
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

    private void setAdd(AddViewHolder holder, int position) {
        holder.nameTv.setHint("请添加用餐时间");
        holder.add.setTag(position);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mealDeelListener)
                    mealDeelListener.add(view, (Integer) view.getTag());
            }
        });

    }

    private void setMinus(MinusViewHolder holder, int position) {
        MapiResourceResult foodResult = (MapiResourceResult) mList.get(position).getData();
        holder.nameTv.setText(TextUtils.isEmpty(foodResult.getName())?"":foodResult.getName());
        holder.minus.setTag(position);
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mealDeelListener)
                    mealDeelListener.minus(view, (Integer) view.getTag());
            }
        });

    }


}
