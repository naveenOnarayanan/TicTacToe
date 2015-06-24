package com.tictactoe.models;

/**
 * Enum responsible for representing player symbols (X or O)
 */
public enum Player {
    X(1), O(2), None(0);

    private int id;
    Player(int id) {
        this.id = id;
    }

    public Player next() {
        return this == X ? O : X;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        switch(this) {
            case X:
                return "X";
            case O:
                return "O";
            default:
                return "";
        }
    }
}
