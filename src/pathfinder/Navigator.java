/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
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
    
    public ArrayList<ColoredPoint> getPoints() {
        ArrayList<ColoredPoint> cps = new ArrayList<>();
        
        cps.add(new ColoredPoint(start, Color.RED));
        
        for (Point p : goals) 
            cps.add(new ColoredPoint(p, Color.BLUE));
        
        for (Point p : obstacles)
            cps.add(new ColoredPoint(p, Color.BLACK));
        
        for (ArrayList<Point> path : paths) 
            for (Point p : path)
                cps.add(new ColoredPoint(p, Color.GREEN));
        
        return cps;
    }
    
    public void setStart(int x, int y) {
        start = new Point(x, y);
    }    
    
    public void clearGoals() {
        goals.clear();
    }
    
    public void addGoal(int x, int y) {
        goals.add(new Point(x, y));
    }
    
    public void setImage(String path) {
        this.oc = new OccupancyGrid(path, imgres);
        
        int x = oc.originX; int y = oc.originY;
        
        //for debug
        //obsctacles = new ArrayList<>(oc.getAsPoints());
    }
    
    public void performSearch() {
        Rectangle bounds = new Rectangle(oc.originX, oc.originY, oc.width, oc.height);
        AStar astar = new AStar(start, goals.get(0), obstacles, bounds, imgres);
        
        ArrayList<Point> path;
        path = astar.performSearch();
        paths.add(path);
    }
    
}
