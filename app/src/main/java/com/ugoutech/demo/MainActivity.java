package com.ugoutech.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ugoutech.searchshowview.R;
import com.ugoutech.searchshowview.SearchShowView;


public class MainActivity extends AppCompatActivity {


    private SearchShowView mSsv;
    private int tagNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSsv = (SearchShowView) findViewById(R.id.ssv);


    }


    /**
     * 添加一个图像
     * @param view
     */
    public void addView(View view){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.tel);
        mSsv.addIconView(imageView,tagNum++ + "");
    }
}
