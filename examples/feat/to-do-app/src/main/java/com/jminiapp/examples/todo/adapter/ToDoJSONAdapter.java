package com.jminiapp.examples.todo.adapter;

import com.jminiapp.core.adapters.JSONAdapter;
import com.jminiapp.examples.todo.state.ToDoState;

public class ToDoJSONAdapter implements JSONAdapter<ToDoState> { 
    
    @Override
    public Class<ToDoState> getstateClass() {
        return ToDoState.class;
    }
    
}