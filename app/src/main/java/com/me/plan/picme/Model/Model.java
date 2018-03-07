package com.me.plan.picme.Model;

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
}
