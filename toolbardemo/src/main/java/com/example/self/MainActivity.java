package com.example.self;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyScrollView.OnScrollChangedListener {

    private Toolbar toolbar;
    private TextView tvTitle;
    private MyScrollView myScrollView;
    private ImageView ivPic;
    private LinearLayout llInner, llOuter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;

    private int searchLayoutTop;
    String[] strings = {"麦迪", "哈达威", "奥拉朱旺", "乔丹", "欧文",
            "科比", "艾佛森", "囧墙", "韦少", "大帝",
            "席梦思", "皮尔斯", "阿伦", "卡哇伊", "大鸟",
            "伯伦", "保罗", "罗宾逊", "罗德曼", "尤因"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initToolbar();
        initData();
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tvTitle = (TextView) findViewById(R.id.title);

        myScrollView = (MyScrollView) findViewById(R.id.myscrollview);

        ivPic = (ImageView) findViewById(R.id.iv_pic);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview1);

        llOuter = (LinearLayout) findViewById(R.id.ll_outer);

        llInner = (LinearLayout) findViewById(R.id.ll_inner);
    }

    private void initListener() {
        myScrollView.setOnScrollChangedListener(this);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        toolbar.getBackground().setAlpha(0);  //先设置透明
        tvTitle.setTextColor(Color.argb(0, 255, 255, 255));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设为 false
            actionBar.setDisplayShowTitleEnabled(true);  //是否隐藏标题
            actionBar.setDisplayHomeAsUpEnabled(true);     //是否显示返回按钮
        }
        //实现透明状态栏效果  并且toolbar 需要设置  android:fitsSystemWindows="true"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }
    }

    private void initData() {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(strings[i]);
        }
        MyGridLayoutManager manager = new MyGridLayoutManager(this, 2);
        //防止卡顿
        manager.setScrollEnabled(false);
        MyAdapter adapter = new MyAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        adapter.setmOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(MainActivity.this, list.get(position), Toast.LENGTH_LONG).show();
            }
        });


        final List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list1.add("标签" + i);
        }
        MyLinearLayoutManager manager1 = new MyLinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        //防止卡顿
        manager1.setScrollEnabled(false);
        TagAdapter adapter1 = new TagAdapter(this, list1);
        recyclerView1.setAdapter(adapter1);
        recyclerView1.setLayoutManager(manager1);
        adapter1.setmOnItemClickListener(new TagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(MainActivity.this, list1.get(position), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            /**
             * 65是toolbar的高度
             * 悬浮位置在toolbar下边，不减的话在顶部
             */
            searchLayoutTop = ivPic.getBottom() - dp2px(70);//获取searchLayout的顶部位置
        }
    }

    @Override
    public void onScrollChanged(ScrollView who, int width, int t, int oldWidth, int oldHeight) {
        float height = ivPic.getHeight();  //获取图片的高度
        /**
         * toolbar 渐变背景 偏移量控制
         */
        if (oldHeight < height) {
            int i = Float.valueOf(oldHeight / height * 255).intValue();    //i 有可能小于 0
            toolbar.getBackground().setAlpha(Math.max(i, 0));   // 0~255 透明度
            /**
             * 字体颜色透明度控制
             */
            tvTitle.setTextColor(Color.argb(i, 255, 255, 255));
        } else {
            toolbar.getBackground().setAlpha(255);
            tvTitle.setTextColor(Color.argb(255, 255, 255, 255));
        }

        /**
         * 悬浮控制
         */
        if (oldHeight >= searchLayoutTop) {
            if (recyclerView1.getParent() != llOuter) {
                llInner.removeView(recyclerView1);
                llOuter.removeAllViews();
                llOuter.addView(recyclerView1);
            }
        } else {
            if (recyclerView1.getParent() != llInner) {
                llOuter.removeView(recyclerView1);
                llInner.removeAllViews();
                llInner.addView(recyclerView1);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toolbar上的左上角的返回箭头的键值为Android.R.id.home  不是R.id.home
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
