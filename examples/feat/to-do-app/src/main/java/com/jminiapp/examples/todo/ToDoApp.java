package com.jminiapp.examples.todo;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;
import com.jminiapp.examples.todo.model.ToDoItem;
import com.jminiapp.examples.todo.state.ToDoState;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID; 

public class ToDoApp extends JMiniApp {
    private static final String EXPORT_FILENAME = "TaskList";
    
    private Scanner scanner;
    private ToDoState state; 
    private boolean running;

    public ToDoApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\n=== To-Do List App ===");
        
        scanner = new Scanner(System.in);
        running = true;

        try {
            context.importData(EXPORT_FILENAME, "json");
            System.out.println("Auto-Import successful: Loaded previous tasks.");
        } catch (IOException e) {
        }

        List<ToDoState> data = context.getData();
        if (data != null && !data.isEmpty()) {
            state = data.get(0);
            System.out.printf("Loaded %d existing tasks.\n", state.getTasks().size());
        } else {
            state = new ToDoState(); 
            System.out.println("Starting with an empty list.");
        }
    }

    @Override
    protected void run() {
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        context.setData(List.of(state));
        
        scanner.close();
        System.out.println("\nState saved. Goodbye!");
    }

    private void displayMenu() {
        System.out.println("\n--- To-Do Tasks ---");
        
        if (state.getTasks().isEmpty()) {
            System.out.println("  [List is empty. Add a task!]");
        } else {
            int index = 1;
            for (ToDoItem item : state.getTasks()) {
                String status = item.isCompleted() ? "[X]" : "[ ]";
                System.out.printf("  %d. %s %s\n", index++, status, item.getDescription());
            }
        }

        System.out.println("\nOptions:");
        System.out.println(" A. Add new task");
        System.out.println(" M. Mark/Unmark task (by number)");
        System.out.println(" N. Delete task (by number)");
        System.out.println(" X. Export to JSON (as TaskList.json)");
        System.out.println(" I. Import from JSON (from TaskList.json)");
        System.out.println(" D. Delete TaskList file (Erase state)");
        System.out.println(" S. Exit");
        System.out.print("\nChoose an option: ");
    }

    private void handleUserInput() {
        try {
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "A": addTask(); break;
                case "M": toggleTask(); break;
                case "N": deleteTask(); break;
                case "X": exportToFile(); break;
                case "I": importFromFile(); break;
                case "D": deleteTasklistFile(); break;
                case "S": running = false; break;
                default: System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void addTask() {
        System.out.print("Enter new task description: ");
        String description = scanner.nextLine().trim();

        if (description.isBlank()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        ToDoItem newItem = new ToDoItem(UUID.randomUUID().toString(), description, false);
        state.addTask(newItem);
        System.out.printf("Task '%s' added.\n", description);
    }

    private void toggleTask() {
        if (state.getTasks().isEmpty()) {
            System.out.println("The task list is empty. Nothing to mark/unmark.");
            return;
        }
        
        System.out.print("Enter the task number to Mark/Unmark: ");
        if (scanner.hasNextInt()) {
            int index = scanner.nextInt();
            scanner.nextLine(); 

            if (index > 0 && index <= state.getTasks().size()) {
                ToDoItem item = state.getTasks().get(index - 1);
                item.setCompleted(!item.isCompleted());
                String action = item.isCompleted() ? "marked as completed" : "unmarked";
                System.out.printf("Task %d ('%s') has been %s.\n", index, item.getDescription(), action);
            } else {
                System.out.println("Invalid task number.");
            }
        } else {
            scanner.nextLine();
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void deleteTask() {
        if (state.getTasks().isEmpty()) {
            System.out.println("The task list is empty. Nothing to delete.");
            return;
        }
        
        System.out.print("Enter the task number to Delete: ");
        if (scanner.hasNextInt()) {
            int index = scanner.nextInt();
            scanner.nextLine(); 

            if (index > 0 && index <= state.getTasks().size()) {
                ToDoItem removedItem = state.getTasks().get(index - 1);
                state.removeTaskByIndex(index - 1);
                System.out.printf("Task '%s' permanently deleted.\n", removedItem.getDescription());
            } else {
                System.out.println("Invalid task number.");
            }
        } else {
            scanner.nextLine();
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    private void exportToFile() {
        try {
            context.setData(List.of(state));
            context.exportData(EXPORT_FILENAME, "json");
            System.out.printf("Task state successfully exported to: '%s.json'\n", EXPORT_FILENAME);
        } catch (IOException e) {
            System.out.println("Error exporting file: " + e.getMessage());
        }
    }

    private void importFromFile() {
        try {
            context.importData(EXPORT_FILENAME, "json");

            List<ToDoState> data = context.getData();
            if (data != null && !data.isEmpty()) {
                this.state = data.get(0); 
                System.out.println("Task state successfully imported. New list loaded.");
            } else {
                System.out.printf("Error: Found no valid data in '%s.json'. The file might be empty or corrupted.\n", EXPORT_FILENAME);
            }
        } catch (IOException e) {
            System.out.printf("Error importing '%s.json': File not found. Please export your list first (Option X) to create the file.\n", EXPORT_FILENAME);
        }
    }

    private void deleteTasklistFile() {
    this.state = new ToDoState(); 
        
        try {
            this.context.setData(List.of(this.state));
            this.context.exportData(EXPORT_FILENAME, "json");
            System.out.printf("Success: Task list was reset and the empty state was saved to '%s.json'.\n", EXPORT_FILENAME);

        } catch (IOException e) {
            System.out.println("Error saving empty state: " + e.getMessage());
        }
    }
}