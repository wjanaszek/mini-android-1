package com.example.projekt1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.projekt1.R;
import com.example.projekt1.domain.AppDatabase;
import com.example.projekt1.domain.Product;
import com.example.projekt1.domain.ProductDao;
import com.example.projekt1.utils.ApplyPreferencesService;
import com.example.projekt1.utils.ProductListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private FloatingActionButton addProductButton;
    private Intent addEditProductIntent;
    private SharedPreferences sharedPreferences;
    private ProductDao productDao;
    private List<Product> productList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initProductDao();
        initProductList();
        initView();
        addEditProductIntent = new Intent(this, AddEditProductActivity.class);
        sharedPreferences = getSharedPreferences(getString(R.string.sp), Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setButtonsBackgroundColorInActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            initProductList();
            refreshAdapter();

            if (wasItemAdded(data)) {
                adapter.notifyItemInserted(productList.size() - 1);
            }

            if (wasItemUpdated(data)) {
                adapter.notifyItemChanged(productList.indexOf(getUpdatedItem(data)));
            }
        }
    }

    public void onAddProductClick(View view) {
        startActivityForResult(addEditProductIntent, 1);
    }

    private Product getUpdatedItem(Intent intent) {
        Long updatedId = Long.valueOf(intent.getStringExtra(getString(R.string.product_list_item_updated)));
        for (Product product : productList) {
            if (product.id.equals(updatedId)) {
                return product;
            }
        }

        return null;
    }

    private void initProductDao() {
        AppDatabase db = Room.databaseBuilder(
            getApplicationContext(),
            AppDatabase.class,
            getString(R.string.database_name)
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()   // @TODO should be removed
            .build();

        productDao = db.productDao();
    }

    private void initProductList() {
        productList = productDao.getAll();
    }

    private void initView() {
        addProductButton = findViewById(R.id.products_add_floating_button);
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager;layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refreshAdapter();
    }

    private void refreshAdapter() {
        adapter = new ProductListAdapter(productList, this, productDao);
        recyclerView.setAdapter(adapter);
    }

    private void setButtonsBackgroundColorInActivity() {
        String hexBackgroundColor = sharedPreferences.getString(
            getString(R.string.sp_button_background),
            getResources().getString(R.string.default_background_color)
        );
        ApplyPreferencesService.setBackgroundColorOnFloatingButton(
            addProductButton,
            hexBackgroundColor
        );
    }

    private boolean wasItemAdded(Intent intent) {
        return intent.getStringExtra(getString(R.string.product_list_item_added)) != null;
    }

    private boolean wasItemUpdated(Intent intent) {
        return intent.getStringExtra(getString(R.string.product_list_item_updated)) != null;
    }
}
