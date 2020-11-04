package com.example.bookkeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private EditText textView2;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    String UID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("NoteBook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView2 = findViewById(R.id.name2);
    }

    public void saveNote(View v) {


          UID = mAuth.getCurrentUser().getUid();
          //DocumentReference documentReference = db.collection("users").document(UID);
         // CollectionReference collectionReference = db.collection("users").document(UID).collection("trips");

        String name = textView2.getText().toString();
        NameID note = new NameID (name);
        notebookRef.document(UID).collection("trip").add(note);

//        String name = textView2.getText().toString();
//        NameID note = new NameID (name);
//
//        notebookRef.add(note);
    }
}