package com.example.projekt1.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.utils.ApplyPreferencesService;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.List;

public class OptionsActivity extends AppCompatActivity implements Validator.ValidationListener {

    private SharedPreferences sharedPreferences;

    @DecimalMin(
        value = 15,
        messageResId = R.string.validation_min_15
    )
    private EditText editTextSize;

    private Button[] buttons;
    private TextView[] textViews;
    private EditText[] editTexts;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        initView();
        initValidator();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshActivityFromPreferences();
    }

    @Override
    public void onValidationSucceeded() {
        sharedPreferences
            .edit()
            .putInt(
                getString(R.string.options_text_size),
                Integer.valueOf(editTextSize.getText().toString())
            )
            .apply();
        Toast.makeText(this, getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
        refreshActivityFromPreferences();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onCancelSaveTextSizeClick(View view) {
        setDefaultTextSizeInEditText();
    }

    public void onChangeTextColorClick(View view) {
        new ColorPickerDialog.Builder(this)
            .setTitle(getString(R.string.options_change_color_dialog_title))
            .setPreferenceName(getString(R.string.options_change_text_color))
            .setPositiveButton(getString(R.string.options_change_color_confirm),
                new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        saveColor(getString(R.string.sp_text_color), envelope);
                        setTextColorInActivity();
                    }
                })
            .setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(false)
            .show();
    }

    public void onChangeButtonsBackgroundColorClick(View view) {
        new ColorPickerDialog.Builder(this)
            .setTitle(getString(R.string.options_change_buttons_background_title))
            .setPreferenceName(getString(R.string.options_change_buttons_background))
            .setPositiveButton(getString(R.string.options_change_color_confirm),
                new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        saveColor(getString(R.string.sp_button_background), envelope);
                        setButtonsBackgroundColorInActivity();
                    }
                })
            .setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(false)
            .show();
    }

    public void onResetColorsClick(View view) {
        sharedPreferences
            .edit()
            .remove(getString(R.string.sp_text_color))
            .apply();
        sharedPreferences
            .edit()
            .remove(getString(R.string.sp_button_background))
            .apply();
        Toast.makeText(this, getString(R.string.option_reseted), Toast.LENGTH_SHORT).show();
        refreshActivityFromPreferences();
    }

    public void onResetTextSizeClick(View view) {
        sharedPreferences
            .edit()
            .putInt(
                getString(R.string.options_text_size),
                35
            )
            .apply();
        Toast.makeText(this, getString(R.string.option_reseted), Toast.LENGTH_SHORT).show();
        setDefaultTextSizeInEditText();
        refreshActivityFromPreferences();
    }

    public void onSaveTextSizeClick(View view) {
        validator.validate(true);
    }

    private void initValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initView() {
        sharedPreferences = getSharedPreferences(getString(R.string.sp), Context.MODE_PRIVATE);

        editTextSize = findViewById(R.id.options_text_size);
        setDefaultTextSizeInEditText();

        Button changeTextColorButton = findViewById(R.id.options_change_text_color);
        Button changeButtonsBackgroundButton = findViewById(R.id.options_change_buttons_background);
        Button saveTextSizeButton = findViewById(R.id.options_save_text_size_button);
        Button resetTextSizeButton = findViewById(R.id.options_reset_text_size_button);
        Button cancelTextSizeButton = findViewById(R.id.options_change_text_size_cancel_button);
        Button resetColorsButton = findViewById(R.id.options_reset_colors);
        TextView colorsOptionsHeader = findViewById(R.id.options_colors_header);
        TextView textSizeLabel = findViewById(R.id.options_change_text_size_label);

        buttons = new Button[]{
            changeTextColorButton,
            changeButtonsBackgroundButton,
            saveTextSizeButton,
            resetTextSizeButton,
            cancelTextSizeButton,
            resetColorsButton
        };
        textViews = new TextView[]{colorsOptionsHeader, textSizeLabel};
        editTexts = new EditText[]{editTextSize};
    }

    private void refreshActivityFromPreferences() {
        setButtonsBackgroundColorInActivity();
        setTextSizeInActivity();
        setTextColorInActivity();
    }

    private void setButtonsBackgroundColorInActivity() {
        String hexBackgroundColor = sharedPreferences.getString(
            getString(R.string.sp_button_background),
            getResources().getString(R.string.default_background_color)
        );
        for (Button button : buttons) {
            ApplyPreferencesService.setBackgroundColorOnButton(
                button,
                hexBackgroundColor
            );
        }
    }

    private void setDefaultTextSizeInEditText() {
        int textSize = sharedPreferences.getInt(getString(R.string.options_text_size), 35);
        editTextSize.setText(String.valueOf(textSize));
    }

    private void setTextColorInActivity() {
        String hexTextColor = sharedPreferences.getString(
            getString(R.string.sp_text_color),
            getResources().getString(R.string.default_text_color)
        );

        for (Button button : buttons) {
            ApplyPreferencesService.setTextColorOnButton(
                button,
                hexTextColor
            );
        }

        for (TextView textView : textViews) {
            ApplyPreferencesService.setTextColorOnTextView(
                textView,
                hexTextColor
            );
        }

        for (EditText editText : editTexts) {
            ApplyPreferencesService.setTextColorOnEditText(
                editText,
                hexTextColor
            );
        }
    }

    private void setTextSizeInActivity() {
        Integer textSize = sharedPreferences.getInt(getString(R.string.options_text_size), 35);

        for (Button button : buttons) {
            ApplyPreferencesService.setTextSizeOnButton(
                button,
                textSize
            );
        }

        for (TextView textView : textViews) {
            ApplyPreferencesService.setTextSizeOnTextView(
                textView,
                textSize
            );
        }

        for (EditText editText : editTexts) {
            ApplyPreferencesService.setTextSizeOnEditText(
                editText,
                textSize
            );
        }
    }

    private void saveColor(String preferencesKey, ColorEnvelope envelope) {
        sharedPreferences
            .edit()
            .putString(
                preferencesKey,
                "#" + envelope.getHexCode()
            )
            .apply();
        Toast.makeText(this, getString(R.string.options_color_changed), Toast.LENGTH_SHORT).show();
    }
}
