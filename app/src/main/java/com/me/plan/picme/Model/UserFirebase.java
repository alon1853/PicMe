package com.me.plan.picme.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Alon on 07/03/2018.
 */

public class UserFirebase {
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static void getFullNameCurrentUser(String userId, final LoadUserInfo loadUserInfo) {
        DatabaseReference mUserReference = mDatabase.child("users").child(userId).child("fullName");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.getValue(String.class);
                loadUserInfo.afterLoadUserInfo(fullName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "getFullNameCurrentUser:onCancelled", databaseError.toException());
            }
        };
        mUserReference.addValueEventListener(userListener);
    }

    public interface LoadUserInfo {
        void afterLoadUserInfo(String fullName);
    }
}
