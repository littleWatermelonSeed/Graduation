package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect;

import android.content.Context;
import android.view.View;
import com.sayhellototheworld.littlewatermelon.graduation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/7/14.
 */

public class PictureDataManager {

    private static List<Folder> folderList;
    private static List<Image> callbackImageList;
    private List<Integer> choosedID;
    private boolean isCallback = false;
    private static PictureDataManager picManager;

    private PictureDataManager() {
        initCallbackImageList();
        choosedID = new ArrayList<>();
    }

    public static synchronized PictureDataManager getManagerInstance() {
        if (picManager == null) {
            picManager = new PictureDataManager();
        }
        return picManager;
    }

    private void initCallbackImageList() {
        callbackImageList = new ArrayList<>();
    }

    public List<Image> getImageList(int position) {
        return folderList.get(position).getImages();
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<Folder> folderList) {
        PictureDataManager.folderList = folderList;
    }

    public List<Image> getCallbackImageList() {
        if (isCallback) {
            return callbackImageList;
        } else {
            return null;
        }
    }

    public void setCallbackImageList(List<Image> callbackImageList) {
        PictureDataManager.callbackImageList = callbackImageList;
    }

    public boolean isCallback() {
        return isCallback;
    }

    public void setCallback(boolean callback) {
        isCallback = callback;
    }

    public void addChoosedID(int id){
        choosedID.add(id);
    }

    public void removeChoosedID(int id){
        for(Integer integer:choosedID){
            if(integer == id){
                choosedID.remove(integer);
                return;
            }
        }
    }

    public boolean isExistChoosedID(int id){
        for(Integer integer:choosedID){
            if(integer == id){
                return true;
            }
        }
        return false;
    }

    public int getCallbackImageSize(){
        return callbackImageList.size();
    }

    public void addCallbackImage(Image image) {
        callbackImageList.add(image);
//        Log.i("niyuanjie","add Image后的长度为：" + callbackImageList.size());
    }

    public void removeCallbackImage(Image image) {
        callbackImageList.remove(image);
//        Log.i("niyuanjie","remove Image后的长度为：" + callbackImageList.size());
    }

    public void removeCallbackImage(int id) {
        for (Image image : callbackImageList) {
            if (image.getID() == id) {
                callbackImageList.remove(image);
//                Log.i("niyuanjie","remove Image后的长度为：" + callbackImageList.size());
                return;
            }
        }
    }

    public void disSelectImage(int folderPosition, int imagesPosition) {
        folderList.get(folderPosition).getImages().get(imagesPosition).setChooseID(-1);
        folderList.get(folderPosition).getImages().get(imagesPosition).setSelect(false);
    }

    public void selectImage(int folderPosition, int imagesPosition, int selectID) {
        folderList.get(folderPosition).getImages().get(imagesPosition).setChooseID(selectID);
        folderList.get(folderPosition).getImages().get(imagesPosition).setSelect(true);
    }

    public void changeImageChoosedID(GridAdapter adapter,int removeChooseID) {
        for (Image image:callbackImageList) {
            if (image.getChooseID() > removeChooseID) {
                image.setChooseID(image.getChooseID() - 1);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void doImageSelect(Context context,GridAdapter.ViewHolder viewHolder, int id){
        viewHolder.imageView_masking.setVisibility(View.VISIBLE);
        viewHolder.imageView_masking.setImageAlpha(150);
        viewHolder.relativeLayout_chooseLayout.setBackground(context.getResources().getDrawable(R.drawable.solidcircle));
        viewHolder.textView.setText(folderList.get(0).getImages().get(id).getChooseID() + "");
//        Log.i("niyuanjie","image select choosedID " + folderList.get(0).getImages().get(id).getChooseID() + "");
    }

    public void doImageDisSelect(Context context,GridAdapter.ViewHolder viewHolder){
        viewHolder.imageView_masking.setVisibility(View.GONE);
        viewHolder.relativeLayout_chooseLayout.setBackground(context.getResources().getDrawable(R.drawable.choosecircle));
        viewHolder.textView.setText("");
    }

    public void clear() {
        if (picManager != null) {
            if (folderList != null){
                folderList.clear();
            }
            if (callbackImageList != null){
                callbackImageList.clear();
            }
            if (picManager != null){
                picManager = null;
            }
            GridAdapter.initChoosed();
        }
    }

}
