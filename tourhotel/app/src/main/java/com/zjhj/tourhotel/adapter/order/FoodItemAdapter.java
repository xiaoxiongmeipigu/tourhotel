package com.zjhj.tourhotel.adapter.order;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/30.
 */
public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiFoodResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    private int parentPos = -1;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FoodItemAdapter(Context context, List<MapiFoodResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_food, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiFoodResult itemResult = mList.get(position);

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(itemResult.getCover_pic()) ? "" : itemResult.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(90), DPUtil.dip2px(70)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.nameTv.setText(TextUtils.isEmpty(itemResult.getName())?"":itemResult.getName());
        holder.numTv.setText("x"+(TextUtils.isEmpty(itemResult.getNum())?"0":itemResult.getNum()));
        holder.oldPriceTv.setText("¥ "+(TextUtils.isEmpty(itemResult.getOriginal_single_price())?"0":itemResult.getOriginal_single_price()));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view,getParentPos());
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.num_tv)
        TextView numTv;
        @Bind(R.id.old_price_tv)
        TextView oldPriceTv;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
