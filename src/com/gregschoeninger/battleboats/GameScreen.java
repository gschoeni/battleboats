package com.gregschoeninger.battleboats;

import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.app.ProgressDialog;
import android.util.Log;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameScreen extends GLScreen {

	private Camera2D guiCam;
	private SpriteBatcher batcher;
	private GameRenderer renderer;
	private Map map;
	private Vector2 touchPoint;
	private SpeechConverter speechConverter;
	private static Battleboats game;
	private static ProgressDialog activityIndicator;
	
	public GameScreen(Battleboats g, String speechFile, String accessToken) {
		super(g);
		game = g;
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		map = new Map();
		renderer = new GameRenderer(glGraphics, batcher, map);
		touchPoint = new Vector2();
		speechConverter = new SpeechConverter(speechFile, accessToken);
	}

	@Override
	public void update(float deltaTime) {
		switch(Map.state) {
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
	        	game.runOnUiThread(new Runnable() {
	        	    public void run() {
	        	    	activityIndicator = game.doSpinner("Speak Now...");
	        	    }
	        	});
	        	speechConverter.buttonPressed();
	        	new java.util.Timer().schedule( 
	        	        new java.util.TimerTask() {
	        	            @Override
	        	            public void run() {
	        	            	game.runOnUiThread(new Runnable() {
	        		        	    public void run() {
	        		        	    	activityIndicator.setMessage("Thinking...");
	        		        	    }
	        		        	});
	        	            }
	        	        }, 
	        	        4000 
	        	);
	        }
	        if (touchedReadyButton(touchPoint)) {
	        	playAutoTurn();
	        }
	    }
		
	}
	
	public static void fireShot(String text, Coordinate c) {
		Log.d(Battleboats.DEBUG_TAG, "Fire! Text: "+text+" Coordinates: "+c);
		if (Map.theirGridSpaces[c.row][c.col].state == GridSpace.EMPTY) {
			if (Map.theirGridSpaces[c.row][c.col].boat == null) {
				Map.theirGridSpaces[c.row][c.col].state = GridSpace.MISS;
    		} else {
    			Map.theirGridSpaces[c.row][c.col].state = GridSpace.HIT;
    		}
		}
		
		game.runOnUiThread(new Runnable() {
    	    public void run() {
    	    	activityIndicator.hide();
    	    }
		});
	}
	
	public static void playAutoTurn() {
		Map.state = Map.GAME_OTHER_TURN;
		Random generator = new Random();
		int count = 0;
    	while(true) {
    		int randX = Math.abs(generator.nextInt()%(Map.MAP_WIDTH));
    		int randY = Math.abs(generator.nextInt()%(Map.MAP_HEIGHT));
    		if (Map.myGridSpaces[randX][randY].state == GridSpace.EMPTY) {
    			if (Map.myGridSpaces[randX][randY].boat == null) {
    				Map.myGridSpaces[randX][randY].state = GridSpace.MISS;
	    		} else {
	    			Map.myGridSpaces[randX][randY].state = GridSpace.HIT;
	    		}
    			break;
	    		//count++;
    		} 
    		//if (count == 64) break;
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
