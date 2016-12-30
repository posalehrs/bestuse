package com.example.pc.bestuse.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pc.bestuse.fragment.FragmentCategory;
import com.example.pc.bestuse.fragment.FragmentHome;
import com.example.pc.bestuse.fragment.FragmentPerson;
import com.example.pc.bestuse.fragment.FragmentPersonLogin;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private boolean isLogin;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new FragmentHome();
                break;
            case 1:
                frag=new FragmentCategory();
                break;
            case 2:
                if(isLogin){
                    frag=new FragmentPersonLogin();
                }else{
                    frag=new FragmentPerson();
                }
                break;
        }
        return frag;
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title="Trang chủ";
                break;
            case 1:
                title="Danh mục";
                break;
            case 2:
                title="Cá nhân";
                break;
        }

        return title;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }
}
