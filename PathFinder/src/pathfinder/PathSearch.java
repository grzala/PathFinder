/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Grzala
 */
public abstract class PathSearch {
    
    protected Point start, goal;
    
    protected ArrayList<Point> obstacles;
    protected ArrayList<Point> path;
    protected float cost;
    
    protected int cellsize;
    
    public PathSearch(Point start, Point goal, ArrayList<Point> obstacles, int cellsize) {
        reset(start, goal, obstacles, cellsize);
    }
    
    public PathSearch() {
        
    }
    
    public final void reset(Point start, Point goal, ArrayList<Point> obstacles, int cellsize) {
        this.start = start;
        this.goal = goal;
        this.obstacles = obstacles;
        this.cellsize = cellsize;  
        path = new ArrayList<>();
        cost = Float.MAX_VALUE;
    }
    
    public ArrayList<Point> getPath() {
        return path;
    }
    
    public float getCost() {
        return cost;
    }
    
    public abstract ArrayList<Point> performSearch();
    
    protected ArrayList<Point> getNext(Point current) {
        ArrayList<Point> toret = new ArrayList<>();
        
        Point W = new Point(current.x - cellsize, current.y);
        Point E = new Point(current.x + cellsize, current.y);
        Point S = new Point(current.x, current.y - cellsize);
        Point N = new Point(current.x, current.y + cellsize);
        boolean Woccupied = false, Eoccupied = false, Soccupied = false, Noccupied = false;
        
        Point WS = new Point(current.x - cellsize, current.y - cellsize);
        Point ES = new Point(current.x + cellsize, current.y - cellsize);
        Point WN = new Point(current.x - cellsize, current.y + cellsize);
        Point EN = new Point(current.x + cellsize, current.y + cellsize);
        boolean WSoccupied = false, ESoccupied = false, WNoccupied = false, ENoccupied = false;
        
        for (Point o : obstacles) {
            Rectangle Wrect = getRect(W, cellsize); Rectangle Erect = getRect(E, cellsize);
            Rectangle Srect = getRect(S, cellsize); Rectangle Nrect = getRect(N, cellsize);
            Rectangle WSrect = getRect(WS, cellsize); Rectangle ESrect = getRect(ES, cellsize);
            Rectangle WNrect = getRect(WN, cellsize); Rectangle ENrect = getRect(EN, cellsize);
            
            if (Wrect.contains(o)) Woccupied = true; if (Erect.contains(o)) Eoccupied = true;
            if (Srect.contains(o)) Soccupied = true; if (Nrect.contains(o)) Noccupied = true;
            if (WSrect.contains(o)) WSoccupied = true; if (ESrect.contains(o)) ESoccupied = true;
            if (WNrect.contains(o)) WNoccupied = true; if (ENrect.contains(o)) ENoccupied = true;
            
            if (Woccupied && Eoccupied && Soccupied && Noccupied &&
                WSoccupied && WNoccupied && ESoccupied && ENoccupied)
                break;   
        }
        
        if (!Woccupied)
            toret.add(W);
        if (!Eoccupied)
            toret.add(E);
        if (!Soccupied)
            toret.add(S);
        if (!Noccupied)
            toret.add(N);
        
        //can cut corners?
        if (!WSoccupied && !Woccupied && !Soccupied)
            toret.add(WS);
        if (!WNoccupied && !Woccupied && !Noccupied)
            toret.add(WN);
        if (!ESoccupied && !Eoccupied && !Soccupied)
            toret.add(ES);
        if (!ENoccupied && !Eoccupied && !Noccupied)
            toret.add(EN);
        
        return toret;
    }
    
    private Rectangle getRect(Point center, int size) { // just INT, make own rect
        return new Rectangle(center.x - size/2, center.y - size/2, size, size);
    }
    
    protected class PrioritisedPoint {
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
    
    protected class PPComparator implements Comparator<PrioritisedPoint> {
        
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
    
    protected float heur(Point p1, Point p2) {
        
        //diagonal heuristic
        float D = 1; //normal cost
        float D2 = (float)Math.sqrt(2); //diagonal cost same as normal//(float)Math.sqrt(2*(cellsize * cellsize));
        
        float dx = Math.abs(p1.x - p2.x);
        float dy = Math.abs(p1.y - p2.y);
        return D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);
    }
}
