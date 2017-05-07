package com.example.pc.bestuse.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.bestuse.R;
import com.example.pc.bestuse.adapter.ProductsHiddenAdapter;
import com.example.pc.bestuse.adapter.ProductsSellingAdapter;
import com.example.pc.bestuse.listener.RecyclerTouchListener;
import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.model.Request;
import com.example.pc.bestuse.rest.ApiProduct;
import com.example.pc.bestuse.rest.InterfaceProduct;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FragmentHidden extends Fragment {
    private Unbinder unbinder;
    View view;

    int pos;

    public static List<Product> productList = new ArrayList<>();
    public static ProductsHiddenAdapter mAdapter;

    @BindView(R.id.recycler_view_hidden)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hidden, container, false);
        final SharedPreferences pre=this.getActivity().getSharedPreferences("token", MODE_PRIVATE);

        unbinder = ButterKnife.bind(this, view);

        productList.clear();

        mAdapter = new ProductsHiddenAdapter(productList);
        mAdapter.setmContext(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        InterfaceProduct apiService= ApiProduct.getClient().create(InterfaceProduct.class);
        Request req=new Request();
        req.setSelling(0);
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                pos=position;

                AlertDialog.Builder b=new AlertDialog.Builder(getContext());

                b.setTitle("Hỏi?");
                b.setMessage("Bạn có muốn hiện tin này?");
                b.setPositiveButton("Có", new DialogInterface. OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Product p=new Product();
                        p.setSelling(1);
                        p.set_id(productList.get(pos).get_id().toString());
                        InterfaceProduct apiService= ApiProduct.getClient().create(InterfaceProduct.class);
                        Call<Product> call = apiService.updateProduct(p, pre.getString("token", null));
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                Toast.makeText(getContext(), "Tin đã được hiện", Toast.LENGTH_LONG).show();

                                productList.remove(pos);
                                mAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {

                            }
                        });

                    }});
                b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
                b.create().show();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }
}
