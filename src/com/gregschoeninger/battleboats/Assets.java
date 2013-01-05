package com.gregschoeninger.battleboats;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {

	public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture foregroundItems;
	
    
    // Suppress default constructor for noninstantiability
    private Assets() {
        throw new AssertionError();
    }
    
    public static void load(GLGame game) {
    	background = new Texture(game, "waffle.png");
    }

    public static void reload() {
        background.reload();
        foregroundItems.reload();
    }

}
