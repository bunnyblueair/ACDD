/*
 * ACDD Project
 * file DelegateComponent.java  is  part of ACCD
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
package org.acdd.runtime;

import android.app.Application;

import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/***Component Delegate,Bundle Component Manger*******/
public class DelegateComponent {
    static Map<String, Application> apkApplications;
    private static Map<String, PackageLite> apkPackages;
    static final Logger log;

    static {
        log = LoggerFactory.getInstance("DelegateComponent");
        apkPackages = new ConcurrentHashMap<String, PackageLite>();
        apkApplications = new HashMap<String, Application>();
    }

    /**
     * get package info of bundle from package cache,maybe  null
     * @param mLocation bundle name
     * ******/
    public static PackageLite getPackage(String mLocation) {
        return apkPackages.get(mLocation);
    }

    /**
     * add new bundle to DelegateComponent
     *
     * @param mLocation   new bundle name
     * @param packageLite the  package info  of new bundle
     *****/
    public static void putPackage(String mLocation, PackageLite packageLite) {
        apkPackages.put(mLocation, packageLite);
    }

    /******
     * remove bundle from DelegateComponent
     *
     * @param mLocation bundle name
     *****/
    public static void removePackage(String mLocation) {
        apkPackages.remove(mLocation);
    }

    /****
     * verify  component install status
     *
     * @param mComponent component name
     * @return is installed,return  the package name of   component
     *****/
    public static String locateComponent(String mComponent) {
        for (Entry<String, PackageLite> entry : apkPackages.entrySet()) {
            if (entry.getValue().components.contains(mComponent)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
