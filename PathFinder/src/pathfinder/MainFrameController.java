/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Point;
import java.awt.event.ActionEvent;
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
        new Thread(() -> {
            frame.createAndShowGUI();
        }).start();
    }
    
    private void setActions() {
        //clear
        frame.clearButton.addActionListener((ActionEvent evt) -> {
            n.clear();
            repaint();
        });
        //fileChoose
        frame.fileChooseButton.addActionListener((ActionEvent evt) -> {
            String path = null;
            MapChooser mc = new MapChooser(frame);
            int returned = mc.returnVal();
            if (returned == MapChooser.CHOOSE) {
                path = mc.getChoice();
            } else if (returned == MapChooser.CUSTOM) {
                JFileChooser chooser = new JFileChooser(".");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) 
                    path = chooser.getSelectedFile().getPath();
            }
            
            n.clear();
            n.setImage(path);
            canvas.setImage(path);
            repaint();
        });
        //search
        frame.searchButton.addActionListener((ActionEvent evt) -> {
            String graphAlgorithm = String.valueOf(frame.graphAlgorithmChooser.getSelectedItem());
            String pathAlgorithm = String.valueOf(frame.pathAlgorithmChooser.getSelectedItem());
            n.setGraphAlgorithm(graphAlgorithm);
            n.setPathAlgorithm(pathAlgorithm);
            n.performSearch();
            repaint();
        });
        //pan
        frame.panButton.addActionListener((ActionEvent evt) -> {
            canvas.setMode(Canvas.Mode.PAN);
        });
        //set start
        frame.setStartButton.addActionListener((ActionEvent evt) -> {
            canvas.setMode(Canvas.Mode.SETSTART);
        });
        //clear
        frame.setGoalButton.addActionListener((ActionEvent evt) -> {
            canvas.setMode(Canvas.Mode.SETGOAL);
        });
        
        //canvas clicks
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
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
    
}
