/*
 * Copyright by uis 2018.
 */

package com.uis.stretch;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;

/**
 * 弹性viewpager,支持弹性恢复和侧滑刷新
 * @author uis 2018/7/18
 */
public class StretchPager extends ViewPager implements ValueAnimator.AnimatorUpdateListener{
    static final String TAG = "StretchPager";
    public static final int STRETCH_NONE = 0x00;
    public static final int STRETCH_LEFT = 0x01;//左边弹性
    public static final int STRETCH_RIGHT = 0x10;//右边弹性
    public static final int STRETCH_BOTH = 0x11;//两边
    public static boolean DEBUG = false;

    private int refreshModel = STRETCH_NONE;//刷新模式,优先级大于弹性模式
    private int stretchModel = STRETCH_BOTH;//弹性模式
    private int directionModel = STRETCH_NONE;

    private int lastPosition = 0;//记录上一次滑动x坐标
    private int distanceX = 0;
    private boolean stretchStatus = false;//侧滑状态
    private boolean isAnimRunning = false;//正在执行回弹动画
    private OnStretchListener listener;
    private final ValueAnimator anim = ValueAnimator.ofInt(0, 1);
    //private final int mTouchSlop;
    private int activePointerId;
    private int firstCurrentId = 0;


    public StretchPager(@NonNull Context context) {
        this(context,null);
    }

    public StretchPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        anim.setInterpolator(new AccelerateInterpolator());
        //mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 设置刷新模式，默认关闭
     * @param model one of {@link #STRETCH_BOTH},{@link #STRETCH_LEFT},{@link #STRETCH_RIGHT},{@link #STRETCH_NONE}
     */
    public void setRefreshModel(int model){
        this.refreshModel = model;
        firstCurrentId = (model & STRETCH_LEFT) > 0 ? 1 : 0;
    }

    public int getRefreshModel() {
        return refreshModel;
    }

    /**
     * 设置弹性模式，默认开启
     * @param model one of {@link #STRETCH_BOTH},{@link #STRETCH_LEFT},{@link #STRETCH_RIGHT},{@link #STRETCH_NONE}
     */
    public void setStretchModel(int model){
        this.stretchModel = model;
    }

    public int getStretchModel() {
        return stretchModel;
    }

    public void setOnStretchListener(OnStretchListener l){
        listener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int actionId = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (actionId){
            case MotionEvent.ACTION_DOWN://0
                break;
            case MotionEvent.ACTION_MOVE://2
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if(null == getAdapter() || -1 == pointerIndex){
                    return true;
                }
                if(stretchStatus){
                    scrollByMove(distanceX);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP://1
                if(stretchStatus || (stretchStatus = getStretchEnable(distanceX)));
            case MotionEvent.ACTION_CANCEL://3
                if(stretchStatus){
                    scrollEndMove();
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN://5
                if(stretchStatus){
                    final int index = ev.getActionIndex();
                    lastPosition = (int)ev.getX(index);//多点触摸位置适配
                    activePointerId = ev.getPointerId(index);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionId = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (actionId){
            case MotionEvent.ACTION_DOWN://0
                lastPosition = (int)ev.getX();
                activePointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE://2
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if(null == getAdapter() || -1 == pointerIndex){
                    break;
                }
                final int currentPosition = (int)ev.getX(pointerIndex);
                distanceX = currentPosition - lastPosition;
                lastPosition = currentPosition;
                if(!stretchStatus){
                    stretchStatus = getStretchEnable(distanceX);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean getStretchEnable(int distanceX){
        boolean enable = true;
        boolean refreshLeft = (STRETCH_LEFT & refreshModel)>0;
        boolean refreshRight = (STRETCH_RIGHT & refreshModel)>0;
        boolean stretchLeft = (STRETCH_LEFT & stretchModel) > 0;
        boolean stretchRight = (STRETCH_RIGHT & stretchModel) > 0;
        if( (stretchLeft || refreshLeft) && (refreshLeft ? 1 : 0) == getCurrentItem() && distanceX>0 ){
            //左滑动弹性滑动或刷新开启，左滑动distanceX大于0
            directionModel = STRETCH_LEFT;
        }else if((stretchRight || refreshRight) && getAdapter().getCount() == getCurrentItem()+1+(refreshRight ? 1 : 0) && distanceX<0){
            directionModel = STRETCH_RIGHT;
        }else{
            directionModel = STRETCH_NONE;
            enable = false;
        }
        return enable;
    }

    private void scrollByMove(int x){
        scrollBy(-x*3/4, 0);
        if(null != listener && (refreshModel & STRETCH_BOTH)>0){
            listener.onScrolled(directionModel,getScrollDistance());
        }
    }

    private void scrollEndMove() {
        final int scrollDistance = getScrollDistance();
        if(null != listener && !isAnimRunning && (refreshModel & STRETCH_BOTH)>0){
            if((STRETCH_LEFT == directionModel && scrollDistance >0) || (STRETCH_RIGHT == directionModel && scrollDistance <0)) {
                listener.onRefresh(directionModel, Math.abs(scrollDistance));
            }
        }
        isAnimRunning = true;
        if(anim.isRunning()){
            anim.removeAllUpdateListeners();
            anim.cancel();
            anim.setDuration(150);
        }else{
            anim.setDuration(300);
        }
        anim.addUpdateListener(this);
        anim.start();
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float percent = animation.getAnimatedFraction();
        int distance = getScrollDistance();
        int dx = (int) (percent * distance);
        scrollBy(dx, 0);
        if(1.0f <= percent){
            anim.removeAllUpdateListeners();
            if(null != listener)listener.onRelease();
            isAnimRunning = false;
            stretchStatus = false;
        }
    }

    private int getScrollDistance(){
        return getExpectScrollX() - getScrollX();
    }

    private int getExpectScrollX(){
        return (getCurrentItem()-firstCurrentId)*getWidth();
    }

    private void Log(String msg){
        if(DEBUG) Log.e(TAG,msg);
    }
}
