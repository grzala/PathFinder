/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author grzala
 */
class OccupancyGrid {
    
    private ArrayList<Boolean> oc;
    int res;
    int originX, originY;
    int width, height;
    
    public OccupancyGrid(BufferedImage img, int resolution) {
        oc = new ArrayList<>();
        this.res = resolution;
        originX = 0; originY = 0;
        
        interpretImg(img);
    }
    
    private void interpretImg(BufferedImage img) {
        int w = img.getWidth(); int h = img.getHeight();
        this.width = w; this.height = h;
        
        for (int y = 0; y < h; y += res) {
            for (int x = 0; x < w; x += res) {
                int clr = img.getRGB(x, y);
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                if (red < 230 && blue < 230 && green < 230) { //not white
                    oc.add(true);
                } else {
                    oc.add(false);
                }
            }
        }
    }
    
    public ArrayList<Point> getAsPoints() {
        ArrayList<Point> toret = new ArrayList<>();
        
        int x = 0; int y = 0;

        for (Boolean b : oc) {
            Point p = new Point(originX + x, originY + y);
            if (b) toret.add(p);
            
            x += res;
            if (x >= width) {
                x = 0;
                y += res;
            }
            if (y >= height) 
                break;
        }
        
        //create map bounds
        for (int i = originX; i < originX + width; i++) {
            toret.add(new Point(i, originY));
            toret.add(new Point(i, originY + height));
        }
        for (int i = originY; i < originY + height; i++) {
            toret.add(new Point(originX, i));
            toret.add(new Point(originX + width, i));
        }
        
        return toret;
    }
    
}
