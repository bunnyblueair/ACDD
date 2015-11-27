/*
 * ACDD Project
 * file ComponentIntentResolver.java  is  part of ACCD
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

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;

import org.acdd.hack.ACDDHacks;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by BunnyBlue on 9/19/15.
 */
public class ComponentIntentResolver extends IntentResolver<IntentFilter, ResolveInfo> {
    protected final HashMap<ComponentName, Object> componentHashMap = new HashMap();
    protected final HashMap<String, ProviderInfo> providerInfoHashMap = new HashMap();
    private int flags;
    static Logger logger= LoggerFactory.getInstance(ComponentIntentResolver.class);
    public Collection<ProviderInfo> getProviders(){
        Collection<ProviderInfo> collection=new ArrayList<ProviderInfo>(providerInfoHashMap.size());
        Iterator iter = providerInfoHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            ProviderInfo mProviderInfo = (ProviderInfo) entry.getValue();
            collection.add(mProviderInfo);
        }
        return  collection;
    }
    public Collection<ProviderInfo> getProviders(String auth){

        Iterator iter = providerInfoHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            if (key.equals(auth))
            {
                Collection<ProviderInfo> collection=new ArrayList<ProviderInfo>(1);
                collection.add((ProviderInfo) entry.getValue());
                return   collection;
            }

        }
        return  null;
    }
    public ComponentIntentResolver() {

    }

    public List queryIntent(Intent intent, String resolvedType, boolean defaultOnly) {
        this.flags = defaultOnly ? 0xffff : 0;
        return super.queryIntent(intent, resolvedType, defaultOnly);
    }

    @Override
    protected boolean isPackageForFilter(String packageName, IntentFilter filter) {
        return true;
    }

    @Override
    protected IntentFilter[] newArray(int size) {
        return new IntentFilter[size];
    }

    public List<?> queryIntent(Intent intent, String resolvedType, int flags) {
        return super.queryIntent(intent, resolvedType, (0xffff & flags) != 0);
    }

    public final void addComponent(Object obj) {
        ComponentName componentName;

        try {
            componentName = (ComponentName) ACDDHacks.PackageParser$Component_getComponentName.invoke(obj, new Object[0]);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            componentName = null;
        }
        if (componentName != null) {
          //  Log.e("ComponentIntentResolveX", componentName.getPackageName() + "/" + componentName.getClassName());
            this.componentHashMap.put(componentName, obj);
        }
        ArrayList arrayList = (ArrayList) ACDDHacks.PackageParser$Activity_intents.get(obj);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
           // Log.e("==debug=",arrayList.get(i).toString());
            addFilter((IntentFilter) arrayList.get(i));
        }
    }
    public final void addProvider(String auth,ProviderInfo obj) {


        providerInfoHashMap.put(auth,obj);
    }
    public final void removeComponent(Object obj) {
        ComponentName componentName=null;
        ArrayList arrayList;
        int size;
        int i;

        if (Build.VERSION.SDK_INT >= 8) {
            try {
                componentName = (ComponentName) ACDDHacks.PackageParser$Component_getComponentName.invoke(obj, new Object[0]);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            this.componentHashMap.remove(componentName);
            arrayList = (ArrayList) ACDDHacks.PackageParser$Activity_intents.get(obj);
            size = arrayList.size();
            for (i = 0; i < size; i++) {
                removeFilter((IntentFilter) arrayList.get(i));
            }
        }

        this.componentHashMap.remove(componentName);
        arrayList = (ArrayList) ACDDHacks.PackageParser$Activity_intents.get(obj);
        size = arrayList.size();
        for (i = 0; i < size; i++) {
            removeFilter((IntentFilter) arrayList.get(i));
        }
    }

    protected ResolveInfo resolveActivityInfo(IntentFilter intentFilter, int flags) {
        try {
            Object obj = ACDDHacks.PackageParser$ActivityIntentInfo_activity.get(intentFilter);
            ActivityInfo activityInfo = (ActivityInfo) obj.getClass().getField("info").get(obj);
            if (activityInfo == null) {
                return null;
            }
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo = activityInfo;
            if ((this.flags & PackageManager.GET_RESOLVED_FILTER) != 0) {
                resolveInfo.filter = intentFilter;
            }
            resolveInfo.priority = intentFilter.getPriority();
            resolveInfo.preferredOrder = 0;
            resolveInfo.match = flags;
            resolveInfo.isDefault = true;
            resolveInfo.labelRes = 0;
            resolveInfo.nonLocalizedLabel = null;
            resolveInfo.icon = 0;
            return resolveInfo;
        } catch (Exception e) {
            return null;
        }
    }
}
