package com.ninja.exMenu;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Le thread sous-jacent au jeu. Il contient la physique,
 * les animations et les timers.
 */
public class PlayContentThread extends Thread {
  
  public class Bound {
    public Bound(float min, float max) { inferior = min; superior = max; }
    public float inferior;
    public float superior;
  }
  
  public class GameContext {
    public GameContext(int wantedDots, Bound timeBound) {
      nDots = wantedDots;
      time = timeBound;
    }
    public int nDots;
    public Bound time;
  }
  
  private GameContext mGameContext;
  
  /** Le temps apr�s Ready. */
  private static long kTimeBeforeGameMs = 1850;
  
  /** Le jigger dans le temps apr�s ready. Donc le temps final est 
   *  kTimeBeforeGameMs + kJiggerMs.
   */
  private static long kJiggerMs = 400;
  
  private static int kSoundSword = 1;
	
	/** La suface de la vue de l'�cran. */
	private SurfaceHolder mSurfaceHolder;
	
	 /** Les dimensions du canvas de l'�cran. */
  private RectF mCanvasDim;
	
	/** Renseignement sur le context dans lequel l'application roule. */
	private Context mContext;
	
	/** Petit profiler rapide pour garder compte du FPS et obtenir le temps entre les frames. */
  private Profiler profiler;
  
  /** Es-ce que le thread de l'application est actif? */
  private boolean mRun = false;
  
  /** Le mode dans lequel l'application se trouve en se moment. */
  private int mMode = kInitial;
  
  /**
   * Le mode dans lequel nous nous trouvions avant le dernier changement
   * d'�tat, il sert principalement � revenir d'un resume.
   */
  private int mLastMode = kInitial;
  
  
  /**
   * J'ai ici plusieurs compte qui serait bien d'�tre ailleur.
   */
  private long countReady = 0;
  private long countUserInput = 0;
  private long countAnimMvt = 0;
  private long timeBeforeGame = 0;
  
  /** Garde m�moire de si l'utilisateur � gagn�. */
  private boolean hasWon = false;
  
  /**
   * On retrouve ici les �tats dans lesquels peut ce situer le minigame.
   */
  public static final int kInitial = 0;
  public static final int kAnimationDebut = 1;
  public static final int kReady = 2;
  public static final int kUserInput = 3;
  public static final int kAnimationMouvement = 4;
  public static final int kAnimationFin = 5;
  public static final int kPause = 6;
  
  /*
   * Les bornes pour le calcul du pourcentage de completion.
   */
  public final Bound kDiffEasy = new Bound(1.3f, 1.9f);
  private final Bound kDiffMedium = new Bound(.9f, 1.8f);
  private final Bound kDiffHard = new Bound(.7f, 1.3f);
  private final Bound kDiffExtreme = new Bound(.55f, .9f);
  
  
  private LinearBitmapAnimation Samurai;
  private LinearBitmapAnimation Tameshigiri;
  private LinearBitmapAnimation Katana;
  private RotationBitmapAnimation KatanaRot;
  
  private Sprite tatami_top;
  private Sprite tatami_bottom;
  private Sprite tatami_complet;
  private Sprite katanaSprite;
  
  private SoundManager mSound = new SoundManager();
  
  private Handler msgHandler;
	
  private PlayGrid mPlayGrid;
  private FollowupLine mFollowupLine;
  
  /**
   * Cr�� un thread sous-jacent au minigame pour g�r� les animations, la physique et les timers.
   * @param surface La surface fournie par la vue, visible par l'utilisateur.
   * @param context Le context android dans lequel roule l'application.
   * @param msgHandler La pompe � �v�nement servant � communiqu� avec la vue.
   */
  public PlayContentThread(SurfaceHolder surface, Context context, Handler hMsg, int difficulty) {
    mSurfaceHolder = surface;
    mContext = context;
    msgHandler = hMsg;
    
    switch(difficulty) {
    case 0 :
        mGameContext = new GameContext(3, kDiffEasy);
        break;
    case 1 :
        mGameContext = new GameContext(4, kDiffMedium);
        break;
    case 2 :
        mGameContext = new GameContext(4, kDiffHard);
        break;
    case 3 :
        mGameContext = new GameContext(5, kDiffExtreme);
        break;
    }
  	
    mCanvasDim = new RectF();    
    profiler = new Profiler();
  		
    mSound.Init(context);
    mSound.Load(kSoundSword, R.raw.fourreau);
    
    Sprite samurai = new Sprite(context.getResources().getDrawable(R.drawable.kendo));
    tatami_top = new Sprite(context.getResources().getDrawable(R.drawable.tatami_top));
    tatami_bottom = new Sprite(context.getResources().getDrawable(R.drawable.tatami_bottom));
    tatami_complet = new Sprite(context.getResources().getDrawable(R.drawable.tatami_uncut));
    katanaSprite =  new Sprite(context.getResources().getDrawable(R.drawable.katana_low_jeu));
    
    samurai.Scale(1.1);
    tatami_complet.Scale(0.7);
    katanaSprite.Scale(0.5);
    
    tatami_top.Scale(0.5);
    tatami_bottom.Scale(0.5);
    
    Samurai = new LinearBitmapAnimation(samurai, 2000, new Point(600, 100), new Point(0, 0));
    Tameshigiri = new LinearBitmapAnimation(tatami_complet, 2000, new Point(100, 100), new Point(300, 100));
    
    mPlayGrid = new PlayGrid();
    mFollowupLine = new FollowupLine();
    
    mPlayGrid.Dots(mGameContext.nDots);
  }
	
  public void doStart() {
    profiler.Tick();
    setState(kInitial);
  }
	
  public void Panic() {
    mRun = false;
  }
	
  @Override
  public void run() {
    while (mRun) {
	    Canvas c = null;
	    try {
	      c = mSurfaceHolder.lockCanvas(null);
		    synchronized (mSurfaceHolder) {
		      doDraw(c);
	      }
	    } finally {
	      if (c != null)
		      mSurfaceHolder.unlockCanvasAndPost(c);
	    }
	  }
  }
	
  public void setRunning(boolean r) { mRun = r; }
	
  public void doDraw(Canvas c) {
    profiler.Tick();
    
	  switch (mMode) {
	    case kInitial:
	      setState(kAnimationDebut);
		    break;
      case kAnimationDebut:
        AnimationDebut(c, profiler.Delta());
		    break;
      case kReady:
        Ready(c, profiler.Delta());
        break;
	    case kUserInput:
	      UserInput(c, profiler.Delta());
		    break;
	    case kAnimationMouvement:
	      AnimationMvt(c, profiler.Delta());
		    break;
	    case kAnimationFin:
		    break;
      case kPause:
        break;
	  }
	}
  
  private void SetUpAnimationMvt() {
    
    float halfHeight = mCanvasDim.bottom / 2;
    long timeToCompletion = countUserInput - timeBeforeGame;
    
    Katana = new LinearBitmapAnimation(katanaSprite, 500, new Point(350, 130), new Point(200, 190));
    KatanaRot = new RotationBitmapAnimation(katanaSprite, 500, -15);
    
    tatami_top.SetPosition(new PointF(mCanvasDim.right / 2 - 4, halfHeight - 53));
    tatami_bottom.SetPosition(new PointF(mCanvasDim.right / 2 + 4, halfHeight + 53));
    Katana.Sprite().PlaceRotationCenter(new PointF(mCanvasDim.right / 2, mCanvasDim.bottom / 2));
  }
  
  private void AnimationMvt(Canvas c, long delta) {
    
    boolean stillMoving = true;
    if (countAnimMvt < 500) {
      countAnimMvt += delta;
      stillMoving = false;
      KatanaRot.Animate(delta);
    } else if (countAnimMvt < 1600) {
      stillMoving = false;
    }
    
    Katana.Animate(delta);
    
    synchronized (mSurfaceHolder) {
      c.drawColor(Color.BLACK);
      
      tatami_bottom.Draw(c);
      Katana.Sprite().Draw(c);
      tatami_top.Draw(c);
      
      profiler.Draw(c, mCanvasDim);
      
      if (!stillMoving) {
        if (hasWon)  Status("Vous avez Gagn�! " + Float.toString((float)((countUserInput - timeBeforeGame) / 1000.0)) + " s");
        else Status("Trop Long");
      }
    }
  }
	
	private void AnimationDebut(Canvas c, long delta) {
	  if (!Samurai.isActive && !Tameshigiri.isActive) {
	    setState(kReady);
	    return;
	  }
	  Samurai.Animate(delta);
	  Tameshigiri.Animate(delta);
	  
	  synchronized (mSurfaceHolder) {
	    c.drawColor(Color.BLACK);
	    Tameshigiri.Sprite().Draw(c);
	    Samurai.Sprite().Draw(c);
	    profiler.Draw(c, mCanvasDim);
	  }
	}
	
	private void Ready(Canvas c, long delta) {
	  countReady += delta;
	  if (countReady > 1100) {
	    setState(kUserInput);
	    return;
	  }
	  synchronized (mSurfaceHolder) {
	    c.drawColor(Color.BLACK);
	    profiler.Draw(c, mCanvasDim);
	  }
	}
	
  private void UserInput(Canvas c, long delta) {
    countUserInput += delta;
    
    // Temps al�atoire avant de commencer.
    if (countUserInput < timeBeforeGame) return;
    
    // T'es trop long, dsl.
    if (countUserInput > 5000) {
      setState(kAnimationMouvement);
      return;
    }
    
    // Tous les cible ont �t� touch�s
    if (mPlayGrid.IsWon()) {
      hasWon = true;
      setState(kAnimationMouvement);
      return;
    }
    
    synchronized (mSurfaceHolder) {
      c.drawColor(Color.BLACK);
      
      mPlayGrid.Draw(c, mCanvasDim);
      mFollowupLine.Draw(c);
      
      profiler.Draw(c, mCanvasDim);
    }
  }
	
	public void doTouch(MotionEvent e) {
	  if (mMode != kUserInput)  return;
	  
	  synchronized (mSurfaceHolder) {
      mPlayGrid.DoTouch(e);
      mFollowupLine.DoTouch(e);
	  }
	}
	
	private void SetUpUserInput() {
	  Status("");
	  
	  Random r = new Random();
	  timeBeforeGame = kTimeBeforeGameMs + r.nextLong() % kJiggerMs;
	}
	
	public void setState(int mode) {
	  
	  switch(mode) {
	    case kAnimationDebut:  break;
	    case kReady:
	      mSound.playSound(1);
	      Status("Ready");
	      break;
	    case kUserInput:
	      SetUpUserInput();
	      break;
	    case kAnimationMouvement:
	      SetUpAnimationMvt();
	      break;
	  }
	  
	  synchronized (mSurfaceHolder) {
		  mMode = mode;
	  }
	}
	
	public void setSurfaceSize(int width, int height) {
		synchronized (mSurfaceHolder) {
		  mCanvasDim.left = 0;
		  mCanvasDim.top = 0;
		  mCanvasDim.right = width;
		  mCanvasDim.bottom = height;
		}		
	}
	public void pause() {}
	public void unpause() {}
	
	private void Status(String status) {
	  Message msg = msgHandler.obtainMessage();
	  Bundle b = new Bundle();
	  b.putString("text", status);
	  msg.setData(b);
	  msgHandler.sendMessage(msg);
	}
}
