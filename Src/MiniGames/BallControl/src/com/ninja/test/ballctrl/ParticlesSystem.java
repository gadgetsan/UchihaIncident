package com.ninja.test.ballctrl;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ParticlesSystem {
	
	private final int xDiv = 26;
	private final int yDiv = 13;
	
	private int offset;
	
	private ArrayList<Collidable> mObstalcesList;
	
	private Drawable SpikesBall;
	
	// largeur de l'�cran
	private int mWidth;

	// hauteur de l'�cran
	private int mHeight;
	
	public ParticlesSystem( Context context ) {
		
		mObstalcesList = new ArrayList<Collidable>();
  	
		Log.d("ParticlesSystem::constructor", "on initialise le drawable de la spikesball");
		
		SpikesBall = context.getResources().getDrawable(R.drawable.ball);
	}
	
	public ArrayList<Collidable> GetObstclesList() {
		return mObstalcesList;
	}
	
	public void Draw(Canvas c, RectF bounds) {
	  	
		Log.d("ParticlesSystem::Draw", "on dessine les particules sur le canvas");
		
		Collidable tmp;
		for(int i = 0; i < mObstalcesList.size(); i++){
			tmp = mObstalcesList.get(i);
			SpikesBall.setBounds((int)(tmp.getX()), 
								 (int)(tmp.getY()), 
								 (int)(tmp.getX()+offset), 
								 (int)(tmp.getY()+offset));
			//SpikesBall.setBounds(0, 0, 100, 100);
			SpikesBall.draw(c);
		}
	}
	
	public void setSurfaceSize(float width, float height) {
		mWidth = (int)width;
		mHeight = (int)height;
		
		String s = "with = " + Integer.toString(mWidth) + "\nheight = " + Integer.toString(mHeight);
		
		Log.d("sizes check", s);
		
		scaler();

		// Obstacles list later i'd want it to be loaded form an XML file
		PlaceObstacles();
	}
	
	private void scaler() {
		// comme l'�cran est toujours en format paysage (sett� dans le xml)
		// on a toujours with > height
		
		// On prend le plus petit coefficient pour que tout l'affichage 
		// rentre dans l'�cran ce qui est une bonne id�e selon moi ^^
		int tmp = (2*mHeight > mWidth) ? mWidth/2 : mHeight;
		
		// On a ici la taille des Drawable en pixels de mani�re a en avoir
		// N en y et 2N en x (N = yDiv)
		offset = tmp/yDiv;
		
		Log.d("OFFSET", "offset = " + Integer.toString(offset));
	}
	
	private void PlaceObstacles() {
	  	
		Log.d("ParticlesSystem::PlaceObstacles", "on place les particules sur le canvas");
		
		// Position maximale ou on va placer un �l�ment collisionable 
		// d'une dimention offset*offset
		final int maxWidth = offset*(xDiv-1);
		final int maxHeight = offset*(yDiv-1);
		
		// place des murs sur les cot�s
		for(int i = 0; i < yDiv; i++) {
			mObstalcesList.add(new Wall(0, i*offset, 1));
			mObstalcesList.add(new Wall(maxWidth,i*offset, 1));
		}

		//place des murs en haut et en bas
		for(int i = 0; i < xDiv-1; i++) {
			mObstalcesList.add(new Wall(i*offset, 0, 1));
			mObstalcesList.add(new Wall(i*offset, maxHeight, 1));
		}

		// 4 points random pour remplire un peu l'�cran
		mObstalcesList.add(new Wall(100, 100, 1));
		mObstalcesList.add(new Wall(200, 200, 1));
		mObstalcesList.add(new Wall(200, 100, 1));
		mObstalcesList.add(new Wall(100, 200, 1));
		
	}
	
}
