package com.example.bookkeeperapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText textView;
    private Button imgbtn;
    private ImageView Image;
    private String downloadImageURL;
    private String name;


    private static final int REQUESCODE = 1 ;
    private static final int PReqCode = 2 ;
    private Uri pickedImgUri = null;
    private StorageReference ProductImageRef;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //String UID = mAuth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("NoteBook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      // startActivity(new Intent(MainActivity.this, SignupActivity.class));

        textView = findViewById(R.id.nametext);
        imgbtn = findViewById(R.id.Imagebtn);
        Image = findViewById(R.id.imageView);
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Prodcut Image");

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ){
            pickedImgUri = data.getData() ;
            Image.setImageURI(pickedImgUri);

        }
    }


    public void saveNote(View v) {

        name = textView.getText().toString();
       // NameID note = new NameID (name);
      //  notebookRef.add(note);

        if(pickedImgUri == null){
            Toast.makeText(this, "Enter Image Huttho", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter Name Huttho", Toast.LENGTH_SHORT).show();
        }else {
            StoreImageInformation();
        }

        //notebookRef.add(note);

    }

    private void StoreImageInformation() {

        final StorageReference filepath = ProductImageRef.child(pickedImgUri.getLastPathSegment() + ".jpg");

        final UploadTask uploadTask = filepath.putFile(pickedImgUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(MainActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Success" , Toast.LENGTH_SHORT).show();

                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){

                            throw task.getException();
                        }
                        downloadImageURL = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            downloadImageURL = task.getResult().toString();
                            Toast.makeText(MainActivity.this, "Image url inserted successfully to database" , Toast.LENGTH_SHORT).show();
                            saveNameandImg();
                        }

                    }
                });
            }
        });
    }

    private void saveNameandImg() {
        HashMap<String , Object> note = new HashMap<>();
        note.put("name",name);
        note.put("image",downloadImageURL);

        notebookRef.add(note);

    }


    // notebookRef.add(note);







    }



