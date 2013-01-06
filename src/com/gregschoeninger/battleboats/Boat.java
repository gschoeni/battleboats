package com.gregschoeninger.battleboats;

import java.util.ArrayList;

import com.badlogic.androidgames.framework.GameObject;
import com.badlogic.androidgames.framework.gl.TextureRegion;

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
		super(row*GridSpace.WIDTH + Map.x_offset, col*GridSpace.HEIGHT+Map.y_offset, getWidthFromOrientation(b, o), getHeightFromOrientation(b, o));
		//super(row*GridSpace.WIDTH + Map.x_offset, col*GridSpace.HEIGHT+Map.y_offset, 35, 70);
		
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
	
	private static int getWidthFromOrientation(BoatType b, BoatOrientation o) {
		int width;
		if (o == BoatOrientation.VERTICAL) {
			width = GridSpace.WIDTH;
		} else {
			width = GridSpace.WIDTH * b.size;
		}
		return width;
	}
	
	private static int getHeightFromOrientation(BoatType b, BoatOrientation o) {
		int height;
		if (o == BoatOrientation.VERTICAL) {
			height = GridSpace.HEIGHT * b.size;
		} else {
			height = GridSpace.HEIGHT;
		}
		return height;
	}
	
}
