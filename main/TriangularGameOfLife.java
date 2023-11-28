package main;

import java.util.Objects;

public class TriangularGameOfLife extends GameOfLife{

    public TriangularGameOfLife(Integer x, Integer y, Object rule) {
        super(x, y, rule);
    }

    public TriangularGameOfLife(Integer[][] board, Object object) {
        super(board, object);
    }

    //Itt csak felüldefiniálom
    @Override
    public Integer countAliveNeighbors(Integer x, Integer y){
        Integer aliveNeighbors = 0;
        boolean flippedOrientation = (x % 2 == 0) ? y % 2 ==0 : y % 2 == 1;
        int[][] relativeCoordinates;
                if(!flippedOrientation){
                    relativeCoordinates = new int[][]{
                            {1, 0}, {0, -1}, {0, 1}
                    };
                }else{
                    relativeCoordinates = new int[][]{
                            {-1, 0}, {0, -1}, {0, 1}
                    };
                }
                for(int[] neighbbor : relativeCoordinates) {
                    int x_offset = neighbbor[0];
                    int y_offset = neighbbor[1];
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