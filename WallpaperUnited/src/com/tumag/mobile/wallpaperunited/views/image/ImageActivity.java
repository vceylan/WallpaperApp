package com.tumag.mobile.wallpaperunited.views.image;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.tumag.mobile.wallpaperunited.R;

public class ImageActivity extends Activity {
	private int categoryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		initializeView();
	}

	private void initializeView() {
		ImageView image = (ImageView) findViewById(R.id.ivSinglePhoto);
//		String id = getIntent().getStringExtra("categoryIndex");
//		categoryId = Integer.parseInt(id);
		image.setImageResource(R.drawable.pic_2_1);
	}

	public void onButtonClick(View v) {
		switch (v.getId()) {
			case R.id.bSaveImage:
				saveImage();
				break;
				
			case R.id.bSetWallpaper:
				setWallpaper();
				break;
				
			default:
				break;
		}
	}

	private void saveImage() {
		
	}
	
	private void setWallpaper() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}
}
