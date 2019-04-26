package com.example.stretch.demo.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.uis.stretch.StretchPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author uis 2018/7/21
 */
public class FragAdapter extends FragmentPagerAdapter{

    List<Fragment> data = new ArrayList();

    public FragAdapter(int size,FragmentManager fm) {
        super(fm);
        for(int i=0;i<size;i++){
            Fragment f = new Frag();
            Bundle b = new Bundle();
            b.putInt("id",i);
            f.setArguments(b);
            data.add(f);
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
}
