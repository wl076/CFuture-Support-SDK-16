package com.lurencun.android.support.widget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 通用适配器。此适配器不能用于显示大数据View。
 * @author chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-2-8
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	private View[] mViewCacheArray;
	protected List<T> mDataCacheList;
	protected LayoutInflater mInflater;
	protected Context mContext;
	
	public CommonAdapter(Context context){
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	/**
	 * 更新适配器缓存的数据
	 * @param dataCache 数据
	 */
	public void updateDataCache(final List<T> dataCache){
		mDataCacheList = dataCache;
		mViewCacheArray = new View[mDataCacheList.size()];
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null == mViewCacheArray[position]){
			mViewCacheArray[position] = createView(mInflater, mDataCacheList.get(position), position, convertView, parent);
		}
		return mViewCacheArray[position];
	}
	
	/**
	 * 当需要创建一个新的View时，此方法被调用
	 * @param inflater 
	 * 			从XML中创建View时，可以调用此对象的inflater方法
	 * @param data
	 * 			数据对象，使用此对象的数据，向创建的View填充数据。
	 * @param position
	 * 			适配器数据缓存集中的数据的位置。
	 * @param convertView
	 * 			
	 * @param parent
	 * 			View的父View
	 * @return
	 * 			返回一个新建的View
	 */
	protected abstract View createView(LayoutInflater inflater,T data,int position, View convertView, ViewGroup parent);
	
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mDataCacheList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return mDataCacheList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
}
