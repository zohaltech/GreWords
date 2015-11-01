package com.zohaltech.app.grewords.classes;


import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Vocabulary;
import com.zohaltech.app.grewords.serializables.Reminder;
import com.zohaltech.app.grewords.serializables.ReminderSettings;

import java.util.ArrayList;

public class  LearningStatus {
    private int vocabIndex;
    private int progress;
    private int vocabCount;

    public static LearningStatus getLearningStatusByLesson(int lesson) {
        LearningStatus learningStatus = new LearningStatus();
        ReminderSettings settings = ReminderManager.getReminderSettings();

        if (settings.getStatus() != ReminderSettings.Status.STOP) {
            ArrayList<Vocabulary> vocabularies = Vocabularies.selectByLesson(lesson);
            int vocabCount = vocabularies.size();
            Reminder reminder = ReminderManager.getLastReminder();
            if (reminder != null) {

                int currentVocabId = reminder.getVocabularyId();

                //        if (currentVocabId == 0)
                //            return null;
                Vocabulary currentVocab = Vocabularies.select(currentVocabId);
                assert currentVocab != null;

                int vocabIndex = 0;
                if (currentVocab.getLesson() != lesson) {
                    for (Vocabulary vocabulary : vocabularies) {
                        if (vocabulary.getLearned())
                            vocabIndex++;
                    }

                } else
                    vocabIndex = indexOf(currentVocab, vocabularies) + 1;

                // int vocabIndex = vocabularies.indexOf(currentVocab) + 1;
                // int vocabIndex = indexOf(currentVocab, vocabularies) + 1;

                if (settings.getStatus() == ReminderSettings.Status.FINISHED) {
                    learningStatus.setProgress(100);
                    learningStatus.setVocabIndex(vocabCount);
                    learningStatus.setVocabCount(vocabCount);
                } else {
                    learningStatus.setProgress(vocabIndex * 100 / vocabCount);
                    learningStatus.setVocabIndex(vocabIndex);
                    learningStatus.setVocabCount(vocabCount);
                }
            } else {
                learningStatus.setProgress(0);
                learningStatus.setVocabIndex(0);
                learningStatus.setVocabCount(vocabCount);
            }
        } else {
            return null;
        }

        return learningStatus;
    }

    private static int indexOf(Vocabulary vocabulary, ArrayList<Vocabulary> elementData) {
        if (vocabulary == null) {
            for (int i = 0; i < elementData.size(); i++)
                if (elementData.get(i) == null)
                    return i;
        } else {
            for (int i = 0; i < elementData.size(); i++)
                if (vocabulary.getId() == (elementData.get(i).getId()))
                    return i;
        }
        return -1;
    }

    public int getVocabIndex() {
        return vocabIndex;
    }

    private void setVocabIndex(int vocabIndex) {
        this.vocabIndex = vocabIndex;
    }

    public int getProgress() {
        return progress;
    }

    private void setProgress(int progress) {
        this.progress = progress;
    }

    public int getVocabCount() {
        return vocabCount;
    }

    private void setVocabCount(int vocabCount) {
        this.vocabCount = vocabCount;
    }
}