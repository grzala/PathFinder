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
public class Dijkstra {
    
    private Point start;
    private ArrayList<Point> goals;
    private ArrayList<Point> obstacles;
    float step;
    
    private ArrayList<Node> nodes;
    
    public Dijkstra(Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, float step) {
        this.start = start;
        this.goals = goals;
        this.obstacles = obstacles;
        this.step = step;
        nodes = new ArrayList<>();
        
        initDiagram();
    }
    
    private void initDiagram() {
        nodes.add(new Node(start));
        for (Point g : goals) 
            nodes.add(new Node(g));
    }
    
    public ArrayList<ArrayList<Point>> performSearch() {
        ArrayList<ArrayList<Point>> paths = new ArrayList<>();
        
        Node currentNode = nodes.get(0); //start from startPoint
        ArrayList<Node> nodesVisited = new ArrayList<>();
        nodesVisited.add(currentNode);
        //closest neighbour, rewrite it later
        while (!(nodesVisited.size() >= nodes.size())) {
            float shortestPath = Float.MAX_VALUE;
            Node targetNode = null;
            
            //populate neighbours
            for (Node n : nodes) {
                if (!(currentNode.equals(n)) && !(nodesVisited.contains(n))) {
                    AStar astar = new AStar(currentNode.node, n.node, obstacles, (int)step);
                    astar.performSearch();
                    currentNode.addEdge(n, astar.getPath(), astar.getCost());
                }
            }
            
            Node newNode = currentNode.getClosestNeighbour();
            paths.add(currentNode.getPathFor(newNode));
            currentNode = newNode;
            nodesVisited.add(currentNode);

        }
        
        return paths;
    }
    
    private void addNode(Node node) {
        nodes.add(node);
    }
    
    private class Node {
        public Point node;
        public HashMap<Node, Float> distances;
        public HashMap<Node, ArrayList<Point>> paths;
        
        public Node(Point pos) {
            node = pos;
            distances = new HashMap<>();
            paths = new HashMap<>();
        }
        
        public void addEdge(Node targetNode, ArrayList<Point> path, float distance) {
            distances.put(targetNode, distance);
            paths.put(targetNode, path);
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
       
    }
    
}
