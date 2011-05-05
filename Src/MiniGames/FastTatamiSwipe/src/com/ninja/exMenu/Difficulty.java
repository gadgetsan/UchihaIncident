package com.ninja.exMenu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Difficulty extends Activity {

	/**
	 * Point de la cr�ation dans le cycle de vie, premi�re chance que
	 * l'on as d'initialiser des choses dans l'application.
	 * Le bundle peut contenir de l'information concernant la derni�re
	 * utilisation.
	 * @param savedInstanceState Un lot d'information sur la derni�re
	 *        utilisation de l'application.
	 */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    /* Il faut toujours, lorsque l'on override, caller l'instance de base. */
  	super.onCreate(savedInstanceState);
    
    /* 
     * Cette ligne indique quelle vue sera utilis� par cette activit�e. 
     * Vous trouverez cette vue dans res\layout\.
     */
    setContentView(R.layout.difficulty);
    
    /*
     * Dans Android, le passage d'une activit� � l'autre et donc implicitement
     * d'une page � l'autre se fait par "Intent". Un intention peut �tre directement
     * le nom de la classe ou nous voulons aller comme ici, ou une intention plus
     * g�n�rale comme "Une applciation capable de prendre des notes" ce qui laisse
     * � l'utilisateur le choix de choisir l'application de notes qu'il veut.
     */
    
    final Intent moveToPlay = new Intent(this, PlayContent.class);
        
    ((Button)findViewById(R.id.radioButton0)).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
			  try {
			    startActivity(moveToPlay.setAction("3"));
			  } catch(ActivityNotFoundException e) {
			    Log.wtf("Front Screen start button click.", "Activity not found.");
			  }
			}
		});
    ((Button)findViewById(R.id.radioButton1)).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
			  try {
			    startActivity(moveToPlay.setAction("2"));
			  } catch(ActivityNotFoundException e) {
			    Log.wtf("Front Screen start button click.", "Activity not found.");
			  }
			}
		});
    ((Button)findViewById(R.id.radioButton2)).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
			  try {
			    startActivity(moveToPlay.setAction("1"));
			  } catch(ActivityNotFoundException e) {
			    Log.wtf("Front Screen start button click.", "Activity not found.");
			  }
			}
		});
    ((Button)findViewById(R.id.radioButton3)).setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
			  try {
			    startActivity(moveToPlay.setAction("0"));
			  } catch(ActivityNotFoundException e) {
			    Log.wtf("Front Screen start button click.", "Activity not found.");
			  }
			}
		});
     
  }

}
