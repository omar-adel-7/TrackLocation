package com.general.DialogOrPopUpList;


public class MyListModel {
    private String text = "";
    private int icon = 0;

    public MyListModel(String text) {
        this.text = text;
    }

    public MyListModel(int icon) {
        this.icon = icon;
    }

    public MyListModel(String text, int icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
