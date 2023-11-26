package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class GridDrawer extends JFrame {
    
    GameOfLife currentGame;
    private Integer[][] grid;
    private GridPanel gridPanel;
    JPanel interactionPanel;

    public GridDrawer(Integer[][] grid, GameOfLife gol) {
        currentGame = gol;
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

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("Fájl");

        JMenuItem saveAndExit = new JMenuItem("Mentés és kilépés");
        saveAndExit.addActionListener(e -> {
            JFrame frame = new JFrame();
            String message = "A mentési fájl neve:";
            String text = JOptionPane.showInputDialog(frame, message);
            boolean abortSave = false;
            if (text == null) {
                abortSave = true;
            }
            if(!abortSave) {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("saves/" + text + ".txt"))){
                    out.writeObject(currentGame.getCurrentState());
                    out.writeObject(currentGame.getRule());
                    System.exit(0);
                } catch (IOException ex) {
                    System.out.println(ex + "\n Nem sikerült menteni!");
                }
            }
        });

        JMenuItem onlyExit = new JMenuItem("Kilépés");
        onlyExit.addActionListener(e -> System.exit(0));

        file.add(saveAndExit);
        file.add(onlyExit);

        menuBar.add(file);
        setJMenuBar(menuBar);


        interactionPanel = new JPanel(new BorderLayout());

        JButton increaseSpeed = new JButton("Sebesség++");
        increaseSpeed.addActionListener(e -> Menu.decreaseSleepTime());
        JPanel increasePanel = new JPanel();
        increasePanel.add(increaseSpeed);

        JButton decreaseSpeed = new JButton("Sebesség--");
        decreaseSpeed.addActionListener(e -> Menu.increaseSleepTime());
        JPanel decreasePanel = new JPanel();
        decreasePanel.add(decreaseSpeed);

        JButton startStop = new JButton("Indítás");
        startStop.addActionListener(e -> {
            Menu.startStop();
            if(Objects.equals(startStop.getText(), "Indítás")){
                startStop.setText("Megállítás");
            }else{
                startStop.setText("Indítás");
            }
        });
        JPanel startStopPanel = new JPanel();
        startStopPanel.add(startStop);

        JButton changeFading = new JButton("Színátmenet be");
        changeFading.addActionListener(e -> {
            currentGame.changeFading();
            if(Objects.equals(changeFading.getText(), "Színátmenet be")){
                changeFading.setText("Színátmenet ki");
            }else{
                changeFading.setText("Színátmenet be");
            }
        });
        JPanel fadingPanel = new JPanel();
        fadingPanel.add(changeFading);

        JPanel togglePanel = new JPanel();

        togglePanel.add(fadingPanel, BorderLayout.NORTH);
        togglePanel.add(startStopPanel, BorderLayout.SOUTH);

        interactionPanel.add(increasePanel, BorderLayout.NORTH);
        interactionPanel.add(togglePanel, BorderLayout.CENTER);
        interactionPanel.add(decreasePanel, BorderLayout.SOUTH);


        add(interactionPanel, BorderLayout.EAST);
    }

    public void setGrid(Integer[][] newGrid) {
        grid = newGrid;
        gridPanel.repaint();
    }

    public void run() throws InterruptedException {
        while(true) {
            Thread.sleep(Menu.SLEEPTIME);
            if(!Menu.paused) {
                currentGame.simulateGeneration();
                setGrid(currentGame.getCurrentState());
                currentGame.display();
            }
        }
    }

    private class GridPanel extends JPanel {

        public GridPanel() {
            addMouseListener(new GridMouseListener());
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

                    Color cellColor = Color.WHITE;

                    for(int i = 0; i <= 100; i += 10){
                        if(grid[row][col] == i && grid[row][col] != null){
                            int unit = (255/100) * i;
                            int red = 255;
                            int green = 255 - unit;
                            int blue = 255 - unit;
                            cellColor = new Color(red, blue, green);
                        }
                    }

                    g.setColor(cellColor);
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
                    currentGame.changeCellState(row, col);
                    setGrid(currentGame.getCurrentState());
                }
            }
        }
    }
}
