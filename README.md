### StretchViewPager
Support left and right pull viewpager,can refresh and stretch

Simple usefull,Simple extends,Simple source code

![Refresh1](/pic/demo10.gif)
![Refresh2](/pic/demo20.gif)

### Use

    implementation 'com.uis:stretch:0.2.1'`
    implementation "com.android.support:support-v4:$supportVer"

    leftView = LayoutInflater.from(this).inflate(R.layout.item_pager_left,null);
    rightView = LayoutInflater.from(this).inflate(R.layout.item_pager_right,null);
    pager.setRefreshView(leftView,rightView);
    adapter = new FragAdapter(4,getSupportFragmentManager());
    pager.setAdapter(adapter);
    pager.setOnStretchListener(this);
    
### Version(call setRefreshView set refresh header)
1. 0.2.0 通过addView增加边界内容
2. 0.2.1 fixed swip next page didn't work

### LICENSE
MIT License

Copyright (c) 2018 uis

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software. 


