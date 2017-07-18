package com.zjhj.commom.result;

import java.util.ArrayList;

/**
 * Created by brain on 2017/6/30.
 */
public class MapiDiscussResult extends MapiBaseResult{

    private String addtime;
    private String reply;
    private String content;
    private String gname;
    private String order_id;
    private ArrayList<MapiResourceResult> pic;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public ArrayList<MapiResourceResult> getPic() {
        return pic;
    }

    public void setPic(ArrayList<MapiResourceResult> pic) {
        this.pic = pic;
    }
}
