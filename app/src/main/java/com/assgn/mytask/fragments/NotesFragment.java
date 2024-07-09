package com.assgn.mytask.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assgn.mytask.MyDatabase;
import com.assgn.mytask.Notes.Notes;
import com.assgn.mytask.R;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    ArrayList<Notes> notesArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;

    MyDatabase myDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllNotes();


    }

    public void getAllNotes() {

        myDatabase = new MyDatabase(getActivity());
        notesArrayList = myDatabase.getAllNotes();

        if (!notesArrayList.isEmpty()) {

            notesAdapter = new NotesAdapter();
            recyclerView.setAdapter(notesAdapter);
            notesAdapter.notifyDataSetChanged();

        } else {

            notesArrayList.clear();
            notesAdapter = new NotesAdapter();
            recyclerView.setAdapter(notesAdapter);
            notesAdapter.notifyDataSetChanged();


        }
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.add);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


        @NonNull
        @Override
        public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_item_notes , parent , false);

            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {

            Notes notes = notesArrayList.get(position);

            holder.title.setText(notes.getTitle());
            holder.desc.setText(notes.getDescription());

            holder.itemView.setOnClickListener((v)->{

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.main_frame,new UpdateDeleteFragment(notes.getId() , holder.title.getText().toString() , holder.desc.getText().toString()))
                        .addToBackStack("UpdateDeleteFragment")
                        .commit();
            });

        }

        @Override
        public int getItemCount() {
            return notesArrayList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            TextView desc;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.title);
                desc = (TextView) itemView.findViewById(R.id.desc);
            }
        }
    }

}
