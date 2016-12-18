/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.util.ArrayList;

    
public class Cell {

    float x, y;
    float cellsize;
    
    private ArrayList<Cell> neighbours;

    public Cell(float x, float y, float cellsize) {
        this.x = x;
        this.y = y;
        this.cellsize = cellsize;
        neighbours = new ArrayList<Cell>();
    }

    public boolean contains(float x, float y) {
        return x >= this.x && y >= this.y && x <= this.x + cellsize && y <= this.y + cellsize;
    }

    public boolean contains(Point p) {
        return contains((int)p.x, (int)p.y);
    }
    
    public Point getCenter() {
        return new Point((int)(x + cellsize/2), (int)(y + cellsize/2));
    }
    
    public ArrayList<Cell> getNeighbours() {
        return neighbours;
    }
    
    public void addNeighbour(Cell c) {
        neighbours.add(c);
    }
    
    public void addAllNeighbours(ArrayList<Cell> ns) {
        neighbours.addAll(ns);
    }
}