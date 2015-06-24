package com.tictactoe.models;

/**
 * Class responsible for keeping track of game state and managing the board
 */
public class Game {
    public Board board = new Board();
    public Player currentPlayer;

    // Variables to keep track of game result
    public boolean gameOver = false;
    public Player winner = Player.None;

    /**
     * Initialize the board and set current player to human player
     */
    public Game(Player humanPlayer) {
        this.board.initializeNewBoard();
        this.currentPlayer = humanPlayer;
    }

    /**
     * Updates the board based on the User's move.
     *
     * If the move is a winning move it updates the game state
     * with the winner
     *
     * @param move      Contains (x,y) coordinates of the grid
     * @param player    The player making the move
     */
    public void setMove(Move move, Player player) {
        board.setMove(move, player);

        if (board.isWinningMove(move, player)) {
            gameOver = true;
            winner = player;
        } else if (board.isFull()) {
            gameOver = true;
        } else {
            currentPlayer = currentPlayer.next();
        }
    }

    /**
     * Resets the board and game state for a new game
     */
    public void reset() {
        board.initializeNewBoard();
        gameOver = false;
        winner = Player.None;
        this.currentPlayer = Player.X;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
