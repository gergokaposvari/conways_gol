package test;

import main.GameOfLife;
import main.GridDrawer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridDrawerTest {
    Integer[][] correctGrid;
    GameOfLife gol;
    @BeforeEach
    public  void initialize(){
        correctGrid = new Integer[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 100, 0, 0},
                {0, 100, 100, 100, 0},
                {0, 0, 100, 0, 0},
                {0, 0, 0, 0, 0}
        };
        gol = new GameOfLife(correctGrid, "B3/S23");
    }

    @Test
    public void setGrid(){
        GridDrawer gridDrawer = new GridDrawer(gol.getCurrentState(), gol, "Négyszög alapú pálya");
        gol.simulateGeneration();
        gridDrawer.setGrid(gol.getCurrentState());

        Assertions.assertAll(
                () -> assertNotEquals(correctGrid, gridDrawer.getGrid())
        );
    }


}