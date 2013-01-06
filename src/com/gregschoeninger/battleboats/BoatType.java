package com.gregschoeninger.battleboats;

import android.util.Log;

import com.badlogic.androidgames.framework.gl.TextureRegion;

public enum BoatType {
	PATROL_BOAT(2, Assets.patrol_boat), SUBMARINE(3, Assets.submarine), DESTROYER(4, Assets.destroyer), AIRCRAFT_CARRIER(5, Assets.aircraft);
	public int size;
	public TextureRegion asset = Assets.submarine;
	
	private BoatType(int size, TextureRegion asset) {
		this.size = size;
		this.asset = Assets.submarine;
	}
	
	public String toString() {
		return "Asset: "+this.asset;
	}
}
