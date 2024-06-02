package com.example.demo.gui;

public class BooleanWrapper {
    private boolean value;

    public BooleanWrapper(boolean value) {
        this.value = value;
    }

    public synchronized boolean isValue() {
        return value;
    }

    public synchronized void setValue(boolean value) {
        this.value = value;
    }
}
