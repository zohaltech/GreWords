package com.zohaltech.app.grewords.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.classes.DialogManager;
import com.zohaltech.app.grewords.classes.ReminderManager;
import com.zohaltech.app.grewords.data.SystemSettings;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.SystemSetting;
import com.zohaltech.app.grewords.entities.Vocabulary;
import com.zohaltech.app.grewords.serializables.Reminder;
import com.zohaltech.app.grewords.serializables.ReminderSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import widgets.MyToast;

public class SchedulerActivity extends EnhancedActivity {
    AppCompatCheckBox chkSa;
    AppCompatCheckBox chkSu;
    AppCompatCheckBox chkMo;
    AppCompatCheckBox chkTu;
    AppCompatCheckBox chkWe;
    AppCompatCheckBox chkTh;
    AppCompatCheckBox chkFr;
    
    AppCompatSpinner spinnerIntervals;
    AppCompatSpinner spinnerWordsPerDay;
    AppCompatSpinner spinnerStartLesson;
    
    Button             btnStart;
    Button             btnStop;
    Button             btnPause;
    Button             btnRestart;
    Button             btnStartTime;
    LinearLayoutCompat layoutRingtone;
    TableRow           rowRingtone;
    Button             btnSelectTone;
    
    TextView txtStatus;
    
    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_scheduler);
        initialise();
    }
    
    private void initialise() {
        //edtStartVocabularyNo = (EditText) findViewById(R.id.edtStartVocabularyNo);
        //edtAlarmIntervals = (EditText) findViewById(R.id.edtAlarmIntervals);
        spinnerIntervals = findViewById(R.id.spinnerIntervals);
        spinnerWordsPerDay = findViewById(R.id.spinnerWordsPerDay);
        spinnerStartLesson = findViewById(R.id.spinnerStartLesson);
        btnStartTime = findViewById(R.id.btnStartTime);
        
        chkSa = findViewById(R.id.chkSa);
        chkSu = findViewById(R.id.chkSu);
        chkMo = findViewById(R.id.chkMo);
        chkTu = findViewById(R.id.chkTu);
        chkWe = findViewById(R.id.chkWe);
        chkTh = findViewById(R.id.chkTh);
        chkFr = findViewById(R.id.chkFr);
        
        btnStop = findViewById(R.id.btnStop);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnRestart = findViewById(R.id.btnRestart);
        layoutRingtone = findViewById(R.id.layoutRingtone);
        rowRingtone = findViewById(R.id.rowRingtone);
        btnSelectTone = findViewById(R.id.btnSelectTone);
        txtStatus = findViewById(R.id.txtStatus);
        
        bind();
        btnStart.setOnClickListener(v -> {
            start();
            setViewsStatus();
        });
        
        btnPause.setOnClickListener(v -> {
            ReminderManager.pause(this);
            bind();
            setViewsStatus();
        });
        
        btnStop.setOnClickListener(v -> {
            ReminderManager.stop(this);
            bind();
            setViewsStatus();
        });
        
        btnRestart.setOnClickListener(v -> {
            start();
            setViewsStatus();
        });
        
        btnStartTime.setOnClickListener(v -> {
            if (btnStartTime.getText().length() > 0) {
                int hour = Integer.valueOf(btnStartTime.getText().toString().substring(0, 2));
                int minute = Integer.valueOf(btnStartTime.getText().toString().substring(3, 5));
                DialogManager.showTimePickerDialog(this, hour, minute, () -> btnStartTime.setText(DialogManager.timeResult));
            } else {
                DialogManager.showTimePickerDialog(this, 12, 0, () -> btnStartTime.setText(DialogManager.timeResult));
            }
        });
        
        btnSelectTone.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
            startActivityForResult(intent, 5);
        });
        
        setViewsStatus();
    }
    
    private void start() {
        ReminderSettings settings = ReminderManager.getReminderSettings();
        
        boolean paused = settings.getStatus() == ReminderSettings.Status.PAUSE;
        
        boolean[] days = {
                chkSu.isChecked(),
                chkMo.isChecked(),
                chkTu.isChecked(),
                chkWe.isChecked(),
                chkTh.isChecked(),
                chkFr.isChecked(),
                chkSa.isChecked()};
        
        int selectedLesson = spinnerStartLesson.getSelectedItemPosition() + 1;
        int startVocabId = Vocabularies.selectByLesson(this, selectedLesson).get(0).getId();
        
        Date reminderTime = Calendar.getInstance().getTime();
        Reminder garbage = settings.getReminder();
        if (garbage != null) {
            if (garbage.getTime() != null) {
                reminderTime = garbage.getTime();
            }
    
            startVocabId = garbage.getVocabularyId();
        }
        Vocabulary vocabulary = Vocabularies.select(this, startVocabId);
        if (vocabulary == null) {
            return;
        }
        
        
        settings.setReminder(new Reminder(vocabulary.getId(), reminderTime, vocabulary.getVocabulary(), vocabulary.getVocabEnglishDef(), true));
        settings.setStartTime(btnStartTime.getText().toString());
        //settings.setIntervals(Integer.parseInt(edtAlarmIntervals.getText().toString()));
        //  settings.setIntervals();
        settings.setIntervals((Integer) spinnerIntervals.getSelectedItem());
        settings.setWordsPerDay((Integer) spinnerWordsPerDay.getSelectedItem());
        settings.setWeekdays(days);
        settings.setStatus(ReminderSettings.Status.RUNNING);
        ReminderManager.applyReminderSettings(settings);
        
        ReminderManager.start(this, paused);
        
        bind();
        
    }
    
    private void bind() {
        ReminderSettings settings = ReminderManager.getReminderSettings();
        
        btnRestart.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);
        
        if (settings.getStatus() == ReminderSettings.Status.STOP) {
            btnStart.setVisibility(View.VISIBLE);
        } else if (settings.getStatus() == ReminderSettings.Status.RUNNING) {
            btnStop.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.VISIBLE);
            
            if (settings.getReminder() != null) {
                Reminder reminder = settings.getReminder();
                if (reminder.getTime() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM HH:mm", Locale.getDefault());
                    MyToast.show(this, "Next alarm: " + sdf.format(reminder.getTime().getTime()), Toast.LENGTH_LONG);
                }
            }
        } else if (settings.getStatus() == ReminderSettings.Status.PAUSE) {
            btnStop.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
        } else if (settings.getStatus() == ReminderSettings.Status.FINISHED) {
            btnRestart.setVisibility(View.VISIBLE);
        }
        
        btnStartTime.setText(settings.getStartTime());
        //edtAlarmIntervals.setText(String.valueOf(settings.getIntervals()));
        
        ArrayList<Integer> intervals = new ArrayList<>();
        
        //intervals.add(1);
        intervals.add(15);
        intervals.add(30);
        intervals.add(45);
        intervals.add(60);
        intervals.add(90);
        ArrayAdapter<Integer> intervalAdapter = new ArrayAdapter<>(this, R.layout.spinner_current_item, intervals);
        intervalAdapter.setDropDownViewResource(R.layout.spinner_item_center);
        spinnerIntervals.setAdapter(intervalAdapter);
        spinnerIntervals.setSelection(intervalAdapter.getPosition(settings.getIntervals()));
        
        
        ArrayList<Integer> wordsPerDay = new ArrayList<>();
        
        wordsPerDay.add(1);
        wordsPerDay.add(2);
        wordsPerDay.add(3);
        wordsPerDay.add(4);
        wordsPerDay.add(5);
        wordsPerDay.add(6);
        wordsPerDay.add(7);
        wordsPerDay.add(8);
        wordsPerDay.add(9);
        wordsPerDay.add(10);
        wordsPerDay.add(11);
        wordsPerDay.add(12);
        
        ArrayAdapter<Integer> wordsPerDayAdapter = new ArrayAdapter<>(this, R.layout.spinner_current_item, wordsPerDay);
        wordsPerDayAdapter.setDropDownViewResource(R.layout.spinner_item_center);
        spinnerWordsPerDay.setAdapter(wordsPerDayAdapter);
        spinnerWordsPerDay.setSelection(wordsPerDayAdapter.getPosition(settings.getWordsPerDay()));
        
        
        ArrayList<String> lessons = new ArrayList<>();
        
        for (int i = 0; i < 42; i++) {
            lessons.add("Lesson " + (i + 1));
        }
        
        ArrayAdapter<String> lessonsAdapter = new ArrayAdapter<>(this, R.layout.spinner_current_item, lessons);
        lessonsAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerStartLesson.setAdapter(lessonsAdapter);
        spinnerStartLesson.setSelection(0);
        
        if (settings.getReminder() != null) {
            Vocabulary vocabulary = Vocabularies.select(this, settings.getReminder().getVocabularyId());
            assert vocabulary != null;
            spinnerStartLesson.setSelection(vocabulary.getLesson() - 1);
        }
        boolean[] days = settings.getWeekdays();
        
        chkSu.setChecked(days[0]);
        chkMo.setChecked(days[1]);
        chkTu.setChecked(days[2]);
        chkWe.setChecked(days[3]);
        chkTh.setChecked(days[4]);
        chkFr.setChecked(days[5]);
        chkSa.setChecked(days[6]);
        
        SystemSetting setting = SystemSettings.getCurrentSettings(this);
        btnSelectTone.setText(setting.getAlarmRingingTone());
    }
    
    private void setViewsStatus() {
        switch (ReminderManager.getReminderSettings().getStatus()) {
            case RUNNING:
                spinnerStartLesson.setEnabled(false);
                btnStartTime.setEnabled(false);
                spinnerIntervals.setEnabled(false);
                spinnerWordsPerDay.setEnabled(false);
                chkSu.setEnabled(false);
                chkMo.setEnabled(false);
                chkTu.setEnabled(false);
                chkWe.setEnabled(false);
                chkTh.setEnabled(false);
                chkFr.setEnabled(false);
                chkSa.setEnabled(false);
                btnSelectTone.setEnabled(false);
                txtStatus.setText("Status : Running");
                break;
            case PAUSE:
                spinnerStartLesson.setEnabled(false);
                btnStartTime.setEnabled(true);
                spinnerIntervals.setEnabled(true);
                spinnerWordsPerDay.setEnabled(true);
                chkSu.setEnabled(true);
                chkMo.setEnabled(true);
                chkTu.setEnabled(true);
                chkWe.setEnabled(true);
                chkTh.setEnabled(true);
                chkFr.setEnabled(true);
                chkSa.setEnabled(true);
                btnSelectTone.setEnabled(true);
                txtStatus.setText("Status : Paused");
                break;
            case STOP:
                spinnerStartLesson.setEnabled(true);
                btnStartTime.setEnabled(true);
                spinnerIntervals.setEnabled(true);
                spinnerWordsPerDay.setEnabled(true);
                chkSu.setEnabled(true);
                chkMo.setEnabled(true);
                chkTu.setEnabled(true);
                chkWe.setEnabled(true);
                chkTh.setEnabled(true);
                chkFr.setEnabled(true);
                chkSa.setEnabled(true);
                btnSelectTone.setEnabled(true);
                txtStatus.setText("Status : Stopped");
                break;
            case FINISHED:
                spinnerStartLesson.setEnabled(true);
                btnStartTime.setEnabled(true);
                spinnerIntervals.setEnabled(true);
                spinnerWordsPerDay.setEnabled(true);
                chkSu.setEnabled(true);
                chkMo.setEnabled(true);
                chkTu.setEnabled(true);
                chkWe.setEnabled(true);
                chkTh.setEnabled(true);
                chkFr.setEnabled(true);
                chkSa.setEnabled(true);
                btnSelectTone.setEnabled(true);
                txtStatus.setText("Status : Finished");
                break;
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onToolbarCreated() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Scheduler");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            SystemSetting setting = SystemSettings.getCurrentSettings(this);
            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                String title = ringtone.getTitle(this);
                setting.setRingingToneUri(uri.toString());
                setting.setAlarmRingingTone(title);
                btnSelectTone.setText(title);
            } else {
                setting.setRingingToneUri(Settings.System.DEFAULT_NOTIFICATION_URI.getPath());
                Ringtone ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_NOTIFICATION_URI);
                setting.setAlarmRingingTone(ringtone.getTitle(this));
                btnSelectTone.setText(ringtone.getTitle(this));
            }
            SystemSettings.update(this, setting);
        }
    }
}
