package com.zjhj.commom.result;

import java.util.List;

/**
 * Created by brain on 2017/5/17.
 */
public class MapiCityResult extends MapiBaseResult{

    private String key;
    private List<MapiCityItemResult> list;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<MapiCityItemResult> getList() {
        return list;
    }

    public void setList(List<MapiCityItemResult> list) {
        this.list = list;
    }
}
