package com.tumag.mobile.wallpaperunited.views.gallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tumag.mobile.wallpaperunited.R;
import com.tumag.mobile.wallpaperunited.adapters.GalleryAdapter;
import com.tumag.mobile.wallpaperunited.customs.CommonVariables;
import com.tumag.mobile.wallpaperunited.models.GalleyModel;
import com.tumag.mobile.wallpaperunited.views.image.ImageActivity;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

public class PhotoGalleryActivity extends Activity implements  OnItemClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_gallery);
		initializeView();
		
		AdView adView = (AdView) findViewById(R.id.adView);

		/*AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("A5BF03C49B1339BB45B094261EB3E204").build();
				*/
		AdRequest adRequest = new AdRequest.Builder().build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
	}

	private void initializeView() {
		GridView gallery = (GridView) findViewById(R.id.gvPhotoGallery);
		GalleryAdapter adapter = new GalleryAdapter(getList(), this);
		gallery.setAdapter(adapter);
		gallery.setOnItemClickListener(this);
		
	}

	private ArrayList<GalleyModel> getList() {
		ArrayList<GalleyModel> list = new ArrayList<GalleyModel>();
		String[] categories = getResources().getStringArray(R.array.category_array);
		
		for (int i = 0; i < categories.length; i++) {
			String imageName = "cat" +i;
			int imageId = getResources().getIdentifier(imageName, "drawable", getPackageName());
			if (imageId != 0) {
				GalleyModel model = new GalleyModel();
				model.CategoryName = categories[i];
				model.imageId = imageId;
				list.add(model);
			}
		}
		
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_gallery, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		Intent intent = new Intent(this, ImageActivity.class);
		CommonVariables.categoryIndex = position;
		CommonVariables.imageIndex = 0;
		startActivity(intent);
	}
}