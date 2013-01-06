package com.gregschoeninger.battleboats;

class Coordinate {
    int row;
    int col;
    
    Coordinate(int r, int c) {
        this.row = r;
        this.col = c;
    }
    
    public String toString() {
        return row + " " + col;
    }
}
