/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import pathfinder.CellList;
import pathfinding.PathSearch;

/**
 *
 * @author Grzala
 */
public class Kruskal extends Salesman {
     public Kruskal(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, CellList clist, float step) {
        super(ps, start, goals, obstacles, clist, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        startTimeMeasurement();
        paths = new ArrayList<>();
        ArrayList<Salesman.Node> Q = new ArrayList<>(nodes);
        
        //find all paths O(n^2)
        HashMap<Float, ArrayList<Salesman.Node[]>> graph = new HashMap<>();
        for (Node n1 : Q) {
            for (Node n2 : Q) {
                if (n1 == n2) continue; //not for the same ones
                
                //path search
                double t = System.nanoTime(); //search time
                ps.reset(n1.getNode(), n2.getNode(), obstacles, clist, (int)step);
                ps.performSearch();
                n1.addEdge(n2, ps.getPath(), ps.getCost());
                pathTimes.add(System.nanoTime() - t);
                Node[] nn = new Node[] {n1, n2};
                float distance = (float) (Math.round(n1.getDistance(n2) * 10000.0) / 10000.0); // round a bit to prevent duplicates

                if (graph.get(distance) == null) graph.put(distance, new ArrayList<Salesman.Node[]>()); //prevent nullPointerException
                
                /* seems unnecessary, leaving commented if errors happen in the future, uncomment
                //does graphs already contain this edge?
                boolean contains = false;
                for (Node [] ar : graph.get(distance)) {
                    if ((ar[0] == n1 && ar[1] == n2) || (ar[0] == n2 && ar[1] == n1)) {
                        contains = true;
                        break;
                    }
                } 
                */
                
                //if (!contains) //add if not yet in graph
                    graph.get(distance).add(nn);
            }
        }
        
        ArrayList<Node []> nodes = new ArrayList<>(); //build sequence
        ArrayList<Float> sortedKeys = new ArrayList<>(graph.keySet()); Collections.sort(sortedKeys);
        Random rand = new Random();
        for (Float f : sortedKeys) { //from smallest
            while (graph.get(f).size() > 0) {
                int i = rand.nextInt(graph.get(f).size()); //if more than 1, choose randomly
                nodes.add(graph.get(f).get(i));
                graph.get(f).remove(i);
            }
        }
        
        DisjointSet<Node> ds = new DisjointSet<>();
        ds.makeSet(Q);
        
        for (Node [] narr : nodes) {
            if (!(ds.together(narr[0], narr[1]))) {
                paths.add(narr[0].getPathFor(narr[1]));
                ds.union(narr[0], narr[1]);
            }
        }
        
        endTimeMeasurement();
        return paths;
    }
    
    private class DisjointSet<T> {
        ArrayList<HashSet<T>> sets;
        
        public void makeSet(ArrayList<T> ar) {
            sets = new ArrayList<>();
            for (T t : ar) {
                HashSet<T> n = new HashSet<T>();
                n.add(t);
                sets.add(n);
            }
        }
        
        public int findRootSetIndex(T item) {
            for (int i = 0; i < sets.size(); i++) 
                if (sets.get(i).contains(item))
                    return i;
            return -1;
        }
        
        public void union(T item1, T item2) {
            int root1 = findRootSetIndex(item1);
            int root2 = findRootSetIndex(item2);
            
            //move to set 1 and remove set 2
            sets.get(root1).addAll(sets.get(root2));
            sets.remove(root2);
        }
        
        public boolean together(T item1, T item2) {
            int root1 = findRootSetIndex(item1);
            int root2 = findRootSetIndex(item2);
            
            return root1 == root2;
        }
    }
}
