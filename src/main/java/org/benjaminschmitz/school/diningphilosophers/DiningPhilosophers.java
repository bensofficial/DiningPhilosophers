package org.benjaminschmitz.school.diningphilosophers;

import java.util.Arrays;

public class DiningPhilosophers {

    public static final int SEATING_CAPACITY = 5;
    private static final long MILLISECONDS_BETWEEN_STATUS_LOGS = 1_000;

    private final Philosopher[] philosophers;
    private final Chopstick[] chopsticks;

    public DiningPhilosophers() {
        philosophers = new Philosopher[SEATING_CAPACITY];
        chopsticks = new Chopstick[SEATING_CAPACITY];

        for (int i = 0; i < SEATING_CAPACITY; i++) {
            chopsticks[i] = new Chopstick();
        }

        for (int i = 0; i < SEATING_CAPACITY; i++) {
            philosophers[i] = new Philosopher("Philosopher " + i, getLeftChopstick(i), getRightChopstick(i));
        }
    }

    private Chopstick getLeftChopstick(int indexOfPhilosopher) {
        return chopsticks[indexOfPhilosopher];
    }

    private Chopstick getRightChopstick(int indexOfPhilosopher) {
        return chopsticks[(indexOfPhilosopher + 1) % SEATING_CAPACITY];
    }

    public void start() {
        for (Philosopher philosopher: philosophers) {
            philosopher.start();
        }

        Thread status = new Thread(() -> {
            while (true) {
                Status[] statuses = new Status[SEATING_CAPACITY];
                for (int i = 0; i < SEATING_CAPACITY; i++) {
                    statuses[i] = philosophers[i].getStatus();
                }
                System.out.println(Arrays.toString(statuses));
                try {
                    Thread.sleep(MILLISECONDS_BETWEEN_STATUS_LOGS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
        status.start();
    }

    public static void main(String[] args) {
        DiningPhilosophers diningPhilosophers = new DiningPhilosophers();
        diningPhilosophers.start();
    }
}
