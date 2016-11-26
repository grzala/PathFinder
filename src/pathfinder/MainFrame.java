/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author grzala
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    Navigator n;
    
    public MainFrame(Navigator n) {
        initComponents();
        canvas.setNavigator(n);
        
        this.n = n;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clearButton = new javax.swing.JButton();
        fileChooseButton = new javax.swing.JButton();
        canvas = new pathfinder.Canvas();
        searchButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PathFInder");
        setFocusable(false);
        setName("MainFrame"); // NOI18N

        clearButton.setText("clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        fileChooseButton.setText("File");
        fileChooseButton.setActionCommand("filechoose");
        fileChooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );

        searchButton.setText("search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(clearButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileChooseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton)
                        .addGap(0, 469, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(fileChooseButton)
                    .addComponent(searchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        n.clearGoals();
        canvas.repaint();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void fileChooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooseButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           n.setImage(chooser.getSelectedFile().getPath());
           canvas.setImage(chooser.getSelectedFile().getPath());
        }
        
    }//GEN-LAST:event_fileChooseButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        n.performSearch();
        canvas.repaint();
        System.out.println("CLICKED");
    }//GEN-LAST:event_searchButtonActionPerformed

    public void createAndShowGUI() {
        setVisible(true);
        setLocation(100, 100);
    }

    
    public void paintLoop() {
        while (true) {
            try {
                canvas.repaint();
                Thread.sleep(10);
            } catch(InterruptedException e) {
            }
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pathfinder.Canvas canvas;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton fileChooseButton;
    private javax.swing.JButton searchButton;
    // End of variables declaration//GEN-END:variables
}
