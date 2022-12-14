// Generated by view binder compiler. Do not edit!
package com.adamgamesstudio.tictactoegame.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.adamgamesstudio.tictactoegame.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySettingsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Spinner appThemeSpinner;

  @NonNull
  public final EditText playerOneNameInput;

  @NonNull
  public final EditText playerTwoNameInput;

  @NonNull
  public final Button saveButton;

  @NonNull
  public final Switch switchPlayersAfterRoundSwitch;

  @NonNull
  public final TextView timerModeOptionsDescription;

  @NonNull
  public final Switch timerModeSwitch;

  @NonNull
  public final Switch vibrateSwitch;

  private ActivitySettingsBinding(@NonNull ConstraintLayout rootView,
      @NonNull Spinner appThemeSpinner, @NonNull EditText playerOneNameInput,
      @NonNull EditText playerTwoNameInput, @NonNull Button saveButton,
      @NonNull Switch switchPlayersAfterRoundSwitch, @NonNull TextView timerModeOptionsDescription,
      @NonNull Switch timerModeSwitch, @NonNull Switch vibrateSwitch) {
    this.rootView = rootView;
    this.appThemeSpinner = appThemeSpinner;
    this.playerOneNameInput = playerOneNameInput;
    this.playerTwoNameInput = playerTwoNameInput;
    this.saveButton = saveButton;
    this.switchPlayersAfterRoundSwitch = switchPlayersAfterRoundSwitch;
    this.timerModeOptionsDescription = timerModeOptionsDescription;
    this.timerModeSwitch = timerModeSwitch;
    this.vibrateSwitch = vibrateSwitch;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySettingsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySettingsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_settings, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySettingsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appThemeSpinner;
      Spinner appThemeSpinner = ViewBindings.findChildViewById(rootView, id);
      if (appThemeSpinner == null) {
        break missingId;
      }

      id = R.id.playerOneNameInput;
      EditText playerOneNameInput = ViewBindings.findChildViewById(rootView, id);
      if (playerOneNameInput == null) {
        break missingId;
      }

      id = R.id.playerTwoNameInput;
      EditText playerTwoNameInput = ViewBindings.findChildViewById(rootView, id);
      if (playerTwoNameInput == null) {
        break missingId;
      }

      id = R.id.saveButton;
      Button saveButton = ViewBindings.findChildViewById(rootView, id);
      if (saveButton == null) {
        break missingId;
      }

      id = R.id.switchPlayersAfterRoundSwitch;
      Switch switchPlayersAfterRoundSwitch = ViewBindings.findChildViewById(rootView, id);
      if (switchPlayersAfterRoundSwitch == null) {
        break missingId;
      }

      id = R.id.timerModeOptionsDescription;
      TextView timerModeOptionsDescription = ViewBindings.findChildViewById(rootView, id);
      if (timerModeOptionsDescription == null) {
        break missingId;
      }

      id = R.id.timerModeSwitch;
      Switch timerModeSwitch = ViewBindings.findChildViewById(rootView, id);
      if (timerModeSwitch == null) {
        break missingId;
      }

      id = R.id.vibrateSwitch;
      Switch vibrateSwitch = ViewBindings.findChildViewById(rootView, id);
      if (vibrateSwitch == null) {
        break missingId;
      }

      return new ActivitySettingsBinding((ConstraintLayout) rootView, appThemeSpinner,
          playerOneNameInput, playerTwoNameInput, saveButton, switchPlayersAfterRoundSwitch,
          timerModeOptionsDescription, timerModeSwitch, vibrateSwitch);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
