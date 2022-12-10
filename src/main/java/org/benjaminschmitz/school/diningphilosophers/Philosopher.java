package org.benjaminschmitz.school.diningphilosophers;

public class Philosopher extends Thread {
    private static final long MAX_SLEEPING_DURATION = 10_000;
    private static final Object obj = new Object();

    private final String name;
    private final Chopstick leftChopstick;
    private final Chopstick rightChopstick;

    private Status status;

    public Philosopher(String name, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.name = name;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.status = Status.INIT;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            think();
            eat();
        }
        status = Status.ABORTED;
    }

    private void think() {
        status = Status.THINKING;
        System.out.println(name + " is thinking...");
        sleep();
    }

    private void eat() {
        System.out.println(name + " wants to eat.");

        synchronized (obj) {
            while (true) {
                try {
                    if (leftChopstick.isTaken() || rightChopstick.isTaken()) {
                        status = Status.WAITING_TO_PICK_UP;
                        System.out.println(name + " is waiting to pick up chopsticks.");
                        obj.wait();
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    System.err.println("Waiting for chopstick to be taken was interrupted.");
                    e.printStackTrace(System.err);
                }
            }

            status = Status.PICKING_UP;
            System.out.println(name + " tries to pick up left chopstick.");
            leftChopstick.take();
            System.out.println(name + " picked up left chopstick.");
            System.out.println(name + " tries to pick up right chopstick.");
            rightChopstick.take();
            System.out.println(name + " picked up right chopstick.");

            obj.notify();
        }

        status = Status.EATING;
        System.out.println(name + " is eating.");
        sleep();

        synchronized (obj) {
            while (true) {
                try {
                    if (!leftChopstick.isTaken() || !rightChopstick.isTaken()) {
                        status = Status.WAITING_TO_RELEASE;
                        System.out.println(name + " is waiting to release chopsticks.");
                        obj.wait();
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    System.err.println("Waiting for chopstick to be taken was interrupted.");
                    e.printStackTrace(System.err);
                }
            }

            status = Status.RELEASING;
            System.out.println(name + " tries to release left chopstick.");
            leftChopstick.release();
            System.out.println(name + " released left chopstick.");
            System.out.println(name + " tries to release right chopstick.");
            rightChopstick.release();
            System.out.println(name + " released right chopstick.");

            obj.notify();
        }
    }

    private void sleep() {
        try {
            sleep(randomSleepDurationInMilliSeconds());
        } catch (InterruptedException e) {
            System.err.println(name + " was interrupted while sleeping.");
            e.printStackTrace(System.err);
        }
    }

    private long randomSleepDurationInMilliSeconds() {
        return (long) (Math.random() * MAX_SLEEPING_DURATION);
    }

    public Status getStatus()  {
        return status;
    }
}

enum Status {
    INIT, THINKING, WAITING_TO_PICK_UP, PICKING_UP, EATING, WAITING_TO_RELEASE, RELEASING, ABORTED
}
