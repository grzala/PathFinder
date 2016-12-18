/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphfinding;

import pathfinding.PathSearch;
import java.awt.Point;
import java.util.ArrayList;
import pathfinder.CellList;

/**
 *
 * @author grzala
 */
public class ClosestNeighbour extends Salesman {
    
    public ClosestNeighbour(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, CellList clist, float step) {
        super(ps, start, goals, obstacles, clist, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        startTimeMeasurement();
        paths = new ArrayList<ArrayList<Point>>();
        
        Salesman.Node currentNode = nodes.get(0); //start from startPoint
        ArrayList<Salesman.Node> nodesVisited = new ArrayList<Node>();
        nodesVisited.add(currentNode);
        //closest neighbour, rewrite it later
        while (!(nodesVisited.size() >= nodes.size())) {
            //populate neighbours
            for (Salesman.Node n : nodes) {
                if (!(currentNode.equals(n)) && !(nodesVisited.contains(n))) {
                    double t = System.nanoTime(); //search time
                    ps.reset(currentNode.getNode(), n.getNode(), obstacles, clist, (int)step);
                    ps.performSearch();
                    currentNode.addEdge(n, ps.getPath(), ps.getCost());
                    pathTimes.add(System.nanoTime() - t);
                }
            }
            
            Salesman.Node newNode = currentNode.getClosestNeighbour();
            if (newNode == null) break;
            ArrayList<Point> p = currentNode.getPathFor(newNode);
            if (p != null && p.size() > 0) //if path exists
                paths.add(currentNode.getPathFor(newNode));
            currentNode = newNode;
            nodesVisited.add(currentNode);
        }
        
        endTimeMeasurement();
        return paths;
    }
}
