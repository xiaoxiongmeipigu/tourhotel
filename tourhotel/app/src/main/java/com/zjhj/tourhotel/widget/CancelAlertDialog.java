package com.zjhj.tourhotel.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.application.AppContext;
import com.zjhj.tourhotel.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/6.
 */
public class CancelAlertDialog extends Dialog {

    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.left_button)
    TextView mLeftButton;
    @Bind(R.id.right_button)
    TextView mRightButton;
    @Bind(R.id.dell_ll)
    LinearLayout dellLl;
    @Bind(R.id.info_et)
    EditText infoEt;

    public CancelAlertDialog(Context context) {
        super(context, R.style.dialog_theme);
        initView();
    }

    public CancelAlertDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_cancel_alert);
        ButterKnife.bind(this);
    }

    public CancelAlertDialog setLeftBtnText(String str) {
        if (str.length() > 0) {
            mLeftButton.setText(str);
            mLeftButton.setVisibility(View.VISIBLE);
        } else {
            mLeftButton.setVisibility(View.GONE);
        }
        return this;
    }

    public CancelAlertDialog setHint(String str){
        if (str.length() > 0) {
            infoEt.setHint(str);
        }
        return this;
    }

    public CancelAlertDialog setRightBtnText(String str) {
        if (str.length() > 0) {
            mRightButton.setText(str);
            mRightButton.setVisibility(View.VISIBLE);
        } else {
            mRightButton.setVisibility(View.GONE);
        }
        return this;
    }

    public CancelAlertDialog setTitle(String str) {
        mTitle.setText(str);
        return this;
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getInfo() {
        return infoEt.getText().toString();
    }

    public void setInfo(String info){
        infoEt.setText(TextUtils.isEmpty(info)?"":info);
    }

    public CancelAlertDialog setLeftClickListener(View.OnClickListener clickListener) {
        mLeftButton.setOnClickListener(clickListener);
        return this;
    }

    public CancelAlertDialog setRightClickListener(View.OnClickListener clickListener) {
        mRightButton.setOnClickListener(clickListener);
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) AppContext.getInstance().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
