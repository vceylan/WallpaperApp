package com.tumag.mobile.wallpaperunited.adapters;

import java.util.ArrayList;

import com.tumag.mobile.wallpaperunited.R;
import com.tumag.mobile.wallpaperunited.models.GalleyModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {
	private ArrayList<GalleyModel>	list;
	private Activity				context;

	public GalleryAdapter(ArrayList<GalleyModel> list, Activity context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		int count = this.list.size();
		return count;
	}

	@Override
	public GalleyModel getItem(int position) {
		GalleyModel model = this.list.get(position);
		return model;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.context.getLayoutInflater();
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gallery_cell, parent, false);
		}
		
		GalleyModel model = this.getItem(position);
		ImageView image = (ImageView) convertView.findViewById(R.id.ivCellImage);
		TextView categoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
		
		image.setImageResource(model.imageId);
		categoryName.setText(model.CategoryName);
		
		return convertView;
	}

}
