package com.example.stretch.demo.frag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stretch.demo.R;

/**
 * @author uis 2018/7/21
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TextHolder> {
    public RecyclerAdapter() {
    }

    @Override
    public RecyclerAdapter.TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.TextHolder holder, int position) {
        holder.binder("postion = " + position);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    static class TextHolder extends RecyclerView.ViewHolder{
        TextView tvText;
        public TextHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text,parent,false));
            tvText = itemView.findViewById(R.id.tv_txt);
        }

        void binder(String txt){
            tvText.setText(txt);
        }
    }
}
