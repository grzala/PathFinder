/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinding;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import pathfinder.CellList;

/**
 *
 * @author Grzala
 */
public class DijkstraPath extends PathSearch {
    
    public DijkstraPath(Point start, Point goal, ArrayList<Point> obstacles,CellList clist, int cellsize) {
        super(start, goal, obstacles, clist, cellsize);
    }
    
    public DijkstraPath() {
        
    }
    
    @Override
    public ArrayList<Point> performSearch() {
        ArrayList<Point> path = new ArrayList<Point>();
        Comparator<PrioritisedPoint> comparator = new PPComparator();
        PriorityQueue<PrioritisedPoint> head = new PriorityQueue(16, comparator);
        
        HashMap<Point, Point> cameFrom = new HashMap<Point, Point>();
        HashMap<Point, Float> costSoFar = new HashMap<Point, Float>();
        
        head.add(new PrioritisedPoint(start, 0.f));
        cameFrom.put(start, null);
        costSoFar.put(start, 0.0f);
        
        Rectangle goalRect = getRect(goal, cellsize);
        while (head.size() > 0) {
            
            Point current = head.poll().toPoint();
            
            if (goalRect.contains(current)) {//goal reached
                if (current != goal) {
                    float c = costSoFar.get(current);
                    current = cameFrom.get(current); //the current one is might make path longer
                    costSoFar.put(current, c);
                    cameFrom.put(goal, current);
                    float newCost = costSoFar.get(current) + heur(current, goal);
                    costSoFar.put(goal, newCost);
                }
                break;
            }
            
            for (Point next : getNext(current)) {
                if (costSoFar.get(next) == null) 
                    costSoFar.put(next, Float.MAX_VALUE);
                
                float alt = costSoFar.get(current) + heur(current, next);
                
                if (alt < costSoFar.get(next)) {
                    costSoFar.put(next, alt);
                    cameFrom.put(next, current);
                    head.add(new PrioritisedPoint(next, alt));
                }
            }
        }
        
        if (cameFrom.get(goal) == null) { // no path
            System.out.println("No path found");
            return path;
        }
        
        Point p = goal;
        while (p != null) {
            path.add(p);
            p = cameFrom.get(p);
        }
        
        ArrayList<Point> reverse = new ArrayList<Point>();
        for (int i = path.size()-1; i >= 0; i--) {
            reverse.add(path.get(i));
        }
        path = reverse;
        
        //Add remove points that are in line; //put these things above in PathSearch as protected methods
        //timing
        
        this.path = path;
        this.cost = costSoFar.get(goal);
        
        return path;
    }
    
}
