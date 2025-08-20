import javax.swing.*;
import itsc2214.GameOfLife;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

@SuppressWarnings("serial")

/**
 * GameofLifeMain - main program to implement a java GUI
 * wrapper around project 1.
 */
public class GameofLifeMain {
    public static void main(String[] args)
    {
        new GameOfLifeMain("data/glidder.txt");
    }

    private int BOX_WIDTH = 20;
    private final int BORDER = 10;

    private JFrame win = null;
    private JComponent drawArea;
    private JLabel message;
    private JButton next;

    private boolean hasFile = false;
    private GameOfLife game;
    private int genNum;


    /**
     * GameofLifeMain - creates the graphical user interface and sets listeners
     * so that the image can be evaluated.
     */
    public GameOfLifeMain(String inFile) 
    {
        game = new Project1();
        try {
            game.loadFromFile(inFile);
            hasFile = true;
            genNum = 0;
            setupWindow(inFile);
        }
        catch (FileNotFoundException f)
        {
        }
    }
    public GameOfLifeMain() 
    {
        game = new Project1(10,10);
        game.randomInitialize(0.4);
        hasFile = false;
        genNum = 0;
        setupWindow(null);

    }

    /**
     * Set the title of the window to reflect either
     * a file that is loaded or a randomly generated
     * game...
     */
    private void setTitle(String message)
    {
        win.setTitle("GameOfFile - "+message);
    }
    /**
     * loadFile and set things up
     * @param inFile
     */
    private void loadFromFile(String file)
    {
        game = new Project1();
        try {
            game.loadFromFile(file);
            genNum = 0;
            hasFile = true;
        }
        catch (FileNotFoundException f) {}
    }

    /**
     * internal routine to setup the main window with all the buttons
     * and other options.
     * @param inFile
     */
    private void setupWindow(String inFile)
    {
        // create window
        win = new JFrame("GameOfLife");
        if (inFile == null) {
            setTitle("Random game");
            hasFile = false;
        }
        else {
            setTitle("File: "+inFile);
            hasFile = true;
        }
        win.getContentPane().setLayout(new BorderLayout());

        // Message panel on the North part of the border layout
        // showing a short message with the file name being
        // shown and a second message panel that will show
        // the results of the execution
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Creates a new window with a randomly generated
        // new game of life.
        JButton newWindow = new JButton("New");
        newWindow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new GameOfLifeMain();
            }
        });

        // Load - select a file and open that file in this
        // window (load file).
        JButton loadFile = new JButton("Load");
        loadFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)  {
                JFileChooser fileChooser = new JFileChooser();
                // Opens in user's home directory
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Now you can work with the selectedFile, e.g., read its content
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    loadFromFile(selectedFile.getAbsolutePath());
                    message.setText("Game loaded");
                    setTitle("File: "+selectedFile.getName());
                    next.setEnabled(true);  // enable the nextGen button again
                    drawArea.repaint();
                } else {
                    System.out.println("File selection cancelled");
                }
            }});

        // Reloads this game, if we had a file, reload it
        // back to its initial state
        // if this was randomly generated, just regenerates it.
        JButton reload = new JButton("Reload");
        reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (hasFile) {
                    loadFromFile(inFile);
                    message.setText("Game reloaded.");
                    genNum = 0;
                }
                else {
                    game.randomInitialize(0.4);
                    genNum = 0;
                    message.setText("");
                    setTitle("Random game");
                }
                next.setEnabled(true);  // enable the nextGen button again
                drawArea.repaint();
            }
        });

        // New Open Reload buttons
        panel.add(newWindow);
        panel.add(loadFile);
        panel.add(reload);
        win.getContentPane().add(panel, BorderLayout.NORTH);


        // The south part of the border layout shows
        // a row of buttons. Clear resets the grid to
        // the value before the traverse was run.
        // Traverse calls the traverse() method.
        // Pickup calls the pickupGoldCoins() method.
        panel = new JPanel();

        message = new JLabel("");
        panel.add(message);

        next = new JButton("▶");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                genNum++;
                game.nextGeneration();
                if (game.isStillLife()) {
                    next.setEnabled(false);
                    message.setText("Reached a still life");
                }
                else {
                    message.setText("Generation #"+genNum);
                }

                drawArea.repaint();
            }
        });
        panel.add(next);

        win.getContentPane().add(panel, BorderLayout.SOUTH);

        // The center part of the border layout contains
        // the grid area of the image.
        drawArea = new JComponent() {
            public void paintComponent(Graphics g) {
                BOX_WIDTH = (Math.min(getWidth(), getHeight()) - 2 * BORDER) / game.numCols();
                for (int i = 0; i < game.numRows(); i++) {
                    for (int j = 0; j < game.numCols(); j++) {
                        // draw square
                        drawCell(g, i, j);
                    }
                }
            }

            public void drawCell(Graphics g, int row, int col) {
                if (game.isAlive(row, col))
                    g.setColor(Color.GREEN);
                else
                    g.setColor(Color.WHITE);

                g.fillRect(BORDER + col * BOX_WIDTH, BORDER 
                    + row * BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
                g.setColor(Color.black);
                g.drawRect(BORDER + col * BOX_WIDTH, BORDER 
                    + row * BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
            }
        };
        win.getContentPane().add(drawArea, BorderLayout.CENTER);

        // Set the window size to fit everything snuggly
        win.setSize(400,500);
        win.setVisible(true);
    }
}
