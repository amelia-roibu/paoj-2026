package com.pao.laboratory03.bonus.model;

public class Task {
    private final String id;
    private final String title;
    private Status status;
    private Priority priority;
    private String assignee;

    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.TODO;
    }

    @Override
    public String toString() {
        return String.format("Task{id='%s', title='%s', priority=%s, status=%s, assignee=%s}",
                id, title, priority, status, assignee);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}