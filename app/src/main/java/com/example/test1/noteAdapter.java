package com.example.test1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class noteAdapter extends RecyclerView.Adapter<noteAdapter.MyNoteViewHolder> {

    private List<NotesModel> notesModelList;
    private Context context;

    public noteAdapter(Context context) {
        this.context = context;
        notesModelList = new ArrayList<>();
    }
    public void add(NotesModel notesModel ){
        notesModelList.add(notesModel);
        notifyDataSetChanged();
    }
    public void clear(){
        notesModelList.clear();
        notifyDataSetChanged();
    }

    public void fillterList(List<NotesModel> newList ){
        notesModelList=newList;
        notifyDataSetChanged();
    }

    public List<NotesModel>getNotesModelList(){
        return notesModelList;
    }
    @NonNull
    @Override
    public MyNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_tow,parent,false);
        return new MyNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNoteViewHolder holder, int position) {
        NotesModel notesModel =  notesModelList.get(position);
        holder.title.setText(notesModel.getTitle());
        holder.description.setText(notesModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,updateActivity.class);
                intent.putExtra("id",notesModel.getId());
                intent.putExtra("title",notesModel.getTitle());
                intent.putExtra("description",notesModel.getDescription());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public class MyNoteViewHolder extends RecyclerView.ViewHolder{
        private TextView title, description;
        public MyNoteViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

        }
    }
}
