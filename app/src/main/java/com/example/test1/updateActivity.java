package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class updateActivity extends AppCompatActivity {

    String Title, story, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        Title = intent.getStringExtra("title");
        story = intent.getStringExtra("description");

        EditText title = (EditText) findViewById(R.id.title);
        EditText description = (EditText) findViewById(R.id.description);
        Button delete = (Button) findViewById(R.id.delete);

        title.setText(Title);
        description.setText(story);

        FloatingActionButton update = findViewById(R.id.saveBtn);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setTitle("Deleting");
                progressDialog.setMessage("your Story.");
                progressDialog.show();
                FirebaseFirestore.getInstance()
                        .collection("notes").
                        document(id).
                        delete();
                        finish();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = title.getText().toString();
                story = description.getText().toString();
                updateNote();
            }
        });
    }

    private void updateNote() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("your Story.");
        progressDialog.show();
        NotesModel notesModel = new NotesModel(id, Title, story, firebaseAuth.getUid());
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("notes").
                document(id).
                set(notesModel).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(updateActivity.this, "Story Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(updateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });

    }
}