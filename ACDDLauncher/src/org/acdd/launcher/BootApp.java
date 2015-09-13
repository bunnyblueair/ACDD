/*
 * ACDD Project
 * file BootApp.java  is  part of ACCD
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
package org.acdd.launcher;

import org.acdd.android.compat.ACDDApp;
import org.acdd.framework.InternalConstant;
import org.acdd.framework.ACDDConfig;

public class BootApp extends ACDDApp {
	static{
		ACDDConfig.DELAY = new String[]{"com.acdd.qrcode","libcom.acdd.testapp1","libcom.acdd.testapp2"};
		ACDDConfig.AUTO = new String[]{"com.acdd.homelauncher","com.acdd.qrcode","com.acdd.android.game2","com.acdd.universalimageloader.sample"};
		ACDDConfig.STORE = new String[]{"com.acdd.android.appcenter","com.acdd.universalimageloader.sample"};
	}

	

	static final String TAG = "TestApp";


	@Override
	public void onCreate() {

		super.onCreate();
	
		InternalConstant.BundleNotFoundActivity=BundleNotFoundActivity.class;
	}





}
