package com.example.pc.bestuse.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.bestuse.AddProductActivity;
import com.example.pc.bestuse.EditProductActivity;
import com.example.pc.bestuse.R;
import com.example.pc.bestuse.adapter.ProductsSellingAdapter;
import com.example.pc.bestuse.adapter.ProductsSellingAdapter;
import com.example.pc.bestuse.listener.RecyclerTouchListener;
import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.model.Request;
import com.example.pc.bestuse.rest.ApiProduct;
import com.example.pc.bestuse.rest.InterfaceProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSelling extends Fragment {
    private Unbinder unbinder;
    View view;

    public static List<Product> productList = new ArrayList<>();
    public static ProductsSellingAdapter mAdapter;

    @BindView(R.id.recycler_view_selling)
    RecyclerView recyclerView;

    @BindView(R.id.fab_add_product)
    FloatingActionButton fabAddProduct;

    int pos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selling, container, false);
        unbinder = ButterKnife.bind(this, view);
        final SharedPreferences pre=this.getActivity().getSharedPreferences("token", MODE_PRIVATE);

        productList.clear();

        mAdapter = new ProductsSellingAdapter(productList);
        mAdapter.setmContext(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                pos=position;

                Intent intent = new Intent(getContext(), EditProductActivity.class);
                intent.putExtra("product", productList.get(position));
                intent.putExtra("position", position);
                startActivity(intent);

                AlertDialog.Builder b=new AlertDialog.Builder(getContext());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        InterfaceProduct apiService= ApiProduct.getClient().create(InterfaceProduct.class);
        Request req=new Request();
        req.setSelling(1);
        Call<List<Product>> call=apiService.getListProduct(req,pre.getString("token",null));
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                for (Product product:response.body()) {
                    productList.add(product);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("huhu","huhu");
            }
        });

        return view;
    }
    @OnClick(R.id.fab_add_product)
    void onClickFabAddProduct(){
        Intent intent = new Intent(getContext(), AddProductActivity.class);
        startActivity(intent);
    }
}
