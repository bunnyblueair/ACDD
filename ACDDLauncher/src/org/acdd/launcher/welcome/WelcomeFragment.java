/*
 * ACDD Project
 * file WelcomeFragment.java  is  part of ACCD
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
package org.acdd.launcher.welcome;

/**
 * @author BunnyBlue
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

import org.acdd.framework.InternalConstant;
import org.acdd.launcher.AA;
import org.acdd.launcher.LauncherActivity;
import org.acdd.launcher.R;


public class WelcomeFragment extends android.support.v4.app.Fragment implements Callback {
	private static final int MSG_ANIMATE_LOGO = 1235;
	private static final int MSG_CONSUME_FINISH = 11;

	private static final int MSG_FINISH_WELCOME = 1236;

	private static final String TAG = "WelcomeFregment";
	private BundlesInstallBroadcastReceiver acddBroadCast;
	private long bundlestart;
	private boolean firstResume;
	private boolean initFinish;
	// private Bitmap mBmStart;
	private Handler mHandler;
	private boolean mHasBitmap;
	private PathView[] pathViewArray;
	private View welcomSlogan;
	public static boolean start = false;

	private class BundlesInstallBroadcastReceiver extends BroadcastReceiver {


		private BundlesInstallBroadcastReceiver() {

		}

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				System.err.println(intent.getAction() + "BundlesInstallBroadcastReceiver");
				WelcomeFragment.this.consumeFinish();
				WelcomeFragment.this.mHandler.sendEmptyMessage(WelcomeFragment.MSG_CONSUME_FINISH);
			} catch (Exception e) {
			}
		}
	}

	public WelcomeFragment() {
		this.mHandler = null;
		//this.mBmStart = null;
		this.mHasBitmap = false;
		this.initFinish = false;
		this.firstResume = true;
		this.bundlestart = 0;
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
		View imageView = null;
		// super.onCreate(bundle);
		this.mHandler = new Handler(this);
		ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.welcome, viewGroup, false);
		final PathView pathView = (PathView) viewGroup2.findViewById(R.id.pathViewS);
		PathView pathViewJ = (PathView) viewGroup2.findViewById(R.id.pathViewJ);
		PathView pathViewT = (PathView) viewGroup2.findViewById(R.id.pathViewT);
		PathView pathViewB = (PathView) viewGroup2.findViewById(R.id.pathViewB);
//      final Path path = makeConvexArrow(50, 100);
//      pathView.setPath(path);
		Log.d("ver",Build.VERSION.SDK_INT+"<<<<<");
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
			pathView.setFillAfter(true);
			pathView.useNaturalColors();
			pathView.getPathAnimator().
				delay(100).
				duration(1500).
				interpolator(new AccelerateDecelerateInterpolator()).
				start();
			pathViewJ.setFillAfter(true);
			pathViewJ.useNaturalColors();
			pathViewJ.getPathAnimator().
				delay(100).
				duration(1500).
				interpolator(new AccelerateDecelerateInterpolator()).
				start();

			pathViewT.setFillAfter(true);
			pathViewT.useNaturalColors();
			pathViewT.getPathAnimator().
				delay(100).
				duration(1500).
				interpolator(new AccelerateDecelerateInterpolator()).
				start();

			pathViewB.setFillAfter(true);
			pathViewB.useNaturalColors();
			pathViewB.getPathAnimator().
				delay(100).
				duration(1500).
				interpolator(new AccelerateDecelerateInterpolator()).
				start();
		}
		init();
		return viewGroup2;
	}


	@Override
	public void onResume() {
		super.onResume();
		if (this.firstResume) {
			this.firstResume = false;

		}
	}

	@Override
	public boolean handleMessage(Message message) {
		switch (message.what) {
			case MSG_CONSUME_FINISH /*11*/:
				this.initFinish = true;
				gotoMainActivity(false);
				break;

			case MSG_ANIMATE_LOGO /*1235*/:
//                View findViewById = getView().findViewById(R.id.tv_tips);
//                findViewById.setVisibility(0);
//                findViewById.startAnimation(AnimationUtils.loadAnimation(Globals.getApplication(), R.anim.fade_in));
				break;
			case MSG_FINISH_WELCOME /*1236*/:
				getActivity().finish();
				break;
		}
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();


	}


	private void init() {


		if ("flase".equals(System.getProperty("BUNDLES_INSTALLED", "flase"))) {
			this.acddBroadCast = new BundlesInstallBroadcastReceiver();
			getActivity().registerReceiver(this.acddBroadCast, new IntentFilter(InternalConstant.ACTION_BROADCAST_BUNDLES_INSTALLED));
			this.bundlestart = System.currentTimeMillis();
			System.err.println("MSG_CONSUME_TIMEOUT");
			// this.mHandler.sendEmptyMessageDelayed(MSG_CONSUME_TIMEOUT, 4000);
		} else {
			System.err.println("MSG_CONSUME_FINISH");
			this.mHandler.sendEmptyMessageDelayed(MSG_CONSUME_FINISH, 600);
		}

	}

	public void consumeFinish() {

		if (this.acddBroadCast != null) {
			getActivity().unregisterReceiver(this.acddBroadCast);
		}

		//   this.mHandler.removeMessages(MSG_CONSUME_TIMEOUT);
	}

	public void gotoMainActivity(boolean z) {
		System.out.println("WelcomeFragment.gotoMainActivity()");
		if (start) {
			start = true;


			return;
		}

		//  boolean z2 = false;.//com.openatlas.homelauncher.MainActivity
		if (getActivity() != null && LauncherActivity.class == getActivity().getClass()) {

			Intent mIntent = new Intent();
			mIntent.setClassName(getActivity(), "com.acdd.homelauncher.MainActivity");
			mIntent.putExtra("aa", new AA());
			startActivity(mIntent);
			LauncherActivity.doLaunchoverUT();
			getActivity().finish();
		} else {
			Log.e(getClass().getSimpleName(), "getActivity() is null");
		}
	}


}