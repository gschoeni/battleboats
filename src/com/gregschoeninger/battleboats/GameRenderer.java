package com.gregschoeninger.battleboats;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLGraphics;
import com.badlogic.androidgames.framework.math.Rectangle;

public class GameRenderer {
	
	static final float FRUSTUM_WIDTH = 320;
	static final float FRUSTUM_HEIGHT = 480;
	GLGraphics glGraphics;
	Camera2D cam;
	SpriteBatcher batcher;
	
	public GameRenderer(GLGraphics g, SpriteBatcher batcher){
		this.glGraphics = g;
		this.batcher = batcher;
		this.cam = new Camera2D(g, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
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
		
		batcher.endBatch();
	}
	
	private void renderForeground() {
		GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.foregroundItems);
        
        
        
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
	}
	
	

}
