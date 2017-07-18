package com.zjhj.commom.result;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by brain on 2017/6/30.
 */
public class MapiFoodResult extends MapiBaseResult implements IPickerViewData {

    private String cover_pic;
    private String num;
    private String original_single_price;
    private String original_price;

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOriginal_single_price() {
        return original_single_price;
    }

    public void setOriginal_single_price(String original_single_price) {
        this.original_single_price = original_single_price;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    //重写hashcode和equals使得根据id来判断是否是同一个bean

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapiFoodResult)) {
            return false;
        }
        MapiFoodResult other = (MapiFoodResult) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
