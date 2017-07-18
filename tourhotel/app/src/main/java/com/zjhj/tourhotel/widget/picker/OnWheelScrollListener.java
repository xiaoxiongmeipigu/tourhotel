
package com.zjhj.tourhotel.widget.picker;



/**
 * 滚轮滑动接口
 * @author brain
 * @since 2015.7.22
 */
public interface OnWheelScrollListener {
	void onScrollingStarted(WheelView wheel);
	void onScrollingFinished(WheelView wheel);
	void onScrollingStarted(ArrayWheelView wheel);
	void onScrollingFinished(ArrayWheelView wheel);
}
