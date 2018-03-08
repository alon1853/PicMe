package com.me.plan.picme.Model;

import android.graphics.Bitmap;

/**
 * Created by Alon on 23/02/2018.
 */

public class Model {
    public static final Model instance = new Model();

    private ModelFirebase modelFirebase;

    private Model() {
        modelFirebase = new ModelFirebase();
    }

    public void SignInWithEmailAndPassword(String email, String password, ModelFirebase.SignInInterface signInInterface) {
        modelFirebase.SignInWithEmailAndPassword(email, password, signInInterface);
    }
    public void createUserWithEmailAndPassword(String fullName, String email, String password, ModelFirebase.RegisterInterface registerInterface) {
        modelFirebase.createUserWithEmailAndPassword(fullName, email, password, registerInterface);
    }

    public User getCurrentUser() {
        return this.modelFirebase.getCurrentUser();
    }

    public void LoadImage(String imagePath, final ModelFirebase.LoadImageInterface loadImageInterface) {
        modelFirebase.LoadImage(imagePath, loadImageInterface);
    }

    public void UploadImage(Bitmap bitmap, String imageName, ModelFirebase.UploadImageInterface uploadImageInterface) {
        modelFirebase.UploadImage(bitmap, imageName, uploadImageInterface);
    }

    public void getPicture(String pictureId, ModelFirebase.GetPictureInterface getPictureInterface) {
        modelFirebase.getPicture(pictureId, getPictureInterface);
    }
}
