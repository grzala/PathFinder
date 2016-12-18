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
 * @author grzala
 */
public class AStar extends PathSearch {
    
    public AStar(Point start, Point goal, ArrayList<Point> obstacles, CellList clist, int cellsize) {
        super(start, goal, obstacles, clist, cellsize);
    }
    
    public AStar() {
        
    }
    
    @Override
    public ArrayList<Point> performSearch() {
        ArrayList<Point> path = new ArrayList<>();
        Comparator<PrioritisedPoint> comparator = new PPComparator();
        PriorityQueue<PrioritisedPoint> head = new PriorityQueue(16, comparator);
        
        head.add(new PrioritisedPoint(start, 0));
        
        HashMap<Point, Point> cameFrom = new HashMap<>();
        HashMap<Point, Float> costSoFar = new HashMap<>();
        
        cameFrom.put(start, null);
        costSoFar.put(start, 0.0f);
        
        Rectangle goalRect = getRect(goal, cellsize);
        try {
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
                    float newCost = costSoFar.get(current) + heur(current, next);

                    if (costSoFar.get(next) == null || newCost < costSoFar.get(next)) {
                        costSoFar.put(next, newCost);
                        float priority = newCost + heur(goal, next);
                        head.add(new PrioritisedPoint(next, priority));
                        cameFrom.put(next, current);
                    }
                }
            }
        } catch(OutOfMemoryError e) {
            System.out.println("Out Of Memory");
            e.printStackTrace();
            return path;
        } 
        
        if (cameFrom.get(goal) == null) { // no path
            System.out.println("No path found between: " + start + "  :  " + goal);
            this.path = path;
            return path;
        }
        
        Point p = goal;
        while (p != null) {
            path.add(p);
            p = cameFrom.get(p);
        }
        
        ArrayList<Point> reverse = new ArrayList<>();
        for (int i = path.size()-1; i >= 0; i--) {
            reverse.add(path.get(i));
        }
        path = reverse;
        
        //Add remove points that are in line;
        
        this.path = path;
        this.cost = costSoFar.get(goal);
        
        return path;
    }
    
}
