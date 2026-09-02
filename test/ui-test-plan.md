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
Now you have 2 task in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 task in the list.
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
