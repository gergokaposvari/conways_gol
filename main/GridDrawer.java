package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Random;

public class GridDrawer extends JFrame {
    
    GameOfLife currentGame;
    private Integer[][] grid;
    private JPanel gridPanel;
    JPanel interactionPanel;
    Object shape;

    public GridDrawer(Integer[][] grid, GameOfLife gol, Object gridType) {
        currentGame = gol;
        this.grid = grid;
        this.shape = gridType;
        initUI(gridType);
    }

    private void initUI(Object gridType) {
        setTitle("Életjáték a négyzetrácson");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 1080);
        setLocationRelativeTo(null);

        switch (gridType.toString()){
            case "Hatszög alapú pálya":
                gridPanel = new HexagonalGrid();
                break;

            case "Háromszög alapú pálya":
                gridPanel = new TriangularGrid();
                break;
            default:
                gridPanel = new GridPanel();
                break;
        }
        add(gridPanel, BorderLayout.CENTER);

        JMenuBar menuBar = getjMenuBar();
        setJMenuBar(menuBar);


        interactionPanel = new JPanel();
        interactionPanel.setLayout(new BoxLayout(interactionPanel, BoxLayout.Y_AXIS));

        JButton increaseSpeed = new JButton("Sebesség++");
        increaseSpeed.addActionListener(e -> Menu.decreaseSleepTime());
        JPanel increasePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        increasePanel.add(increaseSpeed);

        JButton decreaseSpeed = new JButton("Sebesség--");
        decreaseSpeed.addActionListener(e -> Menu.increaseSleepTime());
        JPanel decreasePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
        JPanel startStopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startStopPanel.add(startStop);

        Random random = new Random();
        JButton setGridRandomly = new JButton("Randomizálás");
        setGridRandomly.addActionListener(e -> {
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    int randomValue = random.nextInt(2);
                    if(randomValue % 2 == 0){
                        currentGame.changeCellState(i, j);
                    }

                }
            }
            setGrid(currentGame.getCurrentState());
        });
        JPanel setRandomlyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        setRandomlyPanel.add(setGridRandomly);

        JButton changeFading = new JButton("Színátmenet be");
        changeFading.addActionListener(e -> {
            currentGame.changeFading();
            if(Objects.equals(changeFading.getText(), "Színátmenet be")){
                changeFading.setText("Színátmenet ki");
            }else{
                changeFading.setText("Színátmenet be");
            }
        });
        JPanel fadingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fadingPanel.add(changeFading);


        interactionPanel.add(increaseSpeed);
        interactionPanel.add(startStop);
        interactionPanel.add(setRandomlyPanel);
        interactionPanel.add(changeFading);
        interactionPanel.add(decreaseSpeed);


        add(interactionPanel, BorderLayout.EAST);
    }

    private JMenuBar getjMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("Fájl");

        JMenuItem saveAndExit = new JMenuItem("Mentés és kilépés");
        saveAndExit.addActionListener(e -> {
            JFrame frame = new JFrame();
            String message = "A mentési fájl neve:";
            String text = JOptionPane.showInputDialog(frame, message);
            boolean abortSave = (text == null);
            if(!abortSave) {
                String location = switch (shape.toString()) {
                    case "Hatszög alapú pálya" -> "saves/hexagon_grid/";
                    case "Háromszög alapú pálya" -> "saves/triangle_grid/";
                    default -> "saves/rectangle_grid/";
                };
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(location + text + ".txt"))){
                    out.writeObject(currentGame.getCurrentState());
                    out.writeObject(currentGame.getRule());
                    out.writeObject(shape);
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
        return menuBar;
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
            }
        }
    }

    public class GridPanel extends JPanel {

        public GridPanel() {
            addMouseListener(new GridMouseListener());
        }

        public int width;
        public int height;


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

    public class HexagonalGrid extends JPanel {

        private static int HEX_SIZE;
        private static int HEX_WIDTH ;
        private static int HEX_HEIGHT;

        public int width;
        public int height;


        public HexagonalGrid(){
            addMouseListener(new HexGridMouseListener());
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            width = getWidth();
            height = getHeight();
            HEX_SIZE = 6;
            HEX_WIDTH = (int) (Math.sqrt(3) * HEX_SIZE);
            HEX_HEIGHT = 2 * HEX_SIZE;

            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    int x = (col * HEX_WIDTH) + HEX_WIDTH;
                    int y = (row * (HEX_HEIGHT - HEX_HEIGHT/4)) + HEX_HEIGHT; // Adjust spacing as needed

                    // Offset odd rows by half a hexagon's width
                    if (row % 2 == 1) {
                        x -= HEX_WIDTH / 2;
                    }


                    drawHexagon(g2d, x, y, grid[row][col]);
                }
            }
        }

        private void drawHexagon(Graphics2D g2d, int x, int y, Integer state) {
            int[] xPoints = new int[6];
            int[] yPoints = new int[6];

            for (int i = 0; i < 6; i++) {
                double angleRad = Math.toRadians(60 * i - 30); // Adjust the angle for pointy orientation
                xPoints[i] = x + (int) (HEX_SIZE * Math.cos(angleRad));
                yPoints[i] = y + (int) (HEX_SIZE * Math.sin(angleRad));
            }

            Color cellColor = Color.WHITE;

            for(int i = 0; i <= 100; i += 10){
                if(state == i){
                    int unit = (255/100) * i;
                    int red = 255;
                    int green = 255 - unit;
                    int blue = 255 - unit;
                    cellColor = new Color(red, blue, green);
                }
            }

            g2d.setColor(cellColor);

            g2d.fillPolygon(xPoints, yPoints, 6);

            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 6);

        }

        private class HexGridMouseListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {

                int col;
                int row = (e.getY() - HEX_HEIGHT) / (HEX_HEIGHT - HEX_HEIGHT/4);
                if(row % 2 == 1){
                    col = ((e.getX() + HEX_WIDTH/4) - HEX_WIDTH/2) / HEX_WIDTH;
                }else{
                    col = ((e.getX() + HEX_WIDTH/4) - HEX_WIDTH) / HEX_WIDTH;
                }


                if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
                    currentGame.changeCellState(row, col);
                    setGrid(currentGame.getCurrentState());
                }
            }
        }
    }
    public class TriangularGrid extends JPanel{
        private static int TRIANGLE_SIZE;
        private static int TRIANGLE_SIDE;
        private static int TRIANGLE_HEIGHT;

        public int height;
        public int width;

        public TriangularGrid(){
            addMouseListener(new TriangleGridMouseListener());
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            width = getWidth();
            height = getHeight();
            TRIANGLE_SIZE = 7;
            TRIANGLE_HEIGHT = TRIANGLE_SIZE * 2;
            TRIANGLE_SIDE = (int) ((TRIANGLE_HEIGHT * 2) / Math.sqrt(3));

            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    int x = (col * TRIANGLE_SIDE/2 + TRIANGLE_SIDE);
                    int y = row * TRIANGLE_HEIGHT + TRIANGLE_HEIGHT/2;

                    boolean flippedOrientation = (row % 2 == 0) ? col % 2 ==0 : col % 2 == 1;
                    drawTriangle(g2d, x, y, grid[row][col], flippedOrientation);
                }
            }
        }
        private void drawTriangle(Graphics2D g2d, int x, int y, Integer state, boolean flipped) {
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];

            if (!flipped) {
                xPoints[0] = x;
                yPoints[0] = y;
                xPoints[1] = x + TRIANGLE_SIDE / 2;
                yPoints[1] = y + TRIANGLE_HEIGHT;
                xPoints[2] = x - TRIANGLE_SIDE / 2;
                yPoints[2] = y + TRIANGLE_HEIGHT;
            } else {
                xPoints[0] = x;
                yPoints[0] = y + TRIANGLE_HEIGHT;
                xPoints[1] = x + TRIANGLE_SIDE / 2;
                yPoints[1] = y;
                xPoints[2] = x - TRIANGLE_SIDE / 2;
                yPoints[2] = y;
            }

            Color cellColor = Color.WHITE;

            for(int i = 0; i <= 100; i += 10){
                if(state == i){
                    int unit = (255/100) * i;
                    int red = 255;
                    int green = 255 - unit;
                    int blue = 255 - unit;
                    cellColor = new Color(red, blue, green);
                }
            }

            g2d.setColor(cellColor);

            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 3);
        }
        private class TriangleGridMouseListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {

                int col = ((e.getX() - TRIANGLE_SIDE + TRIANGLE_SIDE/4) * 2 / TRIANGLE_SIDE);
                int row = ((e.getY() - TRIANGLE_HEIGHT/2)/TRIANGLE_HEIGHT);

                if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
                    currentGame.changeCellState(row, col);
                    setGrid(currentGame.getCurrentState());
                }
            }
        }
    }
}
