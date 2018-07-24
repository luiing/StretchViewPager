package com.example.stretch.demo.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.uis.stretch.OnStretchListener;
import com.uis.stretch.StretchPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author uis 2018/7/21
 */
public class FragAdapter extends FragmentPagerAdapter implements OnStretchListener,ViewPager.OnPageChangeListener{

    List<Fragment> data = new ArrayList();
    int current = 0;

    public FragAdapter(FragmentManager fm,int status) {
        super(fm);
        if((status & StretchPager.STRETCH_LEFT) > 0){
            data.add(new FragRefreshLeft());
        }
        for(int i=0;i<4;i++){
            Fragment f = new Frag();
            Bundle b = new Bundle();
            b.putInt("id",i);
            f.setArguments(b);
            data.add(f);
        }
        if((status & StretchPager.STRETCH_RIGHT) > 0){
            data.add(new FragRefreshRight());
        }
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(1 == position) {
            current = 0;
        }else if(getCount() == position+2){
            current = position + 1;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onScrolled(int direction, int distance) {
        if(getItem(current) instanceof OnStretchListener){
            ((OnStretchListener) getItem(current)).onScrolled(direction,distance);
        }
    }

    @Override
    public void onRefresh(int direction, int distance) {
        if(getItem(current) instanceof OnStretchListener){
            ((OnStretchListener) getItem(current)).onRefresh(direction,distance);
        }
    }

    @Override
    public void onRelease(int direction) {
        if(getItem(current) instanceof OnStretchListener){
            ((OnStretchListener) getItem(current)).onRelease(direction);
        }
    }
}
