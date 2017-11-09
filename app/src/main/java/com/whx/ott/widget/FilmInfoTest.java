package com.whx.ott.widget;

import com.whx.ott.R;
import com.whx.ott.bean.HomeInfo;

import java.util.ArrayList;
import java.util.List;



/**
 * @author LittleLiByte 
 * 测试数据
 */
public class FilmInfoTest {

		
		// 图片数组
	private static int[] resIds = new int[] { R.mipmap.jichu_icon, R.mipmap.gebie_icon,
						R.mipmap.tese_icon, R.mipmap.me_icon, R.mipmap.town_jichu
						};


	private static String[] names = new String[] { "基础课", "个别指导", "特色课",
			"我的", ""
	};
	private static int[] iconIds = new int[]{R.mipmap.jichu_icon,R.mipmap.gebie_icon,R.mipmap.tese_icon,R.mipmap.me_icon,R.mipmap.town_jichu};


	public static List<HomeInfo> getfilmInfo() {
		List<HomeInfo> filmList = new ArrayList<HomeInfo>();
			filmList.add(new HomeInfo(names[0], null,null,iconIds[0],resIds[0]));
			filmList.add(new HomeInfo(names[1], null,null,iconIds[1],resIds[1]));
			filmList.add(new HomeInfo(names[2], null,null,iconIds[2],resIds[2]));
			filmList.add(new HomeInfo(names[3], null,null,iconIds[3],resIds[3]));
			filmList.add(new HomeInfo(names[4], null,null,iconIds[4],resIds[4]));
		return filmList;
	}
}
