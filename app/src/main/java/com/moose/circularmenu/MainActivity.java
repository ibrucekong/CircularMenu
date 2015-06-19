package com.moose.circularmenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.moose.com.moose.circularmenu.R;


public class MainActivity extends AppCompatActivity implements CircleMenu.MenuCallBack, View.OnTouchListener {
    private CircleMenu circleMenu;
    private Context mContext;
    private int downX;
    private int downY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.addMenuCallBack(this);
        circleMenu.setOnTouchListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickMenu(CircleMenu.Menu menu) {
        switch (menu) {
            case ACCOUNT:
                Toast.makeText(mContext, "个人中心", Toast.LENGTH_SHORT).show();
                break;
            case APP_INFO:
                Toast.makeText(mContext, "版本信息", Toast.LENGTH_SHORT).show();
                break;
            case KEFU:
                Toast.makeText(mContext, "产能客服", Toast.LENGTH_SHORT).show();
                break;
            case MY_ORDER:
                Toast.makeText(mContext, "我的订单", Toast.LENGTH_SHORT).show();
                break;
            case NEW_ORDER:
                Toast.makeText(mContext, "新建询价单", Toast.LENGTH_SHORT).show();
                break;
            case SEND_ORDER:
                Toast.makeText(mContext, "发起生产订单", Toast.LENGTH_SHORT).show();
                break;
            case SET:
                Toast.makeText(mContext, "设置", Toast.LENGTH_SHORT).show();
                break;
            case WAIT_ORDER:
                Toast.makeText(mContext, "待支付订单", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                if (circleMenu.touchDisplay(downX, downY)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                circleMenu.setOffsetDegrees(moveX - downX);
                break;
            case MotionEvent.ACTION_UP:
                downX = 0;
                downY = 0;
                circleMenu.drawComplete();
                break;
            case MotionEvent.ACTION_CANCEL:
            default:
                break;


        }
        return true;
    }
}
