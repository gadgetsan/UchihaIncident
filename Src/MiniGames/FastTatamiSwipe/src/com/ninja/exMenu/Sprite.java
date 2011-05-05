package com.ninja.exMenu;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

/**
 * Classe repr�sentant un dessins bitmap � afficher � l'�cran.
 */
public class Sprite {
  // Les donn�es � afficher sur le canvas.
  private Drawable data;
  // L'�chelle de l'image � afficher.
  private double scale = 1.0;
  // La position de l'image.
  private Point mCenter;
  
  private float degreeRotation = 0.0f;
  private PointF mRotationCenter = new PointF();
  
  /**
   * Construit un sprite � partir de donn�es images.
   * @param d L'image sous-jacente.
   */
  public Sprite(Drawable d) {
	  data = d;
	  mCenter = new Point();
  }
  
  /**
   * L'�chelle de l'image.
   * @param s L'�chelle � tranf�rer l'image.
   */
  public void Scale(double s) { scale = s; }
  
  public void PlaceRotationCenter(PointF center) { mRotationCenter = center; }
  
  public void SetRotation(float nDegree) { degreeRotation = nDegree; }
  public void Rotate(float nDegree) { degreeRotation += nDegree; }
  
  /**
   * Place l'image � la nouvelle position.
   * @param p Position
   */
  public void SetPosition(PointF p) {
    mCenter.x = (int)p.x;
    mCenter.y = (int)p.y;
  }
  
  /**
   * Retourne l'�chelle pr�sente de l'image.
   * @return
   */
  public double getScale() { return scale; }
  
  /**
   * M�thode permettant d'afficher l'image � la position actuelle.
   * @param c Le canvas sur lequel dessiner.
   */
  public void Draw(Canvas c) {
    int sideBound = (int)(data.getIntrinsicWidth() * scale);
    int heightBound = (int)(data.getIntrinsicHeight() * scale);
      
    if (degreeRotation != 0.0f) {
      c.save();
      c.rotate(degreeRotation, mRotationCenter.x, mRotationCenter.y);
      data.setBounds(mCenter.x - sideBound, mCenter.y - heightBound,
          mCenter.x + sideBound, mCenter.y + heightBound);
  	  data.draw(c);
  	  c.restore();
    } else {
      data.setBounds(mCenter.x - sideBound, mCenter.y - heightBound,
          mCenter.x + sideBound, mCenter.y + heightBound);
      data.draw(c);
    }
  }
}

