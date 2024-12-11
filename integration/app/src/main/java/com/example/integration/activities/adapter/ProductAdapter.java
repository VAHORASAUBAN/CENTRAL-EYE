package com.example.integration.activities.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;
import com.example.integration.activities.admin.Remaining_barcodescanner;
import com.example.integration.activities.model.Product;

import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getAsset_name());

        String barcode = product.getBarcode();
        if (barcode == null || barcode.isEmpty()) {
            // When the barcode is null or empty
            holder.productBarcode.setText("Not Available");
            holder.viewButton.setImageResource(R.drawable.baseline_qr_code_scanner_24); // Replace with another icon
            holder.viewButton.setOnClickListener(v -> {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Remaining_barcodescanner fragment = Remaining_barcodescanner.newInstance(product); // Pass the Product object
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment) // Ensure `fragment_container` exists
                        .addToBackStack(null)
                        .commit();
            });
        } else {
            // When the barcode is available
            holder.productBarcode.setText(barcode);
            holder.productcarView.setOnClickListener(v -> listener.onViewClick(product));
            holder.viewButton.setImageResource(R.drawable.baseline_keyboard_arrow_right_24); // Default icon
            holder.viewButton.setOnClickListener(v -> listener.onViewClick(product)); // Enable click
        }

//        holder.productId.setText("Location: " + product.getLocation());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productBarcode, productId;
        ImageView productImage;
        CardView productcarView;
        ImageView viewButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productBarcode = itemView.findViewById(R.id.productBarcode);
            productcarView=itemView.findViewById(R.id.productcard);
//            productId = itemView.findViewById(R.id.productId);
//            productImage = itemView.findViewById(R.id.productImage);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}

