# StretchViewPager
Support left and right pull viewpager,can refresh and stretch

Simple usefull,Simple extends,Simple source code

![Refresh1](/pic/demo10.gif)
![Refresh2](/pic/demo20.gif)

# Use
`implementation 'com.uis:stretch:0.1.2'`

# Notice
1. First found out currentItem,then compare move distance and direction.
2. Use scrollBy(x,y) move view,then use ValueAnimator to recover position.
3. The most important is caculate crollX distance.

# Version
1. 0.0.2-fixed deviation
2. 0.0.3-fixed PagerAdapter.POSITION_NONE model
3. 0.1.0-add onRelease direction
4. 0.1.2-fixed MultiTouch,add SimpleStretchListener class

# Show
         pager = (StretchPager)findViewById(R.id.pager);
         pager.setRefreshModel(StretchPager.STRETCH_NONE);
         pager.setStretchModel(StretchPager.STRETCH_BOTH);
         FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),pager.getRefreshModel());
         pager.setAdapter(adapter);
         pager.setOnStretchListener(adapter);
         pager.addOnPageChangeListener(adapter);
         pager.setCurrentItem((pager.getRefreshModel()&StretchPager.STRETCH_LEFT)>0 ? 1 : 0);  
         
         pager.setRefreshing();//停止回弹动画，可以保持当前位置，只有在回调方法中onRefresh中使用有效
         pager.setRefreshingDone();//启动回弹动画，恢复初识时位置，同上


