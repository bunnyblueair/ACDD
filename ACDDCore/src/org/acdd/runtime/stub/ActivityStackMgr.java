/*
 * ACDD Project
 * file ActivityStackMgr.java  is  part of ACCD
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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by BunnyBlue on 9/19/15.
 */
public class ActivityStackMgr {
    static final Logger log = LoggerFactory.getInstance(ActivityStackMgr.class);
    private static ActivityStackMgr stackMgr = null;
    private ArrayList<WeakReference<Activity>> stack;

    private ActivityStackMgr() {
        this.stack = new ArrayList();

    }

    public static synchronized ActivityStackMgr getInstance() {
        if (stackMgr !=null){
            return stackMgr;
        }
        synchronized (ActivityStackMgr.class) {
            if (stackMgr == null) {
                stackMgr = new ActivityStackMgr();
            }

        }
        return stackMgr;
    }

    public void handleActivityStack(String name, Intent intent, int flag, int launchMode) {
        String topActivity;
        if (this.stack.size() > 0) {
            topActivity = ((Activity) ((WeakReference) this.stack.get(this.stack.size() - 1)).get()).getClass().getName();
            if (StringUtils.equals(topActivity, name) && (launchMode == ActivityInfo.LAUNCH_SINGLE_TOP || (flag & Intent.FLAG_ACTIVITY_SINGLE_TOP) == Intent.FLAG_ACTIVITY_SINGLE_TOP)) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Set flat FLAG_ACTIVITY_SINGLE_TOP to %s", new Object[]{name}));
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            } else if (launchMode == ActivityInfo.LAUNCH_SINGLE_TASK || launchMode == ActivityInfo.LAUNCH_SINGLE_INSTANCE || (flag & Intent.FLAG_ACTIVITY_NO_HISTORY) == Intent.FLAG_ACTIVITY_NO_HISTORY) {

                boolean updateStack=false;
                int i = 0;
                for ( i=0; i < this.stack.size();i++){
                    if (((Activity) ((WeakReference) this.stack.get(i)).get()).getClass().getName().equals(name)) {
                        updateStack=true;
                        break;
                    }
                }
                if (updateStack) {
                    for (WeakReference weakReference : this.stack.subList(i + 1, this.stack.size())) {
                        if (weakReference.get() != null) {
                            ((Activity) weakReference.get()).finish();
                        }
                    }
                    this.stack.subList(i + 1, this.stack.size()).clear();
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
            }
        }
    }
    public Activity peekTopActivity() {
        if (this.stack != null && this.stack.size() > 0) {
            WeakReference weakReference = (WeakReference) this.stack.get(this.stack.size() - 1);
            if (!(weakReference == null || weakReference.get() == null)) {
                return (Activity) weakReference.get();
            }
        }
        return null;
    }

    public void pushToActivityStack(Activity activity) {
        this.stack.add(new WeakReference(activity));
    }

    public void popFromActivityStack(Activity activity) {
        for (int i = 0; i < this.stack.size(); i++) {
            WeakReference weakReference = (WeakReference) this.stack.get(i);
            if (!(weakReference == null || weakReference.get() == null || weakReference.get() != activity)) {
                this.stack.remove(weakReference);
            }
        }
    }

    public boolean isActivityStackEmpty() {
        return this.stack.size() == 0;
    }

    public int sizeOfActivityStack() {
        return this.stack.size();
    }
}