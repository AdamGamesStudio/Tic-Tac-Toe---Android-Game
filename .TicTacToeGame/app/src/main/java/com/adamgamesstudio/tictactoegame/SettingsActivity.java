package com.adamgamesstudio.tictactoegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences settings;

    private EditText playerOneNameInput, playerTwoNameInput;
    private Spinner appThemeSpinner;
    private Switch timerModeSwitch, vibratesSwitch, switchPlayersAfterRoundSwitch;

    private Button saveButton;

    int appTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.i("AppStatus", "Settings Page is Active!");

        settings = getSharedPreferences("settings", MODE_PRIVATE);

        playerOneNameInput = (EditText) findViewById(R.id.playerOneNameInput);
        playerTwoNameInput = (EditText) findViewById(R.id.playerTwoNameInput);
        appThemeSpinner = (Spinner) findViewById(R.id.appThemeSpinner);
        vibratesSwitch = (Switch) findViewById(R.id.vibrateSwitch);
        timerModeSwitch = (Switch) findViewById(R.id.timerModeSwitch);
        switchPlayersAfterRoundSwitch = (Switch) findViewById(R.id.switchPlayersAfterRoundSwitch);

        ArrayAdapter<CharSequence> appThemeSpinnerArrAdapter = ArrayAdapter.createFromResource(this, R.array.settingsAppThemeDropdown, android.R.layout.simple_spinner_item);
        appThemeSpinnerArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appThemeSpinner.setAdapter(appThemeSpinnerArrAdapter);

        saveButton = (Button) findViewById(R.id.saveButton);

        loadPreferences();

        appThemeSpinner.setOnItemSelectedListener(this);

        switchPlayersAfterRoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchPlayersAfterRoundSwitch.isChecked()) {
                    switchPlayersAfterRoundSwitch.setText("Switch players after round: On");
                } else {
                    switchPlayersAfterRoundSwitch.setText("Switch players after round: Off");
                }
            }
        });

        vibratesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (vibratesSwitch.isChecked()) {
                    vibratesSwitch.setText("On");
                } else {
                    vibratesSwitch.setText("Off");
                }
            }
        });

        timerModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (timerModeSwitch.isChecked()) {
                    timerModeSwitch.setText("Game");
                } else {
                    timerModeSwitch.setText("Round");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

                Toast.makeText(SettingsActivity.this, "Settings saved.", Toast.LENGTH_SHORT).show();

                Intent mainActivity = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenuDefaultSettings:
                resetToDefaultSettings();
                return true;
            case R.id.settingsMenuAppInfo:
                Intent appInfoActivity = new Intent(SettingsActivity.this, AppInfoActivity.class);
                startActivity(appInfoActivity);
                return true;
            case R.id.settingsMenuGetNewerVersions:
                Uri getNeverVersionsURI = Uri.parse("https://adamgamesstudio.github.io/tictactoegame/download.htm");
                startActivity(new Intent(Intent.ACTION_VIEW, getNeverVersionsURI));
                return true;
            case R.id.settingsMenuPrivacy:
                Uri privacyPolicyURI = Uri.parse("https://adamgamesstudio.github.io/tictactoegame/privacypolicy.htm");
                startActivity(new Intent(Intent.ACTION_VIEW, privacyPolicyURI));
                return true;
            case R.id.settingsMenuSendFeedback:
                Uri sendFeedbackURI = Uri.parse("https://adamgamesstudio.github.io/tictactoegame/feedbackmail.htm");
                startActivity(new Intent(Intent.ACTION_VIEW, sendFeedbackURI));
                return true;
            case R.id.settingsContactDeveloper:
                Uri developerContactURI = Uri.parse("https://adamgamesstudio.github.io/devs/contact.htm");
                startActivity(new Intent(Intent.ACTION_VIEW, developerContactURI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        appTheme = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void resetToDefaultSettings() {
        playerOneNameInput.setText("Player One");
        playerTwoNameInput.setText("Player Two");

        appThemeSpinner.setSelection(0);
        appTheme = 0;

        timerModeSwitch.setChecked(true);
        vibratesSwitch.setChecked(true);
        switchPlayersAfterRoundSwitch.setChecked(false);

        saveData();

        Toast.makeText(SettingsActivity.this, "All settings reset.", Toast.LENGTH_SHORT).show();

        Intent mainActivity = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void saveData() {
        SharedPreferences.Editor dataManager = settings.edit();

        if (playerOneNameInput.getText().toString() == null) {
            dataManager.putString("playerOneName", "Player One");
        } else {
            dataManager.putString("playerOneName", playerOneNameInput.getText().toString());
        }

        if (playerTwoNameInput.getText().toString() == null) {
            dataManager.putString("playerTwoName", "Player Two");
        } else {
            dataManager.putString("playerTwoName", playerTwoNameInput.getText().toString());
        }

        dataManager.putInt("appTheme", appTheme);

        dataManager.putBoolean("switchPlayersAfterRound", switchPlayersAfterRoundSwitch.isChecked());
        dataManager.putBoolean("timerMode", timerModeSwitch.isChecked());
        dataManager.putBoolean("vibrations", vibratesSwitch.isChecked());

        dataManager.apply();
    }

    public void loadPreferences() {
        settings = getSharedPreferences("settings", MODE_PRIVATE);

        playerOneNameInput.setText(settings.getString("playerOneName", "Player One"));
        playerTwoNameInput.setText(settings.getString("playerTwoName", "Player Two"));

        appThemeSpinner.setSelection(settings.getInt("appTheme", 0));

        switchPlayersAfterRoundSwitch.setChecked(settings.getBoolean("switchPlayersAfterRound", false));
        if (timerModeSwitch.isChecked()) {
            timerModeSwitch.setText("Switch players after round: On");
        } else {
            timerModeSwitch.setText("Switch players after round: Off");
        }

        timerModeSwitch.setChecked(settings.getBoolean("timerMode", true));
        if (timerModeSwitch.isChecked()) {
            timerModeSwitch.setText("Game");
        } else {
            timerModeSwitch.setText("Round");
        }

        vibratesSwitch.setChecked(settings.getBoolean("vibrations", true));
        if (vibratesSwitch.isChecked()) {
            vibratesSwitch.setText("On");
        } else {
            vibratesSwitch.setText("Off");
        }
    }
}