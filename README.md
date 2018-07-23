# StretchViewPager
Support left and right pull viewpager,can refresh and stretch

Simple usefull,Simple extends,Simple source code

![Refresh1](/pic/demo10.gif)
![Refresh2](/pic/demo20.gif)

# Use
`implementation 'com.uis:stretch:0.0.3'`

# Notice
1. First found out currentItem,then compare move distance and direction.
2. Use scrollBy(x,y) move view,then use ValueAnimator to recover position.
3. The most important is caculate crollX distance.

# Version
1. 0.0.2-fixed deviation
2. 0.0.3-fixed PagerAdapter.POSITION_NONE model

# Show
         pager = (StretchPager)findViewById(R.id.pager);
         pager.setRefreshModel(StretchPager.STRETCH_NONE);
         pager.setStretchModel(StretchPager.STRETCH_BOTH);
         FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),pager.getRefreshModel());
         pager.setAdapter(adapter);
         pager.setOnStretchListener(adapter);
         pager.addOnPageChangeListener(adapter);
         pager.setCurrentItem((pager.getRefreshModel()&StretchPager.STRETCH_LEFT)>0 ? 1 : 0);        


