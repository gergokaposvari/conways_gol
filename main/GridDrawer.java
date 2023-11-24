package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GridDrawer extends JFrame {

    private Integer[][] grid;
    private GridPanel gridPanel;

    private int highlightedRow = -1;
    private int highlightedCol = -1;

    JPanel interactionPanel;

    public GridDrawer(Integer[][] grid) {
        this.grid = grid;
        initUI();
    }

    private void initUI() {
        setTitle("Életjáték a négyzetrácson");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 1080);
        setLocationRelativeTo(null);

        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);

        interactionPanel = new JPanel(new BorderLayout());

        JButton increaseSpeed = new JButton("Sebesség++");
        increaseSpeed.addActionListener(e -> Main.decreaseSleepTime());

        JButton decreaseSpeed = new JButton("Sebesség--");
        decreaseSpeed.addActionListener(e -> Main.increaseSleepTime());

        JButton startStop = new JButton("Indítás/megállítás");
        startStop.addActionListener(e -> Main.startStop());

        interactionPanel.add(increaseSpeed, BorderLayout.NORTH);
        interactionPanel.add(decreaseSpeed, BorderLayout.SOUTH);
        interactionPanel.add(startStop,BorderLayout.CENTER);

        add(interactionPanel, BorderLayout.EAST);


        setVisible(true);
    }

    public void setGrid(Integer[][] newGrid) {
        this.grid = newGrid;
        gridPanel.repaint();
    }

    private class GridPanel extends JPanel {

        public GridPanel() {
            addMouseListener(new GridMouseListener());
            addMouseMotionListener(new GridMouseMotionListener());
        }

        private int width;
        private int height;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            width = getWidth();
            height = getHeight();
            int cellSize = Math.min(width / grid[0].length, height / grid.length);

            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    int x = col * cellSize;
                    int y = row * cellSize;

                    if (grid[row][col] != null && grid[row][col] == 100) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    g.fillRect(x, y, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellSize, cellSize);
                }
            }
        }


        private class GridMouseListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = Math.min(width / grid[0].length, height / grid.length);

                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;

                if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
                    System.out.println("changestate: " + col + " " + row);
                    GameOfLife.changeCellState(row, col);
                    setGrid(GameOfLife.getCurrentState());
                }
            }
        }

        private class GridMouseMotionListener implements MouseMotionListener {
            @Override
            public void mouseMoved(MouseEvent e) {
                int cellSize = Math.min(width / grid[0].length, height / grid.length);

                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;

                if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
                    if (highlightedRow != row || highlightedCol != col) {
                        highlightedRow = row;
                        highlightedCol = col;
                        gridPanel.repaint();
                    }
                } else {
                    if (highlightedRow != -1 || highlightedCol != -1) {
                        highlightedRow = -1;
                        highlightedCol = -1;
                        gridPanel.repaint();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Handle mouse dragging if needed
            }
        }
    }
}
