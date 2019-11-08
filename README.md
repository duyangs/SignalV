# SignalV

[![](https://jitpack.io/v/duyangs/SignalV.svg)](https://jitpack.io/#duyangs/SignalV)
[![](https://img.shields.io/badge/build-15%2B-brightgreen)]()

> Android 自定义信号展示View
<div align="center">
 <img src="https://github.com/duyangs/SignalV/blob/master/device-2019-11-08-180829.gif" width = "200" height = "400" alt="演示图" align=center />
</div>

## How to

### Gradle
```Groovy
//Step1.Add the JitPack repository to your build file
//Add it in your root build.gradle at the end of repositories:

  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
//Step2.Add the dependency

  dependencies {
	        implementation 'com.github.duyangs:SignalV:Tag'
	}

```

### Use
- in `.xml`
```xml
<com.duangs.signalv.SignalView
        android:id="@+id/signal"
        android:layout_width="120dp"
        android:layout_height="80dp"
        app:connected="false"//是否链接网络
        app:level_color="@color/colorAccent"//信号等级覆盖颜色
        app:primary_color="@color/colorPrimary"//默认颜色
        app:signal_maximum="5"//最大信号级别
        app:signal_level="3"//信号级别
        app:spacing="1"//信号柱间隔
        app:unit_width="30"//信号柱宽度
        app:shadow_color="@color/colorPrimaryDark"//阴影颜色
        app:shadow_open="true"//是否开启阴影
        android:layout_gravity="center_horizontal"/>
```
- in `Activity`
```java
//设置信号级别
 signalView.setSignalLevel(0) //信号值需小于等于设置的signal_maximum
 
 //设置链接状态
 signalView.setConnected(true)
```
