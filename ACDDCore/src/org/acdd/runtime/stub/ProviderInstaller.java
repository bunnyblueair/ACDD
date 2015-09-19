/*
 * ACDD Project
 * file ProviderInstaller.java  is  part of ACCD
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

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.util.Log;

import org.acdd.hack.ACDDHacks;
import org.acdd.hack.AndroidHack;
import org.acdd.hack.Hack;

import java.util.Collection;
import java.util.List;

/**
 * Used for  Stub Mode.
 */
public class ProviderInstaller {
    public static void installContentProviders(Context context, Collection<ProviderInfo> pluginProviderInfos) {
        try {
            ACDDHacks.ActivityThread.method("installContentProviders",new Class[]{Context.class, List.class})
                    .invoke(AndroidHack.getActivityThread(), new Object[]{context, pluginProviderInfos});
        } catch (Hack.HackDeclaration.HackAssertionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("ProviderInstaller","installContentProviders");

    }
}
