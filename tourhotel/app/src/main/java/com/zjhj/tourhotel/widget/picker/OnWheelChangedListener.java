package com.zjhj.tourhotel.widget.picker;



/**
 * 滚轮改变接口
 * @author brain
 * @since 2015.7.22
 */
public interface OnWheelChangedListener {
	void onChanged(WheelView wheel, int oldValue, int newValue);
	void onChanged(ArrayWheelView wheel, int oldValue, int newValue);
}
