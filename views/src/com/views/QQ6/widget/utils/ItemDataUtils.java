package com.views.QQ6.widget.utils;

import java.util.ArrayList;
import java.util.List;

import com.views.R;
import com.views.QQ6.widget.entity.ItemBean;

/**
 * 当前类注释: ProjectName：DragHelper4QQ
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a> Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求， 打造极致课程，是菜鸟窝不变的承诺
 */
public final class ItemDataUtils {

	private ItemDataUtils() {
	}

	public static List<ItemBean> getItemBeans() {
		List<ItemBean> itemBeans = new ArrayList<ItemBean>();
		itemBeans.add(new ItemBean(R.drawable.sidebar_purse, "QQ钱包", false));
		itemBeans.add(new ItemBean(R.drawable.sidebar_decoration, "个性装扮", false));
		itemBeans.add(new ItemBean(R.drawable.sidebar_favorit, "我的收藏", false));
		itemBeans.add(new ItemBean(R.drawable.sidebar_album, "我的相册", false));
		itemBeans.add(new ItemBean(R.drawable.sidebar_file, "我的文件", false));
		return itemBeans;
	}

}
