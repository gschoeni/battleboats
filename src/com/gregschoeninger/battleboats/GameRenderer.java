package com.gregschoeninger.battleboats;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLGraphics;

public class GameRenderer {
	
	static final float FRUSTUM_WIDTH = 320;
	static final float FRUSTUM_HEIGHT = 480;
	GLGraphics glGraphics;
	Camera2D cam;
	SpriteBatcher batcher;
	Map map;
	
	public GameRenderer(GLGraphics g, SpriteBatcher batcher, Map m){
		this.glGraphics = g;
		this.batcher = batcher;
		this.cam = new Camera2D(g, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.map = m;
	}
	
	public void render() {
        cam.setViewportAndMatrices();
        renderBackground();
        renderForeground();
    }
	
	private void renderBackground() {
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
        
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.background);
		
		switch(map.state) {
			case Map.GAME_READY:
				batcher.drawSprite(cam.position.x, cam.position.y,
		                FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
		                Assets.backgroundReady);
				break;
			case Map.GAME_ATTACK:
				batcher.drawSprite(cam.position.x, cam.position.y,
		                FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
		                Assets.backgroundAttack);
				break;
			case Map.GAME_OTHER_TURN:
				batcher.drawSprite(cam.position.x, cam.position.y,
		                FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
		                Assets.backgroundOtherTurn);
				break;
		}
		
		batcher.endBatch();
	}
	
	private void renderForeground() {
		GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.foregroundItems);
        renderDebugSquares();
        
        switch(Map.state) {
			case Map.GAME_READY:
				renderBoats();
				break;
			case Map.GAME_ATTACK:
				// renderBoats();
				renderHitsAndMisses(Map.theirGridSpaces);
				break;
			case Map.GAME_OTHER_TURN:
				renderBoats();
				renderHitsAndMisses(Map.myGridSpaces);
				break;
        }
        
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderHitsAndMisses(GridSpace[][] gridSpaces) {
		for(int i = 0; i < Map.MAP_WIDTH; i++) {
			for(int j = 0; j < Map.MAP_WIDTH; j++) {
				if (gridSpaces[i][j].state == GridSpace.HIT) {
					batcher.drawSprite(gridSpaces[i][j].bounds.lowerLeft.x, gridSpaces[i][j].bounds.lowerLeft.y, GridSpace.WIDTH, GridSpace.HEIGHT, Assets.hit);	
				} else if (gridSpaces[i][j].state == GridSpace.MISS) {
					batcher.drawSprite(gridSpaces[i][j].bounds.lowerLeft.x, gridSpaces[i][j].bounds.lowerLeft.y, GridSpace.WIDTH, GridSpace.HEIGHT, Assets.miss);
				}
			}
		}
	}
	
	private void renderBoats() {
		for(Boat b : map.myBoats) {
			switch(b.boatType.size) {
				case 2:
					
					batcher.drawSprite(b.getLowerLeftX(), b.getLowerLeftY(), b.width, b.height, b.orientation == BoatOrientation.VERTICAL ? Assets.patrol_boat_vertical : Assets.patrol_boat_horizontal);
					break;
				case 3:
					batcher.drawSprite(b.getLowerLeftX(), b.getLowerLeftY(), b.width, b.height, b.orientation == BoatOrientation.VERTICAL ? Assets.submarine_vertical : Assets.submarine_horizontal);
					break;
				case 4:
					batcher.drawSprite(b.getLowerLeftX(), b.getLowerLeftY(), b.width, b.height, b.orientation == BoatOrientation.VERTICAL ? Assets.destroyer_vertical : Assets.destroyer_horizontal);
					break;
				case 5:
					batcher.drawSprite(b.getLowerLeftX(), b.getLowerLeftY(), b.width, b.height, b.orientation == BoatOrientation.VERTICAL ? Assets.aircraft_vertical : Assets.aircraft_horizontal);
					break;
			}
		}
	}
	
	private void renderDebugSquares() {
		for(int i = 0; i < Map.MAP_WIDTH; i++) {
			for(int j = 0; j < Map.MAP_WIDTH; j++) {
				batcher.drawSprite(Map.myGridSpaces[i][j].bounds.lowerLeft.x, Map.myGridSpaces[i][j].bounds.lowerLeft.y, GridSpace.WIDTH, GridSpace.HEIGHT, Assets.square); 
			}
		}
	}
	

}
