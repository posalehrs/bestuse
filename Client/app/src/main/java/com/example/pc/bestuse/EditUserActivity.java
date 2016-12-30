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

import com.bumptech.glide.Glide;
import com.example.pc.bestuse.model.User;
import com.example.pc.bestuse.rest.ApiUser;
import com.example.pc.bestuse.rest.InterfaceImage;
import com.example.pc.bestuse.rest.InterfaceUser;
import com.example.pc.bestuse.rest.ServiceGenerator;

import java.io.File;
import java.io.IOException;

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

public class EditUserActivity extends AppCompatActivity {
    @BindView(R.id.btn_up_image)
    Button btnUpImage;
    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.img_edit_user)
    ImageView imgEditUser;

    @BindView(R.id.edt_name)
    EditText name;
    @BindView(R.id.edt_email)
    EditText email;
    @BindView(R.id.edt_password)
    EditText password;
    @BindView(R.id.edt_address)
    EditText address;
    @BindView(R.id.edt_phone)
    EditText phone;

    boolean changeImage=false;

    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;
    Uri filePath;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent callerIntent=getIntent();

        user = (User) callerIntent.getSerializableExtra("user");

        name.setText(user.getName());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        address.setText(user.getAddress());
        phone.setText(user.getNumber_phone());
        Glide.with(getApplicationContext())
                .load(AppConfig.IpServer+"/images/"+user.getImage())
                .into(imgEditUser);


    }

    @OnClick(R.id.btn_up_image)
    void onClickUpImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save)
    void onClickSave(){
        if(changeImage) {
            InterfaceImage apiService = ServiceGenerator.createService(InterfaceImage.class);

            final File file = new File(getPathFromURI(getApplicationContext(), filePath));


            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), descriptionString);
            Call<ResponseBody> call = apiService.upload(description, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    user.setImage(file.getName());
                    updateUser();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("hehe", "up file fail");
                }
            });
        }else {
            updateUser();
        }

    }

    public void updateUser(){

        User us=new User(name.getText().toString(),password.getText().toString(),email.getText().toString(),address.getText().toString(),phone.getText().toString(),user.getImage());
        SharedPreferences pre=getSharedPreferences("token", MODE_PRIVATE);

        InterfaceUser apiService = ApiUser.getClient().create(InterfaceUser.class);
        Call<User> call=apiService.updateUser(us,pre.getString("token",null));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("hehe","huhu");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            changeImage=true;
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imgEditUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
