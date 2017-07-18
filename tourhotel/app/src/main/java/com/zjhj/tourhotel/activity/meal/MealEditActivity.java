package com.zjhj.tourhotel.activity.meal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiFoodResult;
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
import com.zjhj.tourhotel.adapter.meal.MealAddAdapter;
import com.zjhj.tourhotel.adapter.photo.PhotoItemAdapter;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.base.RequestCode;
import com.zjhj.tourhotel.interfaces.MealDeelListener;
import com.zjhj.tourhotel.interfaces.RecyOnItemClickListener;
import com.zjhj.tourhotel.widget.DividerListItemDecoration;
import com.zjhj.tourhotel.widget.MainAlertDialog;
import com.zjhj.tourhotel.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealEditActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.original_price_et)
    EditText originalPriceEt;
    @Bind(R.id.cover_ll)
    LinearLayout coverLl;
    @Bind(R.id.cover_pic)
    SimpleDraweeView coverPic;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.recyclerView_meal)
    RecyclerView recyclerViewMeal;
    @Bind(R.id.content_et)
    EditText contentEt;

    MealAddAdapter mAdapter;
    ArrayList<MapiFoodResult> foodList;
    List<IndexData> mList;
    OptionsPickerView positionOptions;
    List<MapiFoodResult> selList;

    String id = "";

    PhotoItemAdapter photoItemAdapter;
    List<IndexData> photos;
    ArrayList<MapiResourceResult> imgs;
    ArrayList<MapiImageResult> coverPics;

    PhotoDialog photoDialog;

    int type = 0;

    MainAlertDialog passDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_edit);
        ButterKnife.bind(this);
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
        }

        if(!TextUtils.isEmpty(id)){
            initView();
            initListener();
            load();
            loadFood();
        }

    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("套餐详情");
//        tvRight.setText("保存");

        coverPics = new ArrayList<>();
        photos = new ArrayList<>();
        imgs = new ArrayList<>();

        coverPics = new ArrayList<>();
        selList = new ArrayList<>();
        foodList = new ArrayList<>();
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MealAddAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        //选项选择器
        positionOptions = new OptionsPickerView(this);

        photos.add(new IndexData(0, "ADD", new Object()));
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerViewMeal.setLayoutManager(manager);
        recyclerViewMeal.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.VERTICAL, DPUtil.dip2px(10), getResources().getColor(R.color.shop_white)));
        photoItemAdapter = new PhotoItemAdapter(this, photos);
        recyclerViewMeal.setAdapter(photoItemAdapter);

        if (photoDialog == null)
            photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
        photoDialog.setImagePath("meal_image.jpg");

        passDialog = new MainAlertDialog(this);
        passDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认下架？");


    }

    private void load(){
        showLoading();
        ItemApi.footpackageinfo(this, id, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                if(null==success)
                    return;
                String namestr = success.getJSONObject("data").getString("name");
                nameEt.setText(TextUtils.isEmpty(namestr)?"":namestr);

                String original_price_str = success.getJSONObject("data").getString("original_price");
                originalPriceEt.setText(TextUtils.isEmpty(original_price_str)?"":original_price_str);
                String cover_pic = success.getJSONObject("data").getString("cover_pic");
                if(!TextUtils.isEmpty(cover_pic)){
                    MapiImageResult imageResult = new MapiImageResult();
                    imageResult.setUrl(cover_pic);
                    coverPics.clear();
                    coverPics.add(imageResult);
                    coverPic.setVisibility(View.VISIBLE);
                    coverLl.setVisibility(View.GONE);

                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(coverPics.get(0).getUrl());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(coverPic.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    coverPic.setController(controller);
                }

                List<MapiFoodResult> foods = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("foot").toJSONString(),MapiFoodResult.class);
                if(null!=foods&&!foods.isEmpty()){
                    selList.addAll(foods);
                    for(MapiFoodResult foodResult : selList){
                        mList.add(0, new IndexData(mList.size(), "MINUS", foodResult));
                        mList.add(1, new IndexData(mList.size(), "DIVIDER", new Object()));
                    }
                }
                mList.add(new IndexData(mList.size(), "ADD", new Object()));
                mAdapter.notifyDataSetChanged();

                List<MapiResourceResult> pics = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("pic").toJSONString(),MapiResourceResult.class);
                if(null!=pics&&!pics.isEmpty()){
                    imgs.addAll(pics);
                    for(MapiResourceResult resourceResult : imgs){
                        photos.add( new IndexData(photos.size(), "ITEM", resourceResult));
                    }
                    photoItemAdapter.notifyDataSetChanged();
                }

                String contentStr = success.getJSONObject("data").getString("content");
                contentEt.setText(TextUtils.isEmpty(contentStr)?"":contentStr);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void loadFood() {
        showLoading();
        ItemApi.foothotelfoot(this, new RequestCallback<List<MapiFoodResult>>() {
            @Override
            public void success(List<MapiFoodResult> success) {
                hideLoading();
                if (null == success)
                    return;

                for (MapiFoodResult foodResult : success) {
                    foodList.add(foodResult);
                }
                //三级联动效果
                positionOptions.setPicker(foodList);
                //设置选择的三级单位
                positionOptions.setCyclic(false);
                //设置默认选中的三级项目
                //监听确定选择按钮
                positionOptions.setSelectOptions(0);

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void initListener(){

        passDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDialog.dismiss();
            }
        });

        passDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                del();
                passDialog.dismiss();
            }
        });

        originalPriceEt.addTextChangedListener(new TextWatcher() {//设置小数点后两位
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")){
                    if (s.length()-1-s.toString().indexOf(".")>2){
                        s=s.toString().subSequence(0,s.toString().indexOf(".")+3);
                        originalPriceEt.setText(s);
                        originalPriceEt.setSelection(s.length());
                    }
                }

                if(s.toString().trim().substring(0).equals(".")){
                    s ="0"+s;
                    originalPriceEt.setText(s);
                    originalPriceEt.setSelection(2);
                }

                if(s.toString().startsWith("0")
                        && s.toString().trim().length()>1){
                    if(!s.toString().substring(1,2).equals(".")){
                        originalPriceEt.setText(s.subSequence(0,1));
                        originalPriceEt.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        photoItemAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IndexData indexData = photos.get(position);
                if ("ADD".equals(indexData.getType())) {
                    type = 0;
                    photoDialog.showDialog();
                } else {
                    Intent intent = new Intent(AppContext.getInstance(), ShowBigPicActivity.class);
                    intent.putExtra("list", imgs);
                    intent.putExtra("position", position-1);
                    intent.putExtra("isDelete", true);
                    intent.putExtra("deleteReal",false);
                    startActivityForResult(intent, RequestCode.PHOTO_DELETE);
                }
            }
        });

        mAdapter.setOnItemClickListener(new MealDeelListener() {
            @Override
            public void add(View view, int position) {
                positionOptions.show();
            }

            @Override
            public void minus(View view, int position) {
                MapiFoodResult foodResult = (MapiFoodResult) mList.get(position).getData();
                mList.remove(position);
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                if (selList.contains(foodResult))
                    selList.remove(foodResult);
            }
        });

        positionOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                MapiFoodResult foodResult = foodList.get(options1);
                selList.add(foodResult);
                mList.add(0, new IndexData(mList.size(), "MINUS", foodResult));
                mList.add(1, new IndexData(mList.size(), "DIVIDER", new Object()));
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private String getSelFoods() {
        String dishes = "";
        if (null != selList) {
            for (MapiFoodResult foodResult : selList) {
                if (TextUtils.isEmpty(dishes))
                    dishes += foodResult.getId();
                else
                    dishes += "," + foodResult.getId();
            }
        }
        return dishes;
    }

    private String getMealPic() {
        String mealpic = "";
        if (null != imgs) {
            for (MapiResourceResult resourceResult : imgs) {
                if (TextUtils.isEmpty(mealpic))
                    mealpic += resourceResult.getPic_url();
                else
                    mealpic += "," + resourceResult.getPic_url();
            }
        }
        return mealpic;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "meal_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "meal_image.jpg", FileUtil.TYPE_IMAGE);
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
                    if (null != data) {
                        List<MapiResourceResult> delList = (List<MapiResourceResult>) data.getSerializableExtra("list");
                        if (null != delList) {

                            Iterator<MapiResourceResult> it = imgs.iterator();
                            while (it.hasNext()) {
                                MapiResourceResult x = it.next();
                                /*if (delList.contains(x)) {
                                    it.remove();
                                }*/
                                for(MapiResourceResult resourceResult : delList){
                                    if(resourceResult.getPic_url().equals(x.getPic_url())){
                                        DebugLog.i("imgs:"+resourceResult.getPic_url());
                                        it.remove();
                                        break;
                                    }
                                }
                            }

                            Iterator<IndexData> it2 = photos.iterator();
                            while (it2.hasNext()) {
                                IndexData x = it2.next();
                                if (x.getType().equals("ITEM")) {
                                    MapiResourceResult resourceResult = (MapiResourceResult) x.getData();
                                    /*if (delList.contains(resourceResult)) {
                                        it2.remove();
                                    }*/
                                    for(MapiResourceResult resourceResult2 : delList){
                                        if(resourceResult2.getPic_url().equals(resourceResult.getPic_url())){
                                            DebugLog.i("photos:"+resourceResult2.getPic_url());
                                            it2.remove();
                                            break;
                                        }
                                    }
                                }
                            }

                            photoItemAdapter.notifyDataSetChanged();

                        }
                    }
                    break;
            }

        }
    }

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {

                    if (type == 0) {
                        MapiResourceResult resourceResult = new MapiResourceResult();
                        resourceResult.setPic_url(success.getUrl());
                        imgs.add(0, resourceResult);
                        photos.add(1, new IndexData(photos.size(), "ITEM", resourceResult));
                        photoItemAdapter.notifyDataSetChanged();
                    } else if (type == 1) {
                        coverPics.clear();
                        coverPics.add(success);
                        coverPic.setVisibility(View.VISIBLE);
                        coverLl.setVisibility(View.GONE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                        //创建将要下载的图片的URI
                        Uri imageUri = Uri.parse(coverPics.get(0).getUrl());
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .setOldController(coverPic.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        coverPic.setController(controller);
                    }

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
                .fromFile(FileUtil.createFile(this, "meal_image.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @OnClick({R.id.back, R.id.tv_right, R.id.cover_ll, R.id.cover_pic,R.id.del,R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.cover_ll:
                type = 1;
                photoDialog.showDialog();
                break;
            case R.id.cover_pic:
                type = 1;
                photoDialog.showDialog();
                break;
            case R.id.del:
                passDialog.show();
                break;
            case R.id.save:
                if(TextUtils.isEmpty(nameEt.getText())){
                    MainToast.showShortToast("请输入套餐名称");
                    return;
                }
                if(TextUtils.isEmpty(originalPriceEt.getText())){
                    MainToast.showShortToast("请输入套餐单价");
                    return;
                }
                if(null==coverPics||coverPics.isEmpty()){
                    MainToast.showShortToast("请上传套餐封面");
                    return;
                }
                if((null==selList||selList.isEmpty())&&(null==imgs||imgs.isEmpty())){
                    MainToast.showShortToast("请添加菜品或者上传菜单图片");
                    return;
                }
                commit();
                break;
        }
    }

    private void commit(){
        showLoading();
        ItemApi.footpackageedit(this, id, nameEt.getText().toString(), originalPriceEt.getText().toString(), coverPics.get(0).getUrl(),
                getSelFoods(), getMealPic(), contentEt.getText().toString(), new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("新增成功");
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });

    }

    private void del(){
        showLoading();
        ItemApi.footpackagedel(this, id, new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                MainToast.showShortToast("下架成功");
                finish();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != positionOptions && positionOptions.isShowing()) {
            positionOptions.dismiss();
            positionOptions = null;
        }
        if (null != photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }
    }

}
