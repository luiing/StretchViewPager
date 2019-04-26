package com.example.stretch.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stretch.demo.R;

/**
 * @author uis 2018/7/21
 */
public class Frag extends Fragment implements View.OnClickListener{

    static int[] urls = new int[]{R.mipmap.p1001,R.mipmap.p1002,R.mipmap.p1003,R.mipmap.p1004};
    RecyclerView recyclerView;
    ImageView ivIcon;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_icon,container,false);
        recyclerView = v.findViewById(R.id.recycler_view);
        ivIcon = v.findViewById(R.id.iv_icon);

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new RecyclerAdapter());
        int id = getArguments().getInt("id",0);
        ivIcon.setImageResource(urls[id]);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof View.OnClickListener){
                    ((View.OnClickListener) getActivity()).onClick(v);
                }
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),"click...",Toast.LENGTH_SHORT).show();
    }
}
