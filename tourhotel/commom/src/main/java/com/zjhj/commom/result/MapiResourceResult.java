package com.zjhj.commom.result;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;

/**
 * Created by brain on 2016/7/22.
 */
public class MapiResourceResult extends MapiBaseResult implements IPickerViewData {
    private String ZD_ID;
    private String remark;
    private int version;
    private String poster;
    private String title;

    private String e_id;
    private String info;
    private String postage;
    private String icon;

    private boolean isSel;
    private String img;
    private String img_url;
    private String url;
    private String pic_url;
    private String use_begin_time;
    private String use_end_time;

    private String date;

    private String pname;
    private String cname;
    private String aname;

    private String nonoperating_time;

    public String getNonoperating_time() {
        return nonoperating_time;
    }

    public void setNonoperating_time(String nonoperating_time) {
        this.nonoperating_time = nonoperating_time;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUse_begin_time() {
        return use_begin_time;
    }

    public void setUse_begin_time(String use_begin_time) {
        this.use_begin_time = use_begin_time;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public MapiResourceResult(){

    }

    public MapiResourceResult(String id,String name){
        this.id = id;
        this.name = name;
    }

    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public String getZD_ID() {
        return ZD_ID;
    }

    public void setZD_ID(String ZD_ID) {
        this.ZD_ID = ZD_ID;
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
        if (!(obj instanceof MapiResourceResult)) {
            return false;
        }
        MapiResourceResult other = (MapiResourceResult) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

}
