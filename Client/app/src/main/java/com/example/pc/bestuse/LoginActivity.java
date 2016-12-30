package com.example.pc.bestuse;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.bestuse.adapter.PagerAdapter;
import com.example.pc.bestuse.fragment.FragmentPerson;
import com.example.pc.bestuse.model.User;
import com.example.pc.bestuse.rest.ApiUser;
import com.example.pc.bestuse.rest.InterfaceUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity{

    @BindView(R.id.btnLinkToRegisterScreen)
    Button btnLinkToRegisterScreen;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.email)
    EditText userEmail;
    @BindView(R.id.password)
    EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLinkToRegisterScreen)
    public void onClickLinkToRegisterScreen(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnLogin)
    public void onClickLogin(){
//        Intent intent=getIntent();
//        finish();

        User user=new User(userEmail.getText().toString(),userPassword.getText().toString());

        InterfaceUser apiService = ApiUser.getClient().create(InterfaceUser.class);

        Call<User> call = apiService.login(user);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d("hee",response.headers().get("token"));
//                Log.d("hehe",response.body().toString());

                SharedPreferences pre=getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("token", response.headers().get("token"));
                editor.commit();

                Toast.makeText(LoginActivity.this, "Đăng nhập thành công",   Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                Bundle bundle=new Bundle();
                bundle.putBoolean("justLogin",true);
                intent.putExtra("Login", bundle);

                startActivity(intent);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        LoginActivity.this).create();

                alertDialog.setTitle("Thông báo");

                alertDialog.setMessage("Tên hoặc mật khẩu nhập không đúng!");

//                alertDialog.setIcon();

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.show();
            }
        });
    }

}

