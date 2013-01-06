package com.gregschoeninger.battleboats;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {

	public static Texture background;
    public static TextureRegion backgroundReady;
    public static TextureRegion backgroundAttack;
    public static TextureRegion backgroundOtherTurn;
    
    public static Texture foregroundItems;
    
    public static TextureRegion patrol_boat_horizontal;
    public static TextureRegion submarine_horizontal;
    public static TextureRegion destroyer_horizontal;
    public static TextureRegion aircraft_horizontal;
    
    public static TextureRegion patrol_boat_vertical;
    public static TextureRegion submarine_vertical;
    public static TextureRegion destroyer_vertical;
    public static TextureRegion aircraft_vertical;
    
    public static TextureRegion square;
    
    // Suppress default constructor for noninstantiability
    private Assets() {
        throw new AssertionError();
    }
    
    public static void load(GLGame game) {
    	background = new Texture(game, "boards.png");
    	backgroundReady = new TextureRegion(background, 0, 0, 640, 960);
    	backgroundAttack = new TextureRegion(background, 640, 0, 640, 960);
    	backgroundOtherTurn = new TextureRegion(background, 1280, 0, 640, 960);
    	
    	foregroundItems = new Texture(game, "PIECES-FOR-BATTLEBOATS.png");
    	
    	patrol_boat_horizontal = new TextureRegion(foregroundItems, 0, 0, 100, 50);
    	submarine_horizontal = new TextureRegion(foregroundItems, 0, 50, 150, 50);
    	destroyer_horizontal = new TextureRegion(foregroundItems, 0, 100, 200, 50);
    	aircraft_horizontal = new TextureRegion(foregroundItems, 0, 150, 250, 50);
    	
    	patrol_boat_vertical = new TextureRegion(foregroundItems, 0, 200, 50, 250);
    	submarine_vertical = new TextureRegion(foregroundItems, 50, 250, 50, 200);
    	destroyer_vertical = new TextureRegion(foregroundItems, 100, 300, 50, 150);
    	aircraft_vertical = new TextureRegion(foregroundItems, 150, 350, 50, 100);
    	
    	square = new TextureRegion(foregroundItems, 300, 0, 150, 150);
    }

    public static void reload() {
        background.reload();
        foregroundItems.reload();
    }

}
