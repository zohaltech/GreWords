package com.zohaltech.app.grewords.adapters;


import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.data.Notes;
import com.zohaltech.app.grewords.entities.Note;

import java.util.ArrayList;

import widgets.MySnackbar;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context              context;
    ArrayList<Note>      notes;
    FloatingActionButton fab;

    public NoteAdapter(Context context, ArrayList<Note> notes, FloatingActionButton fab) {
        this.context = context;
        this.notes = notes;
        this.fab = fab;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Note note = notes.get(position);
        holder.txtNote.setText(note.getDescription());
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.imgMore);
                popup.getMenuInflater().inflate(R.menu.menu_note, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_edit) {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_edit_note);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setCancelable(true);
                            final EditText edtNote = (EditText) dialog.findViewById(R.id.edtNote);
                            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                            edtNote.setText(note.getDescription());
                            edtNote.selectAll();
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                            btnSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (edtNote.getText().toString().trim().length() > 0) {
                                        note.setDescription(edtNote.getText().toString());
                                        if (Notes.update(note) > 0) {
                                            notes.set(position, note);
                                            notifyDataSetChanged();
                                            fab.show();
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

                        } else if (id == R.id.action_delete) {
                            if (Notes.delete(note) > 0) {
                                notes.remove(position);
                                notifyDataSetChanged();
                                fab.show();
                            }
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView    txtNote;
        public ImageButton imgMore;

        public ViewHolder(View view) {
            super(view);
            txtNote = (TextView) view.findViewById(R.id.txtNote);
            imgMore = (ImageButton) view.findViewById(R.id.imgMore);
        }
    }
}
