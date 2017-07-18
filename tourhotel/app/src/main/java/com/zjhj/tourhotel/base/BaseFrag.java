package com.zjhj.tourhotel.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.sharedpreferences.UserSP;


/**
 * Created by brain on 2016/6/26.
 */
public class BaseFrag extends Fragment{
    protected UserSP userSP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userSP = new UserSP(AppContext.getInstance());
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    public void showLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }
    }

    public void hideLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoading();
        }
    }

    public void load() {

    }

    public void refreshData() {

    }

    public void goTop(){

    }

    public void isGrid(boolean b){

    }

}
