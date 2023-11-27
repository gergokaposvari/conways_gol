package main;

import java.util.Objects;

public class TriangularGameOfLife extends GameOfLife{

    public TriangularGameOfLife(Integer x, Integer y, Object rule) {
        super(x, y, rule);
    }

    public TriangularGameOfLife(Integer[][] board, Object object) {
        super(board, object);
    }

    @Override
    public Integer countAliveNeighbours(Integer x, Integer y){
        Integer aliveNeighbours = 0;
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if((i == -1 && j == 0) || (i == 1 && j == 0) || (i == 0 && j ==1)) {
                    int col = x + i;
                    int row = y + j;
                    if ((col >= 0 && col < width) && (row >= 0 && row < height)) {
                        if (Objects.equals(getCellState((col), (row)), ALIVE)) {
                            aliveNeighbours++;
                        }
                    }
                }
            }
        }
        return aliveNeighbours;
    }
}
