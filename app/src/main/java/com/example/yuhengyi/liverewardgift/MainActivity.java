package com.example.yuhengyi.liverewardgift;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements View.OnClickListener {
    private LinearLayout mGiftContainer;
    private Button mFirstGift;
    private Button mSecondGift;
    private Button mThirdGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LPAnimationManager.init(this);
        mGiftContainer = (LinearLayout) findViewById(R.id.ll_gift_container);
        LPAnimationManager.addGiftContainer(mGiftContainer);

        mFirstGift = (Button) findViewById(R.id.gift_1);
        mFirstGift.setOnClickListener(this);
        mSecondGift = (Button) findViewById(R.id.gift_2);
        mSecondGift.setOnClickListener(this);
        mThirdGift = (Button) findViewById(R.id.gift_3);
        mThirdGift.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mFirstGift)) {
            LPAnimationManager.addAnimalMessage(new AnimMessage("yu", "", 10, "飞机"));
        } else if (v.equals(mSecondGift)) {
            LPAnimationManager.addAnimalMessage(new AnimMessage("hellokitty", "", 10, "飞机"));
        } else if (v.equals(mThirdGift)) {
            LPAnimationManager.addAnimalMessage(new AnimMessage("我是小学生", "", 20, "红包"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LPAnimationManager.release();
    }
}
