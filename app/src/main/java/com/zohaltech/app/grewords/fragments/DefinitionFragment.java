package com.zohaltech.app.grewords.fragments;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Vocabulary;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

public class DefinitionFragment extends Fragment implements
        TextToSpeech.OnInitListener {
    public static final String VOCAB_ID = "VOCAB_ID";
    TextView txtPronunciation;
    TextView txtVocabEnglishDefinition;
    TextView txtVocabPersianMeaning;
    
    private Vocabulary   vocabulary;
    private TextToSpeech textToSpeech;
    
    
    public static DefinitionFragment newInstance(int vocabId) {
        Bundle args = new Bundle();
        args.putInt(VOCAB_ID, vocabId);
        DefinitionFragment fragment = new DefinitionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_definition, container, false);
        
        txtPronunciation = view.findViewById(R.id.txtPronunciation);
        txtVocabEnglishDefinition = view.findViewById(R.id.txtVocabEnglishDefinition);
//        txtVocabPersianMeaning = (TextView) view.findViewById(R.id.txtVocabPersianMeaning);
        Button btnSpeechUS = view.findViewById(R.id.btnSpeechUS);
        Button btnSpeechUK = view.findViewById(R.id.btnSpeechUK);
        
        int vocabId = Objects.requireNonNull(getArguments()).getInt(VOCAB_ID);
        vocabulary = Vocabularies.select(getContext(), vocabId);
        
        textToSpeech = new TextToSpeech(getActivity(), this);
        
        assert vocabulary != null;
        txtPronunciation.setText(MessageFormat.format("Usage: {0}", vocabulary.getPronunciation()));
        txtVocabEnglishDefinition.setText(vocabulary.getVocabEnglishDef());
//        txtVocabPersianMeaning.setText("");
//        txtVocabEnglishDefinition.setText(vocabulary.getEncEngDef());
//        txtVocabPersianMeaning.setText(vocabulary.getEncPersianDef());
    
        Drawable drawable = ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.sound);
    
        btnSpeechUK.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        btnSpeechUK.setOnClickListener(v -> {
            textToSpeech.setLanguage(Locale.UK);
            speakOut();
        });
        
        btnSpeechUS.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        btnSpeechUS.setOnClickListener(v -> {
            textToSpeech.setLanguage(Locale.US);
            speakOut();
        });
        
        return view;
    }
    
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            
            int result = textToSpeech.setLanguage(Locale.US);
            
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            //else {
            //    speakOut();
            //}
            
        } else {
            Log.e("TTS", " Failed!");
        }
    }
    
    private void speakOut() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(vocabulary.getVocabulary(), TextToSpeech.QUEUE_FLUSH, null);
        } else {
            textToSpeech.speak(vocabulary.getVocabulary(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}
