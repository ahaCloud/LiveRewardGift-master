package com.example.yuhengyi.liverewardgift;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by yuhengyi on 2016/11/22.
 * Describe: 动画的管理类   小礼物
 */

public class LPAnimationManager {
    /**
     * 礼物飞出的动画
     */
    private static Animation mGiftLayoutOutAnim;

    private static Context mContext;

    /**
     * 礼物定时器  用于清除和从礼物队列中取礼物
     */
    private static Timer mGiftClearTimer;
    /**
     * 礼物队列
     */
    private static ArrayList<AnimMessage> mGiftList = new ArrayList<>();
    /**
     * 礼物容器 目前只是小礼物的容器
     */
    private static LinearLayout mAnimViewContainer;
    /**
     * 礼物定时器执行间隔
     */
    private static final int mGiftClearTimerInterval = 1500;
    /**
     * 礼物无更新后的存在时间
     */
    private static final int mGiftClearInterval = 3000;
    /**
     * 同时存在的最大礼物数目
     */
    private static final int mGiftMaxNumber = 3;

    /**
     * init 动画 和 context
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        mGiftLayoutOutAnim = AnimationUtils.loadAnimation(context, R.anim.lp_gift_out);
    }

    /**
     * 添加礼物container layout
     *
     * @param container
     * @return 添加成功或失败
     */
    public static boolean addGiftContainer(LinearLayout container) {
        if (container == null || container.getOrientation() == LinearLayout.HORIZONTAL) {
            return false;
        }
        mAnimViewContainer = container;
        return true;
    }

    /**
     * 将动画信息添加到动画队列
     *
     * @param message
     */
    public static void addAnimalMessage(AnimMessage message) {
        if (message != null) {
            mGiftList.add(message);
            if (mGiftClearTimer == null && mAnimViewContainer != null && mContext != null) {
                startTimer();
            }
        }
    }

    /**
     * 添加动画view
     */
    private static View addAnimalView(AnimMessage message) {
        View view = new LPGiftView(mContext, message);
        return view;
    }

    /**
     * 删除动画view
     */
    private static void removeAnimalView(final int index) {
        if (index >= mAnimViewContainer.getChildCount()) {
            return;
        }
        final View removeView = mAnimViewContainer.getChildAt(index);
        mGiftLayoutOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAnimViewContainer.removeViewAt(index);
                    }
                });

                if (mGiftList.size() == 0 && mAnimViewContainer.getChildCount() == 0 && mGiftClearTimer != null) {
                    mGiftClearTimer.cancel();
                    mGiftClearTimer = null;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(mGiftLayoutOutAnim);
            }
        });
    }

    /**
     * 定时清除礼物
     */
    private static void startTimer() {
        mGiftClearTimer = new Timer();
        mGiftClearTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        final int count = mAnimViewContainer.getChildCount();
                        // 清除礼物
                        for (int i = 0; i < count; i++) {
                            View view = mAnimViewContainer.getChildAt(i);
                            AnimMessage message = (AnimMessage) view.getTag();
                            long nowTime = System.currentTimeMillis();
                            long upTime = message.getUpdateTime();
                            if ((nowTime - upTime) >= mGiftClearInterval) {
                                removeAnimalView(i);
                                return;
                            }
                        }
                        // 添加礼物
                        if (count < mGiftMaxNumber) {
                            if (mGiftList.size() > 0) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startGiftAnim(mGiftList.get(0));
                                        mGiftList.remove(0);
                                    }
                                });
                            }
                        }
                    }
                }, 0, mGiftClearTimerInterval
        );
    }

    /**
     * 根据message寻找view
     *
     * @param message
     * @return
     */
    private static View findViewByMessage(AnimMessage message) {
        for (int i = 0; i < mAnimViewContainer.getChildCount(); i++) {
            AnimMessage giftMessage = (AnimMessage) mAnimViewContainer.getChildAt(i).getTag();
            if (giftMessage.getUserName().equals(message.getUserName()) &&
                    giftMessage.getGiftName().equals(message.getGiftName())) {
                return mAnimViewContainer.getChildAt(i);
            }
        }
        return null;
    }

    /**
     * 显示礼物的方法
     */
    private static void startGiftAnim(final AnimMessage giftMessage) {
        View giftView = findViewByMessage(giftMessage);
        if (giftView == null) {//该用户不在礼物显示列表 或者又送了一个新的礼物
            giftView = addAnimalView(giftMessage);
            mAnimViewContainer.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            mAnimViewContainer.invalidate();
        } else {
            //该用户在礼物显示列表  1. 连击动画还未结束，只更新message即可
            final AnimMessage message = (AnimMessage) giftView.getTag();// 原来的礼物view的信息
            message.setGiftNum(message.getGiftNum() + giftMessage.getGiftNum()); // 合并追送的礼物数量
            giftView.setTag(message);
            if (message.isComboAnimationOver()) {
                // 2.连击动画已完成 此时view 未消失，除了1 的操作外，还需重新启动连击动画
                final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);
                giftNum.setText("x" + giftNum.getTag());
                ((LPGiftView) giftView).startComboAnim(giftNum);
            }
        }
    }

    /**
     * 释放资源，必须调用。
     */
    public static void release() {
        if (mGiftClearTimer != null) {
            mGiftClearTimer.cancel();
            mGiftClearTimer = null;
        }
        mGiftList.clear();
        mAnimViewContainer.removeAllViews();
        mGiftLayoutOutAnim = null;
        mContext = null;
    }
}
