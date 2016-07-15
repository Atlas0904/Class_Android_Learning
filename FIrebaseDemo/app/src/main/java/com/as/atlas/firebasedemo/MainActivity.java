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


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Person person = child.getValue(Person.class);
                    log("onChildAdded person:" + person);
                    adapter.add(String.valueOf(person));
                }

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
        //listenDataBaseChange();
        addListener();

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
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    //Getting the data from snapshot
//                    //Person person = postSnapshot.getValue(Person.class);
//
//                    //Adding it to a string
//                    //String string = "Name: "+person.getName()+"\nAddress: "+person.getAddress()+"\n\n";
//
//                    //Displaying it on textview
//                    Log.d(TAG, "listenDataBaseChange() Person: " + postSnapshot.getValue(Person.class));
//
////                    adapter.add(
////                            (String) snapshot.child("Person").getValue());
//                }

                for (DataSnapshot child : snapshot.getChildren()) {
                    Log.d(TAG, child.getValue().toString());
//                    Person person = child.getValue(Person.class);
//                    Log.d(TAG, "Person:" + person);
                }

                HashMap<String, Person> map = null;
                for (DataSnapshot child : snapshot.getChildren()) {
                    map = (HashMap<String, Person>) child.getValue();
                    log("list:" + map);
                }

                if (map != null) {
//                    Set keys = map.keySet();
////                    for (Iterator i = keys.iterator(); i.hasNext(); ) {
////                        String key = (String) i.next();
////                        Person value = (Person) map.get(key);
////                        log(key + " = " + value);
////                    }
//
//                    for (Object key : map.keySet()) {
//
//                        System.out.println("key:" + key + " value=" + map.get(key));
//                    }


                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        Person val = (Person) entry.getValue();

                        log("key:" + key + " value=" + val);
                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void setPeople() {

        Person atlas = new Person();
        atlas.setName("Atlas");
        atlas.setAddress("館前東路26號7-7");
        firebase.child("Person").push().setValue(atlas);

        Person sandy = new Person();
        sandy.setName("Sandy");
        sandy.setAddress("愛十街");


        firebase.child("Person").push().setValue(sandy);

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
