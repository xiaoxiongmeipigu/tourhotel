package com.zjhj.tourhotel.activity.photo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.tourhotel.R;
import com.zjhj.tourhotel.base.BaseActivity;
import com.zjhj.tourhotel.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back_white);
        center.setText("图片管理");
    }

    @OnClick({R.id.back, R.id.out_pic_iv, R.id.inside_pic_iv, R.id.other_pic_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.out_pic_iv:
                ControllerUtil.go2AddPhoto("0","外观图片");
                break;
            case R.id.inside_pic_iv:
                ControllerUtil.go2AddPhoto("1","内部图片");
                break;
            case R.id.other_pic_iv:
                ControllerUtil.go2AddPhoto("2","其它图片");
                break;
        }
    }
}
