# UI Test Plan

This file records the command-line UI test cases for Clammy. Run the cases in the order listed. Each case starts a fresh program process.

## Test case format

### TC-NN: Descriptive name

**Aim:** State the behavior being checked.

**Input:**

```text
Enter commands here, one per line.
End interactive sessions with bye.
```

**Expected output:**

```text
Record the complete expected program output here, including separators and blank lines.
```

## Test cases

### TC-01: Add and list all task types

**Aim:** Verifies that todo, deadline, and event commands create correctly formatted tasks and that list displays
them in their insertion order.

**Input:**

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

### TC-02: Flexible input and task status changes

**Aim:** Verifies that command keywords are case-insensitive, repeated whitespace is accepted, and valid task
numbers can be marked and unmarked.

**Input:**

```text
  TODO   read book
MARK   1
unmark 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] read book
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] read book
____________________________________________________________

____________________________________________________________
OK, I've marked this task as not done yet:
[T][ ] read book
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

### TC-03: All malformed commands remain recoverable

**Aim:** Verifies that missing task fields, invalid and nonexistent task numbers, and unexpected command arguments
produce specific correction messages without terminating Clammy.

**Input:**

```text
todo
deadline submit report
event project meeting /from Monday
mark cat
mark 0
mark 1
list extra
bye extra
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
A todo must have a description.
____________________________________________________________

____________________________________________________________
A deadline must follow: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________

____________________________________________________________
An event must follow: event DESCRIPTION /from START /to END
____________________________________________________________

____________________________________________________________
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a positive task number.
____________________________________________________________

____________________________________________________________
That task number does not exist.
____________________________________________________________

____________________________________________________________
The list command does not take arguments.
____________________________________________________________

____________________________________________________________
The bye command does not take arguments.
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

### TC-04: Unknown command shows valid commands

**Aim:** Verifies that an unknown command produces a concise guide to every supported command.

**Input:**

```text
hello
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
I don't recognize that command. Valid commands are:
todo DESCRIPTION
deadline DESCRIPTION /by DATE_OR_TIME
event DESCRIPTION /from START /to END
list
mark TASK_NUMBER
unmark TASK_NUMBER
bye
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```
