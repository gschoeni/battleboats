package com.gregschoeninger.battleboats;

import java.util.ArrayList;

import com.badlogic.androidgames.framework.GameObject;

public class Boat extends GameObject {
	
	public int size;
	public int damage;
	public int row;
	public int col;
	public BoatType boatType;
	public BoatOrientation orientation;
	public int state;
	public static int VALID_SPACE = 0;
	public static int IS_BEING_DRAGGED = 1;
	
	public Boat(int row, int col, BoatType b, BoatOrientation o) {
		super(row*GridSpace.WIDTH + Map.x_offset, col*GridSpace.HEIGHT+Map.y_offset, GridSpace.WIDTH, GridSpace.HEIGHT);
		this.boatType = b;
		this.orientation = o;
		this.row = row;
		this.col = col;
		this.state = VALID_SPACE;
	}
	
	public ArrayList<GridSpace> getGridSpaces() {
		ArrayList<GridSpace> g = new ArrayList<GridSpace>();
		for(int i = 0; i < boatType.size; i++) {
			if (orientation == BoatOrientation.VERTICAL) {
				g.add(Map.gridSpaces[row][col+i]);
			} else {
				g.add(Map.gridSpaces[row+i][col]);
			}
		}
		return g;
	}
	
	public void setLocation(float x, float y) {
		position.set(x, y);
    	bounds.setLowerLeft(x, y);
	}
	
}
