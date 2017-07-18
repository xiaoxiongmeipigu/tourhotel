package com.zjhj.commom.result;

/**
 * Created by brain on 2017/6/7.
 */
public class MapiMsgResult extends MapiBaseResult{

    private String title;
    private String content;
    private String addtime;

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
