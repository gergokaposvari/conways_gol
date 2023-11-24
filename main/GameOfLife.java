package main;

import java.util.ArrayList;
import java.util.Objects;

public class GameOfLife {
    private Integer width;
    private Integer height;
    private Integer currentState[][];
    private Integer nextState[][];

    private static final Integer ALIVE = 100;
    private static final Integer DEAD = 0;

    private static final Integer SLEEPTIME = 500;

    private boolean isFading = false;
    private ArrayList<Integer> Born;
    private ArrayList<Integer> Alive;

    public GameOfLife(Integer x, Integer y, Object rule){
        Born = new ArrayList<>();
        Alive = new ArrayList<>();
        width = x;
        height = y;
        currentState = new Integer[width][height];
        nextState = new Integer[width][height];

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                nextState[i][j] = DEAD;
                currentState[i][j] = DEAD;
            }
        }
        boolean foundSlash = false;
        for(char c : rule.toString().toCharArray()){
            if(c != 'B' && !foundSlash){
                if(c == '/'){
                    foundSlash = true;
                }else {
                    Born.add(c - '0');
                }
            } else if (c != 'S' && foundSlash) {
                Alive.add(c - '0');
            }
        }
        System.out.println(Born);
        System.out.println(Alive);
    }

    public Integer[][] getCurrentState(){
        return currentState;
    }

    public Integer getCellState(Integer x, Integer y){
        return currentState[x][y];
    }

    public void changeCellState(Integer x, Integer y){
        if(Objects.equals(getCellState(x, y), ALIVE)){
            currentState[x][y] = DEAD;
            nextState[x][y] = DEAD;
        }else{
            currentState[x][y] = ALIVE;
            nextState[x][y] = ALIVE;
        }
    }

    public Integer countAliveNeighbours(Integer x, Integer y){
        Integer aliveNeighbours = 0;
        for(int i = (-1); i <= 1; i++){
            for(int j = (-1); j <= 1; j++){
                if(!(i == 0 && j == 0)){
                     int col = x + i;
                     int row = y + j;
                     if((col >= 0 && col < width) && (row >= 0 && row < height)) {
                         if (Objects.equals(getCellState((col), (row)), ALIVE)) {
                             aliveNeighbours++;
                         }
                     }
                }
            }
        }
        return aliveNeighbours;
    }

    public void calculateNextState(){
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++){
                if(Objects.equals(currentState[i][j], DEAD)){
                    if(Born.contains(countAliveNeighbours(i, j))){
                        nextState[i][j] = ALIVE;
                    }
                }else if(Objects.equals(currentState[i][j], ALIVE)){
                    if(Alive.contains(countAliveNeighbours(i, j))){
                        nextState[i][j] = ALIVE;
                    }else{
                        nextState[i][j] = DEAD;
                    }
                }
            }
        }
    }

    public void simulateGeneration(){
        calculateNextState();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                currentState[i][j] = nextState[i][j];
            }
        }
    }

    public void display(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(Objects.equals(getCellState(i, j), DEAD)){
                    System.out.print(".");
                }else{
                    System.out.print("*");
                }
            }
            System.out.print("\n");
        }
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                System.out.print(countAliveNeighbours(i, j));
            }
            System.out.print("\n");
        }
    }
}