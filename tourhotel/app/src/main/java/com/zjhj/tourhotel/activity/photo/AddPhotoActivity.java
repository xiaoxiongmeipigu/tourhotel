package com.zjhj.tourhotel.activity.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.util.JGJBitmapUtils;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.activity.ShowBigPicActivity;
import com.zjhj.tourhotel.adapter.discuss.DiscussChildItemAdapter;
import com.zjhj.tourhotel.adapter.food.FoodListAdapter;
import com.zjhj.tourhotel.adapter.photo.PhotoItemAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.base.RequestCode;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;
import com.zjhj.tourhotel.util.ControllerUtil;
import com.zjhj.tourhotel.widget.DividerListItemDecoration;
import com.zjhj.tourhotel.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPhotoActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    PhotoItemAdapter mAdapter;
    List<IndexData> mList;
    ArrayList<MapiResourceResult> imgs;

    String type = "0";
    String title="外观";
    int count=0;

    PhotoDialog photoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        ButterKnife.bind(this);
        if(null!=getIntent()){
            type = getIntent().getStringExtra("type");
            title = getIntent().getStringExtra("title");
        }
        if(!TextUtils.isEmpty(type)){
            initView();
            initListener();
            load();
        }
    }

    private void initView() {

        mList = new ArrayList<>();
        imgs = new ArrayList<>();
        back.setImageResource(R.mipmap.back_white);
        center.setText(title);

        GridLayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.VERTICAL, DPUtil.dip2px(10), getResources().getColor(R.color.shop_white)));
        mAdapter = new PhotoItemAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        if (photoDialog == null)
            photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
        photoDialog.setImagePath("photo_image.jpg");

    }

    private void initListener(){
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IndexData indexData = mList.get(position);
                if("ADD".equals(indexData.getType())){
                    photoDialog.showDialog();
                }else{
                    Intent intent = new Intent(AppContext.getInstance(), ShowBigPicActivity.class);
                    intent.putExtra("list",imgs);
                    intent.putExtra("position",position-1);
                    intent.putExtra("isDelete",true);
                    startActivityForResult(intent,RequestCode.PHOTO_DELETE);
                }
            }
        });
    }

    private void load(){
        showLoading();
        ItemApi.hotelpicindex(this, type, new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if(null==success)
                    return;
                imgs.clear();
                imgs.addAll(success);
                initData();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void initData(){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        mList.add(new IndexData(count++,"ADD",new Object()));
        for(MapiResourceResult resourceResult: imgs){
            mList.add(new IndexData(count++,"ITEM",resourceResult));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "photo_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "photo_image.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
                case RequestCode.PHOTO_DELETE:
                    if(null!=data){
                        List<MapiResourceResult> delList = (List<MapiResourceResult>) data.getSerializableExtra("list");
                        if(null!=delList){

                            Iterator<MapiResourceResult> it = imgs.iterator();
                            while(it.hasNext()){
                                MapiResourceResult x = it.next();
                                if(delList.contains(x)){
                                    it.remove();
                                }
                            }

                            Iterator<IndexData> it2 = mList.iterator();
                            while(it2.hasNext()){
                                IndexData x = it2.next();
                                if(x.getType().equals("ITEM")){
                                    MapiResourceResult resourceResult = (MapiResourceResult) x.getData();
                                    if(delList.contains(resourceResult)){
                                        it2.remove();
                                    }
                                }
                            }

                            mAdapter.notifyDataSetChanged();

                        }
                    }
                    break;
            }

        }
    }

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadShopImage(this, file,userSP.getUserBean().getMerchant_id(),type, new RequestCallback<MapiResourceResult>() {
            @Override
            public void success(MapiResourceResult success) {
                hideLoading();
                if (null != success) {
                    imgs.add(0,success);
                    mList.add(1,new IndexData(count++,"ITEM",success));
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                DebugLog.i(message);
            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "photo_image.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }
    }
}
