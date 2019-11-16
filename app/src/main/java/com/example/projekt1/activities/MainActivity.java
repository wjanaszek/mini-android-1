package com.example.projekt1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.utils.ApplyPreferencesService;

public class MainActivity extends AppCompatActivity {

    private Intent productListIntent;
    private Intent optionsIntent;
    private SharedPreferences sharedPreferences;

    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setButtonsBackgroundColorInActivity();
        setTextSizeInActivity();
        setTextColorInActivity();
    }

    public void onProductListClick(View view) {
        startActivity(productListIntent);
    }

    public void onOptionsClick(View view) {
        startActivity(optionsIntent);
    }

    private void initView() {
        Button optionsButton = findViewById(R.id.nav_options);
        Button productListButton = findViewById(R.id.nav_shop_list);
        optionsIntent = new Intent(this, OptionsActivity.class);
        productListIntent = new Intent(this, ProductListActivity.class);
        sharedPreferences = getSharedPreferences(getString(R.string.sp), Context.MODE_PRIVATE);

        buttons = new Button[]{optionsButton, productListButton};
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
    }

    private void setTextSizeInActivity() {
        Integer textSize = sharedPreferences.getInt(getString(R.string.options_text_size), 35);

        for (Button button : buttons) {
            ApplyPreferencesService.setTextSizeOnButton(
                button,
                textSize
            );
        }
    }
}
