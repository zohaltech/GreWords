package com.zohaltech.app.grewords.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.adapters.VocabularyAdapter;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Vocabulary;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    RecyclerView          recyclerSearch;
    ArrayList<Vocabulary> vocabularies = new ArrayList<>();
    VocabularyAdapter     adapter;
    
    public SearchFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch = rootView.findViewById(R.id.recyclerSearch);
        recyclerSearch.setHasFixedSize(true);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        vocabularies.clear();
        adapter = new VocabularyAdapter(getActivity(), vocabularies);
        recyclerSearch.setAdapter(adapter);
        return rootView;
    }
    
    public void search(String text) {
        vocabularies.clear();
        if (text != null && text.length() > 0) {
            vocabularies.addAll(Vocabularies.search(getContext(), text));
        }
        adapter.notifyDataSetChanged();
    }
}
