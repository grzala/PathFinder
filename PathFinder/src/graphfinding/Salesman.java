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
import pathfinder.CellList;

/**
 *
 * @author Grzala
 */
public abstract class Salesman {
    protected Point start;
    protected ArrayList<Point> goals;
    protected ArrayList<Point> obstacles;
    protected CellList clist;
    protected float step;
    
    protected ArrayList<Node> nodes;
    protected ArrayList<ArrayList<Point>> paths;
    
    protected double searchTime;
    protected ArrayList<Double> pathTimes;
    
    protected PathSearch ps;
    
    public Salesman(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, CellList clist, float step) {
        this.ps = ps;
        this.start = start;
        this.goals = goals;
        this.obstacles = obstacles;
        this.clist = clist;
        this.step = step;
        nodes = new ArrayList<Node>();
        paths = new ArrayList<ArrayList<Point>>();
        searchTime = 0;
        pathTimes = new ArrayList<Double>();
        
        
        initDiagram();
    }
    
    protected final void startTimeMeasurement() {
        searchTime = System.nanoTime();
    }
    
    public abstract ArrayList<ArrayList<Point>> performSearch();
    
    protected final void endTimeMeasurement() {
        searchTime = System.nanoTime() - searchTime;
    }
    
    public final double[] getTime() {
        double b = 0;
        
        double sum = 0;
        for (Double d : pathTimes) 
            sum += d;
        
        b = sum / (double)pathTimes.size();
        
        //need to subtract sum from search time as all path search times are in graph search time as well
        //return in millis
        return new double[] {(searchTime - sum)/1000000.0, b/1000000.0};
    }
    
    public ArrayList<ArrayList<Point>> getPaths() {
        return paths;
    }
    
    private void initDiagram() {
        nodes.add(new Node(start));
        for (Point g : goals) 
            nodes.add(new Node(g));
    }
    
    
    protected class Node {
        private Point node;
        private ArrayList<Node> neighbours;
        private HashMap<Node, Float> distances;
        private HashMap<Node, ArrayList<Point>> paths;
        
        public Node(Point pos) {
            node = pos;
            distances = new HashMap<Node, Float>();
            paths = new HashMap<Node, ArrayList<Point>>();
            neighbours = new ArrayList<Node>();
        }
        
        public Point getNode() {
            return node;
        }
        
        public void addEdge(Node targetNode, ArrayList<Point> path, float distance) {
            distances.put(targetNode, distance);
            paths.put(targetNode, path);
            neighbours.add(targetNode);
        }
        
        public boolean hasEdge(Node n) {
            return (distances.get(n) != null && paths.get(n) != null);
        }
        
        public float getDistance(Node n) {
            return distances.get(n);
        }
        
        public boolean equals(Node node) {
            return (this.node.x == node.node.x && this.node.y == node.node.y);
        }
        
        public Node getClosestNeighbour() {
            float dist = Float.MAX_VALUE;
            Node toret = null;
            for (Node n : distances.keySet()) {
                float newdist = distances.get(n);
                if (newdist < dist) {
                    dist = newdist;
                    toret = n;
                }
            }
            return toret;
        }
        
        public ArrayList<Point> getPathFor(Node n) {
            return paths.get(n);
        }
        
        public String toString() {
            return this.node.toString();
        }
        
        public ArrayList<Node> getNeighbours() {
            return neighbours;
        }
    }
}
