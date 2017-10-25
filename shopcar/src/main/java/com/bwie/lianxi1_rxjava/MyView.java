package com.bwie.lianxi1_rxjava;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyView extends LinearLayout implements View.OnClickListener {

    private Button jian;
    private TextView cotent;
    private Button jia;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        View view = View.inflate(context, R.layout.my_view, this);
        jian = (Button) view.findViewById(R.id.jian);
        cotent = (TextView) view.findViewById(R.id.cotent);
        jia = (Button) view.findViewById(R.id.jia);
        jian.setOnClickListener(this);
        jia.setOnClickListener(this);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jian:
                String numResult = cotent.getText().toString().trim();
                Integer intNumResult = Integer.valueOf(numResult);
                if(intNumResult>1){
                    intNumResult-=1;
                }
                cotent.setText(intNumResult+"");
                //回调
                onGetContent.getContentNum(intNumResult);
                break;
            case R.id.jia:
                String numResult2 = cotent.getText().toString().trim();
                Integer intNumResult2 = Integer.valueOf(numResult2);
                if(intNumResult2<6){
                    intNumResult2+=1;
                }
                //回调
                cotent.setText(intNumResult2+"");
                onGetContent.getContentNum(intNumResult2);
                break;
        }
    }
    //接口回调把数量传过去
    public OnGetContent onGetContent;
    public interface OnGetContent{
        void getContentNum(int content);
    }
    public void getNum(OnGetContent onGetContent){
        this.onGetContent = onGetContent;
    }
}
