package uk.co.brightec.example.mediacontroller;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class SuccessActivity extends Activity  implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{
VideoView videoView;
SurfaceView videoSurface;
//public static MediaPlayer player;
   VideoControllerView controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_activity_main);     
      
   //     Uri vidUri = Uri.parse("http://somewebsite.com/somevideo.mp4");
     /*   Uri vidUri=   Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");;
        videoView.setVideoURI(vidUri);
        videoView.setMediaController(new MediaController(this));        
        videoView.start();
        videoView.set
        */
        
        Uri uri = Uri.parse("android.resource://uk.co.brightec.example.mediacontroller/"+R.raw.abc);
        //player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        
        
       
        
        if(VideoPlayerActivity.player!=null){
           /* if(VideoPlayerActivity.player.isPlaying()){
            	VideoPlayerActivity.player.stop();
            }
            VideoPlayerActivity.player.release();
            VideoPlayerActivity.player = null;*/

        
        Toast.makeText(getApplicationContext(), "player not null:::"+VideoPlayerActivity.player,Toast.LENGTH_LONG).show();
        
        }
        
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        
    }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	
		
		   if(VideoPlayerActivity.player!=null){
	           /* if(VideoPlayerActivity.player.isPlaying()){
	            	VideoPlayerActivity.player.stop();
	            }
	            VideoPlayerActivity.player.release();
	            VideoPlayerActivity.player = null;*/

			   VideoPlayerActivity.player.setDisplay(holder);
			//   VideoPlayerActivity.player.release();
			   VideoPlayerActivity.player.seekTo(2);
			   
			   VideoPlayerActivity.player.setOnPreparedListener(this);
	        Toast.makeText(getApplicationContext(), "success"+VideoPlayerActivity.player,Toast.LENGTH_LONG).show();
	        
	        }
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
		
		
		
		
	}
    
}
