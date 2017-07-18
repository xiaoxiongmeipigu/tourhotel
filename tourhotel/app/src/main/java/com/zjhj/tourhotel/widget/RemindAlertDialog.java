package com.zjhj.tourhotel.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zjhj.tourhotel.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/7.
 */
public class RemindAlertDialog extends Dialog {

    @Bind(R.id.close_iv)
    ImageView closeIv;

    public RemindAlertDialog(Context context) {
        super(context, R.style.dialog_theme);
        initView();
    }

    public RemindAlertDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_remind_alert);
        ButterKnife.bind(this);
    }

    public RemindAlertDialog setCloseClickListener(View.OnClickListener clickListener) {
        closeIv.setOnClickListener(clickListener);
        return this;
    }

}
