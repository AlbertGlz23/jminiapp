package com.jminiapp.examples.todo.state;

import com.jminiapp.examples.todo.model.ToDoItem;
import java.util.ArrayList;
import java.util.List;

public class ToDoState {
    
    private List<ToDoItem> tasks = new ArrayList<>();

    public ToDoState() {}

    public List<ToDoItem> getTasks() {
        return tasks;
    }

    public void setTasks(List<ToDoItem> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(ToDoItem item) {
        this.tasks.add(item);
    }
    
    public boolean removeTaskByIndex(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            return true;
        }
        return false;
    }
}