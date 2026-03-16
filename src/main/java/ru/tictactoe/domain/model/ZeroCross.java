package ru.tictactoe.domain.model;

public enum ZeroCross {
    ZERO(1),
    CROSS(2);

    private int value;

    ZeroCross(int value) {
        this.value = value;
    }

    public int getValue() {return value;}
}
