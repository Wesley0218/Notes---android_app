package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class addActivity extends AppCompatActivity {

    private String Title="", Description="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        EditText title = (EditText)findViewById(R.id.title);
        EditText description= (EditText)findViewById(R.id.description);

        FloatingActionButton save = findViewById(R.id.saveBtn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title= title.getText().toString();
                Description= description.getText().toString();
                saveNote();



            }
        });

    }
    private void saveNote(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Saving");
        progressDialog.setMessage("your Story.");
        progressDialog.show();
        String noteId = UUID.randomUUID().toString();
        NotesModel notesModel = new NotesModel(noteId,Title,Description,firebaseAuth.getUid());
        FirebaseFirestore firebaseFirestore  = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("notes").
                document(noteId).
                set(notesModel).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addActivity.this,"Story Saved",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });

    }
}