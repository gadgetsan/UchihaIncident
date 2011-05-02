package com.ninja.exMenu;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Classe permettant de g�rer les transitions lin�aire d'objets sur le canvas.
 */
public class LinearBitmapAnimation {
  // Temps en milisecondes avant le d�part de l'application.
  private long mOffsetMs;
  // Dur�e en milisecondes de l'application.
  private long mLengthMs;
  // Le moment auquel nous sommes rendu dans l'application.
  private long mCountMs;
  // Point de d�part de l'animation.
  private Point mStart;
  // Point de fin de l'animation.
  private Point mFinish;
  // Le sprite sous-jacent � afficher.
  private Sprite mSprite;
  // D�termine si l'animation est pr�sentement actif.
  public boolean isActive;
  
  // Constructeur pour un sprite et une dur�e.
  public LinearBitmapAnimation(Sprite sprite, long length, Point start,
      Point finish) {
    mOffsetMs = 0;
    mLengthMs = length;
    mSprite = sprite;
    mStart = start;
    mFinish = finish;
    isActive = true;
    mCountMs = 0;
  }
  
  /**
   * Dessine un incr�ment de l'animation.
   * @param dTime Le delta temps depuis le dernier draw.
   */
  public void Animate(long dTime) {
    mCountMs += dTime;
    
    if (mCountMs <= mOffsetMs || !isActive)  return;
    if (mCountMs > mLengthMs + mOffsetMs) {
      isActive = false;
      return;
    }
    
    float pourcentage = mCountMs / (float)(mOffsetMs + mLengthMs);
    
    Point delta = new Point(mFinish.x - mStart.x, mFinish.y - mStart.y);
    mSprite.SetPosition(new PointF(mStart.x + delta.x * pourcentage,
        mStart.y + delta.y * pourcentage));
  }
  
  // Retourne le sprite sous-jacent.
  public Sprite Sprite() { return mSprite; }
}
