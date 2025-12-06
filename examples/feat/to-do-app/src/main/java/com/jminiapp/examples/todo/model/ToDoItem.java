package com.jminiapp.examples.todo.model;


public class ToDoItem {
    private String id;
    private String description;
    private boolean completed;
    
    public ToDoItem() {} 
    
    public ToDoItem(String id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "TodoItem{description='" + description + "', completed=" + completed + "}";
    }
}