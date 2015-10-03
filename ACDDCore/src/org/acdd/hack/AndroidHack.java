/*
 * ACDD Project
 * file AndroidHack.java  is  part of ACCD
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
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import org.acdd.framework.ACDDConfig;
import org.acdd.framework.BundleImpl;
import org.acdd.framework.Framework;
import org.acdd.hack.Hack.HackDeclaration.HackAssertionException;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.runtime.ClassLoadFromBundle;
import org.acdd.runtime.DelegateClassLoader;
import org.acdd.runtime.DelegateComponent;
import org.acdd.runtime.DelegateResources;
import org.acdd.runtime.RuntimeVariables;
import org.acdd.runtime.stub.BundlePackageManager;
import org.osgi.framework.BundleException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/****
 * Hack Android ActivityThread
 ***/
public class AndroidHack {
    private static Object _mLoadedApk;
    private static Object _sActivityThread;
    public static final int LAUNCH_ACTIVITY         = 100;
    public static final int RECEIVER                = 113;
    public static final int CREATE_SERVICE          = 114;
    static Logger logger= LoggerFactory.getInstance("AndroidHack");

    static   void  checkActivityOnSubProcess(Object object){
        Field declaredField = null;
        try {
            Class cls = Class.forName("android.app.ActivityThread$ActivityClientRecord");
            declaredField = cls.getDeclaredField("intent");
            declaredField.setAccessible(true);
            Field activityInfo = cls.getDeclaredField("activityInfo");
            activityInfo.setAccessible(true);
            Intent intent = (Intent) declaredField.get(object);

            String mComponentName = intent.getComponent().getClassName();
            ClassLoadFromBundle.checkInstallBundleIfNeed(mComponentName);
            String    packageName = DelegateComponent.locateComponent(mComponentName);
            if (packageName != null) {
                BundleImpl bundleImpl = (BundleImpl) Framework.getBundle(packageName);
                if (bundleImpl != null) {
                    try {
                        bundleImpl.startBundle();
                    } catch (BundleException e) {
                        logger.error(e.getMessage() + " Caused by: ", e.getNestedException());
                    }
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
     static void checkReceiverOnSubProcess(Object obj) {
        logger.debug("checkReceiverOnSubProcess");
        try {
            Class cls = Class.forName("android.app.ActivityThread$ReceiverData");
            Field declaredField = cls.getDeclaredField("intent");
            declaredField.setAccessible(true);
            Intent intent = (Intent) declaredField.get(obj);
          String mComponentName=  intent.getComponent().getClassName();
            ClassLoadFromBundle.checkInstallBundleIfNeed(mComponentName);
            String    packageName = DelegateComponent.locateComponent(mComponentName);
            if (packageName != null) {
                BundleImpl bundleImpl = (BundleImpl) Framework.getBundle(packageName);
                if (bundleImpl != null) {
                    try {
                        bundleImpl.startBundle();
                    } catch (BundleException e) {
                        logger.error(e.getMessage() + " Caused by: ", e.getNestedException());
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    static   void  checkServiceOnSubProcess(Object object){
        Field infoField = null;
        ServiceInfo info=null;
        try {
            infoField = object.getClass().getDeclaredField("info");
            infoField.setAccessible(true);
             info = (ServiceInfo) infoField.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String mComponentName = info.name;
        ClassLoadFromBundle.checkInstallBundleIfNeed(mComponentName);
        String    packageName = DelegateComponent.locateComponent(mComponentName);
        if (packageName != null) {
            BundleImpl bundleImpl = (BundleImpl) Framework.getBundle(packageName);
            if (bundleImpl != null) {
                try {
                    bundleImpl.startBundle();
                } catch (BundleException e) {
                    logger.error(e.getMessage() + " Caused by: ", e.getNestedException());
                }
            }
        }
    }
    static final class HandlerHack implements Callback {
        final Object activityThread;
        final Handler handler;

        HandlerHack(Handler handler, Object obj) {
            this.handler = handler;
            this.activityThread = obj;
        }

        @Override
        public boolean handleMessage(Message message) {
            try {
                AndroidHack.ensureLoadedApk();
                if (RuntimeVariables.inSubProcess&& ACDDConfig.subProcessEnable){
                    if (message.what == CREATE_SERVICE ) {
                        checkServiceOnSubProcess(message.obj);
                    } else if (message.what == LAUNCH_ACTIVITY ) {
                        checkActivityOnSubProcess(message.obj);
                    }else if (message.what==RECEIVER){
                        checkReceiverOnSubProcess(message.obj);
                    }
                }
                if (ACDDConfig.stubModeEnable) {
                    if (message.what == LAUNCH_ACTIVITY) {//activity
                        BundlePackageManager.processActivityIntentIfNeed(message.obj);
                    } else if (message.what == RECEIVER) {

                        BundlePackageManager.processReceiverIntentIfNeed(message.obj);
                    }
                }


                this.handler.handleMessage(message);
                AndroidHack.ensureLoadedApk();
            } catch (Throwable th) {
                th.printStackTrace();

                if ((th instanceof ClassNotFoundException)
                        || th.toString().contains("ClassNotFoundException")) {
                    if (message.what != RECEIVER) {
                        Object loadedApk = AndroidHack.getLoadedApk(
                                RuntimeVariables.androidApplication,
                                this.activityThread,
                                RuntimeVariables.androidApplication
                                        .getPackageName());
                        if (loadedApk == null) {
                            logger.error("",new RuntimeException("loadedapk is null"));
                        } else {
                            ClassLoader classLoader = ACDDHacks.LoadedApk_mClassLoader.get(loadedApk);
                            if (classLoader instanceof DelegateClassLoader) {
                                logger.error("",new RuntimeException("From ACDD:classNotFound ---", th));

                            } else {
                                logger.error("",new RuntimeException("wrong classloader in loadedapk---" + classLoader.getClass().getName(), th));

                            }
                        }
                    }
                } else if ((th instanceof ClassCastException)
                        || th.toString().contains("ClassCastException")) {
                    Process.killProcess(Process.myPid());
                } else {
                    logger.error("", new RuntimeException(th));
                }
            }
            return true;
        }
    }

    static class ActvityThreadGetter implements Runnable {
        ActvityThreadGetter() {
        }

        @Override
        public void run() {
            try {
                AndroidHack._sActivityThread = ACDDHacks.ActivityThread_currentActivityThread
                        .invoke(ACDDHacks.ActivityThread.getmClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (ACDDHacks.ActivityThread_currentActivityThread) {
                ACDDHacks.ActivityThread_currentActivityThread.notify();
            }
        }
    }

    static {
        _sActivityThread = null;
        _mLoadedApk = null;
    }

    public static Object getActivityThread() throws Exception {
        if (_sActivityThread == null) {
            if (Thread.currentThread().getId() == Looper.getMainLooper()
                    .getThread().getId()) {
                _sActivityThread = ACDDHacks.ActivityThread_currentActivityThread
                        .invoke(null);
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                synchronized (ACDDHacks.ActivityThread_currentActivityThread) {
                    handler.post(new ActvityThreadGetter());
                    ACDDHacks.ActivityThread_currentActivityThread.wait();
                }
            }
        }
        return _sActivityThread;
    }

    /**
     * we  nedd hook H(handler),hanlde message
     ***/
    public static Handler hackH() throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception(
                    "Failed to get ActivityThread.sCurrentActivityThread");
        }
        try {
            Handler handler = (Handler) ACDDHacks.ActivityThread
                    .field("mH")
                    .ofType(Hack.into("android.app.ActivityThread$H")
                            .getmClass()).get(activityThread);
            Field declaredField = Handler.class.getDeclaredField("mCallback");
            declaredField.setAccessible(true);
            declaredField.set(handler, new HandlerHack(handler,
                    activityThread));
        } catch (HackAssertionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void ensureLoadedApk() throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception(
                    "Failed to get ActivityThread.sCurrentActivityThread");
        }
        Object loadedApk = getLoadedApk(RuntimeVariables.androidApplication,
                activityThread,
                RuntimeVariables.androidApplication.getPackageName());
        if (loadedApk == null) {
            loadedApk = createNewLoadedApk(RuntimeVariables.androidApplication,
                    activityThread);
            if (loadedApk == null) {
                throw new RuntimeException("can't create loadedApk");
            }
        }
        activityThread = loadedApk;
        if (!((ACDDHacks.LoadedApk_mClassLoader
                .get(activityThread)) instanceof DelegateClassLoader)) {
            ACDDHacks.LoadedApk_mClassLoader.set(activityThread,
                    RuntimeVariables.delegateClassLoader);
            ACDDHacks.LoadedApk_mResources.set(activityThread,
                    RuntimeVariables.delegateResources);
        }
    }

    public static Object getLoadedApk(Application application, Object obj,
                                      String str) {
        WeakReference weakReference = (WeakReference) ((Map) ACDDHacks.ActivityThread_mPackages
                .get(obj)).get(str);
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        _mLoadedApk = weakReference.get();
        return _mLoadedApk;
    }

    public static Object createNewLoadedApk(Application application, Object obj) {
        try {
            Method declaredMethod;
            ApplicationInfo applicationInfo = application.getPackageManager()
                    .getApplicationInfo(application.getPackageName(), 1152);
            application.getPackageManager();
            Resources resources = application.getResources();
            if (resources instanceof DelegateResources) {
                declaredMethod = resources
                        .getClass()
                        .getSuperclass()
                        .getDeclaredMethod("getCompatibilityInfo");
            } else {
                declaredMethod = resources.getClass().getDeclaredMethod(
                        "getCompatibilityInfo");
            }
            declaredMethod.setAccessible(true);
            Class cls = Class.forName("android.content.res.CompatibilityInfo");
            Object invoke = declaredMethod.invoke(application.getResources()
            );
            Method declaredMethod2 = ACDDHacks.ActivityThread.getmClass()
                    .getDeclaredMethod("getPackageInfoNoCheck",
                            ApplicationInfo.class, cls);
            declaredMethod2.setAccessible(true);
            invoke = declaredMethod2.invoke(obj, applicationInfo, invoke);
            _mLoadedApk = invoke;
            return invoke;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
/**
 * inject  system  classloader,we need handle  load class from  bundle
 * @param packageName  package name
 * @param classLoader    delegate  classloader
 * ***/
    public static void injectClassLoader(String packageName, ClassLoader classLoader)
            throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception(
                    "Failed to get ActivityThread.sCurrentActivityThread");
        }
        Object loadedApk = getLoadedApk(RuntimeVariables.androidApplication,
                activityThread, packageName);
        if (loadedApk == null) {
            loadedApk = createNewLoadedApk(RuntimeVariables.androidApplication,
                    activityThread);
        }
        if (loadedApk == null) {
            throw new Exception("Failed to get ActivityThread.mLoadedApk");
        }
        ACDDHacks.LoadedApk_mClassLoader.set(loadedApk, classLoader);
    }

    public static void injectApplication(String packageName, Application application)
            throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception(
                    "Failed to get ActivityThread.sCurrentActivityThread");
        }
        Object loadedApk = getLoadedApk(application, activityThread,
                application.getPackageName());
        if (loadedApk == null) {
            throw new Exception("Failed to get ActivityThread.mLoadedApk");
        }
        ACDDHacks.LoadedApk_mApplication.set(loadedApk, application);
        ACDDHacks.ActivityThread_mInitialApplication.set(activityThread,
                application);
    }

    /***
     * hack Resource  use delegate resource,process  resource in bundle
     *
     * @param application host application object
     * @param resources   delegate resource
     *****/
    public static void injectResources(Application application,
                                       Resources resources) throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception(
                    "Failed to get ActivityThread.sCurrentActivityThread");
        }
        Object loadedApk = getLoadedApk(application, activityThread,
                application.getPackageName());
        if (loadedApk == null) {
            activityThread = createNewLoadedApk(application, activityThread);
            if (activityThread == null) {
                throw new RuntimeException(
                        "Failed to get ActivityThread.mLoadedApk");
            }
            if (!((ACDDHacks.LoadedApk_mClassLoader
                    .get(activityThread)) instanceof DelegateClassLoader)) {
                ACDDHacks.LoadedApk_mClassLoader.set(activityThread,
                        RuntimeVariables.delegateClassLoader);
            }
            loadedApk = activityThread;
        }
        ACDDHacks.LoadedApk_mResources.set(loadedApk, resources);
        ACDDHacks.ContextImpl_mResources.set(application.getBaseContext(),
                resources);
        ACDDHacks.ContextImpl_mTheme.set(application.getBaseContext(), null);
    }

    /***
     * get Instrumentation,should be  hacked Instrumentation
     */
    public static Instrumentation getInstrumentation() throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread != null) {
            return ACDDHacks.ActivityThread_mInstrumentation
                    .get(activityThread);
        }
        throw new Exception(
                "Failed to get ActivityThread.sCurrentActivityThread");
    }

    /***
     * hack Instrumentation,we replace Instrumentation used HackInstrumentation<br>
     * such start activity in Instrumentation ,before this ,we need verify  target class is loaded or
     * load  target class,and so on
     **/
    public static void injectInstrumentationHook(Instrumentation instrumentation)
            throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception(
                    "Failed to get ActivityThread.sCurrentActivityThread");
        }
        ACDDHacks.ActivityThread_mInstrumentation.set(activityThread,
                instrumentation);
    }

    @SuppressWarnings("unused")
    public static void injectContextHook(ContextWrapper contextWrapper,
                                         ContextWrapper contextWrapperValue) {
        ACDDHacks.ContextWrapper_mBase.set(contextWrapper, contextWrapperValue);
    }
}
