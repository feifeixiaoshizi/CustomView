package com.study.customview.customview.coordinatorlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.customview.customview.R;

import java.util.List;

/**
 * Created by jianshengli on 2018/6/9.
 */

public class Adater extends RecyclerView.Adapter<Adater.MyViewHolder> {
    private List<String> datas;
    private Context context;

    public Adater(List<String> datas,Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_view,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


     public class  MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv;
         public MyViewHolder(View itemView) {
             super(itemView);
             tv=itemView.findViewById(R.id.tv);
         }
     }



}
