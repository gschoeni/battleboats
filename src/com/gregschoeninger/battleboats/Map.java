package com.gregschoeninger.battleboats;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Vector2;

public class Map {
	
	public static final int GAME_READY = 0;
	public static final int GAME_ATTACK = 1;
	public static final int GAME_OTHER_TURN = 2;
	public static final int GAME_PAUSED = 3;
	public static final int GAME_OVER = 4;
	public static int state;
	public static GridSpace[][] myGridSpaces;
	public static GridSpace[][] theirGridSpaces;
	public List<Boat> myBoats;
	public List<Boat> theirBoats;
	
	public static int MAP_WIDTH = 8; 
	public static int MAP_HEIGHT = 8;
	public static int x_offset = 79;
	public static int y_offset = 142;
	
	public Map() {
		generateGridSpaces();
		generateBoats();
		state = GAME_READY;
	}
	
	private void generateGridSpaces() {
		myGridSpaces = new GridSpace[MAP_WIDTH][MAP_HEIGHT];
		theirGridSpaces = new GridSpace[MAP_WIDTH][MAP_HEIGHT];
		
		int padding = 0;
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_WIDTH; j++) {
				GridSpace g = new GridSpace(x_offset + i * (GridSpace.WIDTH+padding), y_offset + j * (GridSpace.HEIGHT+padding), i, j);
				GridSpace g1 = new GridSpace(x_offset + i * (GridSpace.WIDTH+padding), y_offset + j * (GridSpace.HEIGHT+padding), i, j);
				myGridSpaces[i][j] = g;
				theirGridSpaces[i][j] = g1;
			}
		}
	}
	
	private void generateBoats() {
		myBoats = new ArrayList<Boat>();
		theirBoats = new ArrayList<Boat>();
		
		// one patrol boat
		Boat patrol1 = new Boat(2, 2, BoatType.PATROL_BOAT, BoatOrientation.VERTICAL, Assets.patrol_boat_horizontal, Assets.patrol_boat_vertical);
		for(GridSpace g : patrol1.getGridSpaces(myGridSpaces)) {
			g.boat = patrol1;
		}
		for(GridSpace g : patrol1.getGridSpaces(theirGridSpaces)) {
			g.boat = patrol1;
		}
		myBoats.add(patrol1);
		theirBoats.add(patrol1);
		
		// one submarine
		Boat submarine = new Boat(0, 0, BoatType.SUBMARINE, BoatOrientation.HORIZONTAL, Assets.submarine_horizontal, Assets.submarine_vertical);
		for(GridSpace g : submarine.getGridSpaces(myGridSpaces)) {
			g.boat = submarine;
		}
		for(GridSpace g : submarine.getGridSpaces(theirGridSpaces)) {
			g.boat = submarine;
		}
		myBoats.add(submarine);
		theirBoats.add(submarine);
		
		// one destroyer
		Boat destroyer = new Boat(7, 3, BoatType.DESTROYER, BoatOrientation.VERTICAL, Assets.destroyer_horizontal, Assets.destroyer_vertical);
		for(GridSpace g : destroyer.getGridSpaces(myGridSpaces)) {
			g.boat = destroyer;
		}
		for(GridSpace g : destroyer.getGridSpaces(theirGridSpaces)) {
			g.boat = destroyer;
		}
		myBoats.add(destroyer);
		theirBoats.add(destroyer);
		
		// aircraft carrier
		Boat aircraft = new Boat(1, 6, BoatType.AIRCRAFT_CARRIER, BoatOrientation.HORIZONTAL, Assets.aircraft_horizontal, Assets.aircraft_vertical);
		for(GridSpace g : aircraft.getGridSpaces(myGridSpaces)) {
			g.boat = aircraft;
		}
		for(GridSpace g : aircraft.getGridSpaces(theirGridSpaces)) {
			g.boat = aircraft;
		}
		myBoats.add(aircraft);
		theirBoats.add(aircraft);
	}
	
	public void checkDraggingBoat(TouchEvent event, Vector2 touchPoint) {
		float x = touchPoint.x + GridSpace.WIDTH/2;
		float y = touchPoint.y + GridSpace.HEIGHT/2;
		
		for(Boat b : myBoats) {
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(b.bounds, x, y)) {
				Log.d(Battleboats.DEBUG_TAG, "TOUCHED "+b.bounds);
				b.state = Boat.IS_BEING_DRAGGED;
				for(GridSpace g : b.getGridSpaces(myGridSpaces)) {
					g.boat = null;
				}
				break;
			}
			if (event.type == TouchEvent.TOUCH_DRAGGED && b.state == Boat.IS_BEING_DRAGGED) {
				b.setLocation(x, y, -1, -1);
			} else if (event.type == TouchEvent.TOUCH_UP && b.state == Boat.IS_BEING_DRAGGED) {
				dropBoat(b);
			}
		}
	}
	
	public void checkRotateBoat(TouchEvent event, Vector2 touchPoint) {
		float x = touchPoint.x + GridSpace.WIDTH/2;
		float y = touchPoint.y + GridSpace.HEIGHT/2;
		
		for(Boat b : myBoats) {
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(b.bounds, x, y)) {
				b.orientation = BoatOrientation.VERTICAL;
				int temp = b.height;
				b.height = b.width;
				b.width = temp;
				break;
			}
		}
	}
	
	private void dropBoat(Boat b) {
		b.state = Boat.VALID_SPACE;
		for(int i = 0; i < Map.MAP_WIDTH; i++) {
			for(int j = 0; j < Map.MAP_WIDTH; j++) {
				if (OverlapTester.pointInRectangle(myGridSpaces[i][j].bounds, b.position)) {
					b.setLocation(myGridSpaces[i][j].position.x, myGridSpaces[i][j].position.y, i, j);
					for(GridSpace g : b.getGridSpaces(myGridSpaces)) {
						g.boat = b;
					}
				}
			}
		}
	}
	
}
