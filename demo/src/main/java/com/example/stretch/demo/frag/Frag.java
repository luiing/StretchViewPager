package com.example.stretch.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stretch.demo.R;

/**
 * @author uis 2018/7/21
 */
public class Frag extends Fragment implements View.OnClickListener{

    RecyclerView recyclerView;
    RecyclerView recyclerViewVer;
    TextView tvTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_frag,container,false);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerViewVer = v.findViewById(R.id.recyclerview);
        tvTxt = v.findViewById(R.id.tv_txt);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new RecyclerAdapter());
        recyclerViewVer.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerViewVer.setAdapter(new RecyclerAdapter());
        tvTxt.setText(getArguments().getString("id"," none "));
        tvTxt.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),"click...",Toast.LENGTH_SHORT).show();
    }
}
