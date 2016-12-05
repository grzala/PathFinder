/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author grzala
 */
public class ClosestNeighbour extends Salesman {
    
    public ClosestNeighbour(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, float step) {
        super(ps, start, goals, obstacles, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        paths = new ArrayList<>();
        
        Salesman.Node currentNode = nodes.get(0); //start from startPoint
        ArrayList<Salesman.Node> nodesVisited = new ArrayList<>();
        nodesVisited.add(currentNode);
        //closest neighbour, rewrite it later
        while (!(nodesVisited.size() >= nodes.size())) {
            //populate neighbours
            for (Salesman.Node n : nodes) {
                if (!(currentNode.equals(n)) && !(nodesVisited.contains(n))) {
                    ps.reset(currentNode.getNode(), n.getNode(), obstacles, (int)step);
                    ps.performSearch();
                    currentNode.addEdge(n, ps.getPath(), ps.getCost());
                }
            }
            
            Salesman.Node newNode = currentNode.getClosestNeighbour();
            ArrayList<Point> p = currentNode.getPathFor(newNode);
            if (p != null && p.size() > 0) //if path exists
                paths.add(currentNode.getPathFor(newNode));
            currentNode = newNode;
            nodesVisited.add(currentNode);
        }
        return paths;
    }
}
