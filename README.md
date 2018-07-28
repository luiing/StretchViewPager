# StretchViewPager
Support left and right pull viewpager,can refresh and stretch

Simple usefull,Simple extends,Simple source code

![Refresh1](/pic/demo10.gif)
![Refresh2](/pic/demo20.gif)

# Use
`implementation 'com.uis:stretch:0.2.0'`

# Notice
1. First found out currentItem,then compare move distance and direction.
   首先找到当前页面的position,比较移动的距离和方向；
2. Use scrollBy(x,y) move view,then use ValueAnimator to recover position.
   使用scrollBY方法移动视图，使用属性动画恢复初始位置；
3. The most important is caculate scrollX distance.
    最重要的是计算恢复初始位置的距离，在进行回弹滑动；
4. It didn't support refreshing stay,unsupport like RecyclerView refreshing.
    此处不支持刷行停留，不能像RecyclerView那样刷行时候，头部保持在顶端
    因为viewpager还是可以滑动的，如果不让滑动很奇怪，需要的可以自己实现。
5. 在ViewPager中addView作为头部，此时显示内容不出或者不觉错位，详细处理见代码onMeasue,onLayout方法    
    
# Version(call setRefreshView set refresh header)
1. 0.2.0-revert refresh head in PagerAdapter,now just call `setRefreshView(View leftView,View rightView)`
        废除了在PagerAdapter中加刷新头部，现在直接调用`setRefreshView(View leftView,View rightView)`

# Show
        leftView = LayoutInflater.from(this).inflate(R.layout.item_pager_left,null);
        rightView = LayoutInflater.from(this).inflate(R.layout.item_pager_right,null);
        pager.setRefreshView(leftView,null);
        adapter = new FragAdapter(4,getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnStretchListener(this);

# Old Version(PagerAdapter set refresh header)
1. 0.0.2-fixed deviation 解决边界问题
2. 0.0.3-fixed PagerAdapter.POSITION_NONE model 解决PagerAdapter.POSITION_NONE下恢复位置异常
3. 0.1.0-add onRelease direction 给回调方法加上方向
4. 0.1.2-fixed MultiTouch,add SimpleStretchListener class 解决多点触摸问题，加入接口的实现类，方便匿名函数调用

# Old Show
         pager = (StretchPager)findViewById(R.id.pager);
         pager.setRefreshModel(StretchPager.STRETCH_NONE);//设置刷行模式，默认STRETCH_NONE
         pager.setStretchModel(StretchPager.STRETCH_BOTH);//设置弹性模式，默认STRETCH_BOHTH
         FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),pager.getRefreshModel());
         pager.setAdapter(adapter);
         pager.setOnStretchListener(adapter);//设置回调
         pager.addOnPageChangeListener(adapter);
         pager.setCurrentItem((pager.getRefreshModel()&StretchPager.STRETCH_LEFT)>0 ? 1 : 0);  


