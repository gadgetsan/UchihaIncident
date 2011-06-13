package com.ninja.exMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * PlayContent est l'activit�/page qui contient toute la logique n�cessaire
 * � faire fonctionner le minigame. Elle contient principalement un thread
 * sous-jacent � la logique et une surface faisant l'abstraction des inputs
 * et des outputs.
 */
public class PlayContent extends Activity {
	
  /** Le thread sur lequel se passe la physique et les animations. */
  private PlayContentThread mContentThread;

  /** La vue s'occupant de tout le input/output. */
  private PlayContentView mContentView;
    
  /** L'identifiant du boutton option dans le menu en jeu. */
  private static final int kOptionMenu = 1;
    
  /** L'identifiant du boutton option dans le menu en jeu. */
  private static final int kPauseMenu = 2;
  
  /** L'identifiant du boutton option dans le menu en jeu. */
  private static final int kResumeMenu = 3;
  
  /** Niveau de difficult� du jeux */
  private static int kDifficulty = 1;
  
	
  /**
   * Phase du cycle de vie correspondant � l'initialisation.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    /* Full screen */
    requestWindowFeature(Window.FEATURE_NO_TITLE); 
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    
    
   	setContentView(R.layout.content);
    
    // On va chercher la r�f�rence des trois �l�ments.
    mContentView = (PlayContentView) findViewById(R.id.ContentView);
    mContentThread = mContentView.getThread();
    mContentView.SetActivity(this);
    
    mContentView.mTextStatus = (TextView)findViewById(R.id.Status);    
  }
    
  /**
   * Cr�ation du menu et des options qui s'y retrouve.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
        
    menu.add(0, kOptionMenu, 0, R.string.menu_options);
    menu.add(0, kPauseMenu, 0, R.string.menu_pause);
    menu.add(0, kResumeMenu, 0, R.string.menu_resume);
        
    return true;
  }
    
  /**
   * Actions � prendre d�pendament de l'option choisit dans le menu.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
  	  case 1:  return true;
  	  case 2:
        Pause();
  		return true;
  	  case 3:
  		Resume();
  		return true;
  	}
    return false;
  }
    
  public void BackToMenu() {
    finish();
  }
  
  public void Retry() {
    mContentThread.FreshStart();
  }
  /**
   * Phase du cycle de vie ou nous ne sommes probablement plus visible et
   * pr�t � �tre mis hors de m�moire.
   */
  @Override
  public void onPause() {
    super.onPause();
    Pause();
  }
    
  /**
   * Finalement, on es revenu du onPause et apparemment, Yay, Android nous
   * dis que nos informations sont suppps� encore �tre en m�moire.
   */
  @Override
  public void onResume() {
    super.onResume();
    Resume();
  }
    
  /** M�thode priv� permettant de g�r� l'�v�nement pause. */
  private void Pause() {  mContentThread.pause();  }
    
  /** M�thode priv� permettant de g�r� l'�v�nement resum�. */
  private void Resume() {  mContentThread.unpause();  }
    
  /**
   * M�thode que nous donne Android pour nous permettre de sauvegarder notre
   * �tat en m�moire juste avant que notre application se fasse fermer pour
   * lib�r� de la m�moire.
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }
    
  @Override
  public void onStop() {
    super.onStop();
    mContentThread.Panic();
  }

	public static void setkDifficulty(int kDifficulty) {
		PlayContent.kDifficulty = kDifficulty;
	}
	
	public static int getkDifficulty() {
		return kDifficulty;
	}
}
