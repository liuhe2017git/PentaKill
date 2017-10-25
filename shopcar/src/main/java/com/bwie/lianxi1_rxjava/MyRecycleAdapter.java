package com.bwie.lianxi1_rxjava;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MyRecycleAdapter extends RecyclerView.Adapter {
    private final List<ShopBean1.DatasBean.CartListBean.GoodsBean> list;
    private final Context context;

    public MyRecycleAdapter(Context context, List<ShopBean1.DatasBean.CartListBean.GoodsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof  ViewHolder){
            //为控件赋值
            final ViewHolder viewHolder = (ViewHolder)holder;
            Picasso.with(context).load(list.get(position).getGoods_image_url()).into(viewHolder.pic);
            viewHolder.name.setText(list.get(position).getGoods_name());
            viewHolder.price.setText(list.get(position).getGoods_price());
            viewHolder.isno.setChecked(list.get(position).isSelect());
            //接口回调，得到自定义控件中的数量
            viewHolder.content.getNum(new MyView.OnGetContent() {
                @Override
                public void getContentNum(int content) {
                    //得到的数量赋值给bean对象
                    list.get(position).setGoods_num(content+"");
                    isnoSelect.setIsno(list.get(position).isSelect(),position);
                }
            });
            //选中按钮的监听事件
           viewHolder.isno.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //给bean对象做个标记
                   boolean select = list.get(position).isSelect();
                   list.get(position).setSelect(!select);
                   notifyDataSetChanged();
                   isnoSelect.setIsno(list.get(position).isSelect(),position);
               }
           });
        }
    }
    //接口回调
    private IsnoSelect isnoSelect;
    public interface IsnoSelect{
        void setIsno(boolean isno,int position);
    }
    public void setIsnoSelect(IsnoSelect isnoSelect){
        this.isnoSelect = isnoSelect;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name,price;
        MyView content;
        CheckBox isno;
        public ViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            content = (MyView) itemView.findViewById(R.id.content);
            isno = (CheckBox) itemView.findViewById(R.id.isno);
        }
    }
}
