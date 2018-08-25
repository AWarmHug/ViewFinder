package com.warm.viewfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.warm.finder_apt.ViewFinder;
import com.warm.finder_apt_annotations.Id;
import com.warm.finder_apt_annotations.OnClick;
import com.warm.finder_apt_annotations.OnLongClick;


public class AptActivity extends AppCompatActivity {
    @Id(R.id.bt1)
    Button bt1;
    @Id(R.id.bt2)
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt);
        ViewFinder.find(this);
        bt1.setText("我是一");
        bt2.setText("我是二");


    }

    @OnClick(value = {R.id.bt1})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                Toast.makeText(this, "我是一", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt2:
                Toast.makeText(this, "我是二", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick(value = {R.id.bt2})
    void OnClick2() {
        Toast.makeText(this, "我是二", Toast.LENGTH_SHORT).show();
    }

    @OnClick(value = {R.id.bt3})
    void OnClick3() {
        startActivity(new Intent(this, Apt2Activity.class));
    }

    @OnLongClick(R.id.bt4)
    void OnLongClick4() {
        Toast.makeText(this, "长按", Toast.LENGTH_SHORT).show();
    }


}
