package com.me.plan.picme.Model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Alon on 23/02/2018.
 */

public class ModelFirebase {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private FirebaseDatabase database;


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

    public void createUserWithEmailAndPassword(final String fullName, String email, String password, final RegisterInterface registerInterface) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Register success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            currentUser = mAuth.getCurrentUser();

                            // Set current Full Name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName).build();
                            currentUser.updateProfile(profileUpdates);

                            registerInterface.AfterSuccessfulRegister();
                        } else {
                            // If register fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            registerInterface.AfterFailedRegister();
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

    public void UploadImage(Bitmap bitmap, String imageName, final UploadImageInterface uploadImageInterface) {
        Log.d("TAG", "Uploading image..");
        StorageReference imageRef = storage.getReference().child(imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("TAG", "Failed to upload image: " + exception.getMessage());
                uploadImageInterface.AfterFailedUploadImage();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadImageInterface.AfterSuccessfulUploadImage();
            }
        });
    }

    public void getPicture(String pictureId, final GetPictureInterface getPictureInterface) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pictures");
        myRef.child(pictureId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picture picture = dataSnapshot.getValue(Picture.class);
                getPictureInterface.AfterSuccessfulGetPicture(picture);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getPictureInterface.AfterFailGetPicture();
            }
        });
    }

    public interface SignInInterface {
        void AfterSuccessfulSignIn();
        void AfterFailedSignIn();
    }

    public interface RegisterInterface {
        void AfterSuccessfulRegister();
        void AfterFailedRegister();
    }

    public interface LoadImageInterface {
        void afterSuccessfulImageLoad(byte[] bytes);
    }

    public interface UploadImageInterface {
        void AfterSuccessfulUploadImage();
        void AfterFailedUploadImage();
    }

    public interface GetPictureInterface {
        void AfterSuccessfulGetPicture(Picture picture);
        void AfterFailGetPicture();
    }
}
