package com.example.projekt1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.projekt1.R;
import com.example.projekt1.domain.AppDatabase;
import com.example.projekt1.domain.Product;
import com.example.projekt1.domain.ProductDao;
import com.example.projekt1.utils.ApplyPreferencesService;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public class AddEditProductActivity extends AppCompatActivity implements Validator.ValidationListener {

    private ProductDao productDao;
    private Product editedProduct;
    private Long editedProductId = null;

    @NotEmpty(
        messageResId = R.string.validation_required
    )
    private EditText name;

    @DecimalMin(
        value = 1,
        messageResId = R.string.validation_min_1
    )
    @DecimalMax(
        value = 10000,
        messageResId = R.string.validation_max_10000
    )
    private EditText quanity;

    @DecimalMin(
        value = 0.01,
        messageResId = R.string.validation_min_0_01
    )
    @DecimalMax(
        value = 10000,
        messageResId = R.string.validation_max_10000
    )
    private EditText price;

    private CheckBox bought;

    private Button submitButton;

    private Intent productListIntent;
    private Validator validator;
    private SharedPreferences sharedPreferences;
    private TextView topBar;

    private Button[] buttons;
    private TextView[] textViews;
    private EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        productListIntent = new Intent();
        sharedPreferences = getSharedPreferences(getString(R.string.sp), Context.MODE_PRIVATE);
        initView();
        initProduct();
        initValidator();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setButtonsBackgroundColorInActivity();
        setTextSizeInActivity();
        setTextColorInActivity();
    }

    public void onCancelButtonClick(View view) {
        finish();
    }


    public void onSubmitButtonClick(View view) {
        validator.validate(true);
    }

    @Override
    public void onValidationSucceeded() {
        if (editedProduct != null) {
            updateProduct();
        } else {
            insertProduct();
        }

        setResult(RESULT_OK, productListIntent);
        finish();
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

    private void insertProduct() {
        Product product = new Product();
        product.name = name.getText().toString();
        product.price = new BigDecimal(price.getText().toString());
        product.quanity = Integer.valueOf(quanity.getText().toString());
        product.isBought = bought.isChecked();
        productDao.insertAll(product);
        productListIntent.putExtra(getString(R.string.product_list_item_added), "true");
        Toast
            .makeText(this, getString(R.string.toast_product_added), Toast.LENGTH_SHORT)
            .show();
    }

    private void initEditedProduct() {
        editedProduct = productDao.findById(editedProductId);

        if (editedProduct != null) {
            name.setText(editedProduct.name);
            quanity.setText(editedProduct.quanity.toString());
            price.setText(editedProduct.price.toString());
            bought.setChecked(editedProduct.isBought);
            topBar.setText(getString(R.string.product_dialog_edit_header));
            submitButton.setText(getString(R.string.product_dialog_update_button));
        }
    }

    private void initProduct() {
        AppDatabase db = Room.databaseBuilder(
            getApplicationContext(),
            AppDatabase.class,
            getString(R.string.database_name)
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();

        productDao = db.productDao();

        if (getIntent().getStringExtra(getString(R.string.product_id)) != null) {
            editedProductId = Long.valueOf(
                getIntent().getStringExtra(getString(R.string.product_id))
            );

            if (editedProductId != 0) {
                initEditedProduct();
            }
        }
    }

    private void initValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initView() {
        name = findViewById(R.id.product_add_edit_name);
        quanity = findViewById(R.id.product_add_edit_quanity);
        price = findViewById(R.id.product_add_edit_price);
        TextView boughtLabel = findViewById(R.id.product_dialog_add_bought_label);
        bought = findViewById(R.id.product_add_edit_bought);
        Button cancelButton = findViewById(R.id.product_add_edit_cancel_button);
        submitButton = findViewById(R.id.product_add_edit_submit_button);
        topBar = findViewById(R.id.product_dialog_add_header);

        buttons = new Button[]{submitButton, cancelButton};
        textViews = new TextView[]{topBar, boughtLabel};
        editTexts = new EditText[]{name, price, quanity};
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

    private void updateProduct() {
        editedProduct.name = name.getText().toString();
        editedProduct.price = new BigDecimal(price.getText().toString());
        editedProduct.quanity = Integer.valueOf(quanity.getText().toString());
        editedProduct.isBought = bought.isChecked();
        productDao.updateOne(editedProduct);
        productListIntent.putExtra(getString(R.string.product_list_item_updated), editedProduct.id.toString());
        Toast
            .makeText(this, getString(R.string.toast_product_updated), Toast.LENGTH_SHORT)
            .show();
    }
}
