package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect;

import java.io.Serializable;

/**
 * Created by 123 on 2017/7/11.
 */

public class Image implements Serializable{

    private String path;
    private String name;
    private long time;
    private boolean select = false;
    private int chooseID = -1;
    private int ID = -1;

    public Image(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getChooseID() {
        return chooseID;
    }

    public void setChooseID(int chooseID) {
        this.chooseID = chooseID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
