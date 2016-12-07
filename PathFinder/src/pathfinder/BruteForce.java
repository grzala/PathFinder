/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Grzala
 */
public class BruteForce extends Salesman {
    
    public BruteForce(PathSearch ps, Point start, ArrayList<Point> goals, ArrayList<Point> obstacles, float step) {
        super(ps, start, goals, obstacles, step);
    }
    
    @Override
    public ArrayList<ArrayList<Point>> performSearch() {
        startTimeMeasurement();
        ArrayList<Node> Q = new ArrayList<>(nodes);
        
        //find all paths O(n^2)
        for (Node n : Q) {
            for (Node n2 : Q) {
                if (n != n2) {
                    double t = System.nanoTime(); //search time
                    ps.reset(n.getNode(), n2.getNode(), obstacles, (int)step);
                    ps.performSearch();
                    n.addEdge(n2, ps.getPath(), ps.getCost());
                    pathTimes.add(System.nanoTime() - t);
                }
            }
        }
        
        //remove start and add it later to ensure it is first on the list
        Node start = Q.remove(0); //remove start;

        ArrayList<List<Node>> perms = new ArrayList<>(listPermutations(Q));
        
        //find shortest permutation
        float distance = Float.MAX_VALUE;
        ArrayList<Node> shortestPath = new ArrayList<>();
        
        for (List<Node> path : perms) {
            path.add(0, start); //start Node
            float newdist = calculateDistance(path);
            if (newdist < distance) {
                shortestPath = new ArrayList<>(path);
                distance = newdist;
            }
        }
        
        //construct path
        for (int i = 0; i < shortestPath.size() - 1; i++) 
            paths.add(shortestPath.get(i).getPathFor(shortestPath.get(i+1)));
        
        endTimeMeasurement();
        
        return paths;
    }
    
    private static <T> List<List<T>> listPermutations(List<T> list) {
        if (list.isEmpty()) {
            List<List<T>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        List<List<T>> returnMe = new ArrayList<>();

        T firstElement = list.remove(0);

        List<List<T>> recursiveReturn = listPermutations(list);
        for (List<T> li : recursiveReturn) {

            for (int index = 0; index <= li.size(); index++) {
                List<T> temp = new ArrayList<>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }

        }
        return returnMe;
    }
    
    private static float calculateDistance(List<Node> path) {
        float result = 0;
        
        for (int i = 1; i < path.size(); i++) {
            Node n1 = path.get(i-1);
            Node n2 = path.get(i);
            result += n1.getDistance(n2);
        }
        
        return result;
    }
}
