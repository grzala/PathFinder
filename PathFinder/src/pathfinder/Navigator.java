/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import graphfinding.BruteForce;
import graphfinding.Salesman;
import graphfinding.ClosestNeighbour;
import graphfinding.Kruskal;
import graphfinding.Order;
import graphfinding.Prim;
import pathfinding.PathSearch;
import pathfinding.DijkstraPath;
import pathfinding.AStar;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author grzala
 */

public class Navigator {
    
    private OccupancyGrid oc;
    
    private Point start;
    private ArrayList<Point> goals;
    private ArrayList<Point> obstacles;
    private ArrayList<Point> additional;
    private ArrayList<ArrayList<Point>> paths;
    
    private GraphAlgorithms graphAlgorithm;
    private PathAlgorithms pathAlgorithm;
    
    private double graphTime, avgPathTime;
    
    int imgres = 1; //pixels;
    float step;
    
    public Navigator() {
        start = new Point();
        goals = new ArrayList<>();
        obstacles = new ArrayList<>();
        additional = new ArrayList<>();
        paths = new ArrayList<>();
        
        start.x = 200; start.y = 200;
        goals.add(new Point(150, 150));
        goals.add(new Point(150, 250));
        goals.add(new Point(250, 150));
        goals.add(new Point(250, 250));
        
        step = 5.f;
        
        graphTime = 0; avgPathTime = 0;
        
        graphAlgorithm = GraphAlgorithms.CLOSESTNEIGHBOUR;
    }
    
    public enum GraphAlgorithms {
        CLOSESTNEIGHBOUR,
        BRUTEFORCE,
        KRUSKAL,
        PRIM,
        NONE
    }
    
    public enum PathAlgorithms {
        ASTAR,
        DIJKSTRA
    }
    
    public void setGraphAlgorithm(GraphAlgorithms g) {
        graphAlgorithm = g;
    }
    
    public void setPathAlgorithm(PathAlgorithms g) {
        pathAlgorithm = g;
    }
    
    public void setGraphAlgorithm(String s) {
        s = s.replaceAll("\\s+","");
        s = s.toLowerCase();
        
        switch (s) {
            case "closestneighbour":
                setGraphAlgorithm(GraphAlgorithms.CLOSESTNEIGHBOUR);
                break;
            case "bruteforce":
                setGraphAlgorithm(GraphAlgorithms.BRUTEFORCE);
                break;
            case "mstkruskal":
                setGraphAlgorithm(GraphAlgorithms.KRUSKAL);
                break;
            case "mstprim":
                setGraphAlgorithm(GraphAlgorithms.PRIM);
                break;
            case "none":
                setGraphAlgorithm(GraphAlgorithms.NONE);
                break;
            default:
                System.out.println("no such algorithm");
        }
    }
    
    public void setPathAlgorithm(String s) {
        s = s.replaceAll("\\s+","");
        s = s.toLowerCase();
        
        switch (s) {
            case "a*":
                setPathAlgorithm(PathAlgorithms.ASTAR);
                break;
            case "dijkstra":
                setPathAlgorithm(PathAlgorithms.DIJKSTRA);
                break;
            default:
                System.out.println("no such algorithm");
        }
    }
    
    public static float calculateDistance(ArrayList<Point> path) {
        float result = 0;
        
        for (int i = 1; i < path.size(); i++) {
            Point p1 = path.get(i-1);
            Point p2 = path.get(i);
            
            float dx = p2.x - p1.x;
            float dy = p2.y - p1.y;
            dx *= dx; dy *= dy;
            result += (float)Math.sqrt(dx + dy);
        }
        
        return result;
    }
    
    public static float calculateTotalDistance(ArrayList<ArrayList<Point>> paths) {
        float result = 0;
        
        for (ArrayList<Point> path : paths) 
            result += calculateDistance(path);
        
        return result;
    }
    
    public ArrayList<ArrayList<Point>> getLines() {
        ArrayList<ArrayList<Point>> ls = new ArrayList<>();
        
        for (ArrayList<Point> path : paths) {
            ArrayList<Point> ps = new ArrayList<>();
            for (Point p : path)
                ps.add(p);
            ls.add(ps);
        }
        
        return ls;
    }
    
    private boolean canAdd(int x, int y) {
        boolean add = true;
        for (Point o : obstacles) {
            Rectangle rect = new Rectangle(x - (int)step, y - (int)step, (int)step, (int)step);
            if (rect.contains(o)) {
                add = false;
                break;
            }
        }
        return add;
    }
    
    public boolean setStart(int x, int y) {
        if (canAdd(x, y)) {
            start = new Point(x, y);
            return true;
        } else { 
            System.out.println("can't add start, to close to obstacle");
            return false;
        }
    }    
    
    public Point getStart() {
        return start;
    }
    
    public void clear() {
        goals.clear();
        additional.clear();
        paths.clear(); /////////////////////////////// separate method?
    }
    
    public boolean addGoal(int x, int y) {
        if (canAdd(x, y)) {
            goals.add(new Point(x, y));
            return true;
        } else {
            System.out.println("Can't add goal: to close to an obstacle");
            return false;
        }
    }
    
    public void removeLastGoal() {
        if (goals.size() > 0) {
            goals.remove(goals.size()-1);
        }
    }
    
    public ArrayList<Point> getGoals() {
        return goals;
        /* //debugging purposes
        ArrayList<Point> r = new ArrayList<>(goals);
        r.addAll(obstacles);
        return r;
        */
    }
    
    public ArrayList<Point> getAdditional() {
        return additional;
    }
    
    public double[] getTimes() {
        double[] d = new double[2];
        
        d[0] = graphTime; d[1] = avgPathTime;
        
        return d;
    }
    
    public void setImage(BufferedImage img) {
        if (img == null) {
            this.oc = null;
            obstacles = new ArrayList<>();
        } else {
            this.oc = new OccupancyGrid(img, imgres);
            obstacles = new ArrayList<>(oc.getAsPoints());
            //additional = new ArrayList<>(obstacles); //for debug
        }
        
        
        while (!canAdd(start.x, start.y)) {
            int minx = 0; int miny = 0;
            int maxx = getSize()[0]; int maxy = getSize()[1];
            Random r = new Random();
            int x = r.nextInt((maxx - minx) + 1) + minx;
            int y = r.nextInt((maxy - miny) + 1) + miny;
            
            start = new Point(x, y);
        }
    }
  
    public void performSearch() {
        paths.clear();
        PathSearch ps = null;
        switch (pathAlgorithm) {
            case ASTAR:
                ps = new AStar();
                break;
            
            case DIJKSTRA:
                ps = new DijkstraPath();
                break;
                
            default:
                System.out.println("ERROR");
                break;
        }
        
        Salesman s = null;
        switch (graphAlgorithm) {
            case CLOSESTNEIGHBOUR:
                s = new ClosestNeighbour(ps, start, goals, obstacles, step);
                break;
                
            case BRUTEFORCE:
                s = new BruteForce(ps, start, goals, obstacles, step);
                break;
                
            case KRUSKAL:
                s = new Kruskal(ps, start, goals, obstacles, step);
                break;
                
            case PRIM:
                s = new Prim(ps, start, goals, obstacles, step);
                break;
                
            case NONE:
                s = new Order(ps, start, goals, obstacles, step);
                break;
                
            default:
                System.out.println("ERROR");
                break;
        }
        
        paths = s.performSearch();
        graphTime = s.getTime()[0]; avgPathTime = s.getTime()[1];
    }
    
    public int[] getSize() {
        if (oc == null) 
            return new int[]{0, 0};
            
        int[] ar = {oc.width, oc.height};
        return ar;
    }
    
}
