package com.lh.lightheartutils.inject;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author liaohui
 * @date 2018/4/17
 */
public interface LHInjector {

    /**
     * 注入activity中
     *
     * @param activity
     */
    void inject(Activity activity);

    /**
     * 注入Fragment中
     *
     * @param fragment
     */
    View inject(Fragment fragment, LayoutInflater inflater, ViewGroup container);

    /**
     * 注入到ViewHolder中
     *
     * @param holder
     * @param view
     */
    void inject(Object holder, View view);

}
