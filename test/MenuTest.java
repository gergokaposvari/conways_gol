package test;

import main.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    public void increaseSleepTime(){
        Menu.increaseSleepTime();

        Assertions.assertAll(
                () -> assertEquals(1920, Menu.SLEEPTIME)
        );
    }

    @Test
    public void decreaseSleepTime(){
        Menu.decreaseSleepTime();

        Assertions.assertAll(
                () -> assertEquals(480, Menu.SLEEPTIME)
        );
    }
}