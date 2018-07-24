package com.uis.stretch;

/**
 * @author uis on 2018/7/19.
 */
public interface OnStretchListener {
    /**
     * @param direction 拽拉方向, STRETCH_LEFT 左边拽拉，STRETCH_RIGHT 右边拽拉
     * @param distance 滑动方向：x GT 0 is move right,x LT 0 is move left(x大于0向右，小于0向左)
     */
    void onScrolled(int direction,int distance);

    /**
     * @param direction 拽拉方向, STRETCH_LEFT 左边拽拉，STRETCH_RIGHT 右边拽拉
     * @param distance 释放时偏移量，为正数abs
     */
    void onRefresh(int direction,int distance);

    /**
     * 回弹结束
     */
    void onRelease(int direction);
}
