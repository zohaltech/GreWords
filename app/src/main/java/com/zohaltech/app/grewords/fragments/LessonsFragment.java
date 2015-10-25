package com.zohaltech.app.grewords.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zohaltech.app.grewords.adapters.LessonAdapter;

import java.util.ArrayList;

import com.zohaltech.app.grewords.R;


public class LessonsFragment extends Fragment {

    RecyclerView recyclerLessons;
    ArrayList<Integer> lessons = new ArrayList<>();
    LessonAdapter adapter;

    public LessonsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lessons, container, false);
        recyclerLessons = (RecyclerView) rootView.findViewById(R.id.recyclerLessons);
        recyclerLessons.setHasFixedSize(true);
        recyclerLessons.setLayoutManager(new LinearLayoutManager(getActivity()));
        populateLessons();
        adapter = new LessonAdapter(getActivity(), lessons);
        recyclerLessons.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        lessons.clear();
        populateLessons();
        adapter.notifyDataSetChanged();
    }

    private void populateLessons(){
        for (int i = 0; i < 32; i++) {
            lessons.add(i + 1);
        }
    }
}