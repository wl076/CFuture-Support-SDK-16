/**
 * 
 */
package com.lurencun.android.support.ui;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 带回退到上一个Activity功能的封装Activity。
 * @author cfuture.chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-1-13
 */
public abstract class BackUIActivity extends BaseActivity{

	private static final String DEFAULT_TIP_TITLE = "返回提示";
	private static final String DEFAULT_TIP_MESSAGE = "您确定返回吗？";
	private static final String DEFAULT_OK_TEXT = "确定";
	private static final String DEFAULT_CANCLE_TEXT = "取消";
	
	private ConfirmDialog mDialog;
	
	@Override
	final protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideTitleBar();
		setContentView(getContentViewLayoutId());
		bindBackAction();
		onCreateEx(savedInstanceState);
	}
	
	/**
	 * 当需要在Activity的OnCreate中执行操作里，
	 * 在此方法中执行。
	 * @param savedInstanceState
	 */
	protected abstract void onCreateEx(Bundle savedInstanceState);
	/**
	 * 设置当前Activity布局的View的资源ID
	 * @return 布局资源ID，例如：R.layout.XXX
	 */
	protected abstract int getContentViewLayoutId();
	
	/**
	 * 返回当前Activity回退按钮的按钮ID
	 * @return 回退按钮的ID，例如：R.id.XXX
	 */
	protected abstract int getBackButtonResId();
	
	/**
	 * 绑定回退按钮的操作动作
	 */
	private void bindBackAction(){
		findViewById(getBackButtonResId()).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isConfirmBack()){
					showTip();
				}else{
					BackUIActivity.this.finish();
					overridePendingTransition(BackUIActivity.this);
				}
			}
		});
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && isConfirmBack()) {
			showTip();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Activity切换动画设置，需要在2.0版本之后才有。
	 * 本SDK最低支持1.6版本。如果需要Activity切换动画
	 * 请覆盖此方法。
	 * @param activity
	 */
	protected void overridePendingTransition(Activity activity){
//		activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	/**
	 * 是否显示返回确认消息。
	 * @return true则显示返回确认，否则直接返回不需要提示。
	 */
	protected abstract boolean isConfirmBack();
	
	/**
	 * 显示退出提示框
	 */
	protected final void showTip(){
//		final AlertDialog alertDialog = new AlertDialog.Builder(BackUIActivity.this)
//			.setTitle(getTipTitle()).setMessage(getTipMessage())
//			.setPositiveButton(DEFAULT_OK_TEXT, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which){
//					BackUIActivity.this.finish();
//				}
//			}).setNegativeButton(DEFAULT_CANCLE_TEXT,new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which){
//					return;
//				}
//			}).create();
//		alertDialog.show();
		if(mDialog == null){
			mDialog = new ConfirmDialog(this);
		}
		mDialog.show();
	}
	
	/**
	 * 返回提示消息的标题字符串资源ID
	 * @return 资源ID
	 */
	protected String getTipTitle(){
		return DEFAULT_TIP_TITLE;
	}
	
	/**
	 * 返回提示消息的提示内容字符串资源ID
	 * @return 资源ID
	 */
	protected String getTipMessage(){
		return DEFAULT_TIP_MESSAGE;
	}
	private class ConfirmDialog extends Dialog{

		private static final int BG_COLOR = 0xFF585858;
		private static final int MSG_BG_COLOR = 0xFF222222;
		private static final int MSG_TEXT_COLOR = 0xFFFFFFFF;
		private static final int SIDE_BTN_COLOR = 0xFFEEEEEE;
		private static final int BTN_TEXT_COLOR = 0xFFF73100;
		
		private Context mContext;
		
		public ConfirmDialog(Context context) {
			super(context, android.R.style.Theme_Dialog);
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
			mContext = context;
			setContentView(createContentView());
		}
		
		private View createContentView() {
			LinearLayout layout = new LinearLayout(mContext);
			layout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			layout.setLayoutParams(params);
			int padding = 10;
			layout.setPadding(padding, padding, padding, padding);
			layout.setBackgroundColor(BG_COLOR);
			TextView mMessageBox = new TextView(mContext);
			mMessageBox.setTextColor(MSG_TEXT_COLOR);
			mMessageBox.setText(getTipMessage());
			mMessageBox.setPadding(padding, padding/2, padding, padding);
			mMessageBox.setBackgroundColor(MSG_BG_COLOR);
			layout.addView(mMessageBox);
			
			LinearLayout buttonsLayout = new LinearLayout(mContext);
			buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
			RelativeLayout.LayoutParams btnLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			btnLayoutParam.topMargin = 2;
			buttonsLayout.setLayoutParams(btnLayoutParam);
			Button okButton = new Button(mContext);
			Button cancleButton = new Button(mContext);
			
			LayoutParams btnParam = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			btnParam.weight = 1.0f;
			
			okButton.setLayoutParams(btnParam);
			okButton.setText(DEFAULT_OK_TEXT);
			okButton.setTextColor(BTN_TEXT_COLOR);
			okButton.setBackgroundColor(SIDE_BTN_COLOR);
			okButton.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(BTN_TEXT_COLOR);
						((Button)v).setTextColor(SIDE_BTN_COLOR);
					}else if(event.getAction() == MotionEvent.ACTION_UP){
						v.setBackgroundColor(SIDE_BTN_COLOR);
						((Button)v).setTextColor(BTN_TEXT_COLOR);
					}
					BackUIActivity.this.finish();
					overridePendingTransition(BackUIActivity.this);
					return false;
				}
			});
			
			
			cancleButton.setLayoutParams(btnParam);
			cancleButton.setText(DEFAULT_CANCLE_TEXT);
			cancleButton.setBackgroundColor(SIDE_BTN_COLOR);
			cancleButton.setTextColor(BTN_TEXT_COLOR);
			cancleButton.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(BTN_TEXT_COLOR);
						((Button)v).setTextColor(SIDE_BTN_COLOR);
					}else{
						v.setBackgroundColor(SIDE_BTN_COLOR);
						((Button)v).setTextColor(BTN_TEXT_COLOR);
					}
					ConfirmDialog.this.dismiss();
					return false;
				}
			});
			
			buttonsLayout.addView(okButton);
			buttonsLayout.addView(cancleButton);
			layout.addView(buttonsLayout);
			return layout;
		}
		
	}
}
