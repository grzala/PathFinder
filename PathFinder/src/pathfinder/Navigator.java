/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import graphfinding.BruteForce;
import graphfinding.Salesman;
import graphfinding.ClosestNeighbour;
import graphfinding.Greedy;
import graphfinding.Kruskal;
import graphfinding.Order;
import graphfinding.Prim;
import pathfinding.PathSearch;
import pathfinding.DijkstraPath;
import pathfinding.AStar;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import pathfinding.HeuristicSearch;

/**
 *
 * @author grzala
 */

public class Navigator {
    
    private OccupancyGrid oc;
    private CellList cl;
    
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
        goals = new ArrayList<Point>();
        obstacles = new ArrayList<Point>();
        additional = new ArrayList<Point>();
        paths = new ArrayList<ArrayList<Point>>();
        
        start.x = 200; start.y = 200;
        goals.add(new Point(150, 150));
        goals.add(new Point(150, 250));
        goals.add(new Point(250, 150));
        goals.add(new Point(250, 250));
        
        step = 6.f;
        
        graphTime = 0; avgPathTime = 0;
        
        graphAlgorithm = GraphAlgorithms.CLOSESTNEIGHBOUR;
    }
    
    public enum GraphAlgorithms {
        CLOSESTNEIGHBOUR,
        BRUTEFORCE,
        KRUSKAL,
        PRIM,
        NONE,
        GREEDY
    }
    
    public enum PathAlgorithms {
        ASTAR,
        DIJKSTRA,
        HEURISTIC
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
        
        if (s.equals("closestneighbour"))
            setGraphAlgorithm(GraphAlgorithms.CLOSESTNEIGHBOUR);
        else if (s.equals("bruteforce"))
            setGraphAlgorithm(GraphAlgorithms.BRUTEFORCE);
        else if (s.equals("mstkruskal"))
            setGraphAlgorithm(GraphAlgorithms.KRUSKAL);
        else if (s.equals("mstprim"))
            setGraphAlgorithm(GraphAlgorithms.PRIM);
        else if (s.equals("none"))
            setGraphAlgorithm(GraphAlgorithms.NONE);
        else if (s.equals("greedy"))
            setGraphAlgorithm(GraphAlgorithms.GREEDY);
        else
            System.out.println("no such algorithm");
        
    }
    
    public void setPathAlgorithm(String s) {
        s = s.replaceAll("\\s+","");
        s = s.toLowerCase();
        
        if (s.equals("a*"))
            setPathAlgorithm(PathAlgorithms.ASTAR);
        else if (s.equals("dijkstra"))
            setPathAlgorithm(PathAlgorithms.DIJKSTRA);
        else if (s.equals("heuristicsearch"))
            setPathAlgorithm(PathAlgorithms.HEURISTIC);
        else
            System.out.println("no such algorithm");
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
        ArrayList<ArrayList<Point>> ls = new ArrayList<ArrayList<Point>>();
        
        for (ArrayList<Point> path : paths) {
            ArrayList<Point> ps = new ArrayList<Point>();
            for (Point p : path)
                ps.add(p);
            ls.add(ps);
        }
        
        return ls;
    }
    
    private boolean canAdd(int x, int y) {
        boolean add = true;
        
        //on obstacle?
        /* deprecated
        for (Point o : obstacles) {
            Rectangle rect = new Rectangle(x - (int)step/2, y - (int)step/2, (int)step, (int)step);
            if (rect.contains(o)) {
                add = false;
                break;
            }
        }
        */
        if (cl != null) 
            add = (cl.getCell(new Point(x, y)) != null);
        
        //distance from oters
        ArrayList<Point> points = new ArrayList<Point>(goals); points.add(start);
        for (Point g : points) {
            //distance from oters
            float distancesqr = (float)(Math.pow(g.x - x, 2.d) + Math.pow(g.y - y, 2));
            if (Math.sqrt(distancesqr) < 10) {
                add = false;
                break;
            }
        }
        
        //on map?
        int xx = getSize()[0];
        int yy = getSize()[1];
        if (yy > 0 && xx > 0) {
            if (x < 0 || x > xx)
                add = false;
            if (y < 0 || y > yy)
                add = false;
        }
        
        return add;
    }
    
    public boolean setStart(int x, int y) {
        if (canAdd(x, y)) {
            start = new Point(x, y);
            return true;
        } else { 
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
            obstacles = new ArrayList<Point>();
            cl = null;
        } else {
            this.oc = new OccupancyGrid(img, imgres);
            obstacles = new ArrayList<Point>(oc.getAsPoints());
            cl = new CellList(obstacles, step, new float[]{0, 0, getSize()[0], getSize()[1]});
            //additional = new ArrayList<>(obstacles); //for debug
            //additional = cl.getCellCenters();
        }
        
        int x = start.x; int y = start.y;
        start = new Point(-100, -100);
        while (!canAdd(x, y)) {
            int minx = 0; int miny = 0;
            int maxx = getSize()[0]; int maxy = getSize()[1];
            Random r = new Random();
            x = r.nextInt((maxx - minx) + 1) + minx;
            y = r.nextInt((maxy - miny) + 1) + miny;
        }
        start = new Point(x, y);
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
                
            case HEURISTIC:
                ps = new HeuristicSearch();
                break;
                
            default:
                System.out.println("ERROR");
                break;
        }
        
        Salesman s = null;
        switch (graphAlgorithm) {
            case CLOSESTNEIGHBOUR:
                s = new ClosestNeighbour(ps, start, goals, obstacles, cl, step);
                break;
                
            case BRUTEFORCE:
                s = new BruteForce(ps, start, goals, obstacles, cl, step);
                break;
                
            case KRUSKAL:
                s = new Kruskal(ps, start, goals, obstacles, cl, step);
                break;
                
            case PRIM:
                s = new Prim(ps, start, goals, obstacles, cl, step);
                break;
                
            case NONE:
                s = new Order(ps, start, goals, obstacles, cl, step);
                break;
            
            case GREEDY:
                s = new Greedy(ps, start, goals, obstacles, cl, step);
                break;
                
            default:
                System.out.println("ERROR");
                break;
        }
        
        paths = s.performSearch();
        //AStar a = new AStar(start, goals.get(0), obstacles, cl, (int)step);
        //a.performSearch();
        //paths.add(a.getPath());
        graphTime = s.getTime()[0]; avgPathTime = s.getTime()[1];
    }
    
    public int[] getSize() {
        if (oc == null) 
            return new int[]{0, 0};
            
        int[] ar = {oc.width, oc.height};
        return ar;
    }
    
    public void generateRandomGoals(int n, int[] frameSize) {
        int minx = 0; int miny = 0;
        int maxx = getSize()[0]; int maxy = getSize()[1];
        if (maxx <= 0 || maxy <= 0) {
            maxx = frameSize[0]; maxy = frameSize[1];
        }
        
        goals.clear();
        Random r = new Random();
        
        for (int i = 0; i < n; i++) {
            int x = r.nextInt((maxx - minx) + 1) + minx;
            int y = r.nextInt((maxy - miny) + 1) + miny;
            Point p = new Point(x, y);
            
            while (!canAdd(x, y) || goals.contains(p)) {
                x = r.nextInt((maxx - minx) + 1) + minx;
                y = r.nextInt((maxy - miny) + 1) + miny;
                p = new Point(x, y);
            }
                
            goals.add(p);
            
        }
    }
    
}
