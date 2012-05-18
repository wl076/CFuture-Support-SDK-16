/**
 * 
 */
package com.lurencun.android.support.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.lurencun.android.sdk.util.DoubleClickExit;

/**
 * 封装Activity一些基本操作的其类
 * @author chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-2-6
 */
public abstract class BaseActivity extends Activity {

	private boolean mIsFullScreen = false;
	private int mExitWaitTime = 2000;
	
	private DoubleClickExit mExitTip;
	
	/**
	 * 切换全屏状态
	 */
	public void toggleFullScreen() {
		manualFullScreen(!mIsFullScreen);
	}

	/**
	 * 手动设定Activity全屏状态
	 * @param isFullScreen 为true则全屏，否则非全屏
	 */
	public final void manualFullScreen(boolean isFullScreen) {
		mIsFullScreen = isFullScreen;
		if (isFullScreen) {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(params);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(params);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 隐藏标题栏
	 * <br/><b>* 必须在setContentView方法之前执行</b>
	 */
	public final void hideTitleBar(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);    
	}
	
	/**
	 * 设置屏幕为竖向
	 * <br/><b>* 必须在setContentView方法之前执行</b>
	 */
	public final void setScreenVertical(){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 * 设置屏幕为横向
	 * <br/><b>* 必须在setContentView方法之前执行</b>
	 */
	public final void setScreenHorizontal(){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * 给Activity绑定双击返回键退出程序的功能。
	 * <br/>此方法依赖于onKeyDown()方法，如果覆盖onKeyDown方法，
	 * 则需要执行super.onKeyDown()，否则功能将失效。
	 */
	public final void bindDoubleClickExit(){
		mExitTip = new DoubleClickExit(this);
	}
	
	/**
	 * 给Activity绑定双击返回键退出程序的功能。
	 * <br/>此方法依赖于onKeyDown()方法，如果覆盖onKeyDown方法，
	 * 则需要执行super.onKeyDown()，否则功能将失效。
	 * @param time 等待时间
	 */
	public final void bindDoubleClickExit(int time){
		mExitTip = new DoubleClickExit(this);
		mExitWaitTime = time;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mExitTip != null){
			return mExitTip.doubleClickExit(keyCode,mExitWaitTime);
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
}
