package com.ninja.exMenu;

public class RotationBitmapAnimation {
  // Temps en milisecondes avant le d�part de l'application.
  private long mOffsetMs;
  // Dur�e en milisecondes de l'application.
  private long mLengthMs;
  // Le moment auquel nous sommes rendu dans l'application.
  private long mCountMs;
  // Le sprite sous-jacent � afficher.
  private Sprite mSprite;
  // D�termine si l'animation est pr�sentement actif.
  public boolean isActive;
  // Le total de degree de rotation � appliquer.
  public float totalRotation;
  
  // Constructeur pour un sprite et une rotation.
  public RotationBitmapAnimation(Sprite sprite, long length, float degree) {
    mOffsetMs = 0;
    mLengthMs = length;
    mSprite = sprite;
    totalRotation = degree;
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
    mSprite.SetRotation(totalRotation * pourcentage);
  }
  
  // Retourne le sprite sous-jacent.
  public Sprite Sprite() { return mSprite; }
}
