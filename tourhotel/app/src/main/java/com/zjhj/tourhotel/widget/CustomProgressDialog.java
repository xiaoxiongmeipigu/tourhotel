package com.zjhj.tourhotel.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.tourhotel.R;


/**
 * 自定义旋转进度条
 * 
 * @author brain
 * @since 2015.8.4
 */
public class CustomProgressDialog extends ProgressDialog {
	private Context context;
	private ImageView loadingIv;
	private TextView tvMsg;
	private String message;

	public CustomProgressDialog(Context context, String message) {
		super(context);
		this.context = context;
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * 初始化并设置动画
	 */
	private void init() {
		setContentView(R.layout.dialog_customprogress);
		loadingIv = (ImageView) findViewById(R.id.loading_iv);
		tvMsg = (TextView) findViewById(R.id.tv_loadingmsg);
		if(!TextUtils.isEmpty(message))
			tvMsg.setVisibility(View.VISIBLE);
		else
			tvMsg.setVisibility(View.GONE);
		tvMsg.setText(message);
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.anim_common_progress);
		loadingIv.startAnimation(animation);
	}
}
