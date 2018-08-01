[![Android Gems](http://www.android-gems.com/badge/bunnyblue/ACDD.svg?branch=master)](http://www.android-gems.com/lib/bunnyblue/ACDD)

# ACDDCore  non-Proxy  Android Dynamic Deployment Framework
![](art/ACDD_logo_full.png)<br>
 [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20ACDD-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2056)<br>
[![Join the chat at https://gitter.im/bunnyblue/ACDD](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bunnyblue/ACDD?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)<br>
 Build Status [![Build Status](https://travis-ci.org/bunnyblue/ACDD.svg?branch=master)](https://travis-ci.org/bunnyblue/ACDD)


ACDD ,Android Component Dynamic Deployment Framework(Android  Plugin Framework)<br>

The MIT License (MIT) Copyright (c) 2015 Bunny Blue,achellies<br>
### [README-中文](README-Zh.md)


  <br>use patched aapt  to build Dynamic Module，it's different with  some plugin frameworks which  implement through proxy，any  question open a issue</br>

# Simple Project& Build System(aapt and ...) has Moved to https://gitlab.com/bunnyblue/ACDDExtension




## plugin start
download aapt from repo,and  you should use build-tool version 23.x.x,
write your plugin as normal app, ant build  with  hacked aapt.
### plugin resource notice
```groovy

android {//you need use buildToolsVersion 23.x gradle plugin 1.3.+
    compileSdkVersion 23
    buildToolsVersion "23.0.2"

    productFlavors {
        acdd {
            aaptOptions.additionalParameters '--ACDD-resoure-id', '0x5e', '--ACDD-shared-resources', rootProject.file("public.xml").getAbsolutePath()
        }
        normal {
        }
    }
}
```

##Sample & Art
<a href="https://gitlab.com/bunnyblue/ACDDExtension/blob/master/Dist/ACDDLauncher.apk">
  Sample Apk,you can download from here
</a>

![Sample Gif](https://gitlab.com/bunnyblue/ACDDExtension/raw/master/art/demo.gif)

# License
 [![License](https://img.shields.io/badge/License-MIT%20License-brightgreen.svg)]()<br>
The MIT License (MIT) Copyright (c) 2015 Bunny Blue,achellies

# [Feature @ Wiki](https://gitlab.com/bunnyblue/ACDD/wiki#feature)

# Android Support

| Android version        | Status           | 
| ------------- |:-------------:| 
| Android 6.0      | tested | 
| Android 5.0     | tested      |   
| Android  4.0 | tested     |  
| Android 3.x | unsupported|
| Android 2.x | coming soon|
