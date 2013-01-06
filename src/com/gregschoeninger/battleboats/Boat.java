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
	
	public Boat(int row, int col, BoatType b, BoatOrientation o) {
		super(row*GridSpace.WIDTH, col*GridSpace.HEIGHT, GridSpace.WIDTH, GridSpace.HEIGHT);
		this.boatType = b;
		this.orientation = o;
		this.row = row;
		this.col = col;
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
	
}
