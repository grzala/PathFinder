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
    float step;
    
    public Navigator() {
        start = new Point();
        goals = new ArrayList<>();
        obstacles = new ArrayList<>();
        paths = new ArrayList<>();
        
        start.x = 47; start.y = 270;
        goals.add(new Point(30, 40));
        goals.add(new Point(140, 40));
        goals.add(new Point(250, 40));
        
        step = 6.f;
    }
    
    public static float calculateDistance(ArrayList<Point> path) {
        float result = 0;
        
        for (int i = 1; i < path.size(); i++) {
            Point p1 = path.get(i-1);
            Point p2 = path.get(i);
            
            float dx = p2.x - p1.x;
            float dy = p2.y - p1.y;
            dx *= dx; dy *= dy;
            result += (float)Math.sqrt(dx + dy);
        }
        
        return result;
    }
    
    public static float calculateTotalDistance(ArrayList<ArrayList<Point>> paths) {
        float result = 0;
        
        for (ArrayList<Point> path : paths) 
            result += calculateDistance(path);
        
        return result;
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
    
    private boolean canAdd(int x, int y) {
        boolean add = true;
        for (Point o : obstacles) {
            Rectangle rect = new Rectangle(x - (int)step, y - (int)step, (int)step, (int)step);
            if (rect.contains(o)) {
                add = false;
                break;
            }
        }
        return add;
    }
    
    public boolean setStart(int x, int y) {
        if (canAdd(x, y)) {
            start = new Point(x, y);
            return true;
        } else { 
            System.out.println("can't add start, to close to obstacle");
            return false;
        }
    }    
    
    public Point getStart() {
        return start;
    }
    
    public void clear() {
        goals.clear();
        paths.clear(); /////////////////////////////// separate method?
    }
    
    public boolean addGoal(int x, int y) {
        if (canAdd(x, y)) {
            goals.add(new Point(x, y));
            return true;
        } else {
            System.out.println("Can't add goal: to close to an obstacle");
            return false;
        }
    }
    
    public void removeLastGoal() {
        if (goals.size() > 0) {
            goals.remove(goals.size()-1);
        }
    }
    
    public ArrayList<Point> getGoals() {
        return goals;
        /* //debugging purposes
        ArrayList<Point> r = new ArrayList<>(goals);
        r.addAll(obstacles);
        return r;
        */
    }
    
    public void setImage(String path) {
        this.oc = new OccupancyGrid(path, imgres);
        obstacles = new ArrayList<>(oc.getAsPoints());
    }
  
    public void performSearch() {
        paths.clear();
        Salesman s = new BruteForce(start, goals, obstacles, step);
        paths = s.performSearch();
    }
    
    public int[] getSize() {
        if (oc == null) 
            return new int[]{0, 0};
            
        int[] ar = {oc.width, oc.height};
        return ar;
    }
    
}
