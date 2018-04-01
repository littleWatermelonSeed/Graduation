package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/7/11.
 */

public class Folder implements Serializable{

    private List<Image> images;
    private String name;

    public Folder() {
    }

    public Folder(String name) {
        this.name = name;
    }

    public Folder(List<Image> images, String name) {
        this.images = images;
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addImage(Image image){
        if(image != null && image.getPath() != null){
            if(images == null){
                images = new ArrayList<Image>();
            }
            images.add(image);
        }
    }

}
