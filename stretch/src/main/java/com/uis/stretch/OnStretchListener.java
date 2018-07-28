package com.uis.stretch;

/**
 * @author uis on 2018/7/19.
 */
public interface OnStretchListener {
    /**
     * scrolling
     * @param direction model one of {@link StretchPager#STRETCH_RIGHT},{@link StretchPager#STRETCH_RIGHT};
     *                 拽拉方向, STRETCH_LEFT 左边拽拉，STRETCH_RIGHT 右边拽拉
     * @param distance 滑动方向：x GT 0 is move right,x LT 0 is move left(x大于0向右，小于0向左)
     */
    void onScrolled(int direction,int distance);

    /**
     * 释放时状态，正在刷新 refreshing
     * @param direction model one of {@link StretchPager#STRETCH_RIGHT},{@link StretchPager#STRETCH_RIGHT};
     *                  拽拉方向, STRETCH_LEFT 左边拽拉，STRETCH_RIGHT 右边拽拉
     * @param distance 释放时偏移量，为正数abs
     */
    void onRefresh(int direction,int distance);

    /**
     * 回弹结束 end
     * @param direction model one of {@link StretchPager#STRETCH_RIGHT},{@link StretchPager#STRETCH_RIGHT};
     */
    void onRelease(int direction);
}
