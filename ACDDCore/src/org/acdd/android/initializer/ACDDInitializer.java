/*
 * ACDD Project
 * file ACDDInitializer.java  is  part of ACCD
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
package org.acdd.android.initializer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;

import org.acdd.android.task.Coordinator;
import org.acdd.android.task.Coordinator.TaggedRunnable;
import org.acdd.bundleInfo.BundleInfoList;
import org.acdd.framework.ACDD;
import org.acdd.framework.InternalConstant;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.runtime.Globals;
import org.acdd.runtime.RuntimeVariables;
import org.acdd.util.ApkUtils;
import org.acdd.util.ProcessUtil;

import java.util.Properties;

public class ACDDInitializer {
	Logger log=LoggerFactory.getInstance("ACDDInitializer");
    private static long initStartTime = 0;
    private static boolean inTargetApp;
    private Application mApplication;
    private String mPackageName;
    private BundleDebug mDebug;
    private boolean tryInstall;

    private Properties mProperties = new Properties();
    private boolean isUpdate = false;


    public ACDDInitializer(Application application, String packagename) {
        this.mApplication = application;
        this.mPackageName = packagename;


        if (application.getPackageName().equals(packagename)) {
            inTargetApp = true;
        }
    }

    public void init() {
    
        initStartTime = System.currentTimeMillis();

        try {
            ACDD.getInstance().init(this.mApplication);
            log.debug("ACDD framework inited end " + this.mPackageName + " " + (System.currentTimeMillis() - initStartTime) + " ms");
        } catch (Throwable e) {
            Log.e("ACDDInitializer", "Could not init ACDD framework !!!", e);
            throw new RuntimeException("ACDD initialization fail" + e.getMessage());
        }

            RuntimeVariables.currentProcessName= ProcessUtil.getCurrentProcessName();
            RuntimeVariables.inSubProcess=  !RuntimeVariables.currentProcessName.equals(mPackageName);

        isUpdate=isUpdate();
    }

    public void startUp() {
        if (RuntimeVariables.inSubProcess){
            this.isUpdate=false;
        }
        this.mProperties.put(InternalConstant.BOOT_ACTIVITY, InternalConstant.BOOT_ACTIVITY);
        this.mProperties.put(InternalConstant.COM_ACDD_DEBUG_BUNDLES, "true");
        this.mProperties.put(InternalConstant.ACDD_APP_DIRECTORY, this.mApplication.getFilesDir().getParent());

        try {

            Globals.init(this.mApplication, ACDD.getInstance().getDelegateClassLoader());
            this.mDebug = new BundleDebug();
            if (this.mApplication.getPackageName().equals(this.mPackageName)) {
                if (!( verifyRuntime() || !ApkUtils.isRootSystem())) {
                    this.mProperties.put(InternalConstant.ACDD_PUBLIC_KEY, SecurityBundleListner.PUBLIC_KEY);
                    ACDD.getInstance().addBundleListener(new SecurityBundleListner());
                }

                if (this.isUpdate || this.mDebug.isDebugable()) {
                    this.mProperties.put("osgi.init", "true");
                }
            }
           BundlesInstaller mBundlesInstaller = BundlesInstaller.getInstance();
            OptDexProcess mOptDexProcess = OptDexProcess.getInstance();
            if (this.mApplication.getPackageName().equals(this.mPackageName) && (this.isUpdate || this.mDebug.isDebugable())) {
            	mBundlesInstaller.init(this.mApplication,  this.mDebug, inTargetApp);
                mOptDexProcess.init(this.mApplication);
            }
            log.debug("ACDD framework prepare starting in process " + this.mPackageName + " " + (System.currentTimeMillis() - initStartTime) + " ms");
            ACDD.getInstance().setClassNotFoundInterceptorCallback(new ClassNotFoundInterceptor());
            if (InstallPolicy.install_when_findclass && BundleInfoList.getInstance().getBundles()==null) {
            	InstallPolicy.install_when_oncreate = true;
                this.tryInstall = true;
            }

            try {
                ACDD.getInstance().startup(this.mProperties);
                if (RuntimeVariables.inSubProcess) { //if application in  sub-process,skip reinsall plugins,just restore  profile
                    Utils.notifyBundleInstalled(mApplication);
                } else {
                    installBundles(mBundlesInstaller, mOptDexProcess);
                }
                log.debug("ACDD framework end startUp in process " + this.mPackageName + " " + (System.currentTimeMillis() - initStartTime) + " ms");
            } catch (Throwable e) {
                Log.e("ACDDInitializer", "Could not start up ACDD framework !!!", e);
                throw new RuntimeException(e);
            }
        } catch (Throwable e) {
            throw new RuntimeException("Could not set Globals !!!", e);
        }
    }

    private void installBundles(final BundlesInstaller mBundlesInstaller, final OptDexProcess mOptDexProcess) {

        if (this.mDebug.isDebugable()) {
        	InstallPolicy.install_when_oncreate = true;
        }
        if (this.mApplication.getPackageName().equals(this.mPackageName)) {
            if (InstallPolicy.install_when_oncreate) {

            }
            if (this.isUpdate || this.mDebug.isDebugable()) {
                if (InstallPolicy.install_when_oncreate) {
                    Coordinator.postTask(new  TaggedRunnable("ACDDInitializer") {
						@Override
						public void run() {
							mBundlesInstaller.process(true, false);
							mOptDexProcess.processPackages(true, false);
							
						}
					});

                    return;
                }
                Utils.notifyBundleInstalled(mApplication);
                Utils.updatePackageVersion(this.mApplication);
                Utils.saveInfoBySharedPreferences(this.mApplication);
            } else if (!this.isUpdate) {
                if (this.tryInstall) {
                    Coordinator.postTask(new TaggedRunnable("ACDDInitializer") {
						@Override
						public void run() {
							mBundlesInstaller.process(false, false);
							mOptDexProcess.processPackages(false, false);
						}
					});
                    return;
                }else{
                     Utils.notifyBundleInstalled(mApplication);
                }
            }
        }
    }



    @SuppressLint({"DefaultLocale"})
    private boolean verifyRuntime() {
    
        if ((Build.BRAND == null || !Build.BRAND.toLowerCase().contains("xiaomi") || Build.HARDWARE == null || !Build.HARDWARE.toLowerCase().contains("mt65")) && VERSION.SDK_INT >= 14) {
            return false;
        }
        return true;
    }

    private boolean isUpdate() {
        if (RuntimeVariables.inSubProcess){
            return  false;
        }

        try {

            PackageInfo packageInfo = mApplication.getPackageManager().getPackageInfo(mApplication.getPackageName(), 0);
            SharedPreferences sharedPreferences =mApplication.getSharedPreferences(InternalConstant.ACDD_CONFIGURE, 0);
            int last_version_code = sharedPreferences.getInt("last_version_code", 0);
            CharSequence last_version_name = sharedPreferences.getString("last_version_name", "");
            boolean isUpdate= packageInfo.versionCode > last_version_code || ((packageInfo.versionCode == last_version_code && !TextUtils.equals(Globals.getInstalledVersionName(), last_version_name)) );
            return  isUpdate;
        } catch (Throwable e) {
            Log.e("ACDDInitializer", "Error to get PackageInfo >>>", e);
            throw new RuntimeException(e);
        }
    }
}
