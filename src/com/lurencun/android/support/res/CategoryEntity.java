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

import android.graphics.Bitmap;

/**
 * 目录实体
 * @author cfuture.chenyoca [桥下一粒砂] (chenyoca@163.com)
 * @date 2012-3-3
 */
public class CategoryEntity {
	public String dir = new String();
	public String name = new String();
	public int value = 0;
	public Bitmap icon = null;
	public boolean lock = false;
}
