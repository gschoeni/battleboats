package com.gregschoeninger.battleboats;

public enum BoatType {
	PATROL_BOAT(2), SUBMARINE(3), DESTROYER(4), AIRCRAFT_CARRIER(5);
	public int size;
	
	private BoatType(int size) {
		this.size = size;
	}
}
