package com.zjhj.tourhotel.widget.picker;


/**
 * 日期，时间适配器
 * @author zouwei
 * @since 2015.7.22
 */
public class NumericWheelAdapter implements WheelAdapter {

	public static final int DEFAULT_MAX_VALUE = 9;

	private int minValue;
	private int maxValue;

	private String format;

	private String values = null;
	public NumericWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	/**
	 * @param minValue 最小
	 * @param maxValue 最大
	 * @param format   格式
	 */
	public NumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			values = (format != null ? String.format(format, value) : Integer
					.toString(value));
			setValue(values);
			return values;
		}
		return null;
	}

	public String getValues() {
		return values;
	}

	public void setValue(String value) {
		this.values = value;
	}

	public int getItemsCount() {
		return maxValue - minValue + 1;
	}

	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		if(minValue==maxValue){
			return 2;
		}
		return maxValue - minValue + 1;
	}

}
