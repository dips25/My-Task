package com.assgn.mytask.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.assgn.mytask.MainActivity;
import com.assgn.mytask.MyDatabase;
import com.assgn.mytask.Notes.Notes;
import com.assgn.mytask.R;
import com.assgn.mytask.Utils.Utils;

public class AddFragment extends Fragment {

    EditText title , desc;
    Button add;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_note , container , false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        title = (EditText) view.findViewById(R.id.title);
        desc = (EditText) view.findViewById(R.id.desc);
        add = (Button) view.findViewById(R.id.btn_add);

        add.setOnClickListener((v)->{

            if (!Utils.isBlank(title.getText().toString().trim())
                    && !Utils.isBlank(desc.getText().toString().trim())) {

                Notes n = new Notes(title.getText().toString().trim()
                        , desc.getText().toString().trim());

                MyDatabase db = new MyDatabase(getActivity());

                db.insert(n);

                for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {

                    if (fragment instanceof NotesFragment) {

                        ((NotesFragment) fragment).getAllNotes();
                        break;
                    }
                }

                ((MainActivity) getActivity()).onBackPressed();


            } else {

                Toast.makeText(getActivity(), "All Balnk", Toast.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        //super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.add);
        MenuItem item1 = menu.findItem(R.id.delete);
        item.setVisible(false);
        item1.setVisible(false);
    }
}
