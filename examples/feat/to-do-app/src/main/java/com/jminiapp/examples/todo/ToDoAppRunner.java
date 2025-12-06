package com.jminiapp.examples.todo;

import com.jminiapp.core.engine.JMiniAppRunner;
import com.jminiapp.examples.todo.adapter.ToDoJSONAdapter;
import com.jminiapp.examples.todo.state.ToDoState;

public class ToDoAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(ToDoApp.class)
            .withState(ToDoState.class)
            .withAdapters(new ToDoJSONAdapter())
            .withResourcesPath("examples/feat/to-do-app/src/main/resources/")
            .named("To-Do List")
            .run(args);
    }
}