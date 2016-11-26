/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

/**
 *
 * @author grzala
 */
public class PathFinder {

    public static void main(String[] args) {
        Navigator n = new Navigator();
        MainFrame frame = new MainFrame(n);
        
        new Thread(() -> {
            frame.createAndShowGUI();
        }).start();
    }
    
}
