package com.example.pc.bestuse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.bestuse.R;
import com.example.pc.bestuse.model.User;
import com.example.pc.bestuse.rest.ApiUser;
import com.example.pc.bestuse.rest.InterfaceUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity{
    @BindView(R.id.btnLinkToLoginScreen)
    Button btnLinkToLoginScreen;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLinkToLoginScreen)
    public void onClickLinkToLoginScreen(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnRegister)
    public void onClickRegister(){
        User user=new User();
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());

        InterfaceUser apiService= ApiUser.getClient().create(InterfaceUser.class);
        Call<User> call=apiService.registerUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d("hee",response.headers().get("token"));
//                Log.d("hehe",response.body().toString());
                if(response.body()==null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            RegisterActivity.this).create();
                    alertDialog.setTitle("Thông báo");
                    alertDialog.setMessage("Đã tồn tại người dùng này! Hãy chọn đăng nhập.");
//                alertDialog.setIcon();
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    alertDialog.show();
                }else {

                    SharedPreferences pre = getSharedPreferences("token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putString("token", response.headers().get("token"));
                    editor.commit();

                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("justLogin", true);
                    intent.putExtra("Login", bundle);

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
//                AlertDialog alertDialog = new AlertDialog.Builder(
//                        RegisterActivity.this).create();
//
//                alertDialog.setTitle("Thông báo");
//
//                alertDialog.setMessage("Đã tồn tại người dùng này! Hãy chọn đăng nhập.");
//
////                alertDialog.setIcon();
//
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
////                        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                alertDialog.show();
                Log.d("hehe","huhu");
            }
        });
    }

}

