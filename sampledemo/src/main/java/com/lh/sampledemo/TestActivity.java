package com.lh.sampledemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lh.lightheartutils.anno.InjectLayout;
import com.lh.lightheartutils.anno.InjectView;
import com.lh.lightheartutils.anno.OnClick;
import com.lh.lightheartutils.inject.InjectUtils;

/**
 * @author liaohui
 * @date 2018/4/17
 */
@InjectLayout(R.layout.activity_test)
public class TestActivity extends BaseActivity {
    @InjectView(R.id.image_view)
    ImageView imageView;

    @InjectView(R.id.container)
    FrameLayout container;
    private ChildHolder childHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View child = LayoutInflater.from(this).inflate(R.layout.item_test, null);
        childHolder = new ChildHolder();
        InjectUtils.get().inject(childHolder, child);
        container.addView(child);
    }

    class ChildHolder {

        @InjectView(R.id.item_btn)
        Button item_btn;

        @InjectView(R.id.item_btn2)
        Button item_btn2;

        @OnClick(R.id.item_btn)
        public void seto(int id) {
            item_btn.setText("春天在哪里" + id);
        }

        @OnClick(R.id.item_btn2)
        public void setodd(int id) {
            item_btn2.setText("春天在ffaa哪里" + id);
        }

    }
}
