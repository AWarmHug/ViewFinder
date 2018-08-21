package com.warm.viewfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.warm.finder_apt.ViewFinder;
import com.warm.finder_apt_annotations.Id;


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
}
