package org.benjaminschmitz.school.diningphilosophers;

public class Chopstick {
    private boolean taken = false;

    public void take() {
        taken = true;
    }

    public void release() {
        taken = false;
    }

    public boolean isTaken() {
        return taken;
    }
}
