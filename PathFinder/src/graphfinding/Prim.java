/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import pathfinder.CellList;
import pathfinding.PathSearch;

/**
 *
 * @author Grzala
 */
public class Prim extends Salesman {
     public Prim(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, CellList clist, float step) {
        super(ps, start, goals, obstacles, clist, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        startTimeMeasurement();
        paths = new ArrayList<ArrayList<Point>>();
        ArrayList<Salesman.Node> Q = new ArrayList<Node>(nodes);
        
        //find all paths O(n^2)
        for (Node n1 : Q) {
            for (Node n2 : Q) {
                if (n1 != n2) {
                    double t = System.nanoTime(); //search time
                    ps.reset(n1.getNode(), n2.getNode(), obstacles, clist, (int)step);
                    ps.performSearch();
                    n1.addEdge(n2, ps.getPath(), ps.getCost());
                    pathTimes.add(System.nanoTime() - t);
                }
            }
        }
        
        Random rand = new Random();
        int i = rand.nextInt(Q.size());
        Node startNode = Q.get(i);
        
        HashMap <Node, Node> cameFrom = new HashMap<Node, Node>();
        HashMap <Node, Float> priority = new HashMap<Node, Float>();
        ArrayList<Node> queue = new ArrayList<Node>();
        
        for (Node n : Q) {
            priority.put(n, Float.MAX_VALUE);
            queue.add(n);
        }
        
        priority.put(startNode, 0.f);
        
        while (!queue.isEmpty()) {
            Node current = poll(queue, priority);
            
            for (Node n : current.getNeighbours()) {
                float dist = current.getDistance(n);
                if (queue.contains(n) && dist < priority.get(n)) {
                    priority.put(n, dist);
                    cameFrom.put(n, current);
                }
            }
        }
        
        for (Node n : cameFrom.keySet()) 
            paths.add(n.getPathFor(cameFrom.get(n)));
        
        endTimeMeasurement();
        return paths;
    }
    
    public Node poll(ArrayList<Node> q, HashMap<Node, Float> pr) {
        int index = 0;
        float lowest_priority = Float.MAX_VALUE;
        
        for (int i = 0; i < q.size(); i++) {
            Node node = q.get(i);
            if (pr.get(node) < lowest_priority) {
                lowest_priority = pr.get(node);
                index = i;
            }
        }
        
        return q.remove(index);
    }
}
