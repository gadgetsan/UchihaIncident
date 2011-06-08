package com.ninja.test.ballctrl;

public class Collidable {
    private int mPosX;
    private int mPosY;
    private float mElasticity;
    
    private static int rayon;
    
    Collidable(int aPosX, int aPosY, float aElasticity) {
    	mPosX = aPosX;
    	mPosY = aPosY;
    	mElasticity = aElasticity;
    	
    	setSides();
    }
    
    public static void setOffset(int offset) {
    	rayon = offset/2;
    }
    
    public static boolean collided(Collidable a, Collidable b) {
    	int collideDistance = a.getRayon() + b.getRayon();
    	
    	int distX = a.getX() - b.getX();
    	int distY = a.getY() - b.getY();
    	
    	int normaliseur = (int) Math.pow(distX*distX + distY*distY, 1/2);
    	
    	int distAbs = (int) Math.abs(distX / normaliseur) + Math.abs(distY / normaliseur);
    	
    	if(distAbs <= collideDistance)
    		return true;
    	else 
    		return false;
    	
    	// distance entre le centre du mur et la droite de d�placement de l'objet dois �tre
    	// inf�rieur � a.rayon + b.rayon
    	//int collideDistance = a.getRayon() + b.getRayon();
    	
    	
    	//return true;
    }
    
    /*
     * � l'initialisation, les coordonn�es sont celles du coin supp�rieur gauche
     * de l'objet, on va modifier ces coordonn�es pour qu'elle correspondent
     * au centre de l'objet
     */
    public void setSides() {
    	mPosX += rayon;
    	mPosY += rayon;
    }

	public int getX() {
		return mPosX;
	}

	public void setX(int mPosX) {
		this.mPosX = mPosX;
	}

	public int getY() {
		return mPosY;
	}

	public void setY(int mPosY) {
		this.mPosY = mPosY;
	}

	public float getElasticity() {
		return mElasticity;
	}

	public int getRayon() {
		return rayon;
	}

}
