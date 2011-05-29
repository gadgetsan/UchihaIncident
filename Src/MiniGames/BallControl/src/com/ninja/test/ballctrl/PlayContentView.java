package com.ninja.test.ballctrl;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayContentView extends SurfaceView implements SurfaceHolder.Callback {

   private PlayContentThread mContentThread;
   
   public PlayContentView(Context context, AttributeSet attrs) {
      super(context, attrs);
  	
		Log.d("ContentView::constructor", "on construit l'objet view");
       
         SurfaceHolder h = getHolder();
         
         mContentThread = new PlayContentThread(h, context);
         
         h.addCallback(this);
      
      
      setFocusable(true);
   }

   public PlayContentThread getThread() { return mContentThread; }

   @Override
   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      mContentThread.setSurfaceSize(width, height);   
      
   }

   @Override
   public void surfaceCreated(SurfaceHolder holder) {
	  	
		Log.d("ContentView::surfaceCreated", "on cr�� la surface");
		
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
