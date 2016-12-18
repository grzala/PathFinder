/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphfinding;

import java.awt.Point;
import java.util.ArrayList;
import pathfinder.CellList;
import pathfinding.PathSearch;

public class Order extends Salesman {
     public Order(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, CellList clist, float step) {
        super(ps, start, goals, obstacles, clist, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        startTimeMeasurement();
        paths = new ArrayList<>();
        
        for (int i = 0; i < nodes.size()-1; i++) {
            Node n1 = nodes.get(i);
            Node n2 = nodes.get(i + 1);
            
            double t = System.nanoTime(); //search time
            ps.reset(n1.getNode(), n2.getNode(), obstacles, clist, (int)step);
            ps.performSearch();
            paths.add(ps.getPath());
            pathTimes.add(System.nanoTime() - t);
        }
        
        endTimeMeasurement();
        return paths;
    }
}
