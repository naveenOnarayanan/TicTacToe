package com.tictactoe.AI;

import com.tictactoe.models.Move;

/**
 * AIMove that also keeps track of the score for the purpose of finding
 * the optimal move
 */
public class AIMove extends Move {
    public int score;

    public AIMove(Move m, int score) {
        super(m.x, m.y);
        this.score = score;
    }
}
