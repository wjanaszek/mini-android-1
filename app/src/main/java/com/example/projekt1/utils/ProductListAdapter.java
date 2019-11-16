package com.example.projekt1.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.AddEditProductActivity;
import com.example.projekt1.activities.ProductListActivity;
import com.example.projekt1.domain.Product;
import com.example.projekt1.domain.ProductDao;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Product> data;
    private Context context;
    private Intent editProductIntent;
    private ProductDao productDao;
    private SharedPreferences sharedPreferences;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView name;
        TextView quanityAndPrice;
        CheckBox isBought;
        Button editButton;
        Button removeButton;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.product_name);
            quanityAndPrice = view.findViewById(R.id.product_quanity_price);
            isBought = view.findViewById(R.id.product_bought);
            editButton = view.findViewById(R.id.product_edit_button);
            removeButton = view.findViewById(R.id.product_remove_button);
        }
    }

    public ProductListAdapter(
        List<Product> data,
        Context context,
        ProductDao productDao
    ) {
        this.data = data;
        this.context = context;
        this.editProductIntent = new Intent(context, AddEditProductActivity.class);
        this.productDao = productDao;
        this.sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.sp),
            Context.MODE_PRIVATE
        );
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(
        ViewGroup parent,
        int viewType
    ) {
        // create a new view
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Product product = data.get(position);
        holder.name.setText(product.name);
        holder.quanityAndPrice.setText(
            context.getString(R.string.product_quanity)
                + " " + product.quanity.toString()
                + ". " + context.getString(R.string.product_price)
                + " " + product.price.toString()
        );
        holder.isBought.setChecked(product.isBought);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editProductIntent.putExtra(context.getString(R.string.product_id), product.id.toString());
                ((ProductListActivity) context).startActivityForResult(editProductIntent, 1);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                notifyItemRemoved(position);
                data.remove(product);
                productDao.delete(product);
            }
        });

        Button[] buttons = new Button[]{holder.removeButton, holder.editButton};
        TextView[] textViews = new TextView[]{holder.name, holder.quanityAndPrice};

        setButtonBackgroundColorInList(buttons);
        setTextColorInList(buttons, textViews);
        setTextSizeInList(buttons, textViews);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    private void setButtonBackgroundColorInList(Button[] buttons) {
        String hexBackgroundColor = sharedPreferences.getString(
            context.getString(R.string.sp_button_background),
            context.getResources().getString(R.string.default_background_color)
        );

        for (Button button : buttons) {
            ApplyPreferencesService.setBackgroundColorOnButton(
                button,
                hexBackgroundColor
            );
        }
    }

    private void setTextColorInList(Button[] buttons, TextView[] textViews) {
        String hexTextColor = sharedPreferences.getString(
            context.getString(R.string.sp_text_color),
            context.getResources().getString(R.string.default_text_color)
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
    }

    private void setTextSizeInList(Button[] buttons, TextView[] textViews) {
        Integer textSize = sharedPreferences.getInt(context.getString(R.string.options_text_size), 35);

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
    }
}