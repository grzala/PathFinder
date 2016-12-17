/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinder;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

/**
 *
 * @author grzala
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    
    private final ButtonGroup canvasMode;
    
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        initComponents();
        
        canvasMode = new ButtonGroup();
        canvasMode.add(setStartButton);
        canvasMode.add(panButton);
        canvasMode.add(setGoalButton);
        
        canvas.setMode(Canvas.Mode.PAN);
        
        URL iconURL = getClass().getResource("/resources/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        setButtonIcons();
        
        canvas.requestFocus(true);
    }
    
    private void setButtonIcons() {
        try {
            BufferedImage img = null;
            Dimension dim = null;
            Image dimg = null;
            ImageIcon imageIcon = null;
            
            //start button
            img = ImageIO.read(getClass().getResource("/resources/start.png"));
            dim = setStartButton.getSize();
            dimg = img.getScaledInstance(dim.width * 3/4, dim.height * 3/4, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            setStartButton.setIcon(imageIcon);

            //goal button
            img = ImageIO.read(getClass().getResource("/resources/goal.png"));
            dim = setGoalButton.getSize();
            dimg = img.getScaledInstance(dim.width * 3/4, dim.height * 3/4, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            setGoalButton.setIcon(imageIcon);
            
            //pan button
            img = ImageIO.read(getClass().getResource("/resources/pan.png"));
            dim = panButton.getSize();
            dimg = img.getScaledInstance(dim.width * 3/4, dim.height * 3/4, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            panButton.setIcon(imageIcon);
            
            //clear button
            img = ImageIO.read(getClass().getResource("/resources/clear.png"));
            dim = clearButton.getSize();
            dimg = img.getScaledInstance(dim.width * 3/4, dim.height * 3/4, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            clearButton.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mapGroup = new javax.swing.ButtonGroup();
        controlPanel = new javax.swing.JPanel();
        choicePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pathAlgorithmChooser = new javax.swing.JComboBox<>();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel2 = new javax.swing.JLabel();
        graphAlgorithmChooser = new javax.swing.JComboBox<>();
        buttonPanel = new javax.swing.JPanel();
        panButton = new javax.swing.JToggleButton();
        setStartButton = new javax.swing.JToggleButton();
        setGoalButton = new javax.swing.JToggleButton();
        clearButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 32767));
        searchButton = new javax.swing.JButton();
        canvasPanel = new javax.swing.JPanel();
        canvas = new pathfinder.Canvas();
        jMenuBar = new javax.swing.JMenuBar();
        mapMenu = new javax.swing.JMenu();
        bigMazeMenuItem = new javax.swing.JCheckBoxMenuItem();
        circleMazeMenuItem = new javax.swing.JCheckBoxMenuItem();
        obstaclesMenuItem = new javax.swing.JCheckBoxMenuItem();
        customMenuItem = new javax.swing.JCheckBoxMenuItem();
        noMapMenuItem = new javax.swing.JCheckBoxMenuItem();
        moreMenu = new javax.swing.JMenu();
        randomGoals1 = new javax.swing.JMenuItem();
        randomGoals2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PathFinder");
        setFocusable(false);
        setIconImages(null);
        setMinimumSize(new java.awt.Dimension(500, 200));
        setName("MainFrame"); // NOI18N
        setSize(new java.awt.Dimension(640, 480));

        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Pathfinding Algorithm");
        choicePanel.add(jLabel1);

        pathAlgorithmChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A*", "Dijkstra", "Heuristic search" }));
        choicePanel.add(pathAlgorithmChooser);
        choicePanel.add(filler3);

        jLabel2.setText("Graph search algorithm");
        choicePanel.add(jLabel2);

        graphAlgorithmChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Closest Neighbour", "Brute Force", "MST Kruskal", "MST Prim", "None" }));
        graphAlgorithmChooser.setFocusable(false);
        choicePanel.add(graphAlgorithmChooser);

        controlPanel.add(choicePanel);

        panButton.setSelected(true);
        panButton.setToolTipText("Pan: click to move around the map");
        panButton.setFocusPainted(false);
        panButton.setMaximumSize(null);
        panButton.setMinimumSize(null);
        panButton.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonPanel.add(panButton);

        setStartButton.setToolTipText("Set Start: left click to move start point on the map");
        setStartButton.setFocusPainted(false);
        setStartButton.setMaximumSize(null);
        setStartButton.setMinimumSize(null);
        setStartButton.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonPanel.add(setStartButton);

        setGoalButton.setToolTipText("Set Goals: left click to add a goal, right click to remove last added goal");
        setGoalButton.setFocusPainted(false);
        setGoalButton.setMaximumSize(null);
        setGoalButton.setMinimumSize(null);
        setGoalButton.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonPanel.add(setGoalButton);

        clearButton.setToolTipText("Clear: click to remove all goals");
        clearButton.setFocusPainted(false);
        clearButton.setMaximumSize(null);
        clearButton.setMinimumSize(null);
        clearButton.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonPanel.add(clearButton);
        buttonPanel.add(filler2);

        searchButton.setFocusPainted(false);
        searchButton.setLabel("GO!");
        buttonPanel.add(searchButton);

        controlPanel.add(buttonPanel);

        getContentPane().add(controlPanel, java.awt.BorderLayout.PAGE_START);

        canvasPanel.setLayout(new javax.swing.BoxLayout(canvasPanel, javax.swing.BoxLayout.LINE_AXIS));

        canvas.setPreferredSize(new java.awt.Dimension(640, 450));

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 397, Short.MAX_VALUE)
        );

        canvasPanel.add(canvas);

        getContentPane().add(canvasPanel, java.awt.BorderLayout.CENTER);

        mapMenu.setText("Map");

        mapGroup.add(bigMazeMenuItem);
        bigMazeMenuItem.setText("Big maze");
        mapMenu.add(bigMazeMenuItem);

        mapGroup.add(circleMazeMenuItem);
        circleMazeMenuItem.setText("Circle maze");
        mapMenu.add(circleMazeMenuItem);

        mapGroup.add(obstaclesMenuItem);
        obstaclesMenuItem.setText("Obstacles");
        mapMenu.add(obstaclesMenuItem);

        mapGroup.add(customMenuItem);
        customMenuItem.setText("Custom map");
        mapMenu.add(customMenuItem);

        mapGroup.add(noMapMenuItem);
        noMapMenuItem.setSelected(true);
        noMapMenuItem.setText("No map");
        mapMenu.add(noMapMenuItem);

        jMenuBar.add(mapMenu);

        moreMenu.setText("More");

        randomGoals1.setText("30 random goals");
        randomGoals1.setToolTipText("This may cause poor performance. This is because obstacles are kept as an array and itakes O(n) each path step, but saves memory. This will be changed in the future");
        moreMenu.add(randomGoals1);

        randomGoals2.setText("70 random goals");
        randomGoals2.setToolTipText("Dont' use this on maps with obstacles!");
        moreMenu.add(randomGoals2);

        jMenuBar.add(moreMenu);

        setJMenuBar(jMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void createAndShowGUI() {
        setVisible(true);
        setLocation(100, 100);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JCheckBoxMenuItem bigMazeMenuItem;
    private javax.swing.JPanel buttonPanel;
    private pathfinder.Canvas canvas;
    private javax.swing.JPanel canvasPanel;
    private javax.swing.JPanel choicePanel;
    public javax.swing.JCheckBoxMenuItem circleMazeMenuItem;
    public javax.swing.JButton clearButton;
    private javax.swing.JPanel controlPanel;
    public javax.swing.JCheckBoxMenuItem customMenuItem;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    public javax.swing.JComboBox<String> graphAlgorithmChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.ButtonGroup mapGroup;
    private javax.swing.JMenu mapMenu;
    public javax.swing.JMenu moreMenu;
    public javax.swing.JCheckBoxMenuItem noMapMenuItem;
    public javax.swing.JCheckBoxMenuItem obstaclesMenuItem;
    public javax.swing.JToggleButton panButton;
    public javax.swing.JComboBox<String> pathAlgorithmChooser;
    public javax.swing.JMenuItem randomGoals1;
    public javax.swing.JMenuItem randomGoals2;
    public javax.swing.JButton searchButton;
    public javax.swing.JToggleButton setGoalButton;
    public javax.swing.JToggleButton setStartButton;
    // End of variables declaration//GEN-END:variables
}
