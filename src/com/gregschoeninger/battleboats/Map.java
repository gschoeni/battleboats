package com.gregschoeninger.battleboats;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Vector2;

public class Map {
	
	public static final int GAME_SETUP = 0;
	public static final int GAME_PLAYING_P1 = 1;
	public static final int GAME_PLAYING_P2 = 2;
	public static final int GAME_PAUSED = 3;
	public static final int GAME_OVER = 4;
	public static GridSpace[][] gridSpaces;
	public List<Boat> boats;
	
	public static int MAP_WIDTH = 8; 
	public static int MAP_HEIGHT = 8;
	public static int x_offset = 50;
	public static int y_offset = 150;
	
	public Map() {
		generateGridSpaces();
		generateBoats();
	}
	
	private void generateGridSpaces() {
		gridSpaces = new GridSpace[MAP_WIDTH][MAP_HEIGHT];
		
		int padding = 0;
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_WIDTH; j++) {
				GridSpace g = new GridSpace(x_offset + i * (GridSpace.WIDTH+padding), y_offset + j * (GridSpace.HEIGHT+padding), i, j);
				gridSpaces[i][j] = g;
			}
		}
	}
	
	private void generateBoats() {
		boats = new ArrayList<Boat>();
		
		// one patrol boat
		Boat patrol1 = new Boat(2, 3, BoatType.PATROL_BOAT, BoatOrientation.VERTICAL);
		for(GridSpace g : patrol1.getGridSpaces()) {
			g.boat = patrol1;
		}
		boats.add(patrol1);
		
		// one submarine
		Boat submarine = new Boat(0, 0, BoatType.SUBMARINE, BoatOrientation.HORIZONTAL);
		for(GridSpace g : submarine.getGridSpaces()) {
			g.boat = submarine;
		}
		boats.add(submarine);
		
		// one destroyer
		Boat destroyer = new Boat(6, 1, BoatType.DESTROYER, BoatOrientation.VERTICAL);
		for(GridSpace g : destroyer.getGridSpaces()) {
			g.boat = destroyer;
		}
		boats.add(destroyer);
		
		// aircraft carrier
		Boat aircraft = new Boat(1, 6, BoatType.AIRCRAFT_CARRIER, BoatOrientation.HORIZONTAL);
		for(GridSpace g : aircraft.getGridSpaces()) {
			g.boat = aircraft;
		}
		boats.add(aircraft);
	}
	
	public void checkDraggingBoat(TouchEvent event, Vector2 touchPoint) {
		float x = touchPoint.x + GridSpace.WIDTH/2;
		float y = touchPoint.y + GridSpace.HEIGHT/2;
		for(Boat b : boats) {
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(b.bounds, x, y)) {
				b.state = Boat.IS_BEING_DRAGGED;
			}
			if (event.type == TouchEvent.TOUCH_DRAGGED && b.state == Boat.IS_BEING_DRAGGED) {
				b.setLocation(x, y);
			} else if (event.type == TouchEvent.TOUCH_UP && b.state == Boat.IS_BEING_DRAGGED) {
				dropBoat(b);
			}
		}
	}
	
	private void dropBoat(Boat b) {
		b.state = Boat.VALID_SPACE;
		for(int i = 0; i < Map.MAP_WIDTH; i++) {
			for(int j = 0; j < Map.MAP_WIDTH; j++) {
				if (OverlapTester.pointInRectangle(gridSpaces[i][j].bounds, b.position)) {
					b.setLocation(gridSpaces[i][j].position.x, gridSpaces[i][j].position.y);
				}
			}
		}
	}
	
}
