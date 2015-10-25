package com.zohaltech.app.grewords.entities;


public class Vocabulary {
    private int     id;
    private int     lesson;
    private String  vocabulary;
    private String  pronunciation;
    private String  vocabEnglishDef;
    //    private String  vocabPersianDef;
    //private String  examples;
    private Boolean learned;
    private Boolean bookmarked;


    public Vocabulary(int lesson, String vocabulary, String pronunciation, String vocabEnglishDef, Boolean learned, Boolean bookmarked) {
        setLesson(lesson);
        //setExamples(examples);
        setVocabulary(vocabulary);
        setPronunciation(pronunciation);
        setVocabEnglishDef(vocabEnglishDef);
        //        setVocabPersianDef(vocabPersianDef);
        setLearned(learned);
        setBookmarked(bookmarked);
    }

    public Vocabulary(int id, int lesson, String vocabulary, String pronunciation, String vocabEnglishDef, Boolean learned, Boolean bookmarked) {
        this(lesson, vocabulary, pronunciation, vocabEnglishDef, learned, bookmarked);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }


    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public String getVocabEnglishDef() {
        return vocabEnglishDef;
    }

    public void setVocabEnglishDef(String vocabEnglishDef) {
        this.vocabEnglishDef = vocabEnglishDef;
    }

    public Boolean getLearned() {
        return learned;
    }

    public void setLearned(Boolean learned) {
        this.learned = learned;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    //public String getExamples() {
    //    return examples;
    //}
    //
    //public void setExamples(String examples) {
    //    this.examples = examples;
    //}

}
