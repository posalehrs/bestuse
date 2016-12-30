package com.example.pc.bestuse.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.bestuse.AppConfig;
import com.example.pc.bestuse.R;
import com.example.pc.bestuse.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductsHiddenAdapter extends RecyclerView.Adapter<ProductsHiddenAdapter.MyViewHolder> {

    private List<Product> productsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description,old_price,new_price,amount;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.product_name);
            old_price = (TextView) view.findViewById(R.id.old_price);
            new_price = (TextView) view.findViewById(R.id.new_price);
            amount=(TextView)view.findViewById(R.id.product_amount);
            logo = (ImageView) view.findViewById(R.id.product_image);
        }
    }


    public ProductsHiddenAdapter(List<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row_hidden, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        Product product = productsList.get(position);
        holder.name.setText(product.getName());
        holder.old_price.setText(formatter.format(product.getOld_price())+"đ");
        holder.old_price.setPaintFlags(holder.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_price.setText(formatter.format(product.getNew_price())+"đ");
        holder.amount.setText("Số lượng còn "+formatter.format(product.getAmount()));
//        holder.expiration_date.setText("Hạn dùng còn "+product.getExpiration_date() +" ngày");
//        holder.up_date.setText("Cập nhật "+df.format("hh:mm dd/MM/yyyy", product.getUp_date()));
        Glide.with(mContext)
                .load(AppConfig.IpServer+"/images/"+product.getImage())
                .into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
