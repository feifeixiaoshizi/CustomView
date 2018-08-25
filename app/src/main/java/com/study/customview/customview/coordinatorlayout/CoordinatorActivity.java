package com.study.customview.customview.coordinatorlayout;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.study.customview.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianshengli on 2018/6/9.
 */

public class CoordinatorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> datas;
    private Adater adater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_content);
        recyclerView = findViewById(R.id.recyclerView);
        initView();

    }


    private void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datas= new ArrayList<>();
        for(int i=0;i<100;i++){
            datas.add(""+i);
        }
        adater = new Adater(datas,this);
        recyclerView.setAdapter(adater);


    }
}
