package com.example.stretch.demo;

import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stretch.demo.frag.FragAdapter;
import com.uis.stretch.OnStretchListener;
import com.uis.stretch.StretchPager;

public class MainActivity extends AppCompatActivity implements OnStretchListener{

    private StretchPager pager;
    FragAdapter adapter;
    View leftView,rightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        animLeftStart.setFillAfter(true);
        animLeftStart.setDuration(300);
        animLeftEnd.setDuration(300);

        animRightStart.setFillAfter(true);
        animRightStart.setDuration(300);
        animRightEnd.setDuration(300);
        leftView = LayoutInflater.from(this).inflate(R.layout.item_pager_left,null);
        rightView = LayoutInflater.from(this).inflate(R.layout.item_pager_right,null);

        pager.setRefreshView(leftView,rightView);
        adapter = new FragAdapter(4,getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnStretchListener(this);

    }

    static final int Distance = (int)(Resources.getSystem().getDisplayMetrics().density*80+0.5);
    RotateAnimation animLeftStart = new RotateAnimation(0f,-180f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    RotateAnimation animLeftEnd = new RotateAnimation(-180f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    RotateAnimation animRightStart = new RotateAnimation(0f,180f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    RotateAnimation animRightEnd = new RotateAnimation(180f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

    int lastStatus = 0;

    @Override
    public void onScrolled(int direction, int distance) {
        if(StretchPager.STRETCH_LEFT== direction) {//left direction
            int status = (Math.abs(distance) > Distance) ? 1 : 0;
            if(status != lastStatus){
                TextView tvTips = leftView.findViewById(R.id.tv_tips);
                ImageView ivPull = leftView.findViewById(R.id.iv_pull);
                tvTips.setText(getResources().getString(status==0 ? R.string.gd_pull_normal: R.string.gd_pull_refresh));
                if(0 == status && 1 == lastStatus){
                    ivPull.startAnimation(animLeftEnd);
                }else if(1 == status){
                    ivPull.startAnimation(animLeftStart);
                }
            }
            lastStatus = status;
        }else if(StretchPager.STRETCH_RIGHT== direction){
            int status = (Math.abs(distance) > Distance) ? 1 : 0;
            if(status != lastStatus){
                TextView tvTips = rightView.findViewById(R.id.tv_tips);
                ImageView ivPull = rightView.findViewById(R.id.iv_pull);
                tvTips.setText(getResources().getString(status==0 ? R.string.gd_pull_normal: R.string.gd_pull_refresh));
                if(0 == status && 1 == lastStatus){
                    ivPull.startAnimation(animRightEnd);
                }else if(1 == status){
                    ivPull.startAnimation(animRightStart);
                }
            }
            lastStatus = status;
        }
    }

    @Override
    public void onRefresh(int direction, int distance) {
        if(StretchPager.STRETCH_LEFT == direction) {
            if(distance >= Distance){

            }
        }else if(StretchPager.STRETCH_RIGHT == direction) {
            if(distance >= Distance){
                Toast.makeText(this,"refresh success",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getContext(),ListActivity.class);
                //startActivity(intent);
            }
        }
    }

    @Override
    public void onRelease(int direction) {
        lastStatus = 0;
        if(StretchPager.STRETCH_LEFT== direction) {
            TextView tvTips = leftView.findViewById(R.id.tv_tips);
            ImageView ivPull = leftView.findViewById(R.id.iv_pull);
            ivPull.clearAnimation();
            tvTips.setText(getResources().getString(R.string.gd_pull_normal));
        }else if(StretchPager.STRETCH_RIGHT == direction) {
            TextView tvTips = rightView.findViewById(R.id.tv_tips);
            ImageView ivPull = rightView.findViewById(R.id.iv_pull);
            ivPull.clearAnimation();
            tvTips.setText(getResources().getString(R.string.gd_pull_normal));
            adapter.notifyDataSetChanged();
        }
    }
}
