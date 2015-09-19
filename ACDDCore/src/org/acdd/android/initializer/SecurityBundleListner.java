/*
 * ACDD Project
 * file SecurityBundleListner.java  is  part of ACCD
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

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.acdd.framework.ACDD;
import org.acdd.runtime.RuntimeVariables;
import org.acdd.util.ApkUtils;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import java.io.File;


public class SecurityBundleListner implements BundleListener {
    public static final String PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100bd707bf7328e9140c591920d776fa77f0fbfde6f1576308efee3cb82d1c106e6bd04a3b0a36255821bb4cbe355905fa55cd000bbe3de8c4351a48fb233c02f440bf26b1a881d694e7428d523baf7061b8a3a121596d226d71f2b831d32bbac6ad2de6b58b98ddc277baa77d0fcf46acc03b90a5bc8072198718675a5220691b757e0be8bc81b7562972e8fa0f6efbd462e5d55e840de3b36677807f2171ef1f70549992f8f88450b921a18ae387c737e955fe4c53fb05e37375b4a18b58845e777b4ef73af72a4d42adabf63ace80490396ce479db7b159cc1d8d19c9f6b4ed242af9fe3ee0b8d290c5998e3357b2d98cdf971ff3c67436c0a3b5bcda88a813d0203010001";

    ProcessHandler mProcessHandler;
    private Handler mSecurityCheckHandler;
    private HandlerThread mHandlerThread;


    private final class SecurityCheckHandler extends Handler {


        public SecurityCheckHandler(Looper looper) {
            super(looper);


        }

        @Override
        public void handleMessage(Message message) {

            if (message != null) {
                String location = (String) message.obj;
                if (!TextUtils.isEmpty(location) ) {
                    File bundleFile = ACDD.getInstance().getBundleFile(location);
                    if (bundleFile != null&&!TextUtils.isEmpty(SecurityBundleListner.PUBLIC_KEY)) {
                        String  bKey=ApkUtils.getApkPublicKey(bundleFile.getAbsolutePath());
                        if (!SecurityBundleListner.PUBLIC_KEY.equals(bKey)) {
                            Log.e("SecurityBundleListner", "Security check failed. " + location+" "+bKey);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RuntimeVariables.androidApplication, "Public Key error，PLZ update your  public key", Toast.LENGTH_SHORT).show();
                                    mProcessHandler.sendEmptyMessageDelayed(0, 5000);
                                }
                            });

                        }

                    }
                }
            }
        }
    }

    public static class ProcessHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            Process.killProcess(Process.myPid());
        }
    }


    public SecurityBundleListner() {
        this.mHandlerThread = null;
        this.mProcessHandler = new ProcessHandler();

        this.mHandlerThread = new HandlerThread("Check bundle security");
        this.mHandlerThread.start();
        this.mSecurityCheckHandler = new SecurityCheckHandler(this.mHandlerThread.getLooper());
    }

    public void bundleChanged(BundleEvent bundleEvent) {

        switch (bundleEvent.getType()) {
            case BundleEvent.INSTALLED:
            case BundleEvent.UPDATED:
                Message obtain = Message.obtain();
                obtain.obj = bundleEvent.getBundle().getLocation();
                this.mSecurityCheckHandler.sendMessage(obtain);
                return;
            default:
                return;
        }
    }


}
