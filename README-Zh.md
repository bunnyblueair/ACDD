# ACDD-非代理Android动态部署框架
![](art/ACDD_logo_full.png)<br>
 [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20ACDD-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2056)<br>
[![Join the chat at https://gitter.im/bunnyblue/ACDD](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bunnyblue/ACDD?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)<br>
 Build Status [![Build Status](https://travis-ci.org/bunnyblue/ACDD.svg?branch=master)](https://travis-ci.org/bunnyblue/ACDD)<br>



ACDDCore Android动态部署框架（你可以认为是插件，但又与插件不一样），与传统意义上的插件不一样<br>The MIT License (MIT) Copyright (c) 2015 Bunny Blue,achellies<br>



  <br>代码遵循MIT License，Android动态部署框架，与通过代理方式实现的插件区别很大，用过代理的应该知道，代理方式会有各种莫名其妙的问题，有bug的话可以在issue里面提交。</br>

### ，示例以及编译工具已经移动到 https://github.com/bunnyblue/ACDDExtension


### Contributors
[achellies](https://github.com/achellies)<br>
[BunnyBlue](https://github.com/bunnyblue)<br>

## plugin start
从ACDDExt下载aapt，建议使用build-tool 22版本，21不在维护，后面的小版本无所谓，主版本是22
编写动态部署的组件跟开发普通App没区别，只不过最后编译的时候需要注意资源分区.
### 组件资源注意事项
在gradle1.3之前的版本，通过aapt修改参数不太好处理，aapt修改的时候gradle插件1.3还没出来。
对于资源分区使用versionName做了个中转，这样把资源的Package-id传给aapt,当然现在方案很多了，后面重构，现在先这样。
比如说原来你这样写

宿主的0x7f这个一般不动。0x10到0x7e的都可以用，当然，0x0这一块的最好不要动,0x00是共享资源，跟你没啥关系基本上，0x01是Android系统资源， 0x02是WebView资源(Android 5.0新增)

```
versionName:"1.0.1"

```
现在versionName应该把package-id的十六进制附加在versionName后面，注意十六进制要小写

```
"1.0.10x7a"

```
那versionName不就变了吗？没，aapt在编译时候会去掉你添加的后4位。编译出来还是1.0.1

##Demo Apk & Gif演示动画
<a href="https://github.com/bunnyblue/ACDDExtension/blob/master/Dist/ACDDLauncher.apk">
 点我下载Demo.apk
</a>

![Sample Gif](https://github.com/bunnyblue/ACDDExtension/raw/master/art/demo.gif)

# License
 [![License](https://img.shields.io/badge/License-MIT%20License-brightgreen.svg)]()<br>
The MIT License (MIT) Copyright (c) 2015 Bunny Blue,achellies


