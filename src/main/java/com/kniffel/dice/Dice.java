package com.kniffel.dice;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    private int value;
    private boolean locked;
    private int index;

    public Dice() {

    }

    public Dice(int index) {
        this.index = index;
        this.locked = false;
        this.value = 1;
    }

    public void roll() {
        this.value = ThreadLocalRandom.current().nextInt(1,7);
    }

    public void changeLock() {
        this.locked = !this.locked;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
