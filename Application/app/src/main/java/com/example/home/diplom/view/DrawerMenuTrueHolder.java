package com.example.home.diplom.view;


public class DrawerMenuTrueHolder {
    public boolean nav_note_true = false;
    public boolean nav_remind_true = false;
    public boolean nav_settings_true = false;
    public boolean nav_about_true = false;

    public DrawerMenuTrueHolder() {
        setNav_note_true(false);
        setNav_remind_true(false);
        setNav_settings_true(false);
        setNav_about_true(false);
    }


    public boolean isNav_note_true() {
        return nav_note_true;
    }

    public void setNav_note_true(boolean nav_note_true) {
        this.nav_note_true = nav_note_true;
    }

    public boolean isNav_remind_true() {
        return nav_remind_true;
    }

    public void setNav_remind_true(boolean nav_remind_true) {
        this.nav_remind_true = nav_remind_true;
    }

    public boolean isNav_settings_true() {
        return nav_settings_true;
    }

    public void setNav_settings_true(boolean nav_settings_true) {
        this.nav_settings_true = nav_settings_true;
    }

    public boolean isNav_about_true() {
        return nav_about_true;
    }

    public void setNav_about_true(boolean nav_about_true) {
        this.nav_about_true = nav_about_true;
    }

}

