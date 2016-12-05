/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
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
        frame = new MainFrame(n);
        
        startGUIThread();
        setActions();
        
        canvas = frame.getCanvas();
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
            canvas.repaint();
        });
        //fileChoose
        frame.fileChooseButton.addActionListener((ActionEvent evt) -> {
            JFileChooser chooser = new JFileChooser(".");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                n.setImage(chooser.getSelectedFile().getPath());
                canvas.setImage(chooser.getSelectedFile().getPath());
            }
            n.clear();
        });
        //search
        frame.searchButton.addActionListener((ActionEvent evt) -> {
            String graphAlgorithm = String.valueOf(frame.graphAlgorithmChooser.getSelectedItem());
            n.setGraphAlgorithm(graphAlgorithm);
            n.performSearch();
            canvas.repaint();
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
    }
    
}
