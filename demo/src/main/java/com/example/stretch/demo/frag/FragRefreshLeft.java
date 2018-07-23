package com.example.stretch.demo.frag;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stretch.demo.ListActivity;
import com.example.stretch.demo.R;
import com.uis.stretch.OnStretchListener;
import com.uis.stretch.StretchPager;

/**
 * @author uis 2018/7/21
 */
public class FragRefreshLeft extends Fragment implements OnStretchListener {

    ImageView ivPull;
    TextView ivTips;

    RotateAnimation animStart = new RotateAnimation(0f,-180f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    RotateAnimation animEnd = new RotateAnimation(-180f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    static final int Distance = (int)(Resources.getSystem().getDisplayMetrics().density*80+0.5);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_pager_left,container,false);
        ivPull = v.findViewById(R.id.iv_pull);
        ivTips = v.findViewById(R.id.tv_tips);
        animStart.setDuration(300);
        animStart.setFillAfter(true);
        animEnd.setDuration(300);
        ivPull.setRotation(180f);
        return v;
    }

    int lastStatus = 0;

    @Override
    public void onScrolled(int direction, int distance) {
        if(StretchPager.STRETCH_LEFT== direction) {//left direction
            int status = (Math.abs(distance) > Distance) ? 1 : 0;
            if(status != lastStatus){
                ivTips.setText(getResources().getString(status==0 ? R.string.gd_pull_normal: R.string.gd_pull_refresh));
                if(0 == status && 1 == lastStatus){
                    ivPull.startAnimation(animEnd);
                }else if(1 == status){
                    ivPull.startAnimation(animStart);
                }
            }
            lastStatus = status;
        }
    }

    @Override
    public void onRefresh(int direction, int distance) {
        if(StretchPager.STRETCH_LEFT == direction) {
            if(distance >= Distance){
                Intent intent = new Intent(getContext(),ListActivity.class);
                //startActivity(intent);
            }
        }
    }

    @Override
    public void onRelease() {
        lastStatus = 0;
        ivPull.clearAnimation();
        ivTips.setText(getResources().getString(R.string.gd_pull_normal));
    }
}
