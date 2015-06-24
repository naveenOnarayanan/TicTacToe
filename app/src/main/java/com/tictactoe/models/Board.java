package com.tictactoe.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for the Tic Tac Toe Board
 */
public class Board {
    public final int LENGTH = 3;
    public final int MAX_MOVES = LENGTH*LENGTH;

    private Player[] board;

    public int numMoves = 0;

    public Board() {
        board = new Player[LENGTH * LENGTH];
    }

    /**
     * Copy Constructor
     * @param board     Board which needs to be copied
     */
    public Board(Board board) {
        this.board = board.board.clone();
        this.numMoves = board.numMoves;
    }

    /**
     * Resets the board
     **/
    public void initializeNewBoard() {
        for (int i = 0; i < MAX_MOVES; i++) {
            board[i] = Player.None;
        }
        this.numMoves = 0;
    }

    /**
     * Checks if the board is full
     * @return  True if there are no more possible moves. False otherwise.
     */
    public boolean isFull() {
        return numMoves == MAX_MOVES;
    }

    /**
     * Checks if the move is on a cell that is empty
     * @param move  Contains (x,y) coordinates of the grid
     * @return      True if the cell is empty. False otherwise.
     */
    public boolean isValidMove(Move move) {
        return getPlayerFromBoard(move) == Player.None;
    }

    /**
     * Checks if the move is a winning move
     *
     * Checks both diagonals, horizontal and vertical cells to see if the user
     * has won
     *
     * @param move      Contains (x,y) coordinates of the grid
     * @param player    Player making the move
     * @return          True if the move leads to victory for that player. False otherwise.
     */
    public boolean isWinningMove(Move move, Player player) {
        int adiagonal = 0, diagonal = 0, horizontal = 0, vertical = 0;

        for (int i = 0; i < LENGTH; i++) {
            if (getPlayerFromBoard(i, i) == player) {
                diagonal++;
            }
            if (getPlayerFromBoard(move.x, i) == player) {
                horizontal++;
            }
            if (getPlayerFromBoard(i, move.y) == player) {
                vertical++;
            }
            if (getPlayerFromBoard(i, LENGTH - i - 1) == player) {
                adiagonal++;
            }
        }

        return diagonal == LENGTH || horizontal == LENGTH
                    || vertical == LENGTH || adiagonal == LENGTH;
    }

    /**
     * Returns a list of moves that are possible for a player
     * @return  List of valid moves that the player can select from.
     * @see     Move
     */
    public List<Move> getAvailableSpaces() {
        List<Move> availableMoves = new ArrayList<>();
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                Move move = new Move(i, j);
                if (isValidMove(move)) {
                    availableMoves.add(move);
                }
            }
        }
        return availableMoves;
    }

    /**
     * Returns the player in a specific grid location on the board
     * @param move  Contains (x,y) coordinates of the grid
     * @return      The player in the specific cell
     * @see         Player
     */
    public Player getPlayerFromBoard(Move move) {
        return getPlayerFromBoard(move.x, move.y);
    }

    /**
     * @see Board#getPlayerFromBoard(Move)
     */
    public Player getPlayerFromBoard(int x, int y) {
        return board[x * LENGTH + y];
    }

    /**
     * Updates the grid with the move made by the player
     * @param move      Contains (x,y) coordinates of the grid
     * @param player    The player making the move
     * @see             Player
     */
    public void setMove(Move move, Player player) {
        int hash = move.x*LENGTH + move.y;
        setMove(hash, player);
    }

    /**
     * Updates the grid with the move made by the player
     * @param hash      Contains the hash by combining the x and y coordinate
     * @param player    The player making the move
     */
    public void setMove(int hash, Player player) {
        board[hash] = player;
        numMoves++;
    }

    /**
     * Undo the provided move on the Board
     * @param move  Contains (x,y) coordinates of the grid
     */
    public void resetMove(Move move) {
        board[move.x * LENGTH + move.y] = Player.None;
        numMoves--;
    }
}
