package com.example.pc.bestuse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.rest.ApiProduct;
import com.example.pc.bestuse.rest.InterfaceImage;
import com.example.pc.bestuse.rest.InterfaceProduct;
import com.example.pc.bestuse.rest.ServiceGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_up_image)
    Button btnUpImage;

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_old_price)
    EditText edtOldPrice;
    @BindView(R.id.edt_new_price)
    EditText edtNewPrice;
    @BindView(R.id.edt_amount)
    EditText edtAmount;
    @BindView(R.id.edt_category)
    EditText edtCategory;
    @BindView(R.id.edt_des)
    EditText edtDes;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_exp)
    EditText edtExp;

    @BindView(R.id.btn_add_product)
    Button btnAddProduct;

    @BindView(R.id.img_product)
    ImageView imgProduct;

    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @OnClick(R.id.btn_up_image)
    void onClickUpImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.d("hehe",filePath.toString());
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imgProduct.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_add_product)
    void onClickAddProduct(){
        InterfaceImage apiServiceImage = ServiceGenerator.createService(InterfaceImage.class);

        final File file = new File(getPathFromURI(getApplicationContext(), filePath));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        Call<ResponseBody> callImage = apiServiceImage.upload(description, body);
        callImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> callImage, Response<ResponseBody> response) {

                Log.d("hehe", file.getName());

                SharedPreferences pre=getSharedPreferences("token", MODE_PRIVATE);
                Product product=new Product(edtName.getText().toString(),edtDes.getText().toString(),file.getName(),edtCategory.getText().toString(),edtAddress.getText().toString(),Integer.parseInt(edtOldPrice.getText().toString()),Integer.parseInt(edtNewPrice.getText().toString()),Integer.parseInt(edtExp.getText().toString()),new Date(),Integer.parseInt(edtAmount.getText().toString()));

                InterfaceProduct apiService= ApiProduct.getClient().create(InterfaceProduct.class);
                Call<Product> call=apiService.productCreate(product,pre.getString("token",null));

                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        Log.d("hehe",response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d("huhu","huhu");
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("hehe", "up file fail");

            }
        });
    }


    private String getPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

}
