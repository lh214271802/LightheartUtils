package com.lh.sampledemo.injecttest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.lh.lightheartutils.anno.InjectLayout;
import com.lh.lightheartutils.anno.InjectView;
import com.lh.lightheartutils.anno.OnClick;
import com.lh.sampledemo.R;

@InjectLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @InjectView(R.id.text1)
    TextView text1;

    @InjectView(R.id.text2)
    TextView text2;

    @InjectView(R.id.text3)
    TextView text3;

    @InjectView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new TestFragment()).commit();
    }

    @OnClick({R.id.text1, R.id.text2, R.id.text3, R.id.button})
    public void onClick(int id) {
        showToast(id + "");
        switch (id) {
            case R.id.text1:
                break;
            case R.id.text2:
                break;
            case R.id.text3:
                break;
            case R.id.button:
                button.setText("跳转啦");
                startActivity(new Intent(this, TestActivity.class));
                break;
        }
    }
}
