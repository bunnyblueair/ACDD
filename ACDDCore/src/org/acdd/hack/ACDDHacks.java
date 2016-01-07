/*
 * ACDD Project
 * file ACDDHacks.java  is  part of ACCD
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
package org.acdd.hack;

import android.app.Application;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.view.ContextThemeWrapper;

import org.acdd.hack.Hack.AssertionFailureHandler;
import org.acdd.hack.Hack.HackDeclaration;
import org.acdd.hack.Hack.HackedClass;
import org.acdd.hack.Hack.HackedField;
import org.acdd.hack.Hack.HackedMethod;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

import dalvik.system.DexClassLoader;
/****
 * this class is ACDD Core Inject Implementation,hook system implementation
 * 1 hack Class(such  hack class ActivityThread)
 * 2 hack method(such ActivityThread_currentActivityThread,mean currentActivityThread at class ActivityThread)
 * 3 hack field(such ActivityThread_mAllApplications,field mAllApplications at class ActivityThread)
 * *****/
public class ACDDHacks extends HackDeclaration implements
        AssertionFailureHandler {
    /**
     * This manages the execution of the main thread in an
     * application process, scheduling and executing activities,
     * broadcasts, and other operations on it as the activity
     * manager requests.
     */
    public static HackedClass<Object> ActivityThread;
    /** Reference to singleton  ActivityThread*/
    public static HackedMethod ActivityThread_currentActivityThread;
    //public static HackedMethod ActivityThread_currentProcessName;
    public static HackedField<Object, ArrayList<Application>> ActivityThread_mAllApplications;
    public static HackedField<Object, Application> ActivityThread_mInitialApplication;
    public static HackedField<Object, Instrumentation> ActivityThread_mInstrumentation;
    public static HackedField<Object, Map<String, Object>> ActivityThread_mPackages;
    public static HackedField<Object, Object> ActivityThread_sPackageManager;

    public static HackedClass<Application> Application;
    public static HackedMethod Application_attach;
    public static HackedClass<AssetManager> AssetManager;
    /**
     * Add an additional set of assets to the asset manager.This can be either a directory or ZIP file.
     * Returns the cookie of the added asset, or 0 on failure.solve  resource  problem
     **/
    public static HackedMethod AssetManager_addAssetPath;
    public static HackedClass<ClassLoader> ClassLoader;
    /**
     * Returns the absolute path of the native library with the specified name,
     * or {@code null}. If this method returns {@code null} then the virtual
     * machine searches the directories specified by the system property
     */
    public static HackedMethod ClassLoader_findLibrary;
    /**
     * Common implementation of Context API, which provides the base
     * context object for Activity and other application components.
     */
    public static HackedClass<Object> ContextImpl;
    public static HackedField<Object, Resources> ContextImpl_mResources;
    public static HackedField<Object, Theme> ContextImpl_mTheme;
    /**
     * A ContextWrapper that allows you to modify the theme from what is in the
     * wrapped context.
     */
    public static HackedClass<ContextThemeWrapper> ContextThemeWrapper;
    public static HackedField<ContextThemeWrapper, Context> ContextThemeWrapper_mBase;
    public static HackedField<ContextThemeWrapper, Resources> ContextThemeWrapper_mResources;
    public static HackedField<ContextThemeWrapper, Theme> ContextThemeWrapper_mTheme;
    /**
     * Proxying implementation of Context that simply delegates all of its calls to
     * another Context.  Can be subclassed to modify behavior without changing
     * the original Context.
     **/
    public static HackedClass<ContextWrapper> ContextWrapper;
    public static HackedField<ContextWrapper, Context> ContextWrapper_mBase;
    public static HackedClass<DexClassLoader> DexClassLoader;
    public static HackedMethod DexClassLoader_findClass;
    public static ArrayList<HackedMethod> GeneratePackageInfoList;
    public static ArrayList<HackedMethod> GetPackageInfoList;
    public static HackedClass<Object> IPackageManager;
    /**
     * LexFile is Ali YunOS exectue file format  like dex,run on lemur  vm
     * */
    public static HackedClass<Object> LexFile;
    public static HackedMethod LexFile_close;
    public static HackedMethod LexFile_loadClass;
    public static HackedMethod LexFile_loadLex;
    //define support ali YunOS end
    /**
     * Local state maintained about a currently loaded .apk,used by ActivityThread
     */
    public static HackedClass<Object> LoadedApk;
    public static HackedField<Object, String> LoadedApk_mAppDir;
    public static HackedField<Object, Application> LoadedApk_mApplication;
    public static HackedField<Object, ClassLoader> LoadedApk_mBaseClassLoader;
    public static HackedField<Object, ClassLoader> LoadedApk_mClassLoader;
    public static HackedField<Object, String> LoadedApk_mResDir;
    public static HackedField<Object, Resources> LoadedApk_mResources;
    public static HackedClass<Resources> Resources;
    public static HackedField<Resources, Object> Resources_mAssets;
    public static HackedClass<Service> Service;
    //support stub mode
    public static HackedClass<Object> PackageParser;
    public static HackedClass<Object> PackageParser$Activity;
    public static HackedClass<Object> PackageParser$Provider;
    public static HackedClass<Object> PackageParser$ActivityIntentInfo;
    public static HackedField<Object, Object> PackageParser$ActivityIntentInfo_activity;
    public static HackedField<Object, ArrayList<Object>> PackageParser$Activity_intents;
    public static HackedClass<Object> PackageParser$Component;
    public static HackedMethod PackageParser$Component_getComponentName;
    public static HackedClass<Object> PackageParser$Package;
    public static HackedField<Object, ArrayList<Object>> PackageParser$Package_activities;
    public static HackedField<Object, ApplicationInfo> PackageParser$Package_applicationInfo;
    public static HackedField<Object, String> PackageParser$Package_packageName;
    public static HackedField<Object, ArrayList<Object>> PackageParser$Package_receivers;
    public static HackedField<Object, ArrayList<Object>> PackageParser$Package_services;
    public static HackedField<Object, ArrayList<Object>> PackageParser$Package_providers;
    public static Hack.HackedConstructor PackageParser_constructor;
    public static HackedMethod PackageParser_generatePackageInfo;
    public static HackedMethod PackageParser_parsePackage;
    //end support stub mode
    protected static final Logger log;
    public static boolean sIsIgnoreFailure;
    public static boolean sIsReflectAvailable;
    public static boolean sIsReflectChecked;
    private AssertionArrayException mExceptionArray;

    public ACDDHacks() {
        this.mExceptionArray = null;
    }

    static {
        log = LoggerFactory.getInstance(ACDDHacks.class);
        sIsReflectAvailable = false;
        sIsReflectChecked = false;
        sIsIgnoreFailure = false;
        GeneratePackageInfoList = new ArrayList<HackedMethod>();
        GetPackageInfoList = new ArrayList<HackedMethod>();
    }

    /**
     * hack all defined class,method ,and field
     ***/
    public static boolean defineAndVerify() throws AssertionArrayException {
        if (sIsReflectChecked) {
            return sIsReflectAvailable;
        }
        ACDDHacks acddHacks = new ACDDHacks();
        try {
            Hack.setAssertionFailureHandler(acddHacks);
            if (VERSION.SDK_INT == 11) {
                acddHacks.onAssertionFailure(new HackAssertionException(
                        "Hack Assertion Failed: Android OS Version 11"));
            }
            allClasses();
            allConstructors();
            allFields();
            allMethods();
            if (acddHacks.mExceptionArray != null) {
                sIsReflectAvailable = false;
                throw acddHacks.mExceptionArray;
            }
            sIsReflectAvailable = true;
            return sIsReflectAvailable;
        } catch (Throwable e) {
            sIsReflectAvailable = false;
            log.error("HackAssertionException", e);
        } finally {
            Hack.setAssertionFailureHandler(null);
            sIsReflectChecked = true;
        }
        return sIsIgnoreFailure;
    }

    /**
     * hack all defined classes
     ***/
    private static void allClasses() throws HackAssertionException {
        if (VERSION.SDK_INT <= 8) {
            LoadedApk = Hack.into("android.app.ActivityThread$PackageInfo");
        } else {
            LoadedApk = Hack.into("android.app.LoadedApk");
        }
        ActivityThread = Hack.into("android.app.ActivityThread");
        Resources = Hack.into(Resources.class);
        Application = Hack.into(Application.class);
        AssetManager = Hack.into(AssetManager.class);
        IPackageManager = Hack.into("android.content.pm.IPackageManager");
        Service = Hack.into(Service.class);
        ContextImpl = Hack.into("android.app.ContextImpl");
        ContextThemeWrapper = Hack.into(ContextThemeWrapper.class);
        ContextWrapper = Hack.into("android.content.ContextWrapper");
        sIsIgnoreFailure = true;
        ClassLoader = Hack.into(ClassLoader.class);
        DexClassLoader = Hack.into(DexClassLoader.class);
        LexFile = Hack.into("dalvik.system.LexFile");
        PackageParser$Component = Hack.into("android.content.pm.PackageParser$Component");
        PackageParser$Activity = Hack.into("android.content.pm.PackageParser$Activity");
        PackageParser$Provider=Hack.into("android.content.pm.PackageParser$Provider");
        PackageParser = Hack.into("android.content.pm.PackageParser");
        PackageParser$Package = Hack.into("android.content.pm.PackageParser$Package");
        PackageParser$ActivityIntentInfo = Hack.into("android.content.pm.PackageParser$ActivityIntentInfo");
        sIsIgnoreFailure = false;
    }

    /***
     * hack  all defied fields
     **/
    private static void allFields() throws HackAssertionException {
        ActivityThread_mInstrumentation = ActivityThread.field(
                "mInstrumentation").ofType(Instrumentation.class);
        ActivityThread_mAllApplications = ActivityThread.field(
                "mAllApplications").ofGenericType(ArrayList.class);
        ActivityThread_mInitialApplication = ActivityThread.field(
                "mInitialApplication").ofType(Application.class);
        ActivityThread_mPackages = ActivityThread.field("mPackages")
                .ofGenericType(Map.class);
        ActivityThread_sPackageManager = ActivityThread.staticField(
                "sPackageManager").ofType(IPackageManager.getmClass());
        LoadedApk_mApplication = LoadedApk.field("mApplication").ofType(
                Application.class);
        LoadedApk_mResources = LoadedApk.field("mResources").ofType(
                Resources.class);
        LoadedApk_mResDir = LoadedApk.field("mResDir").ofType(String.class);
        LoadedApk_mClassLoader = LoadedApk.field("mClassLoader").ofType(
                ClassLoader.class);
        LoadedApk_mBaseClassLoader = LoadedApk.field("mBaseClassLoader")
                .ofType(ClassLoader.class);
        LoadedApk_mAppDir = LoadedApk.field("mAppDir").ofType(String.class);
        ContextImpl_mResources = ContextImpl.field("mResources").ofType(
                Resources.class);
        ContextImpl_mTheme = ContextImpl.field("mTheme").ofType(Theme.class);
        sIsIgnoreFailure = true;
        ContextThemeWrapper_mBase = ContextThemeWrapper.field("mBase").ofType(
                Context.class);
        sIsIgnoreFailure = false;
        ContextThemeWrapper_mTheme = ContextThemeWrapper.field("mTheme")
                .ofType(Theme.class);
        try {
            if (VERSION.SDK_INT >= 17
                    && ContextThemeWrapper.getmClass().getDeclaredField(
                    "mResources") != null) {
                ContextThemeWrapper_mResources = ContextThemeWrapper.field(
                        "mResources").ofType(Resources.class);
            }
        } catch (NoSuchFieldException e) {
            log.warn("Not found ContextThemeWrapper.mResources on VERSION "
                    + VERSION.SDK_INT);
        }
        ContextWrapper_mBase = ContextWrapper.field("mBase").ofType(
                Context.class);
        Resources_mAssets = Resources.field("mAssets");
        PackageParser$Activity_intents = PackageParser$Component.field("intents").ofGenericType(ArrayList.class);
        PackageParser$Package_activities = PackageParser$Package.field("activities").ofGenericType(ArrayList.class);
        PackageParser$Package_services = PackageParser$Package.field("services").ofGenericType(ArrayList.class);
        PackageParser$Package_providers=PackageParser$Package.field("providers").ofGenericType(ArrayList.class);
        PackageParser$Package_receivers = PackageParser$Package.field("receivers").ofGenericType(ArrayList.class);
        PackageParser$Package_applicationInfo = PackageParser$Package.field("applicationInfo").ofType(ApplicationInfo.class);
        PackageParser$Package_packageName = PackageParser$Package.field("packageName").ofGenericType(String.class);
        PackageParser$ActivityIntentInfo_activity = PackageParser$ActivityIntentInfo.field("activity").ofType(PackageParser$Activity.getmClass());
    }

    /***
     * hack all defined methods
     **/
    private static void allMethods() throws HackAssertionException {
        ActivityThread_currentActivityThread = ActivityThread.method(
                "currentActivityThread");
//        ActivityThread_currentProcessName = ActivityThread.method(
//                "currentProcessName");
        AssetManager_addAssetPath = AssetManager.method("addAssetPath",
                String.class);
        Application_attach = Application.method("attach", Context.class);
        ClassLoader_findLibrary = ClassLoader.method("findLibrary",
                String.class);
        if (LexFile != null && LexFile.getmClass() != null) {
            LexFile_loadLex = LexFile.method("loadLex", String.class,
                    Integer.TYPE);
            LexFile_loadClass = LexFile.method("loadClass", String.class,
                    ClassLoader.class);
            LexFile_close = LexFile.method("close");
            DexClassLoader_findClass = DexClassLoader.method("findClass",
                    String.class);
        }
        PackageParser$Component_getComponentName = PackageParser$Component.method("getComponentName", new Class[0]);
    }

    private static void allConstructors() throws HackAssertionException {
        if (VERSION.SDK_INT <= 20) {
            PackageParser_constructor = PackageParser.constructor(String.class);
            return;
        }
        PackageParser_constructor = PackageParser.constructor(new Class[0]);

    }

    @Override
    public boolean onAssertionFailure(HackAssertionException hackAssertionException) {
        if (!sIsIgnoreFailure) {
            if (this.mExceptionArray == null) {
                this.mExceptionArray = new AssertionArrayException(
                        "acdd hack assert failed");
            }
            this.mExceptionArray.addException(hackAssertionException);
        }
        return true;
    }
}
