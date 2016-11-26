/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *
 * @author grzala
 */
public class AStar {
    
    Point start;
    Point goal;
    
    ArrayList<Point> obstacles;
    
    int cellsize, minx, maxx, miny, maxy;
    
    public AStar(Point start, Point goal, ArrayList<Point> obstacles, Rectangle bounds, int cellsize) {
        this.start = start;
        this.goal = goal;
        this.obstacles = obstacles;
        this.cellsize = cellsize;   
        
        minx = bounds.x; miny = bounds.y; 
        maxx = minx + bounds.width; maxy = miny + bounds.height;
    }
    
    public ArrayList<Point> performSearch() {
        ArrayList<Point> path = new ArrayList<>();
        Comparator<PrioritisedPoint> comparator = new PPComparator();
        PriorityQueue<PrioritisedPoint> head = new PriorityQueue(16, comparator);
        
        head.add(new PrioritisedPoint(start, 0));
        
        HashMap<Point, Point> cameFrom = new HashMap<>();
        HashMap<Point, Float> costSoFar = new HashMap<>();
        
        cameFrom.put(start, null);
        costSoFar.put(start, 0.0f);
        
        Rectangle goalRect = new Rectangle(goal.x - cellsize/2, goal.y - cellsize/2, cellsize, cellsize);
        
        while (head.size() > 0) {
            Point current = head.poll().toPoint();
            
            if (goalRect.contains(current)) {//goal reached
                if (current != goal) 
                    cameFrom.put(goal, current);
                break;
            }
                
            for (Point next : getNext(current)) {
                float newCost = costSoFar.get(current) + dist(current, next);
                
                if (costSoFar.get(next) == null || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    float priority = newCost + dist(goal, next);
                    head.add(new PrioritisedPoint(next, priority));
                    cameFrom.put(next, current);
                }
            }
            //path.add(current);
        }
        
        Point p = goal;
        while (p != null) {
            path.add(p);
            p = cameFrom.get(p);
        }
        path.add(start);
        
        ArrayList<Point> reverse = new ArrayList<>();
        for (int i = path.size()-1; i >= 0; i--) {
            reverse.add(path.get(i));
        }
        path = reverse;
        
        return path;
    }
    
    private float dist(Point p1, Point p2) {
        int x1 = p1.x, x2 = p2.x;
        int y1 = p1.y, y2 = p2.y;
        float distance = (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
        return distance;
    }
    
    private ArrayList<Point> getNext(Point current) {
        ArrayList<Point> toret = new ArrayList<>();
        
        if (!(current.x - cellsize <= minx)) 
            toret.add(new Point(current.x - cellsize, current.y));
        
        if (!(current.x + cellsize >= maxx)) 
            toret.add(new Point(current.x + cellsize, current.y));
        
        if (!(current.y - cellsize <= miny)) 
            toret.add(new Point(current.x, current.y - cellsize));
        
        if (!(current.y + cellsize >= maxx))
            toret.add(new Point(current.x, current.y + cellsize));
        
        //exclude occupied
        ArrayList<Point> n = new ArrayList<>();
        for (Point p : toret){
            boolean add = true;
            Rectangle rect = new Rectangle();
            rect.x = p.x - cellsize/2; rect.y = p.y - cellsize/2;
            rect.width = cellsize; rect.height = cellsize;
            for (Point obstacle : obstacles) {
                if (rect.contains(obstacle)) {
                    add = false;
                    break;
                }
            }
            if (add) 
                n.add(p);
        }
        
        toret = n;
        
        return toret;
    }
    
    private class PrioritisedPoint {
        public Point point;
        public float priority;
        
        public PrioritisedPoint(Point p, float pr) {
            point = p;
            priority = pr;
        }
        
        public Point toPoint() {
            return point;
        }
    }
    
    private class PPComparator implements Comparator<PrioritisedPoint> {
        
        @Override
        public int compare(PrioritisedPoint p1, PrioritisedPoint p2) {
            if (p1.priority < p2.priority) {
                return -1;
            } else if (p1.priority > p2.priority) {
                return 1;
            } else {
                return 0;
            }
        }
        
    }
    
}
