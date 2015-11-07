# ACDD-非代理Android动态部署框架
![](art/ACDD_logo_full.png)<br>
 [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20ACDD-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2056)<br>
[![Join the chat at https://gitter.im/bunnyblue/ACDD](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bunnyblue/ACDD?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)<br>
 Build Status [![Build Status](https://travis-ci.org/bunnyblue/ACDD.svg?branch=master)](https://travis-ci.org/bunnyblue/ACDD)<br>



ACDDCore Android动态部署框架（你可以认为是插件，但又与插件不一样），与传统意义上的插件不一样<br>The MIT License (MIT) Copyright (c) 2015 Bunny Blue,achellies<br>



  <br>代码遵循MIT License，Android动态部署框架,Activity 、Receiver支持stub模式，有bug的话可以在issue里面提交。</br>

### ，示例以及编译工具已经移动到 https://github.com/bunnyblue/ACDDExtension


### Contributors
[achellies](https://github.com/achellies)<br>
[BunnyBlue](https://github.com/bunnyblue)<br>

## plugin start
从ACDDExt下载aapt，不再支持eclipse，build-tool为23
编写动态部署的组件跟开发普通App没区别，只不过最后编译的时候需要注意资源分区.
### 组件资源注意事项
```gradle 
//脚本配置，编译acdd产生插件包，public为宿主的定义，详情可以参照https://android.googlesource.com/platform/frameworks/base/+/c8834722d5591d1381dc199f04a544a6b11b74bd/core/res/res/values/public.xml
//要注意的是 5.0之后androidfw资源查找逻辑修改了，如果插件需要新的theme在宿主里面定义，然后xml直接引用
android {
    compileSdkVersion 23
    buildToolsVersion "23.0.2"
    defaultConfig {
        applicationId "com.acdd.testapp2"
        minSdkVersion 14
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
    }

    productFlavors {
        acdd {
            aaptOptions.additionalParameters '--ACDD-resoure-id', '0x5e', '--ACDD-shared-resources', rootProject.file("public.xml").getAbsolutePath()
        }
        normal {
        }
    }
}
```

宿主的0x7f这个一般不动。0x10到0x7e的都可以用，当然，0x0这一块的最好不要动,0x00是共享资源，跟你没啥关系基本上，0x01是Android系统资源， 0x02是WebView资源(Android 5.0新增)


##Demo Apk & Gif演示动画
<a href="https://github.com/bunnyblue/ACDDExtension/blob/master/Dist/ACDDLauncher.apk">
 点我下载Demo.apk
</a>

![Sample Gif](https://github.com/bunnyblue/ACDDExtension/raw/master/art/demo.gif)

# License
 [![License](https://img.shields.io/badge/License-MIT%20License-brightgreen.svg)]()<br>
The MIT License (MIT) Copyright (c) 2015 Bunny Blue,achellies

# [功能详情 @ Wiki](https://github.com/bunnyblue/ACDD/wiki#feature-zh)
