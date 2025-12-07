---
sidebar_position: 2
---

# To-Do List Example Application

A To-Do List application demonstrating advanced lifecycle management, file-based state persistence, and managing object collections (POJOs).

**Features:**
* **Task Management:** Add, Delete, Mark/Unmark tasks.
* **State Persistence:** Automatic and manual state saving (Import/Export to JSON).
* **File Deletion:** Option to erase the persistence file (`TaskList.json`).
* **Interactive Menu:** Complete command-line interface (Terminal I/O) in English.

**Source Code:** [examples/todo-app](https://github.com/jminiapp/jminiapp/tree/main/examples/todo-app)

---

### Key Concepts Demonstrated 🔑

* **Collection Management:** Use of `List<ToDoItem>` within the state class.
* **File Persistence:** Implementation of `context.importData()` and `context.exportData()`.
* **Direct File Handling:** Using `java.io.File` directly to locate and delete the persistence file, circumventing limitations in the standard `JMiniApp` context API for direct file manipulation.
* **POJO Serialization:** Definition of `ToDoItem` and `ToDoState` for JSON serialization.
* **Lifecycle Handling:** Initializing state either from an existing file or creating a new empty list.

---

### Quick Start 🚀

```bash
# Assuming you are in the root of the project
cd examples/feat/to-do-app
mvn clean install
mvn exec:java
```
### Code Highlights

## 1. State Model (ToDoState.java)
Defines the main data container and methods to manipulate the task list.

```java
public class ToDoState {
    private List<ToDoItem> tasks = new ArrayList<>();

    public ToDoState() {}

    public List<ToDoItem> getTasks() { return tasks; }

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
```
## 2. Application Logic (ToDoApp.java)
Shows lifecycle management, state loading, and the custom logic to delete the persistence file.

```java
public class ToDoApp extends JMiniApp {
    private static final String EXPORT_FILENAME = "TaskList";
    private ToDoState state; 
    
    // ... Constructor and variables

    @Override
    protected void initialize() {
        // ... Logic to import state or start with an empty list
    }

    // ... run() and shutdown()

    private void deleteTasklistFile() {
        String fullFilename = EXPORT_FILENAME + ".json";
        
        // Custom path required to locate the resource file
        String resourcePath = "examples/feat/to-do-app/src/main/resources/"; 
        
        File file = new File(resourcePath + fullFilename);
        
        if (file.exists()) {
            if (file.delete()) {
                state = new ToDoState(); 
                System.out.printf("Success: '%s' file was deleted and the task list was reset.\n", fullFilename);
            } else {
                System.out.printf("Error: Could not delete the file '%s'.\n", fullFilename);
            }
        } else {
            System.out.printf("Warning: '%s' file was not found in the resource path. Nothing to delete.\n", fullFilename);
        }
    }
    
    // ... other methods (addTask, toggleTask, exportToFile, etc.)
}
```
## Bootstrap

```java
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(ToDoApp.class)
            .withState(ToDoState.class)
            .withAdapters(new ToDoJSONAdapter())
            .withResourcesPath("examples/feat/to-do-app/src/main/resources/")
            .named("To-Do List")
            .run(args);
    }
```
