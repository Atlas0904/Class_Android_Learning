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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_ATLAS = "Atlas";
    public final String URL_FIREBASE_DEMO = "https://test-51e42.firebaseio.com/";
    public final String CLASS_MESSAGE_FOR_FIREBASE = "message";
    public final String TAG = "Atlas";
    Firebase firebase;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1);
        listView.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(URL_FIREBASE_DEMO);

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded()");
                Log.d(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                Log.d(TAG, "dataSnapshot.child(name):" + dataSnapshot.child("name").getValue());
                Log.d(TAG, "dataSnapshot.child(name):" + dataSnapshot.child("0/name").getValue());
                Log.d(TAG, "dataSnapshot.getChildrenCount():" + dataSnapshot.getChildrenCount());

                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getValue() != null)  Log.d(TAG, "child index:" + (i++) + " " + String.valueOf(child.getValue()));
                }


//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Person person = child.getValue(Person.class);
//                    log("onChildAdded person:" + person);
//                    adapter.add(String.valueOf(person));
//                }

                // From the Json data
                /*
                {
                    "contacts" : [ {
                    "name" : "Sandy"
                }, {
                    "name" : "Atlas"
                }, {
                    "name" : "Warhol"
                }, {
                    "name" : "Maisie"
                } ]
                }
                */
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


        setPeople();
        listenDataBaseChange();
        //addListener();

//        initFirebase();
//        readFirebase();
//        writeFirebase();
    }

    private void addListener() {

        //Value event listener for realtime data update
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Getting the data from snapshot
                    Person person = postSnapshot.getValue(Person.class);

                    //Adding it to a string
                    String string = "Name: "+person.getName()+"\nAddress: "+person.getAddress()+"\n\n";

                    //Displaying it on textview
                    log(string);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void listenDataBaseChange() {
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot childPerson: snapshot.getChildren()) {
                    String name = (String) childPerson.child("name").getValue();
                    String address = (String) childPerson.child("address").getValue();

                    log("1st method Person name:"+ name + " address:" + address);
                }

                for (DataSnapshot childPerson: snapshot.getChildren()) {
                    Person p =childPerson.getValue(Person.class);

                    log("2nd method Person: " + p);
                }

                HashMap<String, Person> map = null;
                for (DataSnapshot child : snapshot.getChildren()) {
                    map = (HashMap<String, Person>) child.getValue();
                    log("list:" + map);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                log("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void setPeople() {


        Person atlas = new Person("Atlas", "中興路");
        Person sandy = new Person("Sandy", "愛十街");

        firebase.push().setValue(atlas);    // 如果本身就是 class, 就不要再用  child("Person")
        firebase.push().setValue(sandy);

//        firebase.child("Person").push().setValue(atlas);
//        firebase.child("Person").push().setValue(sandy);

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

    public void log(String s) {
        Log.d(TAG_ATLAS, s);
    }
}
