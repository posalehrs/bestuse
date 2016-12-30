package com.example.pc.bestuse.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.bestuse.AppConfig;
import com.example.pc.bestuse.DetailActivity;
import com.example.pc.bestuse.EditUserActivity;
import com.example.pc.bestuse.LoginActivity;
import com.example.pc.bestuse.MainActivity;
import com.example.pc.bestuse.R;
import com.example.pc.bestuse.RegisterActivity;
import com.example.pc.bestuse.model.Request;
import com.example.pc.bestuse.model.User;
import com.example.pc.bestuse.rest.ApiUser;
import com.example.pc.bestuse.rest.InterfaceUser;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FragmentPrivate extends Fragment {
    private Unbinder unbinder;
    View view;

    @BindView(R.id.txt_name)
    TextView userName;
    @BindView(R.id.txt_email)
    TextView userEmail;
    @BindView(R.id.txt_address)
    TextView userAddress;
    @BindView(R.id.txt_phone)
    TextView userPhone;

    @BindView(R.id.img_user)
    ImageView userImage;

    @BindView(R.id.btn_logout)
    Button btnLogout;

    @BindView(R.id.fab_edit_user)
    FloatingActionButton fabEditUser;

    User u;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_private, container, false);
        unbinder = ButterKnife.bind(this, view);

        SharedPreferences pre=this.getActivity().getSharedPreferences("token", MODE_PRIVATE);

        InterfaceUser apiService= ApiUser.getClient().create(InterfaceUser.class);
        Call<User> call=apiService.getUser(new Request(),pre.getString("token",null));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                u=response.body();
//                DecimalFormat formatter = new DecimalFormat("#,###,###");
//                android.text.format.DateFormat df = new android.text.format.DateFormat();

                userName.setText(u.getName());
                userEmail.setText(u.getEmail());
                userAddress.setText(u.getAddress());
                userPhone.setText(u.getNumber_phone());

                Glide.with(getContext())
                        .load(AppConfig.IpServer+"/images/"+u.getImage())
                        .into(userImage);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("hehe","huhu");
            }
        });

        return view;
    }

    @OnClick(R.id.btn_logout)
    void onCLickLogout(){
        SharedPreferences pre=this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_edit_user)
    void onClickFabEditUser(){
        Intent intent = new Intent(getContext(), EditUserActivity.class);
        intent.putExtra("user", u);
        startActivity(intent);
    }
}
