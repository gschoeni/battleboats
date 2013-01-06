package com.gregschoeninger.battleboats;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {

	public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture foregroundItems;
    public static TextureRegion boat;
    public static TextureRegion square;
    
    // Suppress default constructor for noninstantiability
    private Assets() {
        throw new AssertionError();
    }
    
    public static void load(GLGame game) {
    	background = new Texture(game, "battleboats-sprites.png");
    	backgroundRegion = new TextureRegion(background, 150, 0, 150, 150);
    	
    	foregroundItems = new Texture(game, "battleboats-sprites.png");
    	boat = new TextureRegion(foregroundItems, 0, 0, 150, 150);
    	square = new TextureRegion(foregroundItems, 300, 0, 150, 150);
    }

    public static void reload() {
        background.reload();
        foregroundItems.reload();
    }

}
