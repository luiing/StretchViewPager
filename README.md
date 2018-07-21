# StretchViewPager
Support left and right pull viewpager,can refresh and stretch

Simple usefull,Simple extends,Simple source code

# Use
`implementation 'com.uis:stretch:0.0.1'`

# Notice
1. First found out currentItem,then compare move distance and direction.
2. Use scrollBy(x,y) move view,then use ValueAnimator to recover position.
3. The most important is caculate crollX distance.

# Show
         pager = (StretchPager)findViewById(R.id.pager);
         pager.setRefreshModel(StretchPager.STRETCH_NONE);
         pager.setStretchModel(StretchPager.STRETCH_BOTH);
         FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),pager.getRefreshModel());
         pager.setAdapter(adapter);
         pager.setOnStretchListener(adapter);
         pager.addOnPageChangeListener(adapter);
         pager.setCurrentItem((pager.getRefreshModel()&StretchPager.STRETCH_LEFT)>0 ? 1 : 0);
         
![Refresh1](/pic/device-2018-07-22-015518.png)
![Refresh2](/pic/device-2018-07-22-015550.png)
![Stretch1](/pic/device-2018-07-22-015938.png)
![Stretch2](/pic/device-2018-07-22-015910.png)
![Stretch3](/pic/device-2018-07-22-015923.png)

