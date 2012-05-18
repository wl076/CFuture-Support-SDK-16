/**
 * 
 */
package com.lurencun.android.support.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

/**
 * 启动过程Activity。
 * 此Activity继承自BaseActivity，具备处理耗时操作能力。
 * 如果需要在启动过程中执行耗时操作，请覆盖onProcess()方法。
 * 如果在处理过程中需要中断等待，请使用sendProcessed()方法发送处理完成信号。
 * @author chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-2-6
 */
public abstract class SplashAnimActivity extends BaseActivity{
	private final static String TAG = SplashAnimActivity.class.getName().toString();
	private final static int SPLASH = 0;
	private final static int FINISH = 1;
	/**
	 * 动画持续时间
	 */
	private int mAnimationDuration = 1500;
	
	/**
	 * 第一个动画被显示的时间间隔
	 */
	private final static int INIT_TIME = 100;
	
	/**
	 * 启动参数
	 * @author chenyoca [桥下一粒砂] (chenyoca@163.com)
	 * @date 2012-2-6
	 * @desc
	 */
	protected class SplashResource {
		private int imageResId;
		private int life;
		private int startAlpha;
		/**
		 * 
		 * @param imageResId
		 * 			启动动画的Drawable资源ID
		 * @param life 
		 * 			显示时间(单位是毫秒)
		 * @param startAlpha 起始透明度。
		 * 			从 0 - 10 这是渐变动画的透明度。
		 * 			0 表示全透明
		 * 			10 表示不透明
		 */
		public SplashResource(int imageResId,int life,int startAlpha){
			this.imageResId = imageResId;
			this.life = life;
			this.startAlpha = startAlpha;
		}
		public int getImageResId() {
			return imageResId;
		}
		public int getLife() {
			return life;
		}
		public int getStartAlpha() {
			return startAlpha;
		}
	}
	/**
	 * 动画资源数组
	 */
	private List<SplashResource> mSplashParams = new ArrayList<SplashResource>();
	
	private Activity mContext;
	private FrameLayout mLayout;
	private ImageView mSplashStage;
	
	protected int[] RndSplashArray = new int[]{0};
	
	private Boolean mWaiting = true;
	/**
	 * 设置启动界面背景颜色
	 * 
	 * @return 颜色值，例如0xFF123456（前两位是透明度，后六位是颜色值）
	 */
	protected int getSplashScreenBackground() {
		return 0xFFFFFFFF;
	}
	
	/**
	 * 随机启动图片
	 * @return
	 */
	protected int getRandomSplashResId(){
		return RndSplashArray[Math.abs(new Random().nextInt() % RndSplashArray.length)];
	}
	
	/**
	 * 设置动画持续时间
	 * @param duration
	 */
	public void setDuration(int duration){
		mAnimationDuration = duration;
	}
	
	/**
	 * 添加启动动画资源。创建一个资源对象，并添加到List中。
	 * @param reources 把创建的动画资源添加到此List对象中
	 */
	protected abstract void addSplashResource(List<SplashResource> resources);
	
	/**
	 * 设置启动动画显示后的UI
	 * @return Activity的Class
	 */
	protected abstract Class<?> nextUI();
	
	/**
	 * 创建动画Layout
	 * @return 返回创建的Layout
	 */
	private View createSplashLayout() {
		mLayout = new FrameLayout(this);
		android.view.ViewGroup.LayoutParams layoutParams = new android.view.ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		mLayout.setLayoutParams(layoutParams);
		mLayout.setBackgroundColor(getSplashScreenBackground());
		mSplashStage = new ImageView(this);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		mLayout.addView(mSplashStage, params);
		return mLayout;
	}

	
	
	/**
	 * 显示动画的Handler
	 */
	private Handler mAnimHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SPLASH){
				int resId = msg.arg1;
				int startAlpha = msg.arg2;
				AlphaAnimation animation = new AlphaAnimation(converToAlpha(startAlpha), 1.0f);
				animation.setDuration(mAnimationDuration);
				mSplashStage.setImageResource(resId);
				mSplashStage.startAnimation(animation);
			}else{
				shouldSkipToNext();
				mContext.startActivity(new Intent(mContext,nextUI()));
				mContext.finish();
			}
			super.handleMessage(msg);
		}
		
	};

	/**
	 * 显示动画
	 */
	private void showSplash() {
		int life = 0;
		int maxIndex = mSplashParams.size()-1;
		for(int i=0;i <= mSplashParams.size(); i++){
			if(i <= maxIndex){
				if( i == 0){
					life += INIT_TIME;
				}else{
					life += mSplashParams.get(i - 1).getLife();
				}
				Message msg = new Message();
				msg.what = SPLASH;
				msg.arg1 =  mSplashParams.get(i).getImageResId();
				msg.arg2 = mSplashParams.get(i).getStartAlpha();
				mAnimHandler.sendMessageDelayed(msg,life);
			}else{
				life += mSplashParams.get(maxIndex).getLife();
				Message msg = new Message();
				msg.what = FINISH;
				mAnimHandler.sendMessageDelayed(msg,life);
			}
		}
	}
	
	/**
	 * 转换透明度
	 * @param val 10进制的透明度。（0-10）
	 * @return 转换成 0.0f - 1.0f
	 */
	private float converToAlpha(int val) {
		return (float) (Math.max(0, Math.min(val, 10)) / 10.0);
	}
	
	/**
	 * OnCreate过程不可被覆盖
	 */
	@Override
	final protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		this.setScreenVertical();
		this.toggleFullScreen();
		this.hideTitleBar();
		setContentView(createSplashLayout());
		addSplashResource(mSplashParams);
		onSplash();
		showSplash();
	}
	
	/**
	 * 在开始执行处理过程前，此方法被调用
	 */
	protected void beforeProcess(){
		
	}
	
	/**
	 * 在动画显示过程前执行方法
	 */
	private void onSplash(){
		beforeProcess();
		new Thread(new Runnable(){
			@Override
			public void run() {
				Looper.prepare();
				onProcess();
				sendProcessed();
			}
		}).start();
	}
	
	/**
	 * 在方法在一个非UI线程中执行。
	 * 如果需要在启动过程中执行比较耗时的操作，请覆盖这个方法。
	 * 如果此方法执行时间比启动动画长，则动画会显示最后一帧，等待此方法返回。
	 * 否则，播放完动画后，直接跳到下一个Activity。
	 */
	protected void onProcess(){}
	
	/**
	 * 是否可以跳转到下一个UI
	 * 在此方法没有返回之前，动画会一直显示，并等待此方法返回
	 */
	private void shouldSkipToNext(){
		while(mWaiting){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Log.e(TAG,e.getMessage());
				mWaiting = false;
			}
		}
	}
	
	/**
	 * 发送完成处理消息。
	 */
	protected final void sendProcessed(){
		synchronized (mWaiting) {
			mWaiting = false;
		}
	}
	

}
