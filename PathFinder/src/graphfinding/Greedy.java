/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphfinding;

import pathfinding.PathSearch;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Grzala
 */
public class Greedy extends Salesman {
    
    public Greedy(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, float step) {
        super(ps, start, goals, obstacles, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        startTimeMeasurement();
        ArrayList<Salesman.Node> Q = new ArrayList<>(nodes);
        
        Node startNode = null;
        for (Node n : Q) 
            if (n.getNode() == start) 
                startNode = n;
        
        int N = Q.size() - 1;
        HashMap<Node, Integer> degree = new HashMap<>();
        ArrayList<Node[]> edges = new ArrayList<>();
        HashMap<Node[], Float> weights = new HashMap<>();
        
        //find all paths O(n^2)
        for (Node n1 : Q) {
            degree.put(n1, 0);
            for (Node n2 : Q) {
                if (n1 != n2) {
                    double t = System.nanoTime(); //search time
                    ps.reset(n1.getNode(), n2.getNode(), obstacles, (int)step);
                    ps.performSearch();
                    n1.addEdge(n2, ps.getPath(), ps.getCost());
                    pathTimes.add(System.nanoTime() - t);
                    
                    Node[] nn = {n1, n2};
                    edges.add(nn);
                    weights.put(nn, n1.getDistance(n2));
                }
            }
        }
        
        //remove parallel
        ArrayList<Node[]> temp = new ArrayList<>();
        for (Node[] nn1 : edges) {
            boolean add = true;
            for (Node[] nn2 : temp) 
                if (nn1[0] == nn2[1] && nn1[1] == nn2[0])
                    add = false;
            if (add)
                temp.add(nn1);
        }
        edges = temp;
        
        //add from start
        ArrayList<Node[]> construction = new ArrayList<>();
        degree.put(startNode, 1);
        for (Node[] nn : edges) {
            if (nn[0] == startNode || nn[1] == startNode) {
                degree.put(nn[0], degree.get(nn[0]) + 1);
                degree.put(nn[1], degree.get(nn[1]) + 1);
                construction.add(nn);
                break; //take one but most important
            }
        }
        
        while (!edges.isEmpty()) {
            
            if (construction.size() >= N) 
                break;
            
            
            Node[] currentEdge = poll(edges, weights);
            
            boolean correctDegreeLevel = degree.get(currentEdge[0]) < 2 && degree.get(currentEdge[1]) < 2;
            boolean createsClosedCycle = willCreateClosedCycle(construction, currentEdge);
            if (correctDegreeLevel && !createsClosedCycle) { //
                degree.put(currentEdge[0], degree.get(currentEdge[0]) + 1);
                degree.put(currentEdge[1], degree.get(currentEdge[1]) + 1);
                construction.add(currentEdge);
            } 
            
        }
        
        //construct path from construction, in order from start to end
        ArrayList<Node> buffer = new ArrayList<>();
        Node currentNode = startNode;
        Node nextNode = findNext(construction, currentNode, buffer);
        while (nextNode != null) {
            buffer.add(currentNode);
            paths.add(currentNode.getPathFor(nextNode));
            currentNode = nextNode;
            nextNode = findNext(construction, currentNode, buffer);
        }
        
        endTimeMeasurement();
        
        return paths;
    }
    
    
    public Node[] poll(ArrayList<Node[]> q, HashMap<Node[], Float> pr) {
        int index = 0;
        float lowest_priority = Float.MAX_VALUE;
        
        for (int i = 0; i < q.size(); i++) {
            Node[] node = q.get(i);
            if (pr.get(node) < lowest_priority) {
                lowest_priority = pr.get(node);
                index = i;
            }
        }
        
        return q.remove(index);
    }
    
    private boolean willCreateClosedCycle(ArrayList<Node[]> edges, Node[] item) {
        if (edges.size() < 2) //can't create cycle with less than three nodes total
            return false;
        
        boolean ret = false;
        
        ArrayList<Node> buffer = new ArrayList<>(); //visitedPoints;
        Node currentNode = item[0];
        
        while(currentNode != null) {
            buffer.add(currentNode);
            if (currentNode == item[1]) {
                ret = true;
                break;
            }
            currentNode = findNext(edges, currentNode, buffer);
        }
        
        return ret;
    }
    
    private Node findNext(ArrayList<Node[]> q, Node item, ArrayList<Node> buffer) {
        Node ret = null;
        for (Node[] n : q) {
            if (n[0].equals(item) && !buffer.contains(n[1])) {
                ret = n[1];
            } else if (n[1].equals(item) && !buffer.contains(n[0])) {
                ret = n[0];
            }
        }
        
        return ret;
    }
}
