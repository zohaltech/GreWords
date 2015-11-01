package com.zohaltech.app.grewords.entities;

public class Example {
    private int    id;
    private int    vocabularyId;
    private String synonyms;
    private String opposites;

    public Example(int id, int vocabularyId,String synonyms, String opposites) {
        this(vocabularyId, synonyms ,opposites);
        this.id = id;
    }

    public Example(int vocabularyId,String synonyms, String opposites) {
        setVocabularyId(vocabularyId);
        setSynonyms(synonyms);
        setOpposites(opposites);
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

    public String getOpposites() {
        return opposites;
    }

    public void setOpposites(String opposites) {
        this.opposites = opposites;
    }
}
