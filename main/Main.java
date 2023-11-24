package main;

public class Main {

    private static Integer SLEEPTIME = 1000;
    private static boolean paused = false;
    public static void main(String[] args) throws InterruptedException {
       /* Menu mainMenu = new Menu();
        mainMenu.startMenu();*/
        Object conway = "B3/S23";
        GameOfLife plsbegood = new GameOfLife(65, 65, conway);
        GameOfLife.changeCellState(1,1);
        GameOfLife.changeCellState(2,1);
        GameOfLife.changeCellState(1,2);
        GameOfLife.changeCellState(2,2);
        GameOfLife.changeCellState(3,3);
        GameOfLife.changeCellState(4,3);
        GameOfLife.changeCellState(3,4);
        GameOfLife.changeCellState(4,4);

        GridDrawer gridDrawer = new GridDrawer(plsbegood.getCurrentState());
        while(true) {
            Thread.sleep(SLEEPTIME);
            if(!paused) {
                plsbegood.simulateGeneration();
                gridDrawer.setGrid(plsbegood.getCurrentState());
            }
        }

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
        if(paused){
            paused = false;
        }else{
            paused = true;
        }
    }
}
