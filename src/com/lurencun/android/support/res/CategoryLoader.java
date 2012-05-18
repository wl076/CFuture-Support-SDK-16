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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.lurencun.android.sdk.res.AssetsReader;
import com.lurencun.android.sdk.res.ResJSONHandler;

/**
 * 目录加载
 * @author cfuture.chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-3-3
 */
public class CategoryLoader extends ResJSONHandler<CategoryEntity> {

	/**
	 * @param context
	 */
	public CategoryLoader(Context context) {
		super(context);
	}

	@Override
	protected CategoryEntity convert(JSONObject json) throws JSONException {
		CategoryEntity item = new CategoryEntity();
		if(json.has("dir")){
			item.dir = json.getString("dir");
		}
		
		if(json.has("lock")){
			item.lock = json.getBoolean("lock");
		}
		
		if(json.has("name")){
			item.name = json.getString("name");		
		}
		
		if(json.has("value")){
			item.value = json.getInt("value");
		}
		
		if(json.has("icon")){
			item.icon = AssetsReader.readBitmap(mContext, json.getString("icon"));
		}
		
		return item;
	}

}
