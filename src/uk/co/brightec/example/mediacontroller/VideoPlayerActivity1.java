package uk.co.brightec.example.mediacontroller;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.RelativeLayout.LayoutParams;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;


public class VideoPlayerActivity1 extends Activity implements
		SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener, VideoControllerView.MediaPlayerControl {

	SurfaceView videoSurface;
	MediaPlayer player;
	VideoControllerView controller;

	private static final int FULL_SCREEN_VIDEO = 0;
	private static final int SMALL_VIDEO = 1;

	int videoSize = 0;

	ImageView btn_changeVideoSize;

	ProgressBar pd;

	public String videourl = "";

	

	int deviceWidth, deviceHeight;

	

	LinearLayout rl_main;
	int timOut;
	boolean isBackPressed=false,isPlayerPuased=false;
	
	public static boolean PLAYER_SCREEN=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 12)
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		else if (android.os.Build.VERSION.SDK_INT > 10)
			setTheme(android.R.style.Theme_Holo_Light);

		setContentView(R.layout.activity_vidoe_player);

	//	HomeFragmentActivity.EXIT = false;

	//	getIntentExtras();

		deviceResolution();

		initUI();

		
		setScreenTimeOut();
		
		
		setListners();

	//	loadBannerAdds(null, this);

		settingVideoView(FULL_SCREEN_VIDEO);

		videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
		

		playChannel();
	}

	private void setScreenTimeOut() {
		// TODO Auto-generated method stub
		try {
			timOut = android.provider.Settings.System.getInt(
					getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);

			android.provider.Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT, 1800000);

		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void resetScreenTimeOut() {
		// TODO Auto-generated method stub
		
			
			android.provider.Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT, timOut);

		
	}
	

	public void deviceResolution() {
		DisplayMetrics dm;
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		deviceHeight = dm.heightPixels;
		deviceWidth = dm.widthPixels;
	}

	@SuppressLint("NewApi")
	private void settingVideoView(int viewSize) {

		videoSize = viewSize;

		switch (viewSize) {
		case FULL_SCREEN_VIDEO:
			// YOU CAN CREATE LEFT, TOP, RIGHT, BOTTOM FOR YOUR VIEW AND SET.
			/*
			 * RelativeLayout.LayoutParams params = new
			 * RelativeLayout.LayoutParams(
			 * RelativeLayout.LayoutParams.MATCH_PARENT,
			 * RelativeLayout.LayoutParams.MATCH_PARENT);
			 * videoView.setLayoutParams(params);
			 */

			btn_changeVideoSize
					.setImageResource(R.drawable.ic_media_fullscreen_shrink);
			// Toast.makeText(getApplicationContext(), "Small called",
			// Toast.LENGTH_LONG).show();
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.ll);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, deviceWidth - 50);

			// videoView.

			float width = deviceWidth;
			float height = deviceHeight;

			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.CENTER_VERTICAL);

			rl.setLayoutParams(params);

			break;
		case SMALL_VIDEO:
			// YOU CAN CREATE LEFT, TOP, RIGHT, BOTTOM FOR YOUR VIEW AND SET.
			params = new RelativeLayout.LayoutParams(deviceWidth - 250,
					deviceHeight - 150);

			// videoView.

			rl = (RelativeLayout) findViewById(R.id.ll);

			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			rl.setLayoutParams(params);

			// videoView.layout(20, 20, 20, 20);
			btn_changeVideoSize
					.setImageResource(R.drawable.ic_media_fullscreen_stretch);
			// Toast.makeText(getApplicationContext(), "Default called",
			// Toast.LENGTH_LONG).show();
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		isBackPressed=true;
		VideoPlayerActivity1.PLAYER_SCREEN=false;
		
	//	loadIntersetialAdds();
		
		try {

			VideoPlayerActivity1.PLAYER_SCREEN=false;
			resetScreenTimeOut();
			if (player != null ) {
				player.stop();
				player.release();
			}
			
		} catch (IllegalStateException e) {
			// TODO: handle exception
		}
		

		super.onBackPressed();
	}

	private void setListners() {
		autoHideThread();

		rl_main.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.d("button pgm", "bton 5");
				btn_changeVideoSize.setVisibility(View.INVISIBLE);
				Log.d("button pgm", "bton 6");
				autoHideThread();
				Log.d("button pgm", "bton 7");
				return false;
			}
		});

		/*
		 * btn_changeVideoSize.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub
		 * 
		 * if (videoSize == FULL_SCREEN_VIDEO)
		 * btn_changeVideoSize.setImageResource
		 * (R.drawable.small_screen_player_selector); else if (videoSize ==
		 * SMALL_VIDEO) btn_changeVideoSize.setImageResource(R.drawable.
		 * full_screen_player_selector);
		 * 
		 * else btn_changeVideoSize.setImageResource(R.drawable.
		 * small_screen_player_selector);
		 * 
		 * runAutoHideThread();
		 * 
		 * return false; } });
		 */

		runAutoHideThread();

		btn_changeVideoSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btn_changeVideoSize.setVisibility(View.GONE);

				if (videoSize == FULL_SCREEN_VIDEO)
					settingVideoView(SMALL_VIDEO);
				else if (videoSize == SMALL_VIDEO)
					settingVideoView(FULL_SCREEN_VIDEO);
				else
					settingVideoView(FULL_SCREEN_VIDEO);

				runAutoHideThread();

			}
		});
	}
	
	

	protected void runAutoHideThread() {
		// TODO Auto-generated method stub
		new CountDownTimer(0, 3000) {

			public void onTick(long millisUntilFinished) {
				if (btn_changeVideoSize.getVisibility() == View.GONE)
					btn_changeVideoSize.setVisibility(View.GONE);
				else
					btn_changeVideoSize.setVisibility(View.GONE);
			}

			public void onFinish() {

				// btn_changeVideoSize.setImageResource(android.R.color.transparent);

			}
		}.start();
	}

	@Override
	public void onDestroy() {
		try {

			VideoPlayerActivity1.PLAYER_SCREEN=false;
			resetScreenTimeOut();
			if (player != null && player.isPlaying()) {
				player.stop();
				player.release();
			}
			
		} catch (IllegalStateException e) {
			// TODO: handle exception
		}
		
		super.onDestroy();

	}
	
	
/*	private void getIntentExtras() {

		Intent i = getIntent();
		videourl = i.getStringExtra(MyConstants.BUNDLE_PLAY_URL).trim();

		if (IsNull.isNull(videourl) == false && videourl.startsWith("rtmp"))
			videourl = videourl.replaceFirst("rtmp", "rtsp");
		else if (IsNull.isNull(videourl)) {
			finish();
			Toast.makeText(this, MyConstants.NO_URL_AVAILABLE,
					Toast.LENGTH_LONG).show();
		}*/

		//videourl="rtsp://54.76.114.221/roklive/103-1-240-224.stream?t=010e419039ad8168536f5cd73cda73e10d7";
		//videourl="rtsp://54.76.40.190/roklive/103-1-240-224.stream?t=010d830c0ba27bccf549a0d7e088336e25f";
		
//	}

	private void autoHideThread() {
		long start = System.currentTimeMillis();
		final long end = start + 5 * 1000;

		final Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {

				if (System.currentTimeMillis() >= end) {
					VideoPlayerActivity1.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							btn_changeVideoSize.setVisibility(View.INVISIBLE);
						}
					});

					myTimer.cancel();
				}
			}
		}, 1000, 1000);

	}
	
	private void playChannel()
	{
		SurfaceHolder videoHolder = videoSurface.getHolder();
		videoHolder.addCallback(this);
		
		if(pd!=null)
			pd.setVisibility(View.VISIBLE);

		player = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnErrorListener(this);
		controller = new VideoControllerView(this);

		/*
		 * pd=new
		 * ProgressDialog(VideoPlayerActivity.this,android.R.style.Theme_Holo_Light
		 * );
		 * 
		 * pd.setMessage("Channel Decoding");
		 */
		// pd.show();

		try {
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			String url = videourl;
			//player.setDataSource(this, Uri.parse(url));
			 player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
	           
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {

		VideoPlayerActivity1.PLAYER_SCREEN=true;
		
		isBackPressed=false;
		isPlayerPuased=false;
		
		rl_main = (LinearLayout) findViewById(R.id.rl_main);
		// videoView = (VideoView) findViewById(R.id.video_View);
		pd = (ProgressBar) findViewById(R.id.pb_loading);
		pd.setVisibility(View.VISIBLE);

		btn_changeVideoSize = (ImageView) findViewById(R.id.btn_changeVideoSize);

		 btn_changeVideoSize.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(controller!=null)
			controller.show();
		return false;
	}

	// Implement SurfaceHolder.Callback
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	
		try
		{
			player.setDisplay(holder);
			player.prepareAsync();
		
			
		}catch(IllegalStateException e)
		{
			
		}
			

	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.v("MEDIA PAUSE","PLAYER STOPPED:"+player);

		isBackPressed=true;
		isPlayerPuased=true;
		
		
		
		try
		{
			resetScreenTimeOut();

				if (player != null && player.isPlaying()) {
					
					player.stop();
					
					player.release();
					player=null;
					controller=null;
					
					Log.i("MEDIA PLYER","PLAYER STOPPED:"+player);
					
					//player.release();	
				}
				
			} catch (IllegalStateException e) {
				// TODO: handle exception
				Log.e("MEDIA PLYER","PLAYER STOPPED:"+e);

			} 
		
		finish();

		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		playChannel();
		
		
		super.onResume();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	// End SurfaceHolder.Callback

	// Implement MediaPlayer.OnPreparedListener
	@Override
	public void onPrepared(MediaPlayer mp) {
		controller.setMediaPlayer(this);
		controller
				.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
		player.start();

	//	dismissProgress();
	}

	// End MediaPlayer.OnPreparedListener

	// Implement VideoMediaController.MediaPlayerControl
	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		
		try {
			if(player!=null)
				return player.getCurrentPosition();
			else
				return 0;
		} catch (IllegalStateException e) {
			// TODO: handle exception
			return 0;
		}
		
	}

	@Override
	public int getDuration() {
		return player.getDuration();
	}

	@Override
	public boolean isPlaying() {
		try {
		
			return player.isPlaying();

		} catch (IllegalStateException e) {
			return true;

		}
	}

	@Override
	public void pause() {
		
		Log.i("hello","PAUSED RiYAS :");
		player.pause();
	}

	@Override
	public void seekTo(int i) {
		player.seekTo(i);
	}

	@Override
	public void start() {
		player.start();
	}

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public void toggleFullScreen() {

	}

	// End VideoMediaController.MediaPlayerControl

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		if(isBackPressed==false)
			Toast.makeText(getApplicationContext(), "NO url",
					5000).show();
		//dismissProgress();
		finish();
		return false;
	}





/*	private void dismissProgress() {
		long start = System.currentTimeMillis();
		final long end = start + 8 * 1000;

		final Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {

				if (System.currentTimeMillis() >= end) {
					VideoPlayerActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							pd.setVisibility(View.GONE);
						}
					});

					myTimer.cancel();
				}
			}
		}, 1000, 1000);
	}*/

}
