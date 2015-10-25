package com.zohaltech.app.grewords.entities;

public class SystemSetting {
    private int     id;
    private Boolean installed;
    private String  alarmRingingTone;
    private String  ringingToneUri;
    private Boolean vibrateInAlarms;
    private Boolean soundInAlarms;

    public SystemSetting(Boolean installed, String ringingToneUri,
                         String alarmRingingTone, Boolean vibrateInAlarms, Boolean soundInAlarms) {
        setInstalled(installed);
        setAlarmRingingTone(alarmRingingTone);
        setRingingToneUri(ringingToneUri);
        setVibrateInAlarms(vibrateInAlarms);
        setSoundInAlarms(soundInAlarms);
    }

    public SystemSetting(int id, Boolean installed, String ringingToneUri,
                         String alarmRingingTone, Boolean vibrateInAlarms, Boolean soundInAlarms) {
        this(installed, ringingToneUri, alarmRingingTone, vibrateInAlarms, soundInAlarms);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getInstalled() {
        return installed;
    }

    public void setInstalled(Boolean installed) {
        this.installed = installed;
    }

    public String getAlarmRingingTone() {
        return alarmRingingTone;
    }

    public void setAlarmRingingTone(String alarmRingingTone) {
        this.alarmRingingTone = alarmRingingTone;
    }

    public String getRingingToneUri() {
        return ringingToneUri;
    }

    public void setRingingToneUri(String ringingToneUri) {
        this.ringingToneUri = ringingToneUri;
    }

    public Boolean getVibrateInAlarms() {
        return vibrateInAlarms;
    }

    public void setVibrateInAlarms(Boolean vibrateInAlarms) {
        this.vibrateInAlarms = vibrateInAlarms;
    }

    public Boolean getSoundInAlarms() {
        return soundInAlarms;
    }

    public void setSoundInAlarms(Boolean soundInAlarms) {
        this.soundInAlarms = soundInAlarms;
    }
}
