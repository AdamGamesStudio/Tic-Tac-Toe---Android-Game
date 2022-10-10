package com.adamgamesstudio.tictactoegame;

//import com.adamgamesstudio.tictactoegame.game.DialogManager;
//import com.adamgamesstudio.tictactoegame.game.GameLoop;
//import com.adamgamesstudio.tictactoegame.game.TimerManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adamgamesstudio.tictactoegame.DialogCreator;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // LAYOUT OBJECT VARS
    public TextView playerOneText, playerOneScore, playerTwoText, playerTwoScore, playerStatus, timerText;
    public Button[] buttons = new Button[9];
    public Button resetGameButton, settingsButton;

    // SHARED PREFS VARS
    SharedPreferences settings;

    public String playerOneColor, playerTwoColor;

    //  TIMER
    // VARS
    public Timer timer; // timer's var; needs timerTask to work correct
    public TimerTask timerTask; // timer's task
    public Double time = 0.0; // timer's time; calculations in getTimerText function
    public int seconds, minutes; // only for popup info in main class

    public boolean isTimerStarted = false;
    public boolean timerMode;

    //  GAME LOOP
    // VARS
    int playerOneScoreCount = 0, playerTwoScoreCount = 0; // setting all vars to zero at start
    public int roundCount = 0;                            // -||-
    public boolean activePlayer = true; // 1st player round => true, 2nd => false
    boolean isGameStarted = false; // false => nothing is happening, timer is off, true => first figure was placed, timer is running
    boolean canPlaceFigure = true; // true => players can place figures, false => not

    int [] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2}; // assigned to every button, defines there state. p1 => 0; p2 => 1; empty => 2

    int [] [] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
            {0, 4, 8}, {2, 4, 6}
    }; // when one of the player's figures placed matches one of this winning positions, wins

    // PREFS VARS
    public boolean vibrations;

    public boolean switchPlayersAfterRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); // hiding action bar

        Log.i("App Logs - Activities/MainActivity", "Main Activity begins to load.");

        playerOneText = (TextView) findViewById(R.id.playerOne);
        playerTwoText = (TextView) findViewById(R.id.playerTwo);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGameButton = (Button) findViewById(R.id.resetGameButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);

        timerText = (TextView) findViewById(R.id.timerText);

        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        Log.i("App Logs - Activities/MainActivity/onCreate", "All vars has been assigned.");

        timerSetup();

        Log.i("App Logs - Activities/MainActivity/onCreate", "Timer .");

        loadPreferences();

        Log.i("App Logs - Activities/MainActivity/onCreate", "All vars has been assigned.");

        resetGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame(true);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsPage = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsPage);
            }
        });

        Log.i("App Logs - Activities/MainActivity", "Activity loading finished with 0 errors.");
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length()));

        figurePlaced(v, gameStatePointer);
    }

    public void loadPreferences() {
        settings = getSharedPreferences("settings", MODE_PRIVATE);

        playerOneText.setText(settings.getString("playerOneName", "Player One"));
        playerTwoText.setText(settings.getString("playerTwoName", "Player Two"));

        switchPlayersAfterRound = settings.getBoolean("switchPlayersAfterRound", true);

        timerMode = settings.getBoolean("timerMode", true);

        vibrations = settings.getBoolean("vibrations", true);

        if (settings.getInt("appTheme", 0) == 0) {
            playerOneColor = "#e645b8";
            playerTwoColor = "#45d8e6";
        } else if (settings.getInt("appTheme", 0) == 1) {
            playerOneColor = "#106910";
            playerTwoColor = "#38E038";
        }  else if (settings.getInt("appTheme", 0) == 2) {
            playerOneColor = "#94370F";
            playerTwoColor = "#E04B0B";
        }  else if (settings.getInt("appTheme", 0) == 3) {
            playerOneColor = "#E5E9A8";
            playerTwoColor = "#79BE9A";
        }

        playerOneText.setTextColor(Color.parseColor(playerOneColor));
        playerTwoText.setTextColor(Color.parseColor(playerTwoColor));
    }


    // GAME LOOP CLASS

    public void figurePlaced(View v, int gameStatePointer) {
        if (canPlaceFigure) {
            if (activePlayer) {
                ((Button) v).setText("X");
                ((Button) v).setTextColor(Color.parseColor(playerOneColor));
                gameState[gameStatePointer] = 0;
            } else {
                ((Button) v).setText("O");
                ((Button) v).setTextColor(Color.parseColor(playerTwoColor));
                gameState[gameStatePointer] = 1;
            } // checking whats player's turn it is, placing their figures, setting their colors and setting game state to their number

            if (!isTimerStarted) {
                timerTask.cancel();
                timerSetup();
                isTimerStarted = true;
                updateTimerText();
            }

            roundCount++; // adding 1 to round count

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // attaching vibrator service to var

            if (checkWinner() || roundCount == 9) {
                if (vibrations) vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)); // turning vibrator on for 1s

                for (int i = 0; i < buttons.length; i++) {
                    if (gameState[i] == 2) {
                        String _buttonID = "btn_" + i;
                        int resourceID = getResources().getIdentifier(_buttonID, "id", getPackageName());
                        Button button = (Button) findViewById(resourceID);
                        //button.setVisibility(View.INVISIBLE);
                    }
                } // making buttons that aren't assigned to the players invisible

                if (timerMode) {
                    isTimerStarted = false;
                }

                canPlaceFigure = false;

                new CountDownTimer(1000, 1000) {
                    public void onFinish() {
                        if (checkWinner()) {
                            if (activePlayer) {
                                playerOneScoreCount++;
                                openWinnerInfoDialog(1);
                            } else {
                                playerTwoScoreCount++;
                                openWinnerInfoDialog(2);
                            }
                        } else if (roundCount == 9) {
                            openWinnerInfoDialog(0);
                        } // after 1s of waiting, checking if its draw, or someone won, and doing actions above

                        resetGame(false); // resetting all buttons and timer after clicking "OK" button on popup

                        updatePlayerScore(); // updating players score
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            } else {
                activePlayer = !activePlayer; // switching players
                if (vibrations) vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)); // turning on vibrator for 50ms
            } // checking if
        }
    }

    public void resetGame(boolean resetButtonPressed) {
        if (resetButtonPressed) {
            if (isGameStarted) {
                playerOneScoreCount = 0; // resetting all values
                playerTwoScoreCount = 0; // -||-

                updatePlayerScore(); // updating players score
            }
        } else {
            if (switchPlayersAfterRound) {
                String sTemp; // declaring temporary vars to switch other vars
                int iTemp; // -||-

                sTemp = playerOneText.getText().toString();
                playerOneText.setText(playerTwoText.getText().toString());
                playerTwoText.setText(sTemp); // switching players names

                iTemp = playerOneScoreCount;
                playerOneScoreCount = playerTwoScoreCount;
                playerTwoScoreCount = iTemp; // switching players scores

                sTemp = playerOneColor;
                playerOneColor = playerTwoColor;
                playerTwoColor = sTemp; // switching players colors

                playerOneText.setTextColor(Color.parseColor(playerOneColor));
                playerTwoText.setTextColor(Color.parseColor(playerTwoColor));

            } // checking is the switching players after round, and if yes switching players
        } // checking if the reset button was clicked, and if yes, resetting everything, but if not, (someone won or its draw) only buttons and timer

        canPlaceFigure = true;

        if (timerMode) {
            resetTimer(); // stopping and resetting timer
        }

        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
            //buttons[i].setVisibility(View.VISIBLE);
        } // resetting all buttons visibility and text
    }

    public boolean checkWinner() {
        boolean winnerResult = false;

        for(int [] winningPositions : winningPositions) {
            if (gameState[winningPositions[0]] == gameState[winningPositions[1]] &&
                    gameState[winningPositions[1]] == gameState[winningPositions[2]] &&
                    gameState[winningPositions[0]] != 2) {
                winnerResult = true;
            }
        } // checking if figures positions matches witch winning positions and returning value

        Log.i("App Logs - Class/GameLoop/checkWinner", "Successively returned \"winnerResult\" (" + winnerResult + ").");
        Log.i("App Logs - Class/GameLoop/checkWinner", "Work finished with 0 errors.");
        return winnerResult;
    }

    void openWinnerInfoDialog(int winner) {
        DialogCreator dialogCreator = new DialogCreator(); // attaching dialog manager class to var

        switch (winner) {
            case 0:
                dialogCreator.setValues("No Winner This Time!",
                        "Draw in " + minutes + " minutes and " + seconds + "seconds.",
                        "OK!");
            case 1:
                dialogCreator.setValues(playerOneText.getText().toString() + " Won!",
                        "Congratulations! You won in " + minutes + " minutes and " + seconds + " seconds!",
                        "OK!");
            case 2:
                dialogCreator.setValues(playerTwoText.getText().toString() + " Won!",
                        "Congratulations! You won in " + minutes + " minutes and " + seconds + " seconds!",
                        "OK!");
        }

        dialogCreator.show(getSupportFragmentManager(), "Winner Info"); // opening popup

        Log.i("App Logs - Class/GameLoop/openWinnerInfoDialog", "Work finished with 0 errors.");
    }

    void updatePlayerScore() {
        playerOneScore.setText(Integer.toString(playerOneScoreCount)); // updating player one score text
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount)); // updating player two score text

        if (playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText(playerOneText.getText().toString() + " is Winning!");
        } else if (playerOneScoreCount < playerTwoScoreCount) {
            playerStatus.setText(playerTwoText.getText().toString() + " is Winning!");
        } else {
            playerStatus.setText("");
        } // updating winning player's status

        Log.i("App Logs - Class/GameLoop/updatePlayerScore", "Work finished with 0 errors.");
    }


    // TIMER CLASS ---------------------------------------------------------------------------------

    public void timerSetup() {
        timer = new Timer(); // setting new timer object

        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateTimerText();
            } // setting delay and period of timer
        }; // setting delay and period of timer
        timer.scheduleAtFixedRate(timerTask, 0, 1024); // setting delay and period of timer

        timerText.setText("Place first figure to start the game."); // resetting timer text

        Log.i("App Logs - Class/TimerManager/setup", "Work finished with 0 errors.");
    }

    public void resetTimer() {
        isTimerStarted = false;
        time = 0.0;
        timerText.setText("Place first figure to start the game."); // resetting timer text
        roundCount = 0;
        Log.i("App Logs - Class/TimerManager/resetTimer", "Work finished with 0 errors.");
    }

    public void updateTimerText() {
        if (isTimerStarted) {
            time++;
            timerText.setText(getTimerText());
            Log.i("App Logs - Class/TimerManager/updateTimerText", "Successively changed timer text to " + getTimerText() + "; added 1 to \"timer\".");
        } else return; // checking if timer is started, and if it is, changing timer text to calculated time; if not, don't change
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);

        seconds = ((rounded % 86400) % 3600) % 60;
        minutes = ((rounded % 86400) % 3600) / 60; // calculating time from double value

        Log.i("App Logs - Class/TimerManager/getTimerText", "Successively returned \"timerText\" (\"" + formatTime(seconds, minutes) + "\").");
        return formatTime(seconds, minutes);
    }

    private String formatTime(int seconds, int minutes) {
        Log.i("App Logs - Class/TimerManager/formatTime", "Successively returned \"formatTime\" (\"" + String.format("%02d", minutes) + " : " + String.format("%02d", seconds) + "\").");
        Log.i("App Logs - Class/TimerManager/formatTime", "Work finished with 1 errors.");
        Log.i("App Logs - Class/TimerManager/formatTime", "Delay detected: 1s.");
        return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}

/*

# Tic Tac Toe - Android Game
## PLAY
### How to install it on your android device, and play

<br>

To play the game, you have to install in on your phone. This app works only on android devices, and there is two ways to try it.

### **1st method** - Google play
The game is published on Google Play, so you can safely download it.

<br>
<br>

## SEE HOW IT WORKS
### That is how to open the project and see the code
<br>
asdasda

 */