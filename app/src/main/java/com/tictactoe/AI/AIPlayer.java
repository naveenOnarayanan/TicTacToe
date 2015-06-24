package com.tictactoe.AI;

import com.tictactoe.models.Board;
import com.tictactoe.models.Move;
import com.tictactoe.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for making moves for the AI
 */
public class AIPlayer {
    final static int MIN_VALUE = Integer.MIN_VALUE;
    final static int MAX_VALUE = Integer.MAX_VALUE;

    private Board board;
    private Player player;

    public AIPlayer(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    /**
     * Determines the next move the AI should take.
     * @return  move    Contains (x,y) coordinates of the grid
     */
    public Move getNextMove() {
        return getOptimalMove(board, MIN_VALUE, MAX_VALUE, player);
    }


    /**
     * Algorithm that determines the next optimal move in order to ensure the best outcome for the
     * AI.
     *
     * Uses Alpha beta pruning along with min-max game theory. The AI determines all possible moves
     * and calculates their score. The best score will traverse up the tree and will determine the move
     * it makes.
     * @param board     The board configuration after making a move (Used to test out different moves)
     * @param alpha     Alpha value calculated after traversing the other move combinations
     * @param beta      Beta value calculated after traversing the other move combinations
     * @param player    The player that is considered when determining the optimal score
     * @return move     Contains (x,y) coordinates of the optimal move for the AI
     */
    public AIMove getOptimalMove(Board board, int alpha, int beta, Player player) {
        List<AIMove> moves = new ArrayList<>();

        // Copy the board to test out a new combination
        Board newBoard = new Board(board);

        for (Move availableMove : newBoard.getAvailableSpaces()) {
            AIMove move = new AIMove(availableMove, player == Player.X ? MAX_VALUE : MIN_VALUE);

            newBoard.setMove(move, player);

            if (newBoard.isWinningMove(move, player)) {
                move.score = (player == Player.X) ? MIN_VALUE : MAX_VALUE;
                return move;
            } else if (newBoard.isFull()) {
                move.score = 0;
                return move;
            }

            // Consider the new board configuration and test out all its possible move combinations
            AIMove moveResult = getOptimalMove(newBoard, alpha, beta, player.next());

            // Reset the move made on this board in order to test another move combination
            newBoard.resetMove(move);

            // If the player is human then propagate the lowest score. Update beta accordingly
            // Else the player is AI thus return the maximum score. Update alpha accordingly
            if (player == Player.X) {
                move.score = Math.min(move.score, moveResult.score);
                beta = Math.min(beta, moveResult.score);
            } else {
                move.score = Math.max(move.score, moveResult.score);
                alpha = Math.max(alpha, moveResult.score);
            }

            // If at any point alpha is greater or equal to beta then that means that there is
            // no need to consider other move combinations for this board configuration (Pruning)
            if (alpha >= beta) {
                move.score = player == Player.X ? beta : alpha;
                return move;
            }

            // Store the moves of each child
            moves.add(move);
        }

        // Return the max or min score out of the possible moves
        if (player == Player.X) {
            return getMinOrMaxMove(moves, false);
        } else {
            return getMinOrMaxMove(moves, true);
        }
    }


    /**
     * Gets the max or min move from a list of moves.
     * @param moves     List of moves from which to select the max or min score move
     * @param isMax     True if the max move is to be returned. False if min move is to be returned.
     * @return          Returns either the max or min move from a list of moves.
     */
    public AIMove getMinOrMaxMove(List<AIMove> moves, boolean isMax) {
        AIMove minOrMaxMove = moves.get(0);

        for (int i = 1; i < moves.size(); i++) {
            if (isMax && moves.get(i).score > minOrMaxMove.score) {
                minOrMaxMove = moves.get(i);
            } else if (!isMax && moves.get(i).score < minOrMaxMove.score) {
                minOrMaxMove = moves.get(i);
            }
        }

        return minOrMaxMove;
    }
}
