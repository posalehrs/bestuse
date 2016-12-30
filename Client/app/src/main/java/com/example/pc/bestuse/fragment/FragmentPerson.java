package com.example.pc.bestuse.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pc.bestuse.LoginActivity;
import com.example.pc.bestuse.R;
import com.example.pc.bestuse.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentPerson extends Fragment {
    private Unbinder unbinder;
    View view;

    @BindView(R.id.btnGoLogin)
    Button btnGoLogin;
    @BindView(R.id.btnGoRegister)
    Button btnGoRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnGoLogin)
    public void onClickGoLogin(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
//        startActivity(intent);
        startActivityForResult(intent,1);
    }

    @OnClick(R.id.btnGoRegister)
    public void onClickGoRegister(){
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
