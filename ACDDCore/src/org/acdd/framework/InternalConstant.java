/*
 * ACDD Project
 * file InternalConstant.java  is  part of ACCD
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
 * ACDD  Platform Configuration
 * @author BunnyBlue
 *
 */
public class InternalConstant {
    /****闪屏activity****/
    public static final String BOOT_ACTIVITY = "org.acdd.welcome.Welcome";
    public static final String BOOT_ACTIVITY_DEFAULT = "org.acdd.launcher.welcome";
    public static final String ACTION_BROADCAST_BUNDLES_INSTALLED = "org.acdd.action.BUNDLES_INSTALLED";
    public static final String ACDD_APP_DIRECTORY = "org.acdd.AppDirectory";
    public static final String INSTALL_LOACTION = "org.acdd.storage";
    public static final String COM_ACDD_DEBUG_BUNDLES = "org.acdd.debug.bundles";
    public static final String ACDD_PUBLIC_KEY = "org.acdd.publickey";
    public static final String ACDD_BASEDIR = "org.acdd.basedir";
    public static final String ACDD_BUNDLE_LOCATION = "org.acdd.jars";
    public static final String ACDD_CLASSLOADER_BUFFER_SIZE = "org.acdd.classloader.buffersize";
    public static final String ACDD_LOG_LEVEL = "org.acdd.log.level";
    public static final String ACDD_DEBUG_BUNDLES = "org.acdd.debug.bundles";
    public static final String ACDD_DEBUG_PACKAGES = "org.acdd.debug.packages";
    public static final String ACDD_DEBUG_SERVICES = "org.acdd.debug.services";
    public static final String ACDD_DEBUG_CLASSLOADING = "org.acdd.debug.classloading";
    public static final String ACDD_DEBUG = "org.acdd.debug";
    public static final String ACDD_FRAMEWORK_PACKAGE = "org.acdd.framework";

    public static final String ACDD_STRICT_STARTUP = "org.acdd.strictStartup";
    public static final String ACDD_AUTO_LOAD = "org.acdd.auto.load";
    public  static  final  String ACDD_CONFIGURE=".ACDD_configs";
    public static Class<?> BundleNotFoundActivity = null;
    /********disable compile code****/
    public  static  final  boolean CODE_ENABLE_COMPILE=false;
    public  static  final  boolean   STUB_PROVIDER=false;
    public  static  final String STUB_BUNDLE_LOCATION="accd_stub1";
    public  static  final String STUB_TARGET="accd_stub2";
    public  static  final String STUB_CHECKED="accd_stub3";
    public  static  final String STUB_DATA="accd_stub4";
    public  static  final String STUB_RAW_COMPONENT="accd_Component";
    public  static  final String STUB_STUB_ACTIVITY="org.acdd.runtime.stub.StubActivity";
    public  static  final String STUB_STUB_RECEIVER="org.acdd.runtime.stub.StubReceiver";


}
