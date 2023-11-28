package test;

import main.GameOfLife;
import main.HexagonalGameOfLife;
import main.TriangularGameOfLife;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeTest {
    Integer[][] correctGrid;
        @BeforeEach
        public  void initialize(){
            correctGrid = new Integer[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 100, 0, 0},
                    {0, 100, 100, 100, 0},
                    {0, 0, 100, 0, 0},
                    {0, 0, 0, 0, 0}
            };
        }

        @Test
        public void rectangleNeighbor(){
            GameOfLife gol = new GameOfLife(correctGrid, "B3/S23");
            int count = gol.countAliveNeighbors(2, 2);

            Assertions.assertAll(
                    () -> assertEquals(4, count)
            );
        }

    @Test
    public void hexagonNeighbor(){
        GameOfLife gol = new HexagonalGameOfLife(correctGrid, "B3/S23");
        int count = gol.countAliveNeighbors(2, 2);

        Assertions.assertAll(
                () -> assertEquals(4, count)
        );
    }
    @Test
    public void triangularNeighbor(){
        GameOfLife gol = new TriangularGameOfLife(correctGrid, "B3/S23");
        int count = gol.countAliveNeighbors(2, 2);

        Assertions.assertAll(
                () -> assertEquals(3, count)
        );
    }

    @Test
    public void rectangleSimulation(){
        GameOfLife gol = new GameOfLife(correctGrid, "B3/S23");
        gol.simulateGeneration();
        Integer[][] expected = {
                {0, 0, 0, 0, 0},
                {0, 100, 100, 100, 0},
                {0, 100, 0, 100, 0},
                {0, 100, 100, 100, 0},
                {0, 0, 0, 0, 0}
        };

        Assertions.assertAll(
                () -> assertArrayEquals(expected, gol.getCurrentState())
        );
    }

    @Test
    public void hexagonSimulation(){
        GameOfLife gol = new HexagonalGameOfLife(correctGrid, "B3/S23");
        gol.simulateGeneration();
        Integer[][] expected = {
                {0, 0, 0, 0, 0},
                {0, 0, 100, 100, 0},
                {0, 100, 0, 0, 0},
                {0, 0, 100, 100, 0},
                {0, 0, 0, 0, 0}
        };

        Assertions.assertAll(
                () -> assertArrayEquals(expected, gol.getCurrentState())
        );
    }

    @Test
    public void triangleSimulation(){
        GameOfLife gol = new TriangularGameOfLife(correctGrid, "B3/S23");
        gol.simulateGeneration();
        Integer[][] expected = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 100, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };


        Assertions.assertAll(
                () -> assertArrayEquals(expected, gol.getCurrentState())
        );
    }

    @Test
    public void rectangleFadingSimulator(){
            GameOfLife gol = new GameOfLife(correctGrid, "B3/S23");
            gol.changeFading();
            gol.simulateGeneration();
            gol.simulateGeneration();
            Integer[][] expected = {
                {0, 0, 100, 0, 0},
                {0, 100, 90, 100, 0},
                {100, 90, 80, 90, 100},
                {0, 100, 90, 100, 0},
                {0, 0, 100, 0, 0}
            };

            Assertions.assertAll(
                    () -> assertArrayEquals(expected, gol.getCurrentState())
            );
    }

    @Test
    public void ruleGetter(){
            GameOfLife gol = new GameOfLife(correctGrid, "B13579/S13579");
            Object o = gol.getRule();

            Assertions.assertAll(
                    () -> assertEquals("B13579/S13579", o)
            );
    }

}