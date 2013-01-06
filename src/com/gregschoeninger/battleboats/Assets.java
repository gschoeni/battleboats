package com.gregschoeninger.battleboats;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {

	public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture foregroundItems;
    
    public static TextureRegion patrol_boat;
    public static TextureRegion submarine;
    public static TextureRegion destroyer;
    public static TextureRegion aircraft;
    
    public static TextureRegion square;
    
    // Suppress default constructor for noninstantiability
    private Assets() {
        throw new AssertionError();
    }
    
    public static void load(GLGame game) {
    	background = new Texture(game, "MAININTERFACEGRID.png");
    	backgroundRegion = new TextureRegion(background, 0, 0, 640, 960);
    	
    	foregroundItems = new Texture(game, "PIECES-FOR-BATTLEBOATS.png");
    	
    	patrol_boat = new TextureRegion(foregroundItems, 0, 0, 100, 50);
    	submarine = new TextureRegion(foregroundItems, 0, 50, 150, 50);
    	destroyer = new TextureRegion(foregroundItems, 0, 100, 200, 50);
    	aircraft = new TextureRegion(foregroundItems, 0, 150, 250, 50);
    	
    	square = new TextureRegion(foregroundItems, 300, 0, 150, 150);
    }

    public static void reload() {
        background.reload();
        foregroundItems.reload();
    }

}
