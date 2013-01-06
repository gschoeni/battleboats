package com.gregschoeninger.battleboats;

import com.badlogic.androidgames.framework.GameObject;

public class GridSpace extends GameObject {
	
	public static int WIDTH = 33;
	public static int HEIGHT = 33;
	public int row;
	public int col;
	public static final int HIT = 0;
	public static final int MISS = 1;
	public static final int EMPTY = 2;
	public int state;
	public Boat boat;
	
	public GridSpace(float x, float y, int row, int col) {
		super(x, y, WIDTH, HEIGHT);
		this.row = row;
		this.col = col;
		this.boat = null;
		this.state = EMPTY;
	}
}
