package com.example.yuhengyi.liverewardgift;

/**
 * Created by yuhengyi on 2016/11/9.
 * Describe:
 */

public class AnimMessage {

    private String giftType = "";
    private String cfm;
    private String userName;
    private int giftDesc;
    private String headUrl;
    private int userId;
    private int giftNum;
    private boolean isComboAnimationOver;
    private long updateTime;
    private String couponName;
    private String giftName;
    private String giftTitle;
    public AnimMessage(){}
    public AnimMessage(String userName,  String headUrl, int giftNum, String giftname){
        this.userName = userName;
        this.giftNum = giftNum;
        this.giftDesc = giftDesc;
        this.giftName = giftname;
    }

    public String getGiftType() {
        return giftType;
    }

    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }

    public String getCfm() {
        return cfm;
    }

    public void setCfm(String cfm) {
        this.cfm = cfm;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getGiftDesc() {
        return giftDesc;
    }

    public void setGiftDesc(int giftDesc) {
        this.giftDesc = giftDesc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public boolean isComboAnimationOver() {
        return isComboAnimationOver;
    }

    public void setComboAnimationOver(boolean comboAnimationOver) {
        isComboAnimationOver = comboAnimationOver;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }


    public String getGiftTitle() {
        return giftTitle;
    }

    public void setGiftTitle(String giftTitle) {
        this.giftTitle = giftTitle;
    }
}
