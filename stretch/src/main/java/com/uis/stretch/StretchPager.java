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
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * stretch viewpager,support stretch recover,support edge refresh
 * @author uis 2018/7/18
 */
public class StretchPager extends ViewPager implements ValueAnimator.AnimatorUpdateListener{
    static final String TAG = "StretchPager";
    public static final int STRETCH_NONE = 0x00;
    public static final int STRETCH_LEFT = 0x01;//left stretch
    public static final int STRETCH_RIGHT = 0x10;//right stretch
    public static final int STRETCH_BOTH = 0x11;//both stretch
    public static boolean DEBUG = false;

    private int refreshModel = STRETCH_NONE;//refresh priority GT stretch
    private int stretchModel = STRETCH_BOTH;
    private int directionModel = STRETCH_NONE;
    private int animDuration = 300;

    private int lastPosition = 0;//last x position
    private int distanceX = 0;
    private boolean stretchStatus = false;
    private boolean isAnimRunning = false;//is recover anim running
    private OnStretchListener listener;
    private final ValueAnimator anim = ValueAnimator.ofInt(0, 1);
    //private final int mTouchSlop;
    private int activePointerId;
    private int firstScrollX = 0;//first touch down,current scrollx vaule

    public StretchPager(@NonNull Context context) {
        this(context,null);
    }

    public StretchPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());//set recover anim interpolator,this is more better
        //mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * set Refresh model，default is: closed
     * @param model one of {@link #STRETCH_BOTH},{@link #STRETCH_LEFT},{@link #STRETCH_RIGHT},{@link #STRETCH_NONE}
     */
    public void setRefreshModel(int model){
        this.refreshModel = model;
    }

    public int getRefreshModel() {
        return refreshModel;
    }

    /**
     * set Stretch model，default is: opened
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

    public void setAnimDuration(int duration){
        this.animDuration = duration;
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
                if(stretchStatus && !isAnimRunning){
                    scrollEndMove();
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN://5
                if(stretchStatus){
                    final int index = ev.getActionIndex();
                    lastPosition = (int)ev.getX(index);//multi-touch
                    activePointerId = ev.getPointerId(index);
                    return true;
                }
                break;
        }
        //Log("actionId="+ev.getAction()+",stretchStatus="+stretchStatus+",distanceX="+distanceX);
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log("actionId="+ev.getAction()+".....");
        int actionId = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (actionId){
            case MotionEvent.ACTION_DOWN://0
                Log("current start x="+getScrollX());
                if(!isAnimRunning) {
                    firstScrollX = getScrollX();
                }
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
            directionModel = STRETCH_LEFT;//left edge and distanceX GT 0
        }else if((stretchRight || refreshRight) && getAdapter().getCount() == getCurrentItem()+1+(refreshRight ? 1 : 0) && distanceX<0){
            directionModel = STRETCH_RIGHT;//right edge and distanceX LT 0
        }else{
            directionModel = STRETCH_NONE;
            enable = false;
        }
        return enable;
    }

    private void scrollByMove(int x){
        scrollBy(-x*4/5, 0);
        if(null != listener){
            listener.onScrolled(directionModel,getScrollDistance());
        }
    }

    private void scrollEndMove() {
        isAnimRunning = true;
        anim.addUpdateListener(this);
        anim.setDuration(animDuration);
        lastTotalDistance = 0;
        anim.start();
        final int scrollDistance = getScrollDistance();
        if(null != listener && !isAnimRunning ){
            if((STRETCH_LEFT == directionModel && scrollDistance >0) || (STRETCH_RIGHT == directionModel && scrollDistance <0)) {
                listener.onRefresh(directionModel, Math.abs(scrollDistance));
            }
        }
        //Log("start anim  running....");
    }


    int lastTotalDistance = 0;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float percent = animation.getAnimatedFraction();
        int distance = getScrollDistance();

        int firstTotalDistance = distance + lastTotalDistance;
        int dx = (int) ((percent>1.0f ? 1.0:percent) * firstTotalDistance) - lastTotalDistance;
        lastTotalDistance += dx;
        scrollBy(dx, 0);
        if(1.0f <= percent){
            anim.removeAllUpdateListeners();
            lastTotalDistance = 0;
            if(null != listener)listener.onRelease(directionModel);
            isAnimRunning = false;
            stretchStatus = false;
            Log("current end x="+getScrollX());
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(0 == l && Math.abs(oldl) > getWidth()){//fixed PagerAdapter.POSITION_NONE  adapter data changed
            firstScrollX = 0;
            //Log("current="+l+",old="+oldl+",item="+getCurrentItem());
        }
    }

    private int getScrollDistance(){
        return getExpectScrollX() - getScrollX();
    }

    private int getExpectScrollX(){
        int width = getWidth();
        int round = (int)Math.round(1.0*firstScrollX/width);//fixed scrollx distance
        return round*getWidth();
    }

    public static void Log(String msg){
        if(DEBUG) Log.e(TAG,msg);
    }
}
