/**
 * 
 */
package com.lurencun.android.support.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 自定义Dialog
 * @author cfuture.chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-2-25
 */
public abstract class LightDialog extends Dialog {

	protected Context mContext;
	
	public LightDialog(Context context) {
		super(context);
		mContext = context;
	}
	
	public LightDialog(Context context, int theme){
        super(context, theme);
        mContext = context;
    }
	
	@Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        View contentView = getContentView();
        onCreateEx(savedInstanceState);
        if(null != contentView){
        	this.setContentView(contentView);
        }else{
        	this.setContentView(getContentViewId());
        }
    }
	
	public final void setFullScreen(boolean isFullScreen) {
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
	 * 返回Dialog布局XML资源ID
	 * @return
	 */
	protected int getContentViewId(){
		return 0;
	}
	
	/**
	 * 返回Dialog布局View对象
	 * 与getContentViewId方法相同，设置布局。
	 * 如果此方法返回null，则使用getContentViewId方法。
	 * @return
	 */
	protected View getContentView(){
		return null;
	}
	
	/**
	 * onCreate方法的扩展
	 * @param savedInstanceState
	 */
	protected void onCreateEx(Bundle savedInstanceState){
		
	}
}
