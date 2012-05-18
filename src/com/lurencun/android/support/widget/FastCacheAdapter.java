/**
 * 
 */
package com.lurencun.android.support.widget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.lurencun.android.sdk.cache.IndexLooper;

/**
 * 大数据快速缓存适配器
 * @author cfuture.chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-2-25
 */
public abstract class FastCacheAdapter<T> extends CommonAdapter<T> {

	public final static int DEFAULT_CACHE_SIZE = 5;
	
	private IndexLooper mIndexLooper;
	private int mCacheSize = DEFAULT_CACHE_SIZE;
	private View[] mViewCache;
	private int mCurrentIndexInCache = (DEFAULT_CACHE_SIZE - 1) / 2;
//	private int mPrePositon = 0;
//	private int mStepCount = 0;
	
	public FastCacheAdapter(Context context) {
		super(context);
	}

	@Override
	public void updateDataCache(List<T> dataCache) {
		mDataCacheList = dataCache;
		mIndexLooper = new IndexLooper(mDataCacheList.size(), mCacheSize);
		mViewCache = new View[mCacheSize];
		int[] posArray = mIndexLooper.init();
		for(int i=0;i<posArray.length;i++){
			int position = posArray[i];
			mViewCache[i] = createView(mInflater, mDataCacheList.get(position), position, null, null);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//如果读取的是边缘View，则后台线程创建
//		int step = position - mPrePositon;
//		mPrePositon = position;
//		if(step>0){
//			
//		}else{
//			
//		}
		return mViewCache[mCurrentIndexInCache];
	}
}
