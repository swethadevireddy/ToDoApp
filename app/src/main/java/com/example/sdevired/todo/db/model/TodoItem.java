package com.example.sdevired.todo.db.model;

import java.text.SimpleDateFormat;

/**
 * Created by sdevired on 9/15/16.
 * Database model object for table todo_list
 */
public class TodoItem implements Comparable<TodoItem> {

    private static final SimpleDateFormat f = new SimpleDateFormat("M/dd/yyyy");
    private long id;
    private String task;
    private String formatedDate;
    private Long date = 0l;
    private String priority;

    @Override
    public int compareTo(TodoItem another) {
        Long prevDate = another.getDate();
        return this.getDate().compareTo(prevDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
        if (date > 0) {
            this.formatedDate = f.format(date);
        }
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


}
