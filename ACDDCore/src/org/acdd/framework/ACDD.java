/*
 * ACDD Project
 * file ACDD.java  is  part of ACCD
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

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;

import org.acdd.hack.ACDDHacks;
import org.acdd.hack.AndroidHack;
import org.acdd.log.ACDDLog;
import org.acdd.log.ILog;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.runtime.BundleLifecycleHandler;
import org.acdd.runtime.ClassLoadFromBundle;
import org.acdd.runtime.ClassNotFoundInterceptorCallback;
import org.acdd.runtime.DelegateClassLoader;
import org.acdd.runtime.DelegateComponent;
import org.acdd.runtime.DelegateResources;
import org.acdd.runtime.FrameworkLifecycleHandler;
import org.acdd.runtime.InstrumentationHook;
import org.acdd.runtime.PackageLite;
import org.acdd.runtime.RuntimeVariables;
import org.acdd.runtime.stub.BundlePackageManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * ACDD  framework controller
 * app upgrade {@link ACDD#updateBundle(String , InputStream ) updateBundle} or
 * {@link ACDD#updateBundle(String ,File  ) updateBundle},
 * ,app install see {@link ACDD#installBundle(String location, InputStream inputStream)}
 * or {@link #installBundle(String , File ) installBundle},
 * app uninstall {@link #uninstallBundle(String)},
 * bundle  is plugin mapping,location is bundle's package name.
 **/
public class ACDD {
    protected static ACDD instance;
    static final Logger log;
    private BundleLifecycleHandler bundleLifecycleHandler;
    private FrameworkLifecycleHandler frameworkLifecycleHandler;

    static {
        log = LoggerFactory.getInstance("ACDD");
    }


    public static ACDD getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (ACDD.class) {
            if (instance == null) {
                instance = new ACDD();
            }

        }
        return instance;
    }

    /***
     * acdd framework  init injection ,include ClassLoader,Resource,android Instrumentation
     * ,more info see  {@link ACDDHacks ACDDHacks} and {@link AndroidHack AndroidHack}
     *****/
    public void init(Application application)
            throws Exception {
        String packageName = application.getPackageName();
        ACDDHacks.defineAndVerify();
        ClassLoader classLoader = ACDD.class.getClassLoader();
        DelegateClassLoader delegateClassLoader = new DelegateClassLoader(classLoader);
        Framework.systemClassLoader = classLoader;
        RuntimeVariables.delegateClassLoader = delegateClassLoader;
        RuntimeVariables.delegateResources = initResources(application);
        RuntimeVariables.androidApplication = application;
        AndroidHack.injectClassLoader(packageName, delegateClassLoader);
        AndroidHack.injectInstrumentationHook(new InstrumentationHook(AndroidHack
                .getInstrumentation(), application.getBaseContext()));
        injectApplication(application, packageName);
        this.bundleLifecycleHandler = new BundleLifecycleHandler();
        Framework.syncBundleListeners.add(this.bundleLifecycleHandler);
        this.frameworkLifecycleHandler = new FrameworkLifecycleHandler();
        Framework.frameworkListeners.add(this.frameworkLifecycleHandler);
        AndroidHack.hackH();
        // RuntimeVariables.inSubProcess = !application.getPackageName().equals(RuntimeVariables.);
        // Framework.initialize(properties);
    }

    /**
     * @since 1.0.0
     **/
    private Resources initResources(Application application) throws Exception {
        Resources resources = application.getResources();
        if (resources != null) {
            return resources;
        }
        log.error(" !!! Failed to get init resources.");
        return application.getPackageManager().getResourcesForApplication(application.getApplicationInfo());
    }

    /***
     * inject ActivityThread for acdd
     *
     * @param application app context
     * @param packageName app packagename
     **/
    private void injectApplication(Application application, String packageName)
            throws Exception {
        ACDDHacks.defineAndVerify();
        AndroidHack.injectApplication(packageName, application);
    }

    /***
     * acdd startup when this method called
     *
     * @param properties framework properties
     **/
    public void startup(Properties properties) throws BundleException {
        Framework.startup(properties);
    }

//    public void startup() throws BundleException {
//        Framework.startup();
//    }

    public void shutdown() throws BundleException {
        Framework.shutdown(false);
    }

    /****
     * get specific   bundle impl,if bundle not installed,you will got null
     *
     * @param pkgName bundle package name
     * @return {@link BundleImpl BundleImpl}
     ***/
    public Bundle getBundle(String pkgName) {
        return Framework.getBundle(pkgName);
    }

    /****
     * get specific   bundle impl on demand,if bundle not install,ACDD will try install budnle and dependency
     *
     * @param pkgName bundle package name
     * @return {@link BundleImpl BundleImpl}
     ***/
    public Bundle getBundleOnDemand(String pkgName) {
        if (pkgName == null || pkgName.length() == 0) {
            return null;
        }
        if (Framework.getBundle(pkgName) == null) {
            ClassLoadFromBundle.checkInstallBundleAndDependency(pkgName);
        }
        return Framework.getBundle(pkgName);
    }

    /*****
     * install new bundle ,if succeed,you will got {@link BundleImpl BundleImpl}
     *
     * @param location    notice!,this param is your target bundle package name,must be right
     * @param inputStream bundle  archive's inputstream
     * @return {@link BundleImpl BundleImpl}
     **/
    public Bundle installBundle(String location, InputStream inputStream)
            throws BundleException {
        return Framework.installNewBundle(location, inputStream);
    }

    /*****
     * install new bundle ,if succeed,you will got {@link BundleImpl BundleImpl}
     *
     * @param location notice!,this param is your target bundle package name,must be right
     * @param apkFile  bundle  archive file
     * @return {@link BundleImpl BundleImpl}
     **/
    public Bundle installBundle(String location, File apkFile) throws BundleException {
        return Framework.installNewBundle(location, apkFile);
    }

    /**
     * this method just use for  ACDD internal,use for  pre-install  bundle
     * ,just cache file,and opt,speed up  for  bundle uninstalled
     *
     * @param location    notice!,this param is your target bundle package name,must be right
     * @param inputStream bundle  archive's inputstream
     * @return {@link BundleImpl BundleImpl}
     ***/
    public Bundle preInstallBundle(String location, InputStream inputStream)
            throws BundleException {
        return Framework.preInstallNewBundle(location, inputStream);
    }

    /**
     * this method just use for  ACDD internal,use for  pre-install  bundle
     * ,just cache file,and opt,speed up  for  bundle uninstalled
     *
     * @param location bundle package name
     * @param apkFile
     * @return {@link BundleImpl BundleImpl}
     * @throws BundleException
     */

    public Bundle preInstallBundle(String location, File apkFile) throws BundleException {
        return Framework.preInstallNewBundle(location, apkFile);
    }

    /**
     * update bundle,when app  restart,update will change
     *
     * @param location    bundle's package name
     * @param inputStream bundle archive stream
     * @throws BundleException
     */
    public void updateBundle(String location, InputStream inputStream)
            throws BundleException {
        Bundle bundle = Framework.getBundle(location);
        if (bundle != null) {
            bundle.update(inputStream);
            return;
        }
        throw new BundleException("Could not update bundle " + location
                + ", because could not find it");
    }

    /**
     * update bundle,when app  restart,update will change
     *
     * @param pkgName     bundle's package name
     * @param mBundleFile bundle archive file
     * @throws BundleException
     */
    public void updateBundle(String pkgName, File mBundleFile) throws BundleException {
        if (!mBundleFile.exists()) {
            throw new BundleException("file not  found" + mBundleFile.getAbsolutePath());
        }
        Bundle bundle = Framework.getBundle(pkgName);
        if (bundle != null) {
            bundle.update(mBundleFile);
            return;
        }
        throw new BundleException("Could not update bundle " + pkgName
                + ", because could not find it");
    }

    /**
     * update bundle  whitout reboot app
     * Notice:this method is Experience!!,maybe  not stabe,don't use is product better
     *
     * @param location    bundle package name
     * @param mBundleFile bundle  archive file
     * @throws BundleException if error occurred,will throw BundleException
     */
    public void updateBundleForce(String location, File mBundleFile) throws BundleException {
        if (!mBundleFile.exists()) {
            throw new BundleException("file not  found" + mBundleFile.getAbsolutePath());
        }
        BundleImpl bundle = (BundleImpl) Framework.getBundle(location);
        if (bundle != null) {
            bundle.update(mBundleFile);
            bundle.refresh();
            try {
                DelegateResources.newDelegateResources(RuntimeVariables.androidApplication, RuntimeVariables.delegateResources, bundle.getArchive().getArchiveFile().getAbsolutePath());
            } catch (Exception e) {
            }
            return;
        }
        throw new BundleException("Could not update bundle " + location
                + ", because could not find it");
    }

    public boolean restoreBundle(String[] packageNames) {

        return Framework.restoreBundle(packageNames);
    }

    @Deprecated
    public void installOrUpdate(String[] packageNames, File[] bundleFiles)
            throws BundleException {
        Framework.installOrUpdate(packageNames, bundleFiles);
    }

    /**
     * uninstall specific bundle
     *
     * @param pkgName bundle package name you wanna uninstall
     * @throws BundleException
     */
    public void uninstallBundle(String pkgName) throws BundleException {
        Bundle bundle = Framework.getBundle(pkgName);
        if (bundle != null) {
            BundleImpl bundleImpl = (BundleImpl) bundle;
            try {
                File archiveFile = bundleImpl.getArchive().getArchiveFile();
                if (archiveFile.canWrite()) {
                    archiveFile.delete();
                }
                bundleImpl.getArchive().purge();
                File revisionDir = bundleImpl.getArchive().getCurrentRevision()
                        .getRevisionDir();
                bundle.uninstall();
                if (revisionDir != null) {
                    Framework.deleteDirectory(revisionDir);
                    return;
                }
                return;
            } catch (Exception e) {
                log.error("uninstall bundle error: " + pkgName + e.getMessage());
                return;
            }
        }
        throw new BundleException("Could not uninstall bundle " + pkgName + ", because could not find it");
    }

    /**
     * get installed  bundle list
     * @return all bundle  installed to ACDD
     */
    public List<Bundle> getBundles() {
        return Framework.getBundles();
    }

    public Resources getDelegateResources() {
        return RuntimeVariables.delegateResources;
    }

    public ClassLoader getDelegateClassLoader() {
        return RuntimeVariables.delegateClassLoader;
    }

    public Class<?> getComponentClass(String pkgName) throws ClassNotFoundException {
        return RuntimeVariables.delegateClassLoader.loadClass(pkgName);
    }

    /**
     * get specific BundleClassLoader
     * @param location  bundle package name
     * @return ClassLoader if bundle installed,you'll got ClassLoader,or null
     */
    public ClassLoader getBundleClassLoader(String location) {
        Bundle bundle = Framework.getBundle(location);
        if (bundle != null) {
            return ((BundleImpl) bundle).getClassLoader();
        }
        return null;
    }
    @Deprecated
    public PackageLite getBundlePackageLite(String pkgName) {
        return DelegateComponent.getPackage(pkgName);
    }

    /**
     * get specific bundle  archive file
     * @param location bundle package name
     * @return bundle archive file or null
     */
    public File getBundleFile(String location) {
        Bundle bundle = Framework.getBundle(location);
        if (bundle != null) {
            return ((BundleImpl) bundle).archive.getArchiveFile();
        }
        return null;
    }

    public InputStream openAssetInputStream(String packageName, String assetName)
            throws IOException {
        Bundle bundle = Framework.getBundle(packageName);
        if (bundle != null) {
            return ((BundleImpl) bundle).archive.openAssetInputStream(assetName);
        }
        return null;
    }

    public InputStream openNonAssetInputStream(String packageName, String assetName)
            throws IOException {
        Bundle bundle = Framework.getBundle(packageName);
        if (bundle != null) {
            return ((BundleImpl) bundle).archive.openNonAssetInputStream(assetName);
        }
        return null;
    }

    public void addFrameworkListener(FrameworkListener frameworkListener) {
        Framework.addFrameworkListener(frameworkListener);
    }

    public void removeFrameworkListener(FrameworkListener frameworkListener) {
        Framework.removeFrameworkListener(frameworkListener);
    }

    public void addBundleListener(BundleListener bundleListener) {
        Framework.addBundleListener(bundleListener);
    }

    public void removeBundleListener(BundleListener bundleListener) {
        Framework.removeBundleListener(bundleListener);
    }

    public void onLowMemory() {
        this.bundleLifecycleHandler.handleLowMemory();
    }

    public void enableComponent(String componentName) {
        PackageLite packageLite = DelegateComponent.getPackage(componentName);
        if (packageLite != null && packageLite.disableComponents != null) {
            for (String disableComponent : packageLite.disableComponents) {
                PackageManager packageManager = RuntimeVariables.androidApplication
                        .getPackageManager();
                ComponentName componentName2 = new ComponentName(
                        RuntimeVariables.androidApplication.getPackageName(),
                        disableComponent);
                try {
                    packageManager.setComponentEnabledSetting(componentName2, 1,
                            1);
                    log.debug("enableComponent: "
                            + componentName2.getClassName());
                } catch (Exception e) {
                    log.error("enableComponent error: "
                            + componentName2.getClassName() + e.getMessage());
                }
            }
        }
    }

    public void setLogger(ILog iLog) {
        ACDDLog.setExternalLogger(iLog);
    }


    public void setClassNotFoundInterceptorCallback(
            ClassNotFoundInterceptorCallback classNotFoundInterceptorCallback) {
        Framework.setClassNotFoundCallback(classNotFoundInterceptorCallback);
    }

    //start stub mode
    public List<ResolveInfo> queryNewIntentActivities(Intent intent, String str, int flags, int userid) {

        return BundlePackageManager.queryIntentActivities(intent, str, flags, userid);
    }

    public List<ResolveInfo> queryNewIntentServices(Intent intent, String str, int flags, int userid) {

        return BundlePackageManager.queryIntentService(intent, str, flags, userid);
    }

    public ActivityInfo getNewActivityInfo(ComponentName componentName, int flags) {

        return BundlePackageManager.getNewActivityInfo(componentName, flags);
    }

    public ServiceInfo getNewServiceInfo(ComponentName componentName, int flags) {

        return BundlePackageManager.getNewServiceInfo(componentName, flags);
    }
    //end stub mode
}
