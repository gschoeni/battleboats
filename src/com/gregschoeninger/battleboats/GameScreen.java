package com.gregschoeninger.battleboats;

import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameScreen extends GLScreen {

	private Camera2D guiCam;
	private SpriteBatcher batcher;
	private GameRenderer renderer;
	private Map map;
	private Vector2 touchPoint;
	private SpeechConverter speechConverter;
	
	
	public GameScreen(Battleboats game, String speechFile, String accessToken) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		map = new Map();
		renderer = new GameRenderer(glGraphics, batcher, map);
		touchPoint = new Vector2();
		speechConverter = new SpeechConverter(speechFile, accessToken);
	}

	@Override
	public void update(float deltaTime) {
		switch(map.state) {
			case Map.GAME_READY:
				updateSetup(deltaTime);
				break;
			case Map.GAME_ATTACK:
				updateAttack(deltaTime);
				break;
			case Map.GAME_OTHER_TURN:
				updateOtherTurn(deltaTime);
				break;
		}
		
	}
	
	private void updateSetup(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        if (touchedReadyButton(touchPoint)) {
	        	map.state = Map.GAME_ATTACK;
	        	break;
	        }
	        map.checkDraggingBoat(event, touchPoint);
		}
	}
	
	private void updateAttack(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_DOWN) return;
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        if (touchedAttackButton(touchPoint)) {
	        	//speechConverter.buttonPressed();
	        	map.state = Map.GAME_OTHER_TURN;
	        	playRandom();
	        }
	    }
		
	}
	
	private void playRandom() {
		Random generator = new Random();
		int count = 0;
    	while(true) {
    		int randX = Math.abs(generator.nextInt()%(Map.MAP_WIDTH));
    		int randY = Math.abs(generator.nextInt()%(Map.MAP_HEIGHT));
    		Log.d(Battleboats.DEBUG_TAG, "Hit:"+randX+" "+randY+" count: "+count);
    		if (map.myGridSpaces[randX][randY].state == GridSpace.EMPTY) {
    			if (map.myGridSpaces[randX][randY].boat == null) {
	    			map.myGridSpaces[randX][randY].state = GridSpace.MISS;
	    		} else {
	    			map.myGridSpaces[randX][randY].state = GridSpace.HIT;
	    		}
	    		count++;
    		} 
    		if (count == 64) break;
    		
    	}
	}
	
	private void updateOtherTurn(float deltaTime) {
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_DOWN) return;
	        map.state = Map.GAME_ATTACK;
		}
	}
	
	private void updatePaused(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        //touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        
	    }
	}
	
	private void showGameOver(float deltaTime) {
		
	}
	
	private void finishActivity() {
		glGame.finish();
	}
	
	private boolean touchedReadyButton(Vector2 p) {
		return p.x > 225 && p.y < 80;
	}
	
	private boolean touchedAttackButton(Vector2 p) {

    	Log.d(Battleboats.DEBUG_TAG, "update attack click"+p);
		return p.x < 100 && p.y < 80;
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderer.render();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//batcher.beginBatch(Assets.foregroundTexture);
		
		guiCam.setViewportAndMatrices();
		
		//batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
