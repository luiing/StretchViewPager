package com.example.stretch.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.stretch.demo.frag.FragAdapter;
import com.uis.stretch.StretchPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private StretchPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (StretchPager)findViewById(R.id.pager);
        pager.setRefreshModel(StretchPager.STRETCH_RIGHT);
        pager.setStretchModel(StretchPager.STRETCH_BOTH);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),pager.getRefreshModel());
        pager.setAdapter(adapter);
        pager.setOnStretchListener(adapter);
        pager.addOnPageChangeListener(adapter);
        pager.setCurrentItem((pager.getRefreshModel()&StretchPager.STRETCH_LEFT)>0 ? 1 : 0);
    }

    @Override
    public void onClick(View view) {

    }
}
