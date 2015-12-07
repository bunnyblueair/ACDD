/*
 * ACDD Project
 * file OptDexProcess.java  is  part of ACCD
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

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import org.acdd.framework.ACDD;
import org.acdd.framework.ACDDConfig;
import org.acdd.framework.BundleImpl;
import org.acdd.framework.InternalConstant;
import org.acdd.framework.bundlestorage.BundleArchiveRevision.DexLoadException;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;

import org.acdd.runtime.RuntimeVariables;
import org.osgi.framework.Bundle;

import java.io.IOException;
import java.util.zip.ZipFile;

public class OptDexProcess {
	Logger log=LoggerFactory.getInstance("OptDexProcess");
    private static OptDexProcess instance;
    private Application mApplication;
    private boolean isInitialized;
    private boolean notifyInstalled;

    OptDexProcess() {
    }

    public static synchronized OptDexProcess getInstance() {
        synchronized (OptDexProcess.class) {
            if (instance == null) {
                instance = new OptDexProcess();
            }
         
        }
        return instance;
    }

    void init(Application application) {
        this.mApplication = application;
        this.isInitialized = true;
    }

    public synchronized void processPackages(boolean onlyOptAutos, boolean noNeedNotifyUI) {
       // onlyOptAutos=false;
        if (!this.isInitialized) {
            Log.e("OptDexProcess", "Bundle Installer not initialized yet, process abort!");
        } else if (!this.notifyInstalled || noNeedNotifyUI) {
            long currentTimeMillis;
            if (onlyOptAutos) {
                currentTimeMillis = System.currentTimeMillis();
                runOptDexAuto();
                if (!noNeedNotifyUI) {
                    finishInstalled();
                }
                log.debug("dexopt auto start bundles cost time = " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
                if (!RuntimeVariables.inSubProcess)
                {
                    runOptDexDelay();
                    log.debug("dexopt delay start bundles cost time = " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
                }

            } else {
                currentTimeMillis = System.currentTimeMillis();
                runOptDexNonDelay();
                log.debug("dexopt bundles not delayed cost time = " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
                if (!noNeedNotifyUI) {
                    finishInstalled();
                }
                currentTimeMillis = System.currentTimeMillis();
                getInstance().runOptDexDelay();
                log.debug("dexopt delayed bundles cost time = " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
            }
            if (!noNeedNotifyUI) {
                this.notifyInstalled = true;
            }
        }
    }

    private void finishInstalled() {
        Utils.saveInfoBySharedPreferences(this.mApplication);
        System.setProperty("BUNDLES_INSTALLED", "true");
        this.mApplication.sendBroadcast(new Intent(InternalConstant.ACTION_BROADCAST_BUNDLES_INSTALLED));
    }

    private void runOptDexNonDelay() {
        for (Bundle bundle : ACDD.getInstance().getBundles()) {
            if (!(bundle == null || contains(ACDDConfig.STORE, bundle.getLocation()))) {
                try {
                    ((BundleImpl) bundle).optDexFile();
                } catch (Throwable e) {
                    if (e instanceof DexLoadException) {
                        throw ((RuntimeException) e);
                    }
                    Log.e("OptDexProcess", "Error while dexopt >>>", e);
                }
            }
        }
    }

    private void runOptDexDelay()  {
        ZipFile zipFile= null;
        try {
            zipFile = new ZipFile(mApplication.getApplicationInfo().sourceDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String location : ACDDConfig.DELAY) {
            Bundle bundle = ACDD.getInstance().getBundle(location);
            if (bundle==null){
                bundle= BundlesInstaller.getInstance().preInstallBundle(zipFile,location,mApplication);
              //  bundle = ACDD.getInstance().getBundle(location);
            }
            if (bundle != null) {
                try {
                    ((BundleImpl) bundle).optDexFile();
                } catch (Throwable e) {
                    if (e instanceof DexLoadException) {
                        throw ((RuntimeException) e);
                    }
                    Log.e("OptDexProcess", "Error while dexopt >>>", e);
                }
            }else {

            }

        }
    }

    private void runOptDexAuto() {

        for (String location : ACDDConfig.AUTO) {
            Bundle mBundle = ACDD.getInstance().getBundle(location);
            if (mBundle != null) {
                try {
                    ((BundleImpl) mBundle).optDexFile();
                 //   Log.e("OptDexProcess", " dexopt >>>"+mBundle.getLocation());
                } catch (Throwable e) {
                    if (e instanceof DexLoadException) {
                        throw ((RuntimeException) e);
                    }
                    Log.e("OptDexProcess", "Error while dexopt >>>", e);
                }
            }
        }
    }

    private boolean contains(String[] bundles, String bundle) {
        if (bundles == null || bundle == null) {
            return false;
        }
        for (String tmp : bundles) {
            if (tmp != null && tmp.equals(bundle)) {
                return true;
            }
        }
        return false;
    }
}
