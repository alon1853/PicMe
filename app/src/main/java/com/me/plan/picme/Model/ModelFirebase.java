package com.me.plan.picme.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Alon on 23/02/2018.
 */

public class ModelFirebase {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;


    public ModelFirebase() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance("gs://picme-8b715.appspot.com");
    }

    public User getCurrentUser() {
        if (currentUser != null) {
            User user = new User();
            user.email = currentUser.getEmail();
            user.name = currentUser.getDisplayName();

            return user;
        }

        return null;
    }

    public void SignInWithEmailAndPassword(String email, String password, final SignInInterface signInInterface) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            signInInterface.AfterSuccessfulSignIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            signInInterface.AfterFailedSignIn();
                        }
                    }
                });
    }

    public void LoadImage(String imagePath, final LoadImageInterface loadImageInterface) {
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(imagePath);

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                loadImageInterface.afterSuccessfulImageLoad(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public interface SignInInterface {
        void AfterSuccessfulSignIn();
        void AfterFailedSignIn();
    }

    public interface LoadImageInterface {
        void afterSuccessfulImageLoad(byte[] bytes);
    }
}
