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
public class HeuristicSearch extends PathSearch {
    
    public HeuristicSearch(Point start, Point goal, ArrayList<Point> obstacles, CellList clist, int cellsize) {
        super(start, goal, obstacles, clist, cellsize);
    }
    
    public HeuristicSearch() {
        
    }
    
    @Override
    public ArrayList<Point> performSearch() {
        ArrayList<Point> path = new ArrayList<>();
        Comparator<PathSearch.PrioritisedPoint> comparator = new PathSearch.PPComparator();
        PriorityQueue<PathSearch.PrioritisedPoint> head = new PriorityQueue(16, comparator);
        
        head.add(new PathSearch.PrioritisedPoint(start, 0));
        
        HashMap<Point, Point> cameFrom = new HashMap<>();
        HashMap<Point, Float> costToGoal = new HashMap<>();
        
        cameFrom.put(start, null);
        costToGoal.put(start, 0.0f);
        
        Rectangle goalRect = getRect(goal, cellsize);
        try {
            while (head.size() > 0) {
                Point current = head.poll().toPoint();

                if (goalRect.contains(current)) {//goal reached
                    if (current != goal) {
                        current = cameFrom.get(current); //the current one is might make path longer
                        cameFrom.put(goal, current);
                        float newCost = heur(goal, current);
                        costToGoal.put(goal, newCost);

                    }
                    break;
                }

                for (Point next : getNext(current)) {
                    float newCost = heur(goal, next);

                    if (costToGoal.get(next) == null || newCost < costToGoal.get(next)) {
                        costToGoal.put(next, newCost);
                        float priority = newCost;
                        head.add(new PathSearch.PrioritisedPoint(next, priority));
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
            System.out.println("No path found");
            return path;
        }
        
        int totalCost = 0;
        Point p = goal;
        while (p != null) {
            path.add(p);
            if (cameFrom.get(p) != null)
                totalCost += heur(p, cameFrom.get(p));
            p = cameFrom.get(p);
        }
        
        ArrayList<Point> reverse = new ArrayList<>();
        for (int i = path.size()-1; i >= 0; i--) {
            reverse.add(path.get(i));
        }
        path = reverse;
        
        //Add remove points that are in line;
        
        this.path = path;
        this.cost = totalCost;
        
        return path;
    }
    
}
