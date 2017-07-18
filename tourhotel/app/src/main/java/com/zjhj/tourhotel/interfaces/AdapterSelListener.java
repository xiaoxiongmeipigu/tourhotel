package com.zjhj.tourhotel.interfaces;

/**
 * Created by brain on 2016/9/9.
 */
public interface AdapterSelListener {
    void isAll();
    void notifyParentStatus(int position);
    void notifyChildStatus(int position);
    void notifyChildNum(int position, int num);
}
