package com.zjhj.commom.result;

import java.io.Serializable;

/**
 * Created by brain on 2016/7/26.
 */
public class MapiImageResult implements Serializable{

    private String pic;
    private String show_pic;
    private String img;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getShow_pic() {
        return show_pic;
    }

    public void setShow_pic(String show_pic) {
        this.show_pic = show_pic;
    }
}
