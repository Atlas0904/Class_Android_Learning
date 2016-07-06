package com.as.atlas.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final String URL_FIREBASE_DEMO = "https://test-51e42.firebaseio.com/";
    public final String CLASS_MESSAGE_FOR_FIREBASE = "message";
    public final String TAG = "Atlas";
    Firebase firebase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1);
        listView.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        new Firebase(URL_FIREBASE_DEMO).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded();");
//                adapter.add(
//                        (String) dataSnapshot.child("name").getValue());

                Log.d(TAG, "dataSnapshot: getChildrenCount:" + dataSnapshot.getChildrenCount());
                Log.d(TAG, "dataSnapshot1:" + dataSnapshot.getValue());
                Log.d(TAG, "dataSnapshot2:" + dataSnapshot.child("/contacts/3/name").getValue());
                Log.d(TAG, "dataSnapshot3:" + dataSnapshot.child("contacts/3/name").getValue());
                Log.d(TAG, "dataSnapshot4:" + dataSnapshot.child("/contacts/name").getValue());
                Log.d(TAG, "dataSnapshot5:" + dataSnapshot.child("contacts/name").getValue());
                Log.d(TAG, "dataSnapshot6:" + dataSnapshot.child("3/name").getValue());
                Log.d(TAG, "dataSnapshot7:" + dataSnapshot.child("/3/name").getValue());

                Log.d(TAG, "dataSnapshot8:" + dataSnapshot.child("name").getValue());

                ArrayList<String> array = (ArrayList<String>) dataSnapshot.getValue();
                for (int i = 0; i < array.size(); i++) {
                    Log.d(TAG, "name =" + String.valueOf(array.get(i)));
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.remove(
                        (String) dataSnapshot.child("name").getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        initFirebase();
//        readFirebase();
//        writeFirebase();
    }

//    private void readFirebase() {
//        firebase.child(CLASS_MESSAGE_FOR_FIREBASE).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange=" + dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//    }

//    private void writeFirebase() {
//        firebase.child(CLASS_MESSAGE_FOR_FIREBASE).setValue("This is my first firebase demo");
//    }
//
//    private void initFirebase() {
//        firebase = new Firebase(URL_FIREBASE_DEMO);
//
//        firebase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                adapter.add((String)dataSnapshot.child("name").getValue());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                adapter.remove((String)dataSnapshot.child("name").getValue());
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
}
