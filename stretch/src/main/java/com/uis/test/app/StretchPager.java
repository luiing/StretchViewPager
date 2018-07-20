/*
 * Copyright by uis 2018.
 */

package com.uis.test.app;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import java.math.BigDecimal;

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

    private int lastPosition = 0;//记录上一次滑动x坐标
    private boolean stretchStatus = false;//侧滑状态
    private boolean isAnimRunning = false;//正在执行回弹动画
    private OnStretchListener listener;
    private int refreshModel = 0x0;//刷新模式,优先级大于弹性模式
    private int stretchModel = 0x11;//弹性模式
    private int directionModel = STRETCH_NONE;

    private ValueAnimator anim = ValueAnimator.ofInt(0, 1).setDuration(300);

    private int mScrollX = 0;
    private int scrollDistance = 0;
    private int scrollDistancePercent = 0;
    private int mTouchSlop;
    private int activePointerId;

    public StretchPager(@NonNull Context context) {
        this(context,null);
    }

    public StretchPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        anim.setInterpolator(new DecelerateInterpolator());
        //mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 设置刷新模式，默认关闭
     * @param model one of {@link #STRETCH_BOTH},{@link #STRETCH_LEFT},{@link #STRETCH_RIGHT},{@link #STRETCH_NONE}
     */
    public void setRefreshModel(int model){
        this.refreshModel = model;
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isAnimRunning){//正在执行回弹动画，屏蔽所有滑动事件
            return false;
        }
        Log("action="+ev.getAction()+",x="+ev.getX());
        switch (ev.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN://0
                mScrollX = getScrollX();
                lastPosition = (int)ev.getX();
                activePointerId = ev.getPointerId(0);
                Log("start scrollX="+mScrollX);
                break;
            case MotionEvent.ACTION_MOVE://2
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if(-1 == pointerIndex){
                    return false;
                }
                int distanceX = (int)(ev.getX(pointerIndex)-lastPosition);
                lastPosition = (int)ev.getX(pointerIndex);
                if(getAdapter() != null){
                    if(stretchStatus || (stretchStatus = getStretchEnable(distanceX))){
                        if(distanceX != 0){
                            scrollByMove(Float.valueOf(0.75f*distanceX).intValue());
                        }
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP://1
            case MotionEvent.ACTION_CANCEL://3
                if(stretchStatus){
                    scrollEndMove();
                    return false;
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
            default:
                if(stretchStatus){
                    return false;
                }
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
        scrollBy(-x, 0);
        if(null != listener && (refreshModel & STRETCH_BOTH)>0){
            listener.onScrolled(directionModel,mScrollX - getScrollX());
        }
    }

    private void scrollEndMove() {
        scrollDistance = mScrollX - getScrollX();
        if(0 != scrollDistance) {
            Log("Anim is running...");
            isAnimRunning = true;
            anim.addUpdateListener(this);
            anim.start();
        }
        if(null != listener && (refreshModel & STRETCH_BOTH)>0){
            listener.onRefresh(directionModel,Math.abs(scrollDistance));
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float percent = animation.getAnimatedFraction();
        int dx = (int) (percent * scrollDistance);
        scrollBy(dx-scrollDistancePercent, 0);
        scrollDistancePercent = dx;
        if(1.0f == percent){
            anim.removeAllUpdateListeners();
            scrollDistancePercent = 0;
            final int expectScrollX = getCurrentItem()*getWidth();
            if(getScrollX() != expectScrollX){//位置修正
                Log("repairAnim is running..."+expectScrollX+",currentX="+getScrollX());
                scrollTo(expectScrollX,0);
            }
            isAnimRunning = false;
            stretchStatus = false;
            Log("end scrollX="+getScrollX());
        }
    }

    private void Log(String msg){
        if(DEBUG){
            Log.e(TAG,msg);
        }
    }
}
