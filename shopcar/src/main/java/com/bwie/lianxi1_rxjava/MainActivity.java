package com.bwie.lianxi1_rxjava;

import android.database.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox quanxuan;
    private TextView zongjia,zongshuliang;
    private  int zongPrice;
    private int zongNum;
    private List<ShopBean1.DatasBean.CartListBean.GoodsBean> goods;
    private MyRecycleAdapter myRecycleAdapter;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());

        //找到控件
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        quanxuan = (CheckBox) findViewById(R.id.quanxuan);
        zongjia = (TextView) findViewById(R.id.zongjia);
        zongshuliang = (TextView) findViewById(R.id.zongshuliang);
        //网络请求
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("key", "c60d5d406d7e9d8dbfb7566c6a725e91")
                .add("goods_id", "100008")
                .add("quantity", "1")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.23.23/mobile/index.php?act=member_cart&op=cart_list")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //解析网络请求
                final String result = response.body().string();
                Gson gson = new Gson();
                ShopBean1 shopBean1 = gson.fromJson(result, ShopBean1.class);
                goods = shopBean1.getDatas().getCart_list().get(0).getGoods();
                //因为从网络获取的数量不一样，所以都默认为数量是1
                for (int i = 0; i < goods.size(); i++) {
                    goods.get(i).setGoods_num(1+"");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配
                        myRecycleAdapter = new MyRecycleAdapter(MainActivity.this, goods);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
                        recyclerView.setAdapter(myRecycleAdapter);
                        //适配器的接口回调
                        myRecycleAdapter.setIsnoSelect(new MyRecycleAdapter.IsnoSelect() {
                            @Override
                            public void setIsno(boolean isno, int position) {
                                zongPrice = 0;
                                zongNum = 0;
                                //遍历集合
                                for (int i = 0; i < goods.size(); i++) {
                                    //得到选中商品
                                    if(goods.get(i).isSelect() == true){
                                        String goods_price = goods.get(i).getGoods_price();
                                        String getGoods_num = goods.get(i).getGoods_num();
                                        double price = Double.parseDouble(goods_price);
                                        num = Integer.parseInt(getGoods_num);
                                        zongPrice += ((int)price)* num;
                                        zongNum += num;
                                    }else {
                                        //如果有一个商品没有全中，全选为false
                                        quanxuan.setChecked(false);
                                    }
                                }
                                //将计算完的总价和数量赋值给控件
                                zongshuliang.setText(zongNum+"");
                                zongjia.setText(zongPrice+"");
                            }
                        });
                    }
                });
            }
        });
        //全选按钮的监听事件
        quanxuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = quanxuan.isChecked();
                zongPrice = 0;
                zongNum = 0;
                //遍历集合改变所有对象的选中状态
                for (int i = 0; i < goods.size(); i++) {
                    goods.get(i).setSelect(checked);
                    //再次计算所有的价格和数量
                    if(checked){
                        String goods_price = goods.get(i).getGoods_price();
                        String getGoods_num = goods.get(i).getGoods_num();
                        double price = Double.parseDouble(goods_price);
                        num = Integer.parseInt(getGoods_num);
                        System.out.println("第"+i+"个"+num);
                        zongPrice += ((int)price)* num;
                        zongNum += num;
                    }
                }
                //将计算完的价格和数量赋值给控件
                zongshuliang.setText(zongNum+"");
                zongjia.setText(zongPrice+"");
                myRecycleAdapter.notifyDataSetChanged();
            }
        });
    }
}
