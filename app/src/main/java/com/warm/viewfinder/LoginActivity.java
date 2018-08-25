package com.warm.viewfinder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.warm.finder_apt.ViewFinder;
import com.warm.finder_apt_annotations.OnClick;

/**
 * 作者：warm
 * 时间：2018-08-25 12:52
 * 描述：
 */
public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewFinder.find(this);
    }

    @OnClick(value = R.id.bt_login)
    void doLogin(){
    }

}
