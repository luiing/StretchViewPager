package com.example.stretch.demo.frag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stretch.demo.R;

/**
 * @author uis 2018/7/21
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TextHolder> {
    static int[] urls = new int[]{R.mipmap.s2001,R.mipmap.s2002,R.mipmap.s2003,R.mipmap.s2004};
    public RecyclerAdapter() {
    }

    @Override
    public RecyclerAdapter.TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.TextHolder holder, int position) {
        holder.binder(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class TextHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        public TextHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon,parent,false));
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }

        void binder(int position){
            ivIcon.setImageResource(urls[position%4]);
        }
    }
}
