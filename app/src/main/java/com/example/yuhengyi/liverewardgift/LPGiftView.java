package com.example.yuhengyi.liverewardgift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by yuhengyi on 2017/1/10.
 */

public class LPGiftView extends RelativeLayout {
    /**
     * 礼物飞进的动画
     */
    private static TranslateAnimation mGiftLayoutInAnim;
    /**
     * icon缩放的动画
     */
    private static ScaleAnimation mIconScaleAnim;
    /**
     * 用户名飞进的动画
     */
    private static TranslateAnimation mUserNameIn;
    /**
     * 礼物名飞进的动画
     */
    private static TranslateAnimation mGiftNameIn;

    private AnimMessage mAnimMessage;

    public LPGiftView(Context context, AnimMessage message) {
        super(context);
        mAnimMessage = message;
        init();
    }

    public LPGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LPGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private  void init(){

        mGiftLayoutInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.lp_gift_in);
        mIconScaleAnim = (ScaleAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.lp_icon_scale);
        mUserNameIn = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.lp_username_in);
        mGiftNameIn = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.lp_giftname_in);

        // 外层是线性布局
        LayoutInflater.from(getContext()).inflate(R.layout.lp_item_gift_animal, this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        final MagicTextView giftNumView = (MagicTextView) findViewById(R.id.giftNum);
        final TextView sendUser = (TextView) findViewById(R.id.send_user);
        final TextView giftName = (TextView) findViewById(R.id.gift_name);
        final ImageView giftIcon = (ImageView) findViewById(R.id.ivgift);
        final RelativeLayout giftTextContainerLayout = (RelativeLayout) findViewById(R.id.rlparent);
        ImageView userIcon = (ImageView) findViewById(R.id.user_icon);
        giftName.setText(String.format(getContext().getResources().getString(R.string.gift_tip),mAnimMessage.getGiftName()));
        sendUser.setText(mAnimMessage.getUserName());
        giftIcon.setImageResource(R.mipmap.live_red_packet);

        userIcon.startAnimation(mIconScaleAnim);
        giftNumView.setTag(1);/*给数量控件设置标记*/
        mAnimMessage.setUpdateTime(System.currentTimeMillis());/*设置时间标记*/
        setTag(mAnimMessage);/*设置view标识*/
        mIconScaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendUser.setVisibility(View.VISIBLE);
                giftName.setVisibility(View.VISIBLE);
                giftTextContainerLayout.setVisibility(View.VISIBLE);
                giftTextContainerLayout.startAnimation(mGiftLayoutInAnim);//开始执行显示礼物的动画
                sendUser.startAnimation(mUserNameIn);
                giftName.startAnimation(mGiftNameIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mGiftLayoutInAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                giftNumView.setVisibility(View.VISIBLE);
                giftIcon.setVisibility(View.VISIBLE);
                giftNumView.setText("x" + giftNumView.getTag());
                startComboAnim(giftNumView);// 设置一开始的连击事件
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }



    /**
     * 连击动画
     *
     * @param giftNumView
     * @param
     */
    public  void startComboAnim(final View giftNumView) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(giftNumView, "scaleX", 1.8f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(giftNumView, "scaleY", 1.8f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(anim1, anim2);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((AnimMessage) getTag()).setUpdateTime(System.currentTimeMillis());//设置时间标记
                giftNumView.setTag((Integer) giftNumView.getTag() + 1);
                //这里用((GiftMessage)giftView.getTag()) 来实时的获取GiftMessage  便于礼物的追加
                if ((Integer) giftNumView.getTag() <= ((AnimMessage) getTag()).getGiftNum()) {
                    ((MagicTextView) giftNumView).setText("x" + giftNumView.getTag());
                    startComboAnim(giftNumView);
                } else {
                    ((AnimMessage)getTag()).setComboAnimationOver(true);
                    return;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
