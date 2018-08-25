package com.warm.viewfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.warm.finder_apt.ViewFinder;
import com.warm.finder_apt_annotations.Id;
import com.warm.finder_apt_annotations.OnClick;

/**
 * 作者：warm
 * 时间：2018-08-25 10:10
 * 描述：
 */
public class Apt2Activity extends AppCompatActivity {

    @Id(R.id.bt1)
    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt2);
        ViewFinder.find(this);
        bt1.setText("哈哈哈");
    }

    @OnClick(value = {R.id.bt2})
    void click(){
        Toast.makeText(this, "点击了一下", Toast.LENGTH_SHORT).show();
    }



}
