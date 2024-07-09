package com.assgn.mytask.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.assgn.mytask.MyDatabase;
import com.assgn.mytask.Notes.Notes;
import com.assgn.mytask.MainActivity;
import com.assgn.mytask.R;
import com.assgn.mytask.Utils.Utils;

public class UpdateDeleteFragment extends Fragment {

    EditText titleText , description;
    Button update;

    String title , desc;
    int id;
    public UpdateDeleteFragment(int id , String title, String desc) {

        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_updatedelete , container , false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        titleText = (EditText) view.findViewById(R.id.title);
        description = (EditText) view.findViewById(R.id.desc);
        update = (Button) view.findViewById(R.id.btn_update);

        titleText.setText(title);
        description.setText(desc);

        update.setOnClickListener((v)->{

            if (!Utils.isBlank(titleText.getText().toString().trim())
                    && !Utils.isBlank(description.getText().toString().trim())) {

                Notes n = new Notes(id , titleText.getText().toString().trim()
                        , description.getText().toString().trim());

                MyDatabase db = new MyDatabase(getActivity());

                db.update(n);

                for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {

                    if (fragment instanceof NotesFragment) {

                        ((NotesFragment) fragment).getAllNotes();
                        break;
                    }
                }

                ((MainActivity) getActivity()).onBackPressed();

            } else {

                Toast.makeText(getActivity(), "Fill All the details.", Toast.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.add);
        MenuItem item1 = menu.findItem(R.id.delete);
        item.setVisible(false);
        item1.setVisible(true);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        //inflater.inflate(R.menu.menu_main, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete) {

            MyDatabase db = new MyDatabase(getActivity());
            db.deleteNote(id);

            for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {

                if (fragment instanceof NotesFragment) {

                    ((NotesFragment) fragment).getAllNotes();
                    break;
                }
            }

            ((MainActivity) getActivity()).onBackPressed();
        }

        return true;
    }


}
