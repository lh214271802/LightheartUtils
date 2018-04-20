package com.lh.sampledemo.injecttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lh.lightheartutils.inject.InjectUtils;

/**
 * @author liaohui
 * @date 2018/4/17
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.get().inject(this);
    }

    public void setOnclicks(View... views) {
        if (views == null) {
            return;
        }
        for (int i = 0; i < views.length; i++) {
            if (views[i] == null) {
                return;
            }
            views[i].setOnClickListener(this);
        }
    }

    public void showToast(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
