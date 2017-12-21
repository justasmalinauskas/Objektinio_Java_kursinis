package com.justas.planuotojaslite;

import java.time.LocalDate;

public class Tasks {

    private final int id;
    private final String task;
    private final LocalDate date;

    public Tasks(int id, String task, LocalDate date) {
        this.id = id;
        this.task = task;
        this.date = date;
    }

    public void getValues() {
        System.out.println(id + " " + task + " " + date);
    }

    public String getStringValues() {
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return task + " " + date.toString();
    }

    public Integer getID() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return task;
    }
}
