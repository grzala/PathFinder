/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author grzala
 */
public class Navigator {
    
    private OccupancyGrid oc;
    
    private Point start;
    private ArrayList<Point> goals;
    
    private ArrayList<Point> obstacles;
    
    private ArrayList<ArrayList<Point>> paths;
    
    int imgres = 2; //pixels;
    
    public Navigator() {
        start = new Point();
        goals = new ArrayList<>();
        obstacles = new ArrayList<>();
        paths = new ArrayList<>();
        
        start.x = 47; start.y = 270;
        goals.add(new Point(30, 40));
        goals.add(new Point(140, 40));
        goals.add(new Point(250, 40));
        
    }
    
    public ArrayList<ArrayList<Point>> getLines() {
        ArrayList<ArrayList<Point>> ls = new ArrayList<>();
        
        for (ArrayList<Point> path : paths) {
            ArrayList<Point> ps = new ArrayList<>();
            for (Point p : path)
                ps.add(p);
            ls.add(ps);
        }
        
        return ls;
    }
    
    public void setStart(int x, int y) {
        start = new Point(x, y);
    }    
    
    public Point getStart() {
        return start;
    }
    
    public void clearGoals() {
        goals.clear();
        paths.clear(); ///////////////////////////////
    }
    
    public void addGoal(int x, int y) {
        goals.add(new Point(x, y));
    }
    
    public void removeLastGoal() {
        if (goals.size() > 0) {
            goals.remove(goals.size()-1);
        }
    }
    
    public ArrayList<Point> getGoals() {
        return goals;
    }
    
    public void setImage(String path) {
        this.oc = new OccupancyGrid(path, imgres);
        
        obstacles = new ArrayList<>(oc.getAsPoints());
    }
  
    public void performSearch() {
        Rectangle bounds = new Rectangle(oc.originX, oc.originY, oc.width, oc.height);
        AStar astar = new AStar(start, goals.get(0), obstacles, bounds, 6);
        
        ArrayList<Point> path;
        path = astar.performSearch();
        paths.add(path);
    }
    
    public int[] getSize() {
        if (oc == null) 
            return new int[]{0, 0};
            
        int[] ar = {oc.width, oc.height};
        return ar;
    }
    
}
