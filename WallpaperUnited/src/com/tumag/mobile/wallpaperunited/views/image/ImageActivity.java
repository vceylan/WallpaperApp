package com.tumag.mobile.wallpaperunited.views.image;

import java.io.IOException;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import com.tumag.mobile.wallpaperunited.R;
import com.tumag.mobile.wallpaperunited.customs.CommonVariables;

public class ImageActivity extends Activity {
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	ImageView image;
	boolean isShowing = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		initializeView();
	}

	protected void onPause() {
		super.onPause();
		System.gc();
		
	}

	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	protected void onStop() {
		super.onStop();
		System.gc();
	}
	
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	private void initializeView() {
		image = (ImageView) findViewById(R.id.ivSinglePhoto);

		int imageId = getResources().getIdentifier(
				"pic_" + CommonVariables.categoryIndex + "_"
						+ CommonVariables.imageIndex, "drawable",
				getPackageName());

		image.setImageResource(imageId);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new InnerGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		image.setOnTouchListener(gestureListener);

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

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return super.onTouchEvent(event);
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
		image.setDrawingCacheEnabled(true);
		Bitmap bitmap = image.getDrawingCache();

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

	class InnerGestureDetector extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				int imageIndex = 0;
				boolean toLeft = false;
				Intent intent = new Intent(
						ImageActivity.this.getApplicationContext(),
						ImageActivity.class);
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					imageIndex = CommonVariables.imageIndex + 1;
					toLeft = true;

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					imageIndex = CommonVariables.imageIndex - 1;
				}

				int imageId = getResources().getIdentifier(
						"pic_" + CommonVariables.categoryIndex + "_"
								+ imageIndex, "drawable", getPackageName());
				if (imageId != 0) {
					CommonVariables.imageIndex = imageIndex;
					ImageActivity.this.startActivity(intent);
					if (toLeft) {
						ImageActivity.this.overridePendingTransition(
								R.anim.right_to_left, R.anim.left_to_right);
					} else {
						ImageActivity.this.overridePendingTransition(
								R.anim.right_to_left_reversed,
								R.anim.left_to_right_reversed);
					}
					ImageActivity.this.finish();
				}

			} catch (Exception e) {
				Log.e("Error", e.getMessage());
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {

			return true;
		}
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
}
