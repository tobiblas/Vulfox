package com.vulfox;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameActivity extends Activity implements OnTouchListener, SurfaceHolder.Callback{
	
	private GameThread mGameThread;
	private ScreenManager mScreenManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SurfaceView view = new SurfaceView(getApplicationContext());
        SurfaceHolder holder = view.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL); //TODO: check other types.
        
		mScreenManager = new ScreenManager(getApplicationContext());
        
		mGameThread = new GameThread(holder, getApplicationContext(), mScreenManager);
		mGameThread.start();

    	holder.addCallback(this);
    	view.setOnTouchListener(this);
    	setContentView(view);	
    }

    
    @Override
    protected void onPause() {
    	super.onPause(); 	
    	mGameThread.onPause();
    }
  
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	mGameThread.onResume();
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mGameThread.onSurfaceChanged(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mGameThread.onSurfaceCreated();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mGameThread.onSurfaceDestroyed();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mGameThread.onTouch(event);
		return true;
	}
    
	/**
	 * Adds a game screen to the top of the screen stack
	 * @param screen An implemented screen
	 */
	public void addScreen(Screen screen)
	{
		mScreenManager.addScreen(screen);
	}
    
}