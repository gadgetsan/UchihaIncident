package com.ninja.test.ballctrl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayContentView extends SurfaceView implements SurfaceHolder.Callback{

   public PlayContentThread mContentThread;
   
   public PlayContentView(Context context, AttributeSet attrs) {
      super(context, attrs);
  	
		Log.d("ContentView::constructor", "on construit l'objet view");
       
         SurfaceHolder h = getHolder();
         h.addCallback(this);
         mContentThread = new PlayContentThread(h, context);
      
      setFocusable(true);
   }

   public PlayContentThread getThread() { return mContentThread; }

   @Override
   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      mContentThread.setSurfaceSize(width, height);   
      
   }

   @Override
   public void surfaceCreated(SurfaceHolder holder) {
		
      mContentThread.setRunning(true);
      mContentThread.start();
      
   }

   @Override
   public void surfaceDestroyed(SurfaceHolder holder) {
      boolean retry = true;
      mContentThread.setRunning(false);
      while (retry) {
        try {
           mContentThread.join();
          retry = false;
        } catch (InterruptedException e) {}
      }
   }
   
}
