package com.example.Ky;

import android.widget.AbsListView;

/**
 * @类说明:ListView头部停靠的监听器
 * @作者:xiechengfa
 * @创建时间:2014-10-27 下午3:42:36
 */
public interface TabBaseFramentListener {
	// 重置滚动状态
	public void restScrollState();

	// 更新滚动状态
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount);
}
