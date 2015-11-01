package com.zohaltech.app.grewords.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.adapters.NoteAdapter;
import com.zohaltech.app.grewords.data.Notes;
import com.zohaltech.app.grewords.entities.Note;

import java.util.ArrayList;

import widgets.MySnackbar;

public class NotesFragment extends Fragment {
    public static final String VOCAB_ID = "VOCAB_ID";

    int             vocabId;
    ArrayList<Note> notes;

    public static NotesFragment newInstance(int vocabId) {
        Bundle args = new Bundle();
        args.putInt(VOCAB_ID, vocabId);
        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        final RecyclerView recyclerNotes = (RecyclerView) view.findViewById(R.id.recyclerNotes);
        final FloatingActionButton fabAddNote = (FloatingActionButton) view.findViewById(R.id.fabAddNote);
        final TextView txtNothingFound = (TextView) view.findViewById(R.id.txtNothingFound);
        recyclerNotes.setHasFixedSize(true);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        vocabId = getArguments().getInt(VOCAB_ID);
        notes = Notes.getNotes(vocabId);
        final NoteAdapter adapter = new NoteAdapter(getActivity(), notes,fabAddNote);
        recyclerNotes.setAdapter(adapter);

        recyclerNotes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("LOG", "dx : " + dx + ", dy : " + dy);
                if (dy > 0) {
                    fabAddNote.hide();
                } else {
                    fabAddNote.show();
                }
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() > 0) {
                    txtNothingFound.setVisibility(View.GONE);
                } else {
                    txtNothingFound.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter.notifyDataSetChanged();

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_edit_note);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                final EditText edtNote = (EditText) dialog.findViewById(R.id.edtNote);
                Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtNote.getText().toString().trim().length() > 0) {
                            Note note = new Note(vocabId, 1, edtNote.getText().toString());
                            long id = Notes.insert(note);
                            if (id > 0) {
                                note.setId(id);
                                notes.add(note);
                                adapter.notifyDataSetChanged();
                            } else {
                                MySnackbar.show(edtNote, "Error submitting note!", Snackbar.LENGTH_SHORT);
                            }
                            dialog.dismiss();
                        } else {
                            MySnackbar.show(edtNote, "Please enter a note!", Snackbar.LENGTH_SHORT);
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }
}
