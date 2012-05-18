/**
 * Copyright (C) 2012  CFutureAndroidSupport
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lurencun.android.support.res;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.lurencun.android.sdk.database.KeyLocker;
import com.lurencun.android.support.ad.AdSense;
import com.lurencun.android.support.ad.OnPayedListener;
import com.lurencun.android.support.widget.CommonAdapter;

public abstract class CategoryAdapter extends CommonAdapter<CategoryEntity> implements OnPayedListener{
	
	private Activity mActivity;
	private AdSense mAd;
	private KeyLocker mLocker;
	private String mCurrentDir;
	
	public CategoryAdapter(Context context,Activity activity) {
		super(context);
		mActivity = activity;
	}
	
	public void setAdSense(AdSense ad){
		mAd = ad;
		mAd.setOnPayedListener(this);
		mAd.setBitAmount(getUnlockOnceAmount());
		mAd.setMassAmount(getUnlockTotalAmount());
		mAd.setMessage(getMessage());
		mLocker = new KeyLocker(mContext);
	}
	
	protected abstract String getAmountName();
	protected abstract String getMessage();
	protected abstract int getUnlockOnceAmount();
	protected abstract int getUnlockTotalAmount();
	/**
	 * 对某个View进行点击绑定
	 * @param view
	 * @param data
	 */
	protected void bindViewClick(View view,final CategoryEntity data){
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentDir = data.dir;
				if(data.lock && mLocker.isLocked(data.dir) && mAd.canShowAd()){
					mAd.showDialog();
				}else{
					switchToNext(mActivity,data);
				}
			}
		});
	}
	
	/**
	 * 跳转到某个Activity
	 * @param activity
	 * @param data
	 */
	protected abstract void switchToNext(Activity activity,CategoryEntity data);

	@Override
	public void onPayedFailed(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPayedSuccess(int payedAmount) {
		System.out.println("支付了："+payedAmount);
		if(isUnlockAll(payedAmount)){
			mLocker.unlockAll();
		}else{
			mLocker.unlock(mCurrentDir);
		}
	}
	
	protected abstract boolean isUnlockAll(int payedAmount);

}
