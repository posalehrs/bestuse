package com.example.pc.bestuse.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.bestuse.DetailActivity;
import com.example.pc.bestuse.R;
import com.example.pc.bestuse.adapter.ProductsHomeAdapter;
import com.example.pc.bestuse.listener.RecyclerTouchListener;
import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.model.Request;
import com.example.pc.bestuse.rest.ApiProduct;
import com.example.pc.bestuse.rest.InterfaceProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    private Unbinder unbinder;
    View view;

    public static List<Product> productList = new ArrayList<>();
    public static ProductsHomeAdapter mAdapter;

    @BindView(R.id.recycler_view_home)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new ProductsHomeAdapter(productList);
        mAdapter.setmContext(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("product", productList.get(position));

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Request req=new Request();
        switch (FragmentCategory.category){
            case 0:
                req.setCategory("Kẹo");
                FragmentCategory.category=-1;
                break;
            case 1:
                req.setCategory("Sữa");
                FragmentCategory.category=-1;
                break;
            case 2:
                req.setCategory("Thực phẩm khô");
                FragmentCategory.category=-1;
                break;
            case 3:
                req.setCategory("Thực phẩm tươi");
                FragmentCategory.category=-1;
                break;
        }

        InterfaceProduct apiService= ApiProduct.getClient().create(InterfaceProduct.class);
        Call<List<Product>> call=apiService.getListProduct(req,"");
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
}
