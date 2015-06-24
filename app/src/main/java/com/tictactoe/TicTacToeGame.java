package com.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tictactoe.AI.AIPlayer;
import com.tictactoe.models.Game;
import com.tictactoe.models.Move;
import com.tictactoe.models.Player;

/**
 * Created by Naveen Narayanan
 *
 * Tic-Tac-Toe Game
 */
public class TicTacToeGame extends Activity {

    private final int BUTTON_COUNT = 9;
    public Game game;

    private Button[] ticTacToeButtons = new Button[BUTTON_COUNT];

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_game);

        // Initialize the tic-tac-toe buttons
        initializeTicTacToeButtons();

        // Create a new game
        game = new Game(Player.X);

        // Request the first move
        requestMove();
    }

    /**
     * Initializes the tick tac toe buttons by setting their tag, on click listener and enable flag.
     */
    public void initializeTicTacToeButtons() {
        LinearLayout grid = (LinearLayout)findViewById(R.id.tic_tac_toe_grid);
        int count = grid.getChildCount();
        for (int i = 0; i < count; i++) {
            LinearLayout parentLayout = (LinearLayout)grid.getChildAt(i);
            for (int j = 0; j < count; j++) {
                int hashIndex = i*count + j;
                ticTacToeButtons[hashIndex] = (Button)parentLayout.getChildAt(j);
                ticTacToeButtons[hashIndex].setOnClickListener(ticTacToeOnClickListener);
                ticTacToeButtons[hashIndex].setTag(hashIndex);
                ticTacToeButtons[hashIndex].setEnabled(true);
            }
        }
    }

    /**
     * Initializes the End game dialog allowing the user to either restart or exit the app
     *
     * @param alertString   The text to be displayed on the alert dialog
     */
    private void initializeResultAlert(String alertString) {
        if (builder == null) {
            builder = new AlertDialog.Builder(TicTacToeGame.this);
            builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    game.reset();
                    resetTicTacToeButtons();
                }
            });

            builder.setNegativeButton(R.string.Exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TicTacToeGame.this.finish();
                    System.exit(0);
                }
            });
        }

        builder.setMessage(alertString);
    }

    /**
     * Enable or disable all the tic tac toe buttons
     * @param enabled   True in order to enable all the buttons. False otherwise
     */
    public void enableTicTacToeButtons(boolean enabled) {
        for (int i = 0; i < BUTTON_COUNT; i++) {
            ticTacToeButtons[i].setEnabled(enabled);
        }
    }

    /**
     * Reset the tic tac toe buttons by reattaching onclick event and removing
     * X and O from button text. Used when resetting the game
     */
    public void resetTicTacToeButtons() {
        for (int i = 0; i < BUTTON_COUNT; i++) {
            ticTacToeButtons[i].setText("");
            ticTacToeButtons[i].setOnClickListener(ticTacToeOnClickListener);
            ticTacToeButtons[i].setEnabled(true);
        }
    }

    /**
     * Request move from either the user or the AI.
     * If it is a user request then the buttons are enabled.
     * Else call AI for an optimal move.
     */
    public void requestMove() {
        boolean gameOver = game.isGameOver();

        if (!gameOver) {
            if (game.currentPlayer == Player.X) {
                enableTicTacToeButtons(true);
            } else {
                // Disable buttons so user cannot input again
                enableTicTacToeButtons(false);

                // Get AI Optimal move
                Move move = new AIPlayer(game.board, game.currentPlayer).getNextMove();

                Button ticTacToeButton = ticTacToeButtons[move.x * game.board.LENGTH + move.y];
                ticTacToeButton.setOnClickListener(null);
                ticTacToeButton.setText(game.currentPlayer.toString());
                ticTacToeButton.setTextColor(Color.RED);

                // Make the optimal AI move
                game.setMove(move, game.currentPlayer);

                // Check if game is over
                gameOver = game.isGameOver();

                // Re-enable buttons for user input
                enableTicTacToeButtons(true);
            }
        }

        // If game is over then show end of game dialog
        if (gameOver) {
            showGameOverDialog();
        }
    }

    /**
     * Display the end of game dialog
     */
    private void showGameOverDialog() {
        Resources res = getResources();
        StringBuilder alertString = new StringBuilder();

        if (game.winner == Player.None) {
            alertString.append(res.getString(R.string.result_dialog_none));
        } else {
            alertString.append(res.getString(R.string.result_dialog_winner, game.winner.getId()));
        }

        alertString.append("\n\n");
        alertString.append(res.getString(R.string.result_dialog_question));

        enableTicTacToeButtons(false);
        initializeResultAlert(alertString.toString());

        builder.show();
    }

    /**
     * On click listener for the tic tac toe grid buttons
     */
    protected View.OnClickListener ticTacToeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button bt = (Button)v;
            int tag = (int)bt.getTag();
            bt.setText(game.currentPlayer.toString());

            Move humanMove;

            switch(tag) {
                case 0:
                    humanMove = new Move(0, 0);
                    break;
                case 1:
                    humanMove = new Move(0, 1);
                    break;
                case 2:
                    humanMove = new Move(0, 2);
                    break;
                case 3:
                    humanMove = new Move(1, 0);
                    break;
                case 4:
                    humanMove = new Move(1, 1);
                    break;
                case 5:
                    humanMove = new Move(1, 2);
                    break;
                case 6:
                    humanMove = new Move(2, 0);
                    break;
                case 7:
                    humanMove = new Move(2, 1);
                    break;
                default:
                    humanMove = new Move(2, 2);
                    break;
            }

            game.setMove(humanMove, game.currentPlayer);

            bt.setEnabled(false);
            bt.setOnClickListener(null);
            bt.setTextColor(Color.CYAN);
            requestMove();
        }
    };
}
