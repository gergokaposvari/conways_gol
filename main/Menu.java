package main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Objects;


public class Menu extends JFrame{

    private static CardLayout cardLayout;
    private final JComboBox<Object> gridSelector = new JComboBox<>(availableGrids);
    private final JComboBox<Object> ruleSelector = new JComboBox<>();

    private static final Object[] availableGrids = new Object[3];
    private static final Object[] rectangleRule = new Object[1];
    private static final Object[] hexagonRule = new Object[1];
    private static final Object[] triangleRule = new Object[1];

    static{
        availableGrids[0] = "Négyszög alapú pálya";
        availableGrids[1] = "Hatszög alapú pálya";
        availableGrids[2] = "Háromszög alapú pálya";

        rectangleRule[0] = "B3/S23";
        hexagonRule[0] = "B3/S23";
        triangleRule[0] = "B3/S23";
    }


    public static Integer SLEEPTIME = 960;
    static boolean paused = true;





    public Menu(){
        super("Game of life");
        cardLayout = new CardLayout();
    }

    public void startMenu(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 250);
        this.setResizable(false);
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);

        JPanel newGamePanel = new JPanel(new BorderLayout());
        cardPanel.add(newGamePanel, "newGame");

        JButton newGameButton = new JButton("Új játék");
        newGameButton.addActionListener(e -> cardLayout.show(cardPanel, "newGame"));

        JPanel loadGamePanel = new JPanel();
        cardPanel.add(loadGamePanel, "loadGame");

        JButton loadGameButton = new JButton("Játék betöltése");
        loadGameButton.addActionListener(e -> cardLayout.show(cardPanel, "loadGame"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);

        JButton exitButton = new JButton("Kilépés");
        exitButton.addActionListener(e -> System.exit(0));
        JPanel exitPanel = new JPanel();
        exitPanel.add(exitButton);

        gridSelector.addActionListener(e -> updateRuleSelector());

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.add(gridSelector);
        comboBoxPanel.add(ruleSelector);

        newGamePanel.add(comboBoxPanel, BorderLayout.CENTER);


        JButton startButton = new JButton("Játék indítása a megadott beállításokkal");
        startButton.addActionListener(e -> {
            try {
                startNewGame();
            } catch (InterruptedException ex) {
                System.out.println("Nem sikerült elindítani");
            }
        });
        newGamePanel.add(startButton, BorderLayout.SOUTH);

        JButton saveSelect = new JButton("Mentési fájl kiválasztása");
        saveSelect.addActionListener(e -> loadGame());
        loadGamePanel.add(saveSelect);


        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(exitPanel, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void startNewGame() throws InterruptedException {
        if(ruleSelector.getSelectedItem() != null) {
            GameOfLife gol = new GameOfLife(65, 65, ruleSelector.getSelectedItem());
            start(gol);
        }else{
            JOptionPane.showMessageDialog(this, "Nem választottad ki a játékszabályt!");
        }
    }

    public void loadGame(){
        File workingDirectory = new File(System.getProperty("user.dir"));


        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(workingDirectory);
        fc.showOpenDialog(this);
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()))){
            Integer[][] board = (Integer[][]) in.readObject();
            Object rule = in.readObject();

            GameOfLife gol = new GameOfLife(board, rule);
            start(gol);
        }catch(ClassNotFoundException | IOException exception){
            JOptionPane.showMessageDialog(this, "Nem sikerult betolteni" + exception.toString());
        }catch(NullPointerException exception){
            JOptionPane.showMessageDialog(this, "Nem választottál fájlt!");
        }
    }

    public void start(GameOfLife loadGame) {
        GridDrawer grid = new GridDrawer(loadGame.getCurrentState(), loadGame);

        Thread simulationThread = new Thread(() -> {
            try {
                grid.run();
            } catch (InterruptedException exception) {
                System.out.println("Nem sikerült a körszimuláció");
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
        SwingUtilities.invokeLater(() -> {
            grid.setVisible(true);
        });
    }

    public void updateRuleSelector(){
            Object grid = gridSelector.getSelectedItem();
            DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
            if(Objects.equals(grid, availableGrids[0])){
                model.addAll(List.of(rectangleRule));
            }else if(Objects.equals(grid, availableGrids[1])){
                model.addAll(List.of(hexagonRule));
            }else{
                model.addAll(List.of(triangleRule));
            }
            ruleSelector.setModel(model);
    }
    public static void increaseSleepTime(){
        if(SLEEPTIME < 3000) {
            SLEEPTIME = SLEEPTIME * 2;
            System.out.println(SLEEPTIME);
        }
    }
    public static void decreaseSleepTime(){
        if(SLEEPTIME > 30) {
            SLEEPTIME = SLEEPTIME/2;
            System.out.println(SLEEPTIME);
        }
    }
    public static void startStop(){
        paused = !paused;
    }
}