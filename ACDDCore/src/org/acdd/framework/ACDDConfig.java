/*
 * ACDD Project
 * file ACDDConfig.java  is  part of ACCD
 * The MIT License (MIT)  Copyright (c) 2015 Bunny Blue,achellies.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
package org.acdd.framework;

/**
 * @author BunnyBlue
 *
 */
public class ACDDConfig {
    /***延时启动配*****/
    public static String[] DELAY = new String[]{};
    public static String[] AUTO = new String[]{};
    public static String[] STORE = new String[]{};

    /**plugin preload  dir ******/
    public static String PRELOAD_DIR = "armeabi";
    public  static  String BUNDLE_PREFIX_COM="lib/"+ ACDDConfig.PRELOAD_DIR+"/libcom_";
    public  static  String BUNDLE_PREFIX_CN="lib/"+ ACDDConfig.PRELOAD_DIR+"/libcn_";
    /**enable opt dex on ART ****/
    public static boolean  optART=true;
    /******enable sub process******/
    public static boolean  subProcessEnable=false;
    public static boolean stubModeEnable = false;//2.3暂不支持STUB


}
