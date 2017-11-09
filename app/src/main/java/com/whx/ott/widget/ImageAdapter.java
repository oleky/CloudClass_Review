package com.whx.ott.widget;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.whx.ott.R;
import com.whx.ott.bean.HomeInfo;
import com.whx.ott.util.BitmapUtil;
import com.whx.ott.util.ImageCacheUtil;

/**
 * @author LittleLiByte
 */
public class ImageAdapter extends FancyCoverFlowAdapter {
	private Context context;
	private List<HomeInfo> filmList;
	 ImageCacheUtil cacheUtil;

	public ImageAdapter(Context context, List<HomeInfo> filmList) {
		this.context = context;
		this.filmList = filmList;
		cacheUtil = new ImageCacheUtil(context);
//		this.options = options;
//		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filmList.get(position % filmList.size());
//		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position % filmList.size();
//		return position;
	}

	@Override
	public View getCoverFlowItem(int position, View convertView,
								 ViewGroup parent) {
		ImageView imageView = (ImageView) convertView;
		if (imageView == null) {
			imageView = new ImageView(context);
		}
		Resources re = context.getResources();
		int resId = filmList.get(position % filmList.size()).getRs();
		InputStream is = re.openRawResource(resId);
//		InputStream is = re.openRawResource(mImagesId[position%mImagesId.length]);
		BitmapDrawable mapdraw = new BitmapDrawable(is);
		Bitmap bitmap = mapdraw.getBitmap();
		if (cacheUtil.getBitmap(resId + "") == null) {
			cacheUtil.putBitmap(resId + "", bitmap);
		}
		Bitmap bmap = cacheUtil.getBitmap(resId + "");

		imageView.setImageBitmap(BitmapUtil.createReflectedBitmap(bmap));
//		imageView.setImageBitmap(bitmap);

//		imageView.setLayoutParams(new Gallery.LayoutParams(350, 590));
		imageView.setLayoutParams(new Gallery.LayoutParams(320, 560));


		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setAdjustViewBounds(true);
		return imageView;
	}



}