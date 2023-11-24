package main;

public class Main {
    public static void main(String[] args) throws InterruptedException {
       /* Menu mainMenu = new Menu();
        mainMenu.startMenu();*/
        Object conway = "B3/S23";
        GameOfLife plsbegood = new GameOfLife(10, 10, conway);
        plsbegood.changeCellState(1,1);
        plsbegood.changeCellState(2,1);
        plsbegood.changeCellState(1,2);
        plsbegood.changeCellState(2,2);

        plsbegood.changeCellState(3,3);
        plsbegood.changeCellState(4,3);
        plsbegood.changeCellState(3,4);
        plsbegood.changeCellState(4,4);

        GridDrawer gridDrawer = new GridDrawer(plsbegood.getCurrentState());
        while(true) {
            Thread.sleep(100);
            plsbegood.simulateGeneration();
            gridDrawer.setGrid(plsbegood.getCurrentState());
        }

    }
}
