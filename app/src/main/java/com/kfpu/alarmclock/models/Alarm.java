package com.kfpu.alarmclock.models;

import java.io.Serializable;

/**
 * Created by hlopu on 07.11.2017.
 */

public class Alarm implements Serializable{
    private int id;
    private int hours;
    private int minutes;
    private String date;
    private String state;


    public Alarm(int hours, int minutes, String date, String state) {
        this.hours = hours;
        this.minutes = minutes;
        this.date = date;
        this.state = state;
    }

    public Alarm(int id, int hours, int minutes, String date, String state) {
        this.id = id;
        this.hours = hours;
        this.minutes = minutes;
        this.date = date;
        this.state = state;
    }

    public int getHours() {
        return hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
