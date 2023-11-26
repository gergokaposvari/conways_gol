package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class GameOfLife implements Serializable {
    private final Integer width;
    private final Integer height;
    private static Integer[][] currentState;
    private static Integer[][] nextState;
    private static boolean isFading = false;
    private static final Integer ALIVE = 100;
    private static final Integer DEAD = 0;
    private final Object object;
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
        this.object = rule;
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

    public GameOfLife(Integer[][] board, Object object){
        this(65,65, object);
        for(int i = 0; i < 65; i++){
            for(int j = 0; j < 65; j++){
                currentState[i][j] = board[i][j];
                nextState[i][j] = board[i][j];
            }
        }
    }

    public Integer[][] getCurrentState(){
        return currentState;
    }

    public Object getRule(){
        return object;
    }

    public static Integer getCellState(Integer x, Integer y){
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
        if(!isFading) {
            deleteNonBinary();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (Objects.equals(currentState[i][j], DEAD)) {
                        if (Born.contains(countAliveNeighbours(i, j))) {
                            nextState[i][j] = ALIVE;
                        }
                    } else if (Objects.equals(currentState[i][j], ALIVE)) {
                        if (Alive.contains(countAliveNeighbours(i, j))) {
                            nextState[i][j] = ALIVE;
                        } else {
                            nextState[i][j] = DEAD;
                        }
                    }
                }
            }
        }else{
            for(int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    if(Objects.equals(currentState[i][j], ALIVE)){
                        if(Alive.contains(countAliveNeighbours(i, j))){
                            nextState[i][j] = ALIVE;
                        }else{
                            nextState[i][j] = 90;
                        }
                    }else{
                        if(Born.contains(countAliveNeighbours(i, j))) {
                            nextState[i][j] = ALIVE;
                        }else{
                            for(int k = 90; k >= 0; k -= 10){
                                if(Objects.equals(currentState[i][j], k)){
                                    if(k == 0){
                                        nextState[i][j] = 0;
                                    } else {
                                        nextState[i][j] -= 10;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteNonBinary(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(currentState[i][j] != 100 && currentState[i][j] != 0){
                    currentState[i][j] = DEAD;
                    nextState[i][j] = DEAD;
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
                System.out.print(currentState[i][j]);
            }
            System.out.print("\n");
        }
    }

    public void changeFading(){
        isFading = !isFading;
    }

}