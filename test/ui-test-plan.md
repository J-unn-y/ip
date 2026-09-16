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
delete TASK_NUMBER
bye
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

### TC-05: Delete all task types and renumber the list

**Aim:** Verifies deletion of middle, first, last, and only tasks; completed status in the confirmation; remaining task numbers; singular and plural counts; and case-insensitive commands with extra whitespace.

**Input:**

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
todo borrow book
mark 3
  DELETE   3
list
delete 1
list
delete 2
list
delete 1
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
Got it. I've added this task:
[T][ ] borrow book
Now you have 4 tasks in the list.
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[E][X] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[T][ ] borrow book
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[T][ ] read book
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)
2.[T][ ] borrow book
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[T][ ] borrow book
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[D][ ] return book (by: Sunday)
Now you have 0 tasks in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

### TC-06: Invalid deletions leave the list unchanged

**Aim:** Verifies empty-list deletion, missing and malformed numbers, integer overflow, nonpositive numbers, extra arguments, and out-of-range numbers. Errors preserve the list and allow later commands.

**Input:**

```text
delete 1
todo keep this task
delete
delete cat
delete 1.5
delete 1 extra
delete 2147483648
delete 0
delete -1
delete 2
delete 2147483647
list
delete 1
delete 1
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
That task number does not exist.
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] keep this task
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a positive task number.
____________________________________________________________

____________________________________________________________
Please provide a positive task number.
____________________________________________________________

____________________________________________________________
That task number does not exist.
____________________________________________________________

____________________________________________________________
That task number does not exist.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep this task
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[T][ ] keep this task
Now you have 0 tasks in the list.
____________________________________________________________

____________________________________________________________
That task number does not exist.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

### TC-07: Delete by number among duplicate descriptions

**Aim:** Verifies that deletion selects the numbered task among duplicate descriptions and that marking and adding tasks still work after deletion.

**Input:**

```text
todo read book
todo read book
mark 2
delete 2
list
mark 1
todo write notes
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
Got it. I've added this task:
[T][ ] read book
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] read book
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[T][X] read book
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] read book
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] write notes
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[T][ ] write notes
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```
