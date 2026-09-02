# Clammy

Clammy is a command-line task manager written in Java. It keeps track of todo items, deadlines, and events during
the current program session. Tasks can also be listed, marked as completed, and marked as incomplete.

## Requirements

- JDK 25
- A terminal or IntelliJ IDEA

On macOS with SDKMAN, select the project Java version when necessary:

```bash
sdk use java 25.0.3.fx-zulu
```

Confirm that the correct compiler is active:

```bash
javac -version
```

The result should report Java 25.

## Compile and run from a terminal

Run these commands from the project root:

```bash
javac -d out src/main/java/clammy/*.java
java -cp out clammy.Clammy
```

The `-d out` option places generated `.class` files in the ignored `out` directory instead of mixing them with
the source files.

## Commands

### Add a todo

```text
todo DESCRIPTION
```

Example:

```text
todo borrow book
```

### Add a deadline

```text
deadline DESCRIPTION /by DATE_OR_TIME
```

Example:

```text
deadline return book /by Sunday
```

### Add an event

```text
event DESCRIPTION /from START /to END
```

Example:

```text
event project meeting /from Monday 2pm /to 4pm
```

### List tasks

```text
list
```

### Mark a task as completed

Task numbers are shown by the `list` command.

```text
mark TASK_NUMBER
```

Example:

```text
mark 1
```

### Mark a task as incomplete

```text
unmark TASK_NUMBER
```

Example:

```text
unmark 1
```

### Exit Clammy

```text
bye
```

## Run in IntelliJ IDEA

1. Open this project directory in IntelliJ IDEA.
2. Set the Project SDK to JDK 25 and the language level to `SDK default`.
3. Open `src/main/java/clammy/Clammy.java`.
4. Run `Clammy.main()` using the run icon beside the `main` method.

Keep Java source files under `src/main/java`. This is the standard source location expected by Java build tools.

## UI test plan

The command-line test cases and their expected output are recorded in
[`test/ui-test-plan.md`](test/ui-test-plan.md).
