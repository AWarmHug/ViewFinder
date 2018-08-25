package com.warm.viewfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.warm.finder.ViewFinder;
import com.warm.finder.annotations.Id;
import com.warm.finder.annotations.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_TAG";

    @Id(R.id.bt1)
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long time = System.currentTimeMillis();
        ViewFinder.inject(this);
        Log.d(TAG, "onCreate: " + (System.currentTimeMillis() - time));
        tv.setText("你好");
    }

    @OnClick(values = {R.id.bt1, R.id.bt2})
    private void onTvClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                if (view instanceof Button) {
                    Toast.makeText(this, "点击了" + ((Button) view).getText(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt2:
                startActivity(new Intent(this, AptActivity.class));
                break;
        }
    }

}
