/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author grzala
 */
public class ColoredPoint {
    
    int x, y, size;
    Color color;
    
    ColoredPoint(int x, int y, Color c, int size) {
        this.x = x; this.y = y;
        this.color = c;
        this.size = size;
    }
    
    ColoredPoint(Point p, Color c, int size) {
        this(p.x, p.y, c, size);
    }
    
    ColoredPoint(int x, int y, Color c) {
        this(x, y, c, 5);
    }
    
    ColoredPoint(Point p, Color c) {
        this(p.x, p.y, c, 5);
    }
    
    
    
}
