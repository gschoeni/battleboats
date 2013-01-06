package com.gregschoeninger.battleboats;

import javax.microedition.khronos.opengles.GL10;

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
		
		batcher.drawSprite(cam.position.x, cam.position.y,
                FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                Assets.backgroundRegion);
		
		batcher.endBatch();
	}
	
	private void renderForeground() {
		GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.foregroundItems);
        
        renderDebugSquares();
        renderBoats();
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderBoats() {
//		for(Boat b : map.boats) {
//			for(GridSpace g : b.getGridSpaces()) {
//				batcher.drawSprite(g.bounds.lowerLeft.x, g.bounds.lowerLeft.y, GridSpace.WIDTH, GridSpace.HEIGHT, Assets.boat); 
//			}
//		}
		for(Boat b : map.boats) {
			batcher.drawSprite(b.getLowerLeftX(), b.getLowerLeftY(), b.width, b.height, Assets.boat);
		}
	}
	
	private void renderDebugSquares() {
		for(int i = 0; i < Map.MAP_WIDTH; i++) {
			for(int j = 0; j < Map.MAP_WIDTH; j++) {
				batcher.drawSprite(Map.gridSpaces[i][j].bounds.lowerLeft.x, Map.gridSpaces[i][j].bounds.lowerLeft.y, GridSpace.WIDTH, GridSpace.HEIGHT, Assets.square); 
			}
		}
	}
	

}
