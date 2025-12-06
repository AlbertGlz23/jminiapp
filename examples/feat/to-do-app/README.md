# To-Do List Example

A practical and interactive command-line To-Do List application that serves as a robust example of **state management**, **data collection (lists)**, and **persistence** within the **JMiniApp** framework.

## Overview

This application showcases the implementation of a full CRUD (Create, Read, Update, Delete) cycle for a list of tasks. Unlike simpler examples, the To-Do app manages a complex state structure—a list of custom objects (`ToDoItem`)—and demonstrates how to use the framework's **JSON Adapter** to automatically serialize and deserialize this collection for file storage.

The primary goal is to provide a clean, console-based interface for managing daily tasks while ensuring all progress is saved and loaded across application sessions.

## Features

The application offers a comprehensive interactive menu to manage the task list:

- **Add (A):** Prompts the user for a description and creates a new, uncompleted `ToDoItem`, appending it to the list.
- **Mark/Unmark (M):** Toggles the `completed` status of a task based on its visible list number, providing immediate feedback on its new state.
- **Delete Task (N):** Permanently removes a task from the list by its numerical index.
- **Export to JSON (X):** Saves the entire current `ToDoState` (the list of tasks) to the designated persistent file, **`TaskList.json`**.
- **Import from JSON (I):** Attempts to load the task list from **`TaskList.json`**, completely replacing the application's current in-memory state.
- **Delete TaskList file (D):** Physically deletes the **`TaskList.json`** file from the resources path, effectively erasing the application's persistent history.
- **Exit (S):** Saves the current state of the task list to `TaskList.json` and cleanly terminates the application.
- **Persistent State:** The list of tasks is automatically loaded upon initialization and saved upon graceful shutdown.

## Project Structure

The code is organized into distinct packages to separate concerns (Model, State, Adapter, and Application Logic):

- `todo/`
  - `pom.xml`
  - `README.md`
  - `src/main/java/com/jminiapp/examples/todo/`
    - `ToDoApp.java` — Main application logic, menu rendering, and I/O handling
    - `ToDoAppRunner.java` — Bootstrap configuration and entry point
    - `model/`
      - `ToDoItem.java` — The core Data Model for a single task (ID, description, completed status)
    - `state/`
      - `ToDoState.java` — The central Application State container (holds the List<ToDoItem>)
    - `adapter/`
      - `ToDoJSONAdapter.java` — JSON format adapter for serialization/deserialization

## Key Components

### ToDoState

- Core **State Model**.  
- Encapsulates a single data field: `private List<ToDoItem> tasks = new ArrayList<>();`.
- Maintains list integrity and provides controlled methods:
  - `addTask(ToDoItem task)`
  - `removeTaskByIndex(int index)`

### ToDoItem

- Data Model class for a single task:
  - `UUID id`
  - `String description`
  - `boolean completed`

### ToDoJSONAdapter

- Handles JSON serialization/deserialization.
- Implements `JSONAdapter<ToDoState>`.
- Registered during bootstrap with `.withAdapters(new ToDoJSONAdapter())`.
- Automatically serializes/deserializes complex structures like lists of `ToDoItem`.

### ToDoApp

- Main application class extending `JMiniApp`.
- **Initialization:** Loads data using `context.importData(EXPORT_FILENAME, "json")` for auto-restore.
- **Main Loop:** Continuously displays the menu and handles user input until the user selects 'S' (Exit).
- **Persistence:** `shutdown()` saves the current state before closing.

### ToDoAppRunner

- Bootstrap configuration.
- Registers the main application (`ToDoApp.class`) and state container (`ToDoState.class`).
- Activates the custom serialization logic with `.withAdapters(new ToDoJSONAdapter())`.
- Configures the storage path using `.withResourcesPath("examples/feat/to-do-app/src/main/resources/")`.

## Building and Running

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build the project

From the project root:

- Run `mvn clean install` to compile the framework and the To-Do example.

### Run the application

- Navigate to the example directory: `cd examples/todo`
- Launch the app using Maven exec plugin: `mvn exec:java`

## Usage Example

### Starting and Interacting

The menu dynamically updates the list status, showing `[X]` for completed tasks and `[ ]` for pending tasks.

- Initial display:

=== To-Do List App ===
Auto-Import successful: Loaded previous tasks.
Loaded 2 existing tasks.

--- To-Do Tasks ---

 Buy coffee beans

 Deploy new feature

markdown
Copiar código

### Menu Options

- A. Add new task
- M. Mark/Unmark task (by number)
- N. Delete task (by number)
- X. Export to JSON (as TaskList.json)
- I. Import from JSON (from TaskList.json)
- D. Delete TaskList file (Erase state)
- S. Exit

### Mark/Unmark Task

- Choose `M` and enter task number 1 → Task marked as completed.
- Updated list:

--- To-Do Tasks ---

 Buy coffee beans

 Deploy new feature

markdown
Copiar código

### Delete Task

- Choose `N` and enter task number 2 → Task permanently deleted.

### Delete State File

- Choose `D` → `TaskList.json` deleted, list reset.

### Exit and Save

- Choose `S` → State saved and application closes.

## Next Steps

Ideas for extending the app:

- Add **priority levels** (High, Medium, Low) to tasks.
- Implement **editing** of task descriptions (option `E`).
- Add **filtering** by status (completed/pending).
- Allow **file backup** by specifying export filenames.
- Enable **sorting** by date, priority, or completion status.