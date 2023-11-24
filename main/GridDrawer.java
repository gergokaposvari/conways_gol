package main;

import javax.swing.*;
import java.awt.*;

public class GridDrawer extends JFrame {

    private Integer[][] grid;
    private GridPanel gridPanel;

    public GridDrawer(Integer[][] grid) {
        this.grid = grid;
        initUI();
    }

    private void initUI() {
        setTitle("Grid Drawer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        gridPanel = new GridPanel();
        add(gridPanel);

        setVisible(true);
    }

    public void setGrid(Integer[][] newGrid) {
        this.grid = newGrid;
        gridPanel.repaint();
    }

    private class GridPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellSize = Math.min(getWidth() / grid[0].length, getHeight() / grid.length);

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Integer[][] array = {
                    {1, 0, 1, 0},
                    {0, 1, 0, 1},
                    {1, 0, 1, 0}
            };
            GridDrawer gridDrawer = new GridDrawer(array);

            // Simulate an update to the array
            Integer[][] updatedArray = {
                    {0, 1, 0, 1},
                    {1, 0, 1, 0},
                    {0, 1, 0, 1}
            };

            // Update the array and repaint the grid
            gridDrawer.setGrid(updatedArray);
        });
    }
}
