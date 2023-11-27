package main;

import java.util.Objects;

public class HexagonalGameOfLife extends GameOfLife{
    public HexagonalGameOfLife(Integer x, Integer y, Object rule) {
        super(x, y, rule);
    }

    public HexagonalGameOfLife(Integer[][] board, Object object) {
        super(board, object);
    }

    @Override
    public Integer countAliveNeighbors(Integer x, Integer y) {
        Integer aliveNeighbors = 0;
        int[][] neighbors;
        if (x % 2 == 0) {
            neighbors = new int[][]{
                    {1, 0}, {1, -1}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}
            };
        }else{
            neighbors = new int[][]{
                    {1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}
            };
        }

        for(int[] neighbor : neighbors){
            int x_offset = neighbor[1];
            int y_offset = neighbor[0];
            int col = x + x_offset;
            int row = y + y_offset;
            if(col >= 0 && col < width && row >= 0 && row < height) {
                if (Objects.equals(getCellState(col, row), ALIVE)) {
                    aliveNeighbors++;
                }
            }
        }
        return aliveNeighbors;
    }
}