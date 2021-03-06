/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Grzala
 */
public class MainFrameController {
    
    private MainFrame frame;
    private Canvas canvas;
    
    private Navigator n;
    
    public MainFrameController() {
        n = new Navigator();
        frame = new MainFrame();
        canvas = frame.getCanvas();
        
        startGUIThread();
        setActions();
        
        repaint();
    }
    
    private void startGUIThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                frame.createAndShowGUI();
            }
        }).start();
    }
    
    private void setActions() {
        //clear
        frame.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                n.clear();
                repaint();
            }
        });
        //search
        frame.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                String graphAlgorithm = String.valueOf(frame.graphAlgorithmChooser.getSelectedItem());
                String pathAlgorithm = String.valueOf(frame.pathAlgorithmChooser.getSelectedItem());
                n.setGraphAlgorithm(graphAlgorithm);
                n.setPathAlgorithm(pathAlgorithm);

                frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                n.performSearch();
                frame.setCursor(Cursor.getDefaultCursor());

                repaint();
            }
        });
        //pan
        frame.panButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                canvas.setMode(Canvas.Mode.PAN);
            }
        });
        //set start
        frame.setStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                canvas.setMode(Canvas.Mode.SETSTART);
            }
        });
        //clear
        frame.setGoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                canvas.setMode(Canvas.Mode.SETGOAL);
            }
        });
        
        //canvas clicks
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Point origin = canvas.getOrigin();
                if (canvas.getMode() == Canvas.Mode.SETSTART) 
                    n.setStart(evt.getX() - origin.x, evt.getY() - origin.y);

                if (canvas.getMode() == Canvas.Mode.SETGOAL) {
                    if(SwingUtilities.isLeftMouseButton(evt)) {
                        n.addGoal(evt.getX() - origin.x, evt.getY() - origin.y);
                    } else if(SwingUtilities.isRightMouseButton(evt)) {
                        n.removeLastGoal();
                    } 
                } 
                repaint();
            }
        });
        
        //map menu
        MapMenuListener ml = new MapMenuListener();
        frame.bigMazeMenuItem.addItemListener(ml);
        frame.circleMazeMenuItem.addItemListener(ml);
        frame.obstaclesMenuItem.addItemListener(ml);
        frame.customMenuItem.addItemListener(ml);
        frame.noMapMenuItem.addItemListener(ml);
        
        //more menu
        frame.randomGoals1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                int[] size = new int[2];
                size[0] = canvas.getSize().width;
                size[1] = canvas.getSize().height;
                n.generateRandomGoals(30, size);
                repaint();
            }
        });
        frame.randomGoals2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                int[] size = new int[2];
                size[0] = canvas.getSize().width;
                size[1] = canvas.getSize().height;
                n.generateRandomGoals(70, size);
                repaint();
            }
        });
    }
    
    private void repaint() {
        canvas.lines = n.getLines();
        canvas.goals = n.getGoals();
        canvas.start = n.getStart();
        canvas.boardSize = n.getSize();
        canvas.times = n.getTimes();
        canvas.additional = n.getAdditional();
        canvas.repaint();
    }
    
    private class MapMenuListener implements ItemListener {
        
         //map menu item changed
        @Override
        public void itemStateChanged(ItemEvent e) {
            try {
            if (e.getStateChange() == ItemEvent.SELECTED) {

                BufferedImage img = null;

                Object source = e.getItemSelectable();
                if (source == frame.bigMazeMenuItem) {
                    img = ImageIO.read(getClass().getResource("/resources/bigmaze.jpg"));
                } else if (source == frame.circleMazeMenuItem) {
                    img = ImageIO.read(getClass().getResource("/resources/circlemaze.jpg"));
                } else if (source == frame.obstaclesMenuItem) {
                    img = ImageIO.read(getClass().getResource("/resources/obstacles.jpg"));
                } else if (source == frame.customMenuItem) {
                    JFileChooser chooser = new JFileChooser(".");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION)  {
                        img = ImageIO.read(chooser.getSelectedFile());
                    } else {
                        return; //no changes
                    }
                } else if (source == frame.noMapMenuItem) {
                   img = null;
                }

                n.clear();
                n.setImage(img);
                frame.getCanvas().setImage(img);
                repaint();
            }
            } catch (IOException ex) {}
        }
    }
    
}
