package com.example.androidutils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClipboardUtils;

public class MainActivity extends AppCompatActivity {

    /**
     * 剪贴板相关 -> ClipboardUtils.java -> Demo
     * copyText             : 复制文本到剪贴板
     * getText              : 获取剪贴板的文本
     * copyUri              : 复制 uri 到剪贴板
     * getUri               : 获取剪贴板的 uri
     * copyIntent           : 复制意图到剪贴板
     * getIntent            : 获取剪贴板的意图
     * addChangedListener   : 增加剪贴板监听器
     * removeChangedListener: 移除剪贴板监听器
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv_clip);
        EditText editText = findViewById(R.id.et_clip);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(tv.getText());
                editText.setText(ClipboardUtils.getText());
            }
        });
    }
}