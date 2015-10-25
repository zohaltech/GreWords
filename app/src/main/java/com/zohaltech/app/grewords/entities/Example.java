package com.zohaltech.app.grewords.entities;

public class Example {
    private int    id;
    private int    vocabularyId;
//    private int    ordinal;



    private String synonyms;
    private String antonyms;

    public Example(int id, int vocabularyId,String synonyms, String antonyms) {
        this(vocabularyId, synonyms ,antonyms);
        this.id = id;
    }

    public Example(int vocabularyId,String synonyms, String antonyms) {
        setVocabularyId(vocabularyId);
//        setOrdinal(ordinal);
        setSynonyms(synonyms);
        setAntonyms(antonyms);
    }
    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVocabularyId() {
        return vocabularyId;
    }

    public void setVocabularyId(int vocabularyId) {
        this.vocabularyId = vocabularyId;
    }


    public String getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String english) {
        this.antonyms = english;
    }
}
