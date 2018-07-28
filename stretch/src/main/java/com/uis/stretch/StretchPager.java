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
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * stretch viewpager,support stretch recover,support edge refresh
 * @author uis 2018/7/18
 */
public class StretchPager extends ViewPager implements ValueAnimator.AnimatorUpdateListener{
    static final String TAG = "StretchPager";
    public static final int STRETCH_NONE = 0x00;
    /** left stretch */
    public static final int STRETCH_LEFT = 0x01;
    /** right stretch */
    public static final int STRETCH_RIGHT = 0x10;
    /** both stretch */
    public static final int STRETCH_BOTH = 0x11;
    public static boolean DEBUG = false;
    /** refresh priority GT stretch */
    private int refreshModel = STRETCH_NONE;
    private int stretchModel = STRETCH_BOTH;
    private int directionModel = STRETCH_NONE;
    private float mRate = 0.55f;//from 0-1.0
    /** last x position */
    private int lastPosition = 0;
    private int distanceX = 0;
    private boolean stretchStatus = false;
    /** is recover anim running */
    private boolean isAnimRunning = false;
    private OnStretchListener listener;
    private final ValueAnimator anim = ValueAnimator.ofInt(0, 1);
    //private final int mTouchSlop;
    private int activePointerId;
    /** first touch down,current scrollx vaule */
    private int firstScrollX = 0;
    private int lastTotalDistance = 0;
    private View leftView,rightView;

    public StretchPager(@NonNull Context context) {
        this(context,null);
    }

    public StretchPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());//set recover anim interpolator,this is more better
        anim.setDuration(300);
        //mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setRefreshView(View leftView,View rightView){
        this.leftView = leftView;
        this.rightView = rightView;
        if(leftView != null){
            refreshModel |= STRETCH_LEFT;
        }
        if(rightView != null){
            refreshModel |= STRETCH_RIGHT;
        }
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

    public void setAnimInterpolator(Interpolator interpolator){
        this.anim.setInterpolator(interpolator);
    }

    public void setAnimDuration(int duration){
        this.anim.setDuration(duration);
    }

    public void setRate(float rate) {
        this.mRate = rate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        View child = getChildAt(count-1);
        if(child != null && (child == leftView || child == rightView)){
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.isDecor = true;//装饰view开启，否则布局内容显示错位
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final int count = getChildCount();//重新放置位置
        View child = getChildAt(count-1);
        if(child != null) {
            int width = getMeasuredWidth();
            int scrollX = getExpectScrollX();
            if (child == leftView) {
                int left = scrollX - width;
                int right = scrollX;
                leftView.layout(left, 0, right, getMeasuredHeight());
            } else if (child == rightView) {
                int left = scrollX + width;
                int right = left + width;
                rightView.layout(left, 0, right, getMeasuredHeight());
            }
        }
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
        return super.onTouchEvent(ev);
    }

    private boolean getStretchEnable(int distanceX){
        boolean enable = true;
        boolean refreshLeft = (STRETCH_LEFT & refreshModel)>0;
        boolean refreshRight = (STRETCH_RIGHT & refreshModel)>0;
        boolean stretchLeft = (STRETCH_LEFT & stretchModel) > 0;
        boolean stretchRight = (STRETCH_RIGHT & stretchModel) > 0;
        if( (stretchLeft || refreshLeft) && 0 == getCurrentItem() && distanceX>0 ){
            directionModel = STRETCH_LEFT;//left edge and distanceX GT 0
        }else if((stretchRight || refreshRight) && getAdapter().getCount() == getCurrentItem()+1 && distanceX<0){
            directionModel = STRETCH_RIGHT;//right edge and distanceX LT 0
        }else{
            directionModel = STRETCH_NONE;
            enable = false;
        }
        return enable;
    }

    private void scrollByMove(int x){
        addLeftRightEdge();
        double rate = Math.exp(-mRate*Math.abs(x)/100);
        x = -(int)(rate*x);
        scrollBy(x, 0);
        if(null != listener){
            listener.onScrolled(directionModel,getScrollDistance());
        }
    }

    private void addLeftRightEdge(){
        if(directionModel == STRETCH_LEFT && leftView != null && leftView.getParent()==null){
            addView(leftView);
        }else if(directionModel == STRETCH_RIGHT && rightView != null && rightView.getParent()==null){
            addView(rightView);
        }
    }

    private void scrollEndMove() {
        isAnimRunning = true;
        final int scrollDistance = getScrollDistance();
        if(null != listener){
            listener.onRefresh(directionModel, Math.abs(scrollDistance));
        }
        refreshDoneAnim();
    }

    private void refreshDoneAnim(){
        anim.addUpdateListener(this);
        anim.start();
    }

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
            removeView(leftView);
            removeView(rightView);
            Log("current end x="+getScrollX());
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
