package com.gregschoeninger.battleboats;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

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
	
	
	public GameScreen(Battleboats game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		map = new Map();
		renderer = new GameRenderer(glGraphics, batcher, map);
	}

	@Override
	public void update(float deltaTime) {
		
	}
	
	private void updateReady(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_DOWN) return;
	    }
		
	}
	
	private void updateRunning(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        
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
