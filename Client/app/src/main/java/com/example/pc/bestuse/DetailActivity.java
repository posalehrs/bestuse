package com.example.pc.bestuse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.model.Request;
import com.example.pc.bestuse.model.User;
import com.example.pc.bestuse.rest.ApiProduct;
import com.example.pc.bestuse.rest.ApiUser;
import com.example.pc.bestuse.rest.InterfaceProduct;
import com.example.pc.bestuse.rest.InterfaceUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_product)
    ImageView img_product;
    @BindView(R.id.txt_product_name)
    TextView productName;
    @BindView(R.id.txt_old_price)
    TextView oldPrice;
    @BindView(R.id.txt_new_price)
    TextView newPrice;
    @BindView(R.id.txt_amount)
    TextView amount;
    @BindView(R.id.txt_user_name)
    TextView userName;
    @BindView(R.id.txt_update_product)
    TextView updateProductDate;
    @BindView(R.id.txt_product_category)
    TextView category;
    @BindView(R.id.txt_address_user)
    TextView address;
    @BindView(R.id.txt_product_des)
    TextView productDes;

    @BindView(R.id.btnCall)
    Button btnCall;
    @BindView(R.id.btnFovorite)
    Button btnFovorite;
    @BindView(R.id.btnShare)
    Button btnShare;

    User u;
    Product product;
    SharedPreferences pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pre=this.getSharedPreferences("token", MODE_PRIVATE);

        Intent callerIntent=getIntent();

        product = (Product) callerIntent.getSerializableExtra("product");

        InterfaceUser apiService= ApiUser.getClient().create(InterfaceUser.class);
        Call<User> call=apiService.getUser(new Request(((ArrayList) product.get_user()).get(0).toString()),"");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                u=response.body();
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                android.text.format.DateFormat df = new android.text.format.DateFormat();

                productName.setText(product.getName());
                oldPrice.setText(formatter.format(product.getOld_price())+"đ");
                oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                newPrice.setText(formatter.format(product.getNew_price())+"đ");
                amount.setText("Số lượng còn "+formatter.format(product.getAmount()));
                userName.setText("Người bán "+u.getName());
                updateProductDate.setText("Cập nhật "+df.format("hh:mm dd/MM/yyyy",product.getUp_date()));
                category.setText(product.getCategory());
                address.setText(product.getAddress());
                productDes.setText(product.getDescription());
                Glide.with(getApplicationContext())
                        .load(AppConfig.IpServer+"/images/"+product.getImage())
                        .into(img_product);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("hehe","huhu");
            }
        });
    }

    @OnClick(R.id.btnCall)
    void onClickCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+u.getNumber_phone().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.btnFovorite)
    void onClickFavorite(){
        InterfaceUser apiService = ApiUser.getClient().create(InterfaceUser.class);
        Call<User> call = apiService.addFavorite(product, pre.getString("token", null));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplication(), "Tin đã được thêm vào yêu thích", Toast.LENGTH_LONG).show();
//                                productList.remove(pos);
//                                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btnShare)
    void onClickShare(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = product.getName() + "\n" + "Giá chỉ " + product.getNew_price() + "đ\n" + product.getDescription() + "\nVô ngay BestUse để mua nhé.";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
