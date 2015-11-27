/*
 * ACDD Project
 * file BundlePackageManager.java  is  part of ACCD
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

package org.acdd.runtime.stub;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import org.acdd.framework.ACDD;
import org.acdd.framework.BundleImpl;
import org.acdd.framework.Framework;
import org.acdd.framework.InternalConstant;
import org.acdd.hack.ACDDHacks;
import org.acdd.hack.Hack;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.runtime.RuntimeVariables;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by BunnyBlue on 9/19/15.
 */

public class BundlePackageManager {
    private Object packageManger;

    public String bundleName;

    private ComponentIntentResolver activityIntentResolver;
    private ComponentIntentResolver serviceIntentResolver;
    private ComponentIntentResolver receiverIntentResolver;
    public ComponentIntentResolver providerIntentResolver;
    static Logger log = LoggerFactory.getInstance("BundlePackageManager");
    public static Queue<Bundle> sTargetBundleStorage = new LinkedList<Bundle>();
    public static Queue<Bundle> sTargetReceiverBundleStorage = new LinkedList<Bundle>();
    public BundlePackageManager() {

    }

    public static BundlePackageManager parseBundle(Context context, org.osgi.framework.Bundle bundle) throws InvocationTargetException {

        String absolutePath = ((BundleImpl) bundle).getArchive().getArchiveFile().getAbsolutePath();
        if (absolutePath == null || absolutePath.length() == 0 || context == null) {
            return null;
        }
        Object instance;
        ACDDHacks.PackageParser.getmClass();
        if (Build.VERSION.SDK_INT <= 20) {
            instance = ACDDHacks.PackageParser_constructor.getInstance(absolutePath);
        } else {
            instance = ACDDHacks.PackageParser_constructor.getInstance(new Object[0]);
        }
        try {
            AssetManager assetManager = (AssetManager) AssetManager.class.newInstance();
            ACDDHacks.AssetManager_addAssetPath.invoke(assetManager, RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
            instance = parse(instance, new Resources(assetManager, RuntimeVariables.delegateResources.getDisplayMetrics(), RuntimeVariables.delegateResources.getConfiguration()), assetManager.openXmlResourceParser(((Integer) ACDDHacks.AssetManager_addAssetPath.invoke(assetManager, absolutePath)).intValue(), "AndroidManifest.xml"));
            if (instance == null) {
                return null;
            }
            ComponentName componentName;
            BundlePackageManager bundlePackageManager = new BundlePackageManager();
            bundlePackageManager.initPackageManger(instance);
            bundlePackageManager.bundleName = bundle.getLocation();
            ACDDHacks.PackageParser$Package_packageName.set(instance, RuntimeVariables.androidApplication.getPackageName());
            ApplicationInfo applicationInfo = (ApplicationInfo) ACDDHacks.PackageParser$Package_applicationInfo.get(instance);
            applicationInfo.name = RuntimeVariables.androidApplication.getApplicationInfo().name;
            applicationInfo.className = RuntimeVariables.androidApplication.getApplicationInfo().className;
            applicationInfo.taskAffinity = RuntimeVariables.androidApplication.getApplicationInfo().taskAffinity;
            applicationInfo.permission = RuntimeVariables.androidApplication.getApplicationInfo().permission;
            applicationInfo.processName = RuntimeVariables.androidApplication.getApplicationInfo().processName;
            applicationInfo.theme = RuntimeVariables.androidApplication.getApplicationInfo().theme;
            applicationInfo.flags = RuntimeVariables.androidApplication.getApplicationInfo().flags;
            applicationInfo.uiOptions = RuntimeVariables.androidApplication.getApplicationInfo().uiOptions;
            applicationInfo.backupAgentName = RuntimeVariables.androidApplication.getApplicationInfo().backupAgentName;
            applicationInfo.descriptionRes = RuntimeVariables.androidApplication.getApplicationInfo().descriptionRes;
            applicationInfo.targetSdkVersion = RuntimeVariables.androidApplication.getApplicationInfo().targetSdkVersion;
            applicationInfo.compatibleWidthLimitDp = RuntimeVariables.androidApplication.getApplicationInfo().compatibleWidthLimitDp;
            applicationInfo.uid = RuntimeVariables.androidApplication.getApplicationInfo().uid;
            applicationInfo.largestWidthLimitDp = RuntimeVariables.androidApplication.getApplicationInfo().largestWidthLimitDp;
            applicationInfo.enabled = RuntimeVariables.androidApplication.getApplicationInfo().enabled;
            applicationInfo.requiresSmallestWidthDp = RuntimeVariables.androidApplication.getApplicationInfo().requiresSmallestWidthDp;
            applicationInfo.packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
            ComponentIntentResolver activityIntentResolver = new ComponentIntentResolver();
            ComponentIntentResolver serviceResolver = new ComponentIntentResolver();
            ComponentIntentResolver receiverResolver = new ComponentIntentResolver();
            ComponentIntentResolver providerResolver=new ComponentIntentResolver();
            List arrayList = new ArrayList();
            ArrayList activityList = (ArrayList) ACDDHacks.PackageParser$Package_activities.get(instance);
            ArrayList serviceList = (ArrayList) ACDDHacks.PackageParser$Package_services.get(instance);
            ArrayList receiverList =  (ArrayList) ACDDHacks.PackageParser$Package_receivers.get(instance);
            ArrayList providerList =  (ArrayList) ACDDHacks.PackageParser$Package_providers.get(instance);
            Intent intent = new Intent();
            intent.putExtra("RawQuery", true);
            for (Object act:activityList){
                componentName = (ComponentName) ACDDHacks.PackageParser$Component_getComponentName.invoke(act, new Object[0]);
                if (componentName != null) {
                    if (componentName.getClassName() == null || !componentName.getClassName().endsWith("Alias")) {
                        arrayList.add(componentName.getClassName());
                    }
                }
                intent.setComponent(componentName);
                if (RuntimeVariables.androidApplication.getPackageManager().resolveActivity(intent, 0) == null) {
                    try {
                        ActivityInfo activityInfo = (ActivityInfo) act.getClass().getField("info").get(act);
                        if (activityInfo.targetActivity != null) {
                            activityInfo.taskAffinity = RuntimeVariables.androidApplication.getApplicationInfo().taskAffinity;
                        }
                        activityInfo.packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // log.debug("=== stub== "+componentName.getClassName());
                    activityIntentResolver.addComponent(act);
                }else
                {
                   // log.debug("=== registed in host \n" + componentName.getClassName());
                }

            }
            bundlePackageManager.initActivityIntentResolver(activityIntentResolver);
            for (Object service:serviceList){
                componentName = (ComponentName) ACDDHacks.PackageParser$Component_getComponentName.invoke(service, new Object[0]);
                if (componentName != null) {
                    arrayList.add(componentName.getClassName());
                }
                intent.setComponent(componentName);
                if (RuntimeVariables.androidApplication.getPackageManager().resolveService(intent, 0) == null) {
                    try {
                        ((ServiceInfo) service.getClass().getField("info").get(service)).packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    serviceResolver.addComponent(service);
                }
            }
            bundlePackageManager.initServiceResolver(serviceResolver);
            for (Object receiver:receiverList){
                componentName = (ComponentName) ACDDHacks.PackageParser$Component_getComponentName.invoke(receiver, new Object[0]);
                if (componentName != null) {
                    arrayList.add(componentName.getClassName());
                }
                intent.setComponent(componentName);
                if (RuntimeVariables.androidApplication.getPackageManager().queryBroadcastReceivers(intent, 0) .isEmpty()) {
                    try {
                        ((ActivityInfo) receiver.getClass().getField("info").get(receiver)).packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    receiverResolver.addComponent(receiver);
                }
            }
            bundlePackageManager.initReceiverResolver(receiverResolver);
            if (InternalConstant.STUB_PROVIDER) {
                // provider
                for (Object provider : providerList) {
                    componentName = (ComponentName) ACDDHacks.PackageParser$Component_getComponentName.invoke(provider, new Object[0]);
                    if (componentName != null) {
                        arrayList.add(componentName.getClassName());
                    }
                    intent.setComponent(componentName);
                    ProviderInfo mProviderInfo = (ProviderInfo) provider.getClass().getField("info").get(provider);
                    ProviderInfo tmp = RuntimeVariables.androidApplication.getPackageManager().resolveContentProvider(mProviderInfo.authority, 0);
                    if (tmp == null) {
                      //  log.debug(mProviderInfo.name + "=== find  unregisted  provider authority=" + mProviderInfo.authority);
                        try {
                            ((ProviderInfo) provider.getClass().getField("info").get(provider)).packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        providerResolver.addProvider(mProviderInfo.authority, mProviderInfo);
                    } else {
                       // log.debug(mProviderInfo.name + "<<< find  in   host");
                    }
                }
                bundlePackageManager.initProviderIntentResolver(providerResolver);
            }
            return bundlePackageManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initReceiverResolver(ComponentIntentResolver receiverResolver) {
        this.receiverIntentResolver=receiverResolver;

    }

    private static Object parse(Object obj, Resources resources, XmlResourceParser xmlResourceParser) {

        String[] errors = new String[1];
        try {
            if (Build.VERSION.SDK_INT > 20) {
                return ACDDHacks.PackageParser.method("parseBaseApk", Resources.class, XmlResourceParser.class, Integer.TYPE, String[].class).invoke(obj, resources, xmlResourceParser, Integer.valueOf(PackageManager.GET_ACTIVITIES|PackageManager.GET_RECEIVERS|PackageManager.GET_SERVICES|PackageManager.GET_PROVIDERS), errors);
            }
//            public Package parsePackage(File sourceFile, String destCodePath,
//                    DisplayMetrics metrics, int flags)
            Object ret= ACDDHacks.PackageParser.method("parsePackage", Resources.class, XmlResourceParser.class, Integer.TYPE, String[].class).invoke(obj, resources, xmlResourceParser, Integer.valueOf(PackageManager.GET_ACTIVITIES | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES|PackageManager.GET_PROVIDERS), errors);
            if (ret==null){
                log.error(errors[0]);
            }
            return ret;
        } catch (Hack.HackDeclaration.HackAssertionException e) {
            try {
                return ACDDHacks.PackageParser.method("parsePackage", Resources.class, XmlResourceParser.class, Integer.TYPE, Boolean.TYPE, String[].class).invoke(obj, resources, xmlResourceParser, Integer.valueOf(PackageManager.GET_ACTIVITIES|PackageManager.GET_RECEIVERS|PackageManager.GET_SERVICES), Boolean.valueOf(false), errors);
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            } catch (Hack.HackDeclaration.HackAssertionException e1) {
                e1.printStackTrace();
            }
        } catch (Throwable th) {
            if ((RuntimeVariables.androidApplication.getApplicationInfo().flags & 2) == 0) {
                return null;
            }

        }
        return null;
    }

    void initPackageManger(Object packageManger) {

        this.packageManger = packageManger;
    }
    void initProviderIntentResolver(ComponentIntentResolver providerIntentResolver) {

        this.providerIntentResolver = providerIntentResolver;
    }


    void initActivityIntentResolver(ComponentIntentResolver activityIntentResolver) {
        ;
        this.activityIntentResolver = activityIntentResolver;
    }

    void initServiceResolver(ComponentIntentResolver serviceIntentResolver) {
        this.serviceIntentResolver = null;
    }



    public ResolveInfo wrapperActivityIntentIfNeed(Intent intent) {
        if (intent == null) {
            return null;
        }

        intent.putExtra(InternalConstant.STUB_CHECKED, true);
        if (this.activityIntentResolver == null) {
            return null;
        }
        Object component = intent.getComponent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (component == null && intent.getSelector() != null) {
                intent = intent.getSelector();
                component = intent.getComponent();
            }
        }
        if (component != null) {
            component = this.activityIntentResolver.componentHashMap.get(component);

            if (component == null) {
                return null;
            }
            try {
                ResolveInfo resolveInfo = new ResolveInfo();
                resolveInfo.activityInfo = (ActivityInfo) component.getClass().getField("info").get(component);
                wrapProxyActivity(intent, resolveInfo.activityInfo);
                return resolveInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (!TextUtils.isEmpty(intent.getPackage()) && !TextUtils.equals(intent.getPackage(), RuntimeVariables.androidApplication.getPackageName())) {
            return null;
        } else {
            List queryIntent = this.activityIntentResolver.queryIntent(intent, intent.resolveTypeIfNeeded(RuntimeVariables.androidApplication.getContentResolver()), false);
            if (queryIntent == null || queryIntent.size() <= 0) {
                return null;
            }
            ResolveInfo resolveInfo = null;
            try {
                Object actIntentInf = ACDDHacks.PackageParser$ActivityIntentInfo.field("activity").get(queryIntent.get(0));
                ActivityInfo activityInfo = (ActivityInfo) ACDDHacks.PackageParser$Activity.field("info").get(actIntentInf);

                wrapProxyActivity(intent, activityInfo);
                resolveInfo = new ResolveInfo();
                resolveInfo.activityInfo = activityInfo;
                return resolveInfo;
            } catch (Hack.HackDeclaration.HackAssertionException e) {
                e.printStackTrace();
            }


            return null;
        }

    }
    public ResolveInfo wrapperReceiverIntentIfNeed(Intent intent) {
        if (intent == null) {
            return null;
        }
       //
        if (this.receiverIntentResolver == null) {
            return null;
        }
        Object component = intent.getComponent();

        if (component != null) {
            component = this.receiverIntentResolver.componentHashMap.get(component);
            if (component == null) {
                return null;
            }
            try {
                ResolveInfo resolveInfo = new ResolveInfo();
                resolveInfo.activityInfo = (ActivityInfo) component.getClass().getField("info").get(component);

                wrapProxyReceiver(intent, resolveInfo.activityInfo);
                return resolveInfo;
            } catch (Exception e) {
                return null;
            }
        } else if (!TextUtils.isEmpty(intent.getPackage()) && !TextUtils.equals(intent.getPackage(), RuntimeVariables.androidApplication.getPackageName())) {
            return null;
        } else {
            List queryIntent = this.receiverIntentResolver.queryIntent(intent, intent.resolveTypeIfNeeded(RuntimeVariables.androidApplication.getContentResolver()), false);
            if (queryIntent == null || queryIntent.size() <= 0) {
                return null;
            }
            Object obj = ACDDHacks.PackageParser$ActivityIntentInfo_activity.get(queryIntent.get(0));
            ActivityInfo activityInfo=null;
            try {
                activityInfo = (ActivityInfo) obj.getClass().getField("info").get(obj);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            wrapProxyReceiver(intent,activityInfo);
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo=activityInfo;
            return  resolveInfo;
           // return (ResolveInfo) queryIntent.get(0);
        }
    }
    private void wrapProxyActivity(Intent intent, ActivityInfo activityInfo) {
        intent.putExtra(InternalConstant.STUB_DATA, intent.getDataString());
        intent.setData(null);
        intent.putExtra(InternalConstant.STUB_TARGET, activityInfo.name);
        intent.putExtra(InternalConstant.STUB_BUNDLE_LOCATION, this.bundleName);
        BundleImpl bundleImpl = (BundleImpl) ACDD.getInstance().getBundle(this.bundleName);
        if (!bundleImpl.getArchive().isDexOpted()) {
            bundleImpl.getArchive().optDexFile();
        }
        intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), InternalConstant.STUB_STUB_ACTIVITY);
        ActivityStackMgr.getInstance().handleActivityStack(activityInfo.name, intent, intent.getFlags(), activityInfo.launchMode);
    }
    private void wrapProxyReceiver(Intent intent, ActivityInfo activityInfo) {

        intent.putExtra(InternalConstant.STUB_CHECKED, true);
        intent.putExtra(InternalConstant.STUB_DATA, intent.getDataString());
        intent.setData(null);
        intent.putExtra(InternalConstant.STUB_TARGET, activityInfo.name);


//        Object obj = ACDDHacks.PackageParser$ActivityIntentInfo_activity.get(activityInfo);
//        try {
//            ActivityInfo activityInfo2 = (ActivityInfo) obj.getClass().getField("info").get(obj);
////log.debug(activityInfo2.name);
//            intent.putExtra(InternalConstant.STUB_TARGET, activityInfo2.name);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
        // intent.putExtra(InternalConstant.STUB_TARGET, activityInfo.name);
        intent.putExtra(InternalConstant.STUB_BUNDLE_LOCATION, this.bundleName);
        BundleImpl bundleImpl = (BundleImpl) ACDD.getInstance().getBundle(this.bundleName);
        if (!bundleImpl.getArchive().isDexOpted()) {
            bundleImpl.getArchive().optDexFile();
        }
        intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), InternalConstant.STUB_STUB_RECEIVER);
        storeReceiverTargetBundleIfNeed(intent);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public ResolveInfo wrapperServiceIntentIfNeed(Intent intent) {
        ;
        if (intent == null) {
            return null;
        }
        intent.putExtra(InternalConstant.STUB_CHECKED, true);
        if (this.serviceIntentResolver == null) {
            return null;
        }
        Object component = intent.getComponent();
        if (component == null && intent.getSelector() != null) {
            intent = intent.getSelector();
            component = intent.getComponent();
        }
        BundleImpl bundleImpl;
        if (component != null) {
            component = this.serviceIntentResolver.componentHashMap.get( component);
            if (component == null) {
                return null;
            }
            try {
                ResolveInfo resolveInfo = new ResolveInfo();
                resolveInfo.serviceInfo = (ServiceInfo) component.getClass().getField("info").get(component);
                bundleImpl = (BundleImpl) ACDD.getInstance().getBundle(this.bundleName);
                if (!bundleImpl.getArchive().isDexOpted()) {
                    bundleImpl.getArchive().optDexFile();
                }
                return resolveInfo;
            } catch (Exception e) {
                return null;
            }
        } else if (!TextUtils.isEmpty(intent.getPackage()) && !TextUtils.equals(intent.getPackage(), RuntimeVariables.androidApplication.getPackageName())) {
            return null;
        } else {
            List queryIntent = this.serviceIntentResolver.queryIntent(intent, intent.resolveTypeIfNeeded(RuntimeVariables.androidApplication.getContentResolver()), false);
            if (queryIntent == null || queryIntent.size() <= 0) {
                return null;
            }
            bundleImpl = (BundleImpl) ACDD.getInstance().getBundle(this.bundleName);
            if (!bundleImpl.getArchive().isDexOpted()) {
                bundleImpl.getArchive().optDexFile();
            }
            ResolveInfo resolveInfo = new ResolveInfo();
            Object actIntentInf = null;
            try {
                actIntentInf = ACDDHacks.PackageParser$ActivityIntentInfo.field("activity").get(queryIntent.get(0));
                ActivityInfo activityInfo = (ActivityInfo) ACDDHacks.PackageParser$Activity.field("info").get(actIntentInf);
                resolveInfo.activityInfo=activityInfo;
            } catch (Hack.HackDeclaration.HackAssertionException e) {
                e.printStackTrace();
            }

           // va.lang.ClassCastException: android.content.pm.PackageParser$ActivityIntentInfo
            return resolveInfo;
        }
    }

//     public static boolean modifyStubReceiver(Intent intent) {
//
//        if (intent == null) {
//            return false;
//        }
//        List<ResolveInfo> resolveInfo=RuntimeVariables.androidApplication.getPackageManager().queryBroadcastReceivers(intent, 0);
//        boolean cmpExist=intent.getComponent()!=null;
//        if (resolveInfo.isEmpty()) {
//            String location=null;
//            for (org.osgi.framework.Bundle bundle: ACDD.getInstance().getBundles()){
//                BundleImpl bundle1= (BundleImpl) bundle;
//                if (cmpExist){
//                    if (bundle1.getPackageManager().receiverIntentResolver.componentHashMap.get(intent.getComponent())!=null)
//                    {
//                        location=bundle1.getLocation();
//                        break;
//                    }
//                }else{
//                    resolveInfo= bundle1.getPackageManager().receiverIntentResolver.queryIntent(intent,intent.resolveTypeIfNeeded(RuntimeVariables.androidApplication.getContentResolver()),false);
//                    if (!resolveInfo.isEmpty())
//                    {
//                        location=bundle1.getLocation();
//                        break;
//                    }
//                }
//
//            }
//            if (location==null){
//                return  false;
//            }
//            BundleImpl bundle = (BundleImpl) ACDD.getInstance().getBundle(location);
//            bundle.getPackageManager().wrapperReceiverIntentIfNeed(intent);
//            intent.putExtra(InternalConstant.STUB_CHECKED, true);
//            intent.putExtra(InternalConstant.STUB_BUNDLE_LOCATION, bundle.getLocation());
//        } else {
//            return false;
//        }
//
//        if (!intent.getBooleanExtra(InternalConstant.STUB_CHECKED, false)) {
//            return true;
//        }
//        if (intent.getStringExtra(InternalConstant.STUB_BUNDLE_LOCATION) == null) {
//            return false;
//        }
//        return false;
//    }

    public static List<ResolveInfo> queryIntentService(Intent intent, String resolveType, int flags, int userid) {
        ;
        for (org.osgi.framework.Bundle bundle : ACDD.getInstance().getBundles()) {
            BundleImpl bundleImpl = (BundleImpl) bundle;
            if (bundleImpl.isUpdated()) {
                ResolveInfo wrapperServiceIntentIfNeed = bundleImpl.getPackageManager().wrapperServiceIntentIfNeed(intent);
                if (wrapperServiceIntentIfNeed != null) {
                    List<ResolveInfo> arrayList = new ArrayList(1);
                    arrayList.add(wrapperServiceIntentIfNeed);
                    return arrayList;
                }
            }
        }
        return null;
    }

    public static List<ResolveInfo> queryIntentActivities(Intent intent, String resolveType, int flags, int userid) {
        ;
        for (org.osgi.framework.Bundle bundle : ACDD.getInstance().getBundles()) {
            BundleImpl bundleImpl = (BundleImpl) bundle;
            if (bundleImpl.isUpdated()) {
                ResolveInfo wrapperActivityIntentIfNeed = bundleImpl.getPackageManager().wrapperActivityIntentIfNeed(intent);
                if (wrapperActivityIntentIfNeed != null) {
                    List<ResolveInfo> arrayList = new ArrayList(1);
                    arrayList.add(wrapperActivityIntentIfNeed);
                    return arrayList;
                }
            }
        }
        return null;
    }

    public static ActivityInfo getNewActivityInfo(ComponentName componentName, int flag) {
        ;
        for (org.osgi.framework.Bundle bundle : ACDD.getInstance().getBundles()) {
            BundleImpl bundleImpl = (BundleImpl) bundle;
            if (bundleImpl.isUpdated()) {
                BundlePackageManager packageManager = bundleImpl.getPackageManager();
                if (!(packageManager == null || packageManager.activityIntentResolver == null )) {
                    Object obj = packageManager.activityIntentResolver.componentHashMap.get(componentName);
                    if (obj != null) {
                        try {
                            return (ActivityInfo) obj.getClass().getField("info").get(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public static ServiceInfo getNewServiceInfo(ComponentName componentName, int flag) {
        for (org.osgi.framework.Bundle bundle : ACDD.getInstance().getBundles()) {
            BundleImpl bundleImpl = (BundleImpl) bundle;
            if (bundleImpl.isUpdated()) {
                BundlePackageManager packageManager = bundleImpl.getPackageManager();
                if (!(packageManager == null || packageManager.serviceIntentResolver == null )) {
                    Object obj = packageManager.serviceIntentResolver.componentHashMap.get(componentName);
                    if (obj != null) {
                        try {
                            return (ServiceInfo) obj.getClass().getField("info").get(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public static List<ResolveInfo> queryIntenServices(Intent intent, String str, int flags, int i2) {
        ;
        for (org.osgi.framework.Bundle bundle : ACDD.getInstance().getBundles()) {
            BundleImpl bundleImpl = (BundleImpl) bundle;
            if (bundleImpl.isUpdated()) {
                ResolveInfo wrapperActivityIntentIfNeed = bundleImpl.getPackageManager().wrapperActivityIntentIfNeed(intent);
                if (wrapperActivityIntentIfNeed != null) {
                    List<ResolveInfo> arrayList = new ArrayList(1);
                    arrayList.add(wrapperActivityIntentIfNeed);
                    return arrayList;
                }
            }
        }
        return null;
    }

    public static void processActivityIntentIfNeed(Object obj) {

        try {
            Class cls = Class.forName("android.app.ActivityThread$ActivityClientRecord");
            Field declaredField = cls.getDeclaredField("intent");
            declaredField.setAccessible(true);
            Intent intent = (Intent) declaredField.get(obj);
            Bundle bundle = sTargetBundleStorage.poll();
            if (intent.getComponent() != null && intent.getComponent().getClassName().equals(InternalConstant.STUB_STUB_ACTIVITY)) {
                Field activityInfo = cls.getDeclaredField("activityInfo");
                activityInfo.setAccessible(true);
                String string = bundle.getString(InternalConstant.STUB_DATA);
                if (!TextUtils.isEmpty(string)) {
                    if (string == null) {
                        string = null;
                    }
                    intent.setData(Uri.parse(string));
                }
                intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), bundle.getString(InternalConstant.STUB_TARGET));
                //if(!TextUtils.isEmpty(bundle.getString(InternalConstant.STUB_TARGET)))
                Object rec  = ((BundleImpl) ACDD.getInstance().getBundle(
                        bundle.getString(InternalConstant.STUB_BUNDLE_LOCATION))).
                        getPackageManager().
                        activityIntentResolver.componentHashMap.get(intent.getComponent());
                intent.removeExtra(InternalConstant.STUB_BUNDLE_LOCATION);
                if (rec == null) {
                    return;
                }
                Object value = Class.forName("android.content.pm.PackageParser$Activity").getField("info").
                        get(rec);
                activityInfo.set(obj, value);
                log.debug("run on portable  mode");
            }
        } catch (ClassNotFoundException th) {
            th.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public static void processReceiverIntentIfNeed(Object obj) {

        try {
            Class cls = Class.forName("android.app.ActivityThread$ReceiverData");
            Field declaredField = cls.getDeclaredField("intent");
            declaredField.setAccessible(true);
            Intent intent = (Intent) declaredField.get(obj);
            intent.setExtrasClassLoader(Framework.getSystemClassLoader());
            Bundle bundle =sTargetReceiverBundleStorage.poll();
            if (intent.getComponent() != null && intent.getComponent().getClassName().equals(InternalConstant.STUB_STUB_RECEIVER)) {
                intent.removeExtra(InternalConstant.STUB_CHECKED);
                Field activityInfo = cls.getDeclaredField("info");
                activityInfo.setAccessible(true);

                String string = bundle.getString(InternalConstant.STUB_DATA);
                if (!TextUtils.isEmpty(string)) {
                    intent.removeExtra(InternalConstant.STUB_DATA);
                    intent.setData(Uri.parse(string));
                }
                intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), bundle.getString(InternalConstant.STUB_TARGET));
                Object rec = ((BundleImpl) ACDD.getInstance().getBundle(
                        bundle.getString(InternalConstant.STUB_BUNDLE_LOCATION))).
                        getPackageManager().
                        activityIntentResolver.componentHashMap.get(intent.getComponent());
                intent.removeExtra(InternalConstant.STUB_BUNDLE_LOCATION);
                if (rec == null) {
                    return;
                }
                Object value = Class.forName("android.content.pm.PackageParser$Activity").getField("info").
                        get(rec);
                activityInfo.set(obj, value);
            }
        } catch (ClassNotFoundException th) {
            th.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


    }
    public static void storeTargetBundleIfNeed(Intent intent) {
        if (intent != null) {
            intent.removeExtra(InternalConstant.STUB_CHECKED);
            Bundle extras = intent.getExtras();
            if (extras != null && intent.getComponent() != null && intent.getComponent().getClassName().equals(InternalConstant.STUB_STUB_ACTIVITY)) {
                sTargetBundleStorage.offer(extras);
                intent.removeExtra(InternalConstant.STUB_DATA);
                intent.removeExtra(InternalConstant.STUB_RAW_COMPONENT);
                intent.removeExtra(InternalConstant.STUB_BUNDLE_LOCATION);
            }
        }
    }
    public static void storeReceiverTargetBundleIfNeed(Intent intent) {
        if (intent != null) {
            intent.removeExtra(InternalConstant.STUB_CHECKED);
            Bundle extras = intent.getExtras();
            if (extras != null && intent.getComponent() != null && intent.getComponent().getClassName().equals(InternalConstant.STUB_STUB_RECEIVER)) {
                sTargetReceiverBundleStorage.offer(extras);
                intent.removeExtra(InternalConstant.STUB_DATA);
                intent.removeExtra(InternalConstant.STUB_RAW_COMPONENT);
                intent.removeExtra(InternalConstant.STUB_BUNDLE_LOCATION);
            }
        }
    }
    public static boolean isNeedCheck(Intent intent) {
        if (intent == null) {
            return false;
        }
        if (!intent.getBooleanExtra(InternalConstant.STUB_CHECKED, false)) {
            return true;
        }
        if (intent.getStringExtra(InternalConstant.STUB_BUNDLE_LOCATION) == null) {
            return false;
        }
        intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), InternalConstant.STUB_STUB_ACTIVITY);
        return false;
    }
    public static boolean isNeedCheckReceiver(Intent intent) {
        if (intent == null) {
            return false;
        }
        if (!intent.getBooleanExtra(InternalConstant.STUB_CHECKED, false)) {
            return true;
        }
        if (intent.getStringExtra(InternalConstant.STUB_BUNDLE_LOCATION) == null) {
            return false;
        }
        intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), InternalConstant.STUB_STUB_RECEIVER);
        return false;
    }
    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param intent The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Intent intent) {
        List<ResolveInfo> list =
                RuntimeVariables.androidApplication.getPackageManager().queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}