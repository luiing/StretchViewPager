### StretchViewPager
Support left and right pull viewpager,can refresh and stretch

Simple usefull,Simple extends,Simple source code,Support AndroidX

![Refresh1](/pic/demo10.gif)
![Refresh2](/pic/demo20.gif)

### Use

    implementation 'com.uis:stretch:0.2.3'
    implementation "com.android.support:support-v4:$supportVer"

    leftView = LayoutInflater.from(this).inflate(R.layout.item_pager_left,null);
    rightView = LayoutInflater.from(this).inflate(R.layout.item_pager_right,null);
    pager.setRefreshView(leftView,rightView);
    adapter = new FragAdapter(4,getSupportFragmentManager());
    pager.setAdapter(adapter);
    pager.setOnStretchListener(this);
    
### Version(call setRefreshView set refresh header)
Version|Remarks|Time
----|----|----
0.2.0 |通过addView增加边界内容|2018/7
0.2.1 |fixed swip next page didn't work|2019/4
0.2.2 |fixed last page disappear content|2019/7
0.2.3 |fixed swip next page didn't work after vertical swip |2019/7

### LICENSE
Copyright 2018, uis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


