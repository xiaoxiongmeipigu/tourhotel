package com.zjhj.commom.result;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by brain on 2017/6/12.
 */
public class MapiRegionResult extends MapiBaseResult implements IPickerViewData {

    private List<MapiRegionResult> children;

    public List<MapiRegionResult> getChildren() {
        return children;
    }

    public void setChildren(List<MapiRegionResult> children) {
        this.children = children;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }

}
