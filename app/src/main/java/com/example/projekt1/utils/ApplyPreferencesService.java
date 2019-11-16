package com.example.projekt1.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ApplyPreferencesService {
    public static void setBackgroundColorOnFloatingButton(FloatingActionButton floatingButton, String hexColor) {
        floatingButton.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(hexColor)
            )
        );
    }

    public static void setBackgroundColorOnButton(Button button, String hexColor) {
        button.getBackground().setColorFilter(
            Color.parseColor(hexColor),
            PorterDuff.Mode.SRC_ATOP
        );
    }

    public static void setTextColorOnButton(Button button, String hexColor) {
        button.setTextColor(
            Color.parseColor(hexColor)
        );
    }

    public static void setTextColorOnTextView(TextView textView, String hexColor) {
        textView.setTextColor(
            Color.parseColor(hexColor)
        );
    }

    public static void setTextColorOnEditText(EditText editText, String hexColor) {
        editText.setTextColor(
            Color.parseColor(hexColor)
        );
    }

    public static void setTextSizeOnTextView(TextView textView, Integer size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public static void setTextSizeOnEditText(EditText editText, Integer size) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public static void setTextSizeOnButton(Button button, Integer size) {
        button.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            size
        );
    }
}
