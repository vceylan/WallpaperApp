package com.tumag.mobile.wallpaperunited.views.image;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.fantasticideasgroup.wallpaperunited.R;
import com.tumag.mobile.wallpaperunited.customs.CommonVariables;

public class ImageActivity extends Activity implements ViewFactory {

	ImageSwitcher imageSwitcher;
	boolean isShowing = true;
	int downX, upX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		initializeView();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		imageSwitcher.destroyDrawingCache();
		super.onBackPressed();
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		imageSwitcher.destroyDrawingCache();
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		imageSwitcher.destroyDrawingCache();
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		imageSwitcher.destroyDrawingCache();
		super.onDestroy();
	}

	private void initializeView() {

		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		imageSwitcher.setFactory(this);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		imageSwitcher.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					downX = (int) event.getX();
					Log.i("event.getX()", " downX " + downX);
					return true;
				}

				else if (event.getAction() == MotionEvent.ACTION_UP) {
					upX = (int) event.getX();
					Log.i("event.getX()", " upX " + downX);
					if (upX - downX > 50) {

						// curIndex current image index in array viewed by user
						CommonVariables.imageIndex--;
						if (CommonVariables.imageIndex < 0) {
							CommonVariables.imageIndex = CommonVariables.maxIndex - 1;
						}

						imageSwitcher.setInAnimation(AnimationUtils
								.loadAnimation(ImageActivity.this,
										android.R.anim.fade_in));
						imageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(ImageActivity.this,
										android.R.anim.fade_out));

						imageSwitcher.setImageResource(getImageId(
								CommonVariables.categoryIndex,
								CommonVariables.imageIndex));
						// GalleryActivity.this.setTitle(curIndex);
					}

					else if (downX - upX > -50) {

						CommonVariables.imageIndex++;
						if (CommonVariables.imageIndex > CommonVariables.maxIndex) {
							CommonVariables.imageIndex = 0;
						}

						imageSwitcher.setInAnimation(AnimationUtils
								.loadAnimation(ImageActivity.this,
										android.R.anim.fade_in));
						imageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(ImageActivity.this,
										android.R.anim.fade_out));

						imageSwitcher.setImageResource(getImageId(
								CommonVariables.categoryIndex,
								CommonVariables.imageIndex));
						// GalleryActivity.this.setTitle(curIndex);
					}
					return true;
				}
				return false;
			}
		});

		imageSwitcher.setImageResource(getImageId(
				CommonVariables.categoryIndex, CommonVariables.imageIndex));

		AdView adView = (AdView) findViewById(R.id.adView2);

		/*
		 * AdRequest adRequest = new AdRequest.Builder()
		 * .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		 * .addTestDevice("A5BF03C49B1339BB45B094261EB3E204").build();
		 */
		AdRequest adRequest = new AdRequest.Builder().build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
	}

	private int getImageId(int categoryIndex, int imageIndex) {
		return getResources().getIdentifier(
				"pic_" + CommonVariables.categoryIndex + "_"
						+ CommonVariables.imageIndex, "drawable",
				getPackageName());
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
		imageSwitcher.setDrawingCacheEnabled(true);
		Bitmap bitmap = imageSwitcher.getDrawingCache();

		try {
			MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
					"title", "from my app");
			Toast.makeText(this, "Image saved.", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "Error saving image.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void setWallpaper() {
		TaskSetWallpaper task = new TaskSetWallpaper();
		task.execute(new Void[] {});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

	private class TaskSetWallpaper extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(ImageActivity.this, "Wallpaper setting...",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			WallpaperManager myWallpaperManager = WallpaperManager
					.getInstance(getApplicationContext());
			try {
				int imageId = getResources().getIdentifier(
						"pic_" + CommonVariables.categoryIndex + "_"
								+ CommonVariables.imageIndex, "drawable",
						getPackageName());
				myWallpaperManager.setResource(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Toast.makeText(ImageActivity.this, "Wallpaper set.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return i;
	}
}
