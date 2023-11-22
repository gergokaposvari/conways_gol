package main;

import javax.swing.*;
import java.awt.*;


public class Menu extends JFrame{

    private static CardLayout cardLayout;

    public Menu(){
        super("Game of life");
        cardLayout = new CardLayout();
    }

    public void startMenu(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);

        JPanel newGamePanel = new JPanel();
        newGamePanel.add(new JLabel("Új játék"));
        cardPanel.add(newGamePanel, "newGame");

        JButton newGameButton = new JButton("Új játék");
        newGameButton.addActionListener(e -> cardLayout.show(cardPanel, "newGame"));

        JPanel loadGamePanel = new JPanel();
        loadGamePanel.add(new JLabel("Játék betöltése"));
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


        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(exitPanel, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
