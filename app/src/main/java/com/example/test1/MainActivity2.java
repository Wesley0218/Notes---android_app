package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test1.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    ActivityMainBinding binding;
    private noteAdapter notesAdapter;
    private List<NotesModel> notesModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        notesAdapter = new noteAdapter(this);

        getSupportActionBar().setTitle("Notes");

        notesModelList=new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.notes_recycler);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton floating = findViewById(R.id.floatingAddBtn);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, addActivity.class);
                startActivity(intent);
            }
        });

        EditText searchBar = (EditText) findViewById(R.id.search);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()> 0){
                    filter(text);
                }
                else{
                    notesAdapter.clear();
                    notesAdapter.fillterList(notesModelList);
                }

            }
        });

    }

    private void filter(String text) {
        List<NotesModel> adapterList = notesAdapter.getNotesModelList();
        List<NotesModel> notesModelList = new ArrayList<>();
        for (int i = 0;i<adapterList.size();i++){
            NotesModel notesModel = adapterList.get(i);
            if (notesModel.getTitle().toLowerCase().contains(text.toLowerCase()) || notesModel.getDescription().toLowerCase().contains(text)){
                notesModelList.add(notesModel);
            }
        }
        notesAdapter.fillterList(notesModelList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Checking User");
        progressDialog.setMessage("In process");
        progressDialog.show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()==null){
            firebaseAuth.signInAnonymously()
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.cancel();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(MainActivity2.this,e.getMessage(),Toast.LENGTH_SHORT);

                        }
                    });



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        getData();
    }

    private void getData() {
        FirebaseFirestore.getInstance().collection("notes").
                whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notesAdapter.clear();
                        List<DocumentSnapshot> dsList =  queryDocumentSnapshots.getDocuments();
                        for (int i=0; i<dsList.size();i++){
                            DocumentSnapshot documentSnapshot = dsList.get(i);
                            NotesModel notesModel = documentSnapshot.toObject(NotesModel.class);
                            notesModelList.add(notesModel);
                            notesAdapter.add(notesModel);
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}