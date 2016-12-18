/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Grzala
 */
public class CellList {
    
    private float cellsize;
    private ArrayList<Point> obstacles;
    private float minx, miny, maxx, maxy;
    
    private HashMap<Float, ArrayList<Cell>> list;
    private ArrayList<Float> sortedKeys;
    
    public CellList(ArrayList<Point> obstacles, float cellsize, float[] size) {
        this.obstacles = obstacles;
        this.cellsize = cellsize;
        
        this.minx = size[0]; this.miny = size[1];
        this.maxx = size[2]; this.maxy = size[3];
        
        list = new HashMap<>();
        
        construct();
        
        if (minx == maxx || miny == maxy) 
            list = null;
    }

    CellList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void construct() {
        float x = minx;
        float y = miny;
        
        ArrayList<Cell> cells = new ArrayList<>();
        
        while (y + cellsize < maxy) {
            list.put(y, new ArrayList<>());
            while (x + cellsize < maxx) {
                Cell cell = new Cell(x, y, cellsize);
                cells.add(cell);
                x += cellsize;
            }
            x = minx;
            y += cellsize;
        }
        
        for (Cell c : cells) {
            
            boolean add = true;
            for (Point p : obstacles) {
                if (c.contains(p)) {
                    add = false;
                    break;
                }
            }
            
            if (add)
                list.get(c.y).add(c);
        }
        
        sortedKeys = new ArrayList<>(list.keySet());
        Collections.sort(sortedKeys);
        
        buildNeighbourGrid();
    }
    
    private void buildNeighbourGrid() {
        for (float key : list.keySet()) {
            for (Cell c : list.get(key)) {
                c.addAllNeighbours(findNeighbours(c));
            }
        }
    }
    
    public ArrayList<Point> getCellCenters() {
        ArrayList<Point> toret = new ArrayList<>();
        for (float key : sortedKeys) {
            for (Cell c : list.get(key)) {
                toret.add(new Point((int)(c.x + cellsize/2), (int)(c.y + cellsize/2)));
            }
        }
        return toret;
    }
    
    
    public Cell getCell(Point p) {
        return getCell((float)p.x, (float)p.y);
    }
    
    public Cell getCell(float x, float y) {
        float key = findKey(y);
        
        if (key < 0) return null;
        
        Cell cell = null;
        for(int i = 0; i < list.get(key).size(); i++) {
            float xx = list.get(key).get(i).x;
            float nextx = xx + cellsize;
            
            if (x >= xx && x < nextx) {
                cell = list.get(key).get(i);
                break;
            }
        }
        return cell;
    }
    
    public ArrayList<Point> findNeighbouringCellCenters(Cell cell) {
        ArrayList<Point> toret = new ArrayList<>();
        for (Cell c : findNeighbours(cell)) {
            toret.add(c.getCenter());
        }
        return toret;
    }
    
    public ArrayList<Cell> getNeighbours(Point p) {
        return getNeighbours(getCell(p));
    }
    
    public ArrayList<Cell> getNeighbours(Cell c) {
        return c.getNeighbours();
    }
    
    public ArrayList<Point> getNeighbouringCellCenters(Point p) {
        ArrayList<Point> toret = new ArrayList<>();
        for (Cell c : getNeighbours(p)) {
            toret.add(c.getCenter());
        }
        return toret;
    }
    
    public ArrayList<Cell> findNeighbours(Point p) {
        return findNeighbours(getCell(p));
    }
    
    public ArrayList<Cell> findNeighbours(Cell c) {
        ArrayList<Cell> toret = new ArrayList<>();
        
        float key = findKey(c.y);
        if (key < 0) return toret; //no neighbours
        
        ArrayList<Cell> toSearch = new ArrayList<>();
        toSearch.addAll(list.get(key));
        if (key > sortedKeys.get(0)) toSearch.addAll(list.get(key-cellsize));
        if (key < sortedKeys.get(sortedKeys.size()-1)) toSearch.addAll(list.get(key+cellsize));
        
        for (Cell cell : toSearch) {
            if (cell == c) continue;
            if (cell.x >= c.x - cellsize && cell.x <= c.x + cellsize)
                toret.add(cell);
        }
        
        return toret;
    }
    
    private float findKey(float y) {
        float key = -1;
        for (int i = 0; i < sortedKeys.size(); i++) {
            float k = sortedKeys.get(i);
            float nextk = k + cellsize;
            if (y >= k && y < nextk) {
                key = k;
                break;
            }
        }
        return key;
    }
    
    public boolean isNull() {
        return list == null;
    }
    
}
