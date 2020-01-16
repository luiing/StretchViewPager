package com.example.stretch.demo.frag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stretch.demo.R;

/**
 * @author uis 2018/7/21
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.IconHolder> {
    static int[] urlIcon = new int[]{R.mipmap.s2001,R.mipmap.s2002,R.mipmap.s2003,R.mipmap.s2004};
    static int[] urlImage = new int[]{R.mipmap.p1001,R.mipmap.p1002,R.mipmap.p1003,R.mipmap.p1004};

    private int first;
    private boolean icon;

    public RecyclerAdapter(int index,boolean icon) {
        this.first = index;
        this.icon = icon;
    }

    @Override
    public IconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IconHolder(icon,first,parent);
    }

    @Override
    public void onBindViewHolder(IconHolder holder, int position) {
        holder.binder(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class IconHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        int first;
        boolean icon;

        public IconHolder(boolean icon,int first,ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(icon ? R.layout.item_icon:R.layout.item_image,parent,false));
            ivIcon = itemView.findViewById(R.id.iv_icon);
            this.first = first;
            this.icon = icon;
        }

        void binder(int position){
            int pos = (first+position)%4;
            ivIcon.setImageResource(icon ? urlIcon[pos] : urlImage[pos]);
        }
    }
}
