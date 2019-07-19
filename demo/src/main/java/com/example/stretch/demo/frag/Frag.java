package com.example.stretch.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stretch.demo.R;

/**
 * @author uis 2018/7/21
 */
public class Frag extends Fragment{


    RecyclerView recycler_view,recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_icon,container,false);
        recycler_view = v.findViewById(R.id.recycler_view);
        recyclerView = v.findViewById(R.id.recyclerView);

        int id = getArguments().getInt("id",0);
        recycler_view.setLayoutManager(new LinearLayoutManager(container.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recycler_view.setAdapter(new RecyclerAdapter(id,true));
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new RecyclerAdapter(id,false));
        return v;
    }
}
