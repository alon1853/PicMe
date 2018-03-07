package com.me.plan.picme.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alon on 05/03/2018.
 */

public class PictureFirebase {
    public interface Callback<T> {
        void onComplete(T data);
    }

    public static void getAllPicturesAndObserve(long lastUpdate, final Callback<List<Picture>> callback) {
        Log.d("TAG", "getAllPicturesAndObserve " + lastUpdate);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pictures");
        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Picture> list = new LinkedList<Picture>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Picture picture = snap.getValue(Picture.class);
                    list.add(picture);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }

    public static void addPicture(Picture picture){
        Log.d("TAG", "add picture to Firebase");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pictures");
        String key = myRef.push().getKey();
        picture.setId(key);

        HashMap<String, Object> json = picture.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);

        myRef.child(picture.id).setValue(json);
    }
}
