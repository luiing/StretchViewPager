package com.uis.test.app;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btNormal;
    Button btSingle;
    Button btMulti;
    StretchPager pager;

    static final int Distance = (int)(Resources.getSystem().getDisplayMetrics().density*62+0.5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btNormal = (Button)findViewById(R.id.bt_normal);
        btSingle = (Button)findViewById(R.id.bt_single);
        btMulti = (Button)findViewById(R.id.bt_multi);
        pager = (StretchPager)findViewById(R.id.pager);


        btNormal.setOnClickListener(this);
        btSingle.setOnClickListener(this);
        btMulti.setOnClickListener(this);
        pager.setRefreshModel(StretchPager.STRETCH_RIGHT);
        pager.setStretchModel(StretchPager.STRETCH_BOTH);
        StretchAdapter adapter = new StretchAdapter();
        pager.setAdapter(adapter);
        pager.setOnStretchListener(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        int type = 0;
        if(R.id.bt_normal == id){

        }else if(R.id.bt_single == id){
            type = 1;
        }else if(R.id.bt_multi == id){
            type = 2;
        }
        startListAct(type);
    }

    void startListAct(int type){
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    static class StretchAdapter extends PagerAdapter implements OnStretchListener{

        List<View> data = new ArrayList<>();

        View lastView;

        int status = 0;
        int lastStatus = -1;
        RotateAnimation animStart = new RotateAnimation(0f,180f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        RotateAnimation animEnd = new RotateAnimation(180f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        public StretchAdapter() {
            animStart.setDuration(300);
            animStart.setFillAfter(true);

            animEnd.setDuration(300);
            //animEnd.setFillAfter(true);
        }


        @Override
        public void onScrolled(int dirction,int distance) {
            Log.e("xx","onScrolled="+distance+",Distance="+Distance);
            if(lastView != null && StretchPager.STRETCH_RIGHT == dirction) {//left direction
                ImageView ivPull = lastView.findViewById(R.id.iv_pull);
                TextView ivTips = lastView.findViewById(R.id.tv_tips);
                if (Math.abs(distance) > Distance) {//切换释放显示
                    status = 1;
                } else {//切换普通显示
                    status = 0;
                }
                if(status != lastStatus){
                    ivTips.setText(lastView.getResources().getString(status==0 ? R.string.gd_pull_normal:R.string.gd_pull_refresh));
                    if(0 == status){
                        if(1 == lastStatus) {
                            ivPull.startAnimation(animEnd);
                        }
                    }else{
                        ivPull.startAnimation(animStart);
                    }
                }
                lastStatus = status;
            }
        }

        @Override
        public void onRefresh(int dirction,int distance) {
            Log.e("xx","onRefresh="+distance);
            if(lastView != null && StretchPager.STRETCH_RIGHT == dirction) {
                if(distance >= Distance){
                    Intent intent = new Intent(lastView.getContext(),ListActivity.class);
                    lastView.getContext().startActivity(intent);
                }
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            if(data.size() < position+1){
                View item;
                if(getCount() == position+1){
                    item = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager,container,false);
                    lastView = item;
                }else {
                    item = new View(container.getContext());
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
                    item.setLayoutParams(params);
                    Random random = new Random();
                    int read = random.nextInt(255);
                    int blue = random.nextInt(255);
                    int green = random.nextInt(255);
                    item.setBackgroundColor(Color.rgb(read, green, blue));
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"position="+position,Toast.LENGTH_SHORT).show();
                    }
                });
                data.add(item);
            }
            View view = data.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
