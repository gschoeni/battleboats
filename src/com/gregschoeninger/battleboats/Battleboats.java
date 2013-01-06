package com.gregschoeninger.battleboats;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Battleboats extends GLGame {
	boolean firstTimeCreate = true;
	public static final String DEBUG_TAG = "BattleBoats";
	public static String SPEECH_FILE_NAME = "/speech.amr";

	@Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
	}
	
	@Override
	public Screen getStartScreen() {
        Log.d(Battleboats.DEBUG_TAG, "Path oncreate "+getFilesDir()+SPEECH_FILE_NAME + " "+ getIntent().getStringExtra("accessToken"));
        
		return new GameScreen(this, getFilesDir()+SPEECH_FILE_NAME, getIntent().getStringExtra("accessToken"));
	}
	
	@Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
    }
}

