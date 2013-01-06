package com.gregschoeninger.battleboats;

import java.util.ArrayList;

import android.util.Log;

import com.badlogic.androidgames.framework.GameObject;
import com.badlogic.androidgames.framework.gl.TextureRegion;

public class Boat extends GameObject {
	
	public int damage;
	public int row;
	public int col;
	public int width;
	public int height;
	public BoatType boatType;
	public BoatOrientation orientation;
	public int state;
	public static int VALID_SPACE = 0;
	public static int IS_BEING_DRAGGED = 1;
	public TextureRegion horizontalTexture;
	private TextureRegion verticalTexture;
	
	public Boat(int row, int col, BoatType b, BoatOrientation o, TextureRegion horizontal, TextureRegion vertical) {
		super(row*GridSpace.WIDTH + Map.x_offset, col*GridSpace.HEIGHT+Map.y_offset, GridSpace.WIDTH, GridSpace.HEIGHT);
		this.boatType = b;
		this.orientation = o;
		this.row = row;
		this.col = col;
		this.width = widthFromOrientation(o);
		this.height = heightFromOrientation(o);
		this.state = VALID_SPACE;
		Log.d(Battleboats.DEBUG_TAG, "Horizontal: "+horizontal);
		this.horizontalTexture = horizontal;
		this.verticalTexture = vertical;
	}
	
	private int widthFromOrientation(BoatOrientation o) {
		if (o == BoatOrientation.HORIZONTAL) {
			return this.boatType.size * GridSpace.WIDTH;
		} else {
			return GridSpace.WIDTH;
		}
	}

	private int heightFromOrientation(BoatOrientation o) {
		if (o == BoatOrientation.VERTICAL) {
			return this.boatType.size * GridSpace.WIDTH;
		} else {
			return GridSpace.WIDTH;
		}
	}
	
	public float getLowerLeftX() {
		if (this.orientation == BoatOrientation.HORIZONTAL) {
			int scale = boatType.size / 2;
			return this.bounds.lowerLeft.x + scale * GridSpace.WIDTH;
		} else {
			return this.bounds.lowerLeft.x;
		}
	}
	
	public float getLowerLeftY() {
		if (this.orientation == BoatOrientation.HORIZONTAL) {
			return this.bounds.lowerLeft.y;
		} else {
			int offset = 0;
			if (boatType == BoatType.DESTROYER) {
				offset = GridSpace.WIDTH+7;
			}
			return this.bounds.lowerLeft.y + GridSpace.WIDTH/boatType.size+offset;
		}
	}
	
	public ArrayList<GridSpace> getGridSpaces(GridSpace[][] gridSpaces) {
		ArrayList<GridSpace> g = new ArrayList<GridSpace>();
		for(int i = 0; i < boatType.size; i++) {
			if (orientation == BoatOrientation.VERTICAL) {
				g.add(gridSpaces[row][col+i]);
			} else {
				g.add(gridSpaces[row+i][col]);
			}
		}
		return g;
	}
	
	public void setLocation(float x, float y, int row, int col) {
		position.set(x, y);
    	bounds.setLowerLeft(x, y);
    	this.row = row;
    	this.col = col;
	}
	
	public TextureRegion getTextureRegion() {
		if (orientation == BoatOrientation.VERTICAL) {
			return verticalTexture;
		} else {
			return horizontalTexture;
		}
	}
	
}