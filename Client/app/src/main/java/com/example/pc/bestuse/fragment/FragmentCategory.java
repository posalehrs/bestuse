package com.example.pc.bestuse.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.bestuse.LoginActivity;
import com.example.pc.bestuse.MainActivity;
import com.example.pc.bestuse.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class FragmentCategory extends Fragment {
    private Unbinder unbinder;
    View view;

    @BindView(R.id.dashboard_grid)
    GridView dashboard_grid;

    static final LauncherIcon[] ICONS = {
            new LauncherIcon(R.drawable.cake, "Kẹo", "cake.jpg"),
            new LauncherIcon(R.drawable.milk, "Sữa", "milk.png"),
            new LauncherIcon(R.drawable.dry_food, "Thực phẩm khô", "dry_food.png"),
            new LauncherIcon(R.drawable.fresh_food, "Thực phẩm tươi", "fresh_food.jpg"),
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);

        dashboard_grid.setAdapter(new GridIconAdapter(getContext()));

        return view;
    }

    @OnItemClick(R.id.dashboard_grid)
    public void onClickGridItem(AdapterView<?> parent, View v, int position, long id){
        SharedPreferences pre=this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    static class GridIconAdapter extends BaseAdapter {
        private Context mContext;

        public GridIconAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int i) {
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                v = vi.inflate(R.layout.dashboard_icon, null);
                holder = new ViewHolder();
                holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
                holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.icon.setImageResource(ICONS[i].imgId);
            holder.text.setText(ICONS[i].text);
            return v;
        }

        static class ViewHolder {
            public ImageView icon;
            public TextView text;
        }
    }
    static class LauncherIcon {
        final String text;
        final int imgId;
        final String map;

        public LauncherIcon(int imgId, String text, String map) {
            super();
            this.imgId = imgId;
            this.text = text;
            this.map = map;
        }

    }
}
