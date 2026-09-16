# UI Test Plan

This file records the command-line UI test cases for Clammy. Run the cases in the order listed. Each case starts a fresh program process.

## Persistence test setup

Run `python3 test/run-ui-tests.py` after compiling with Java 25. The runner executes
cases in plan order and stops at the first failure. Each case has an isolated
working directory; multiple sessions within a case reuse that directory to test
restarts. The real project data folder is never used.

Optional **Setup** JSON creates directories, UTF-8 files, raw bytes (`hex_files`),
symlinks, or Unix permission modes before startup. Permission cases require macOS
or Linux and a non-root account. Permissions are restored after each case.
**Expected saved data** contains exact UTF-8 file contents (including tabs), or
`absent` means that no file should exist. **Data unchanged** checks initial bytes
and directories after all sessions. Optional **Save checkpoints** check the file
after specified commands while the process is still open, before sending the next
command. Every session includes complete console output.

## Fat JAR verification

After compiling all Java sources into `out/` and running the regular UI tests,
build the executable JAR with `./gradlew shadowJar`. Run the same cases against
the packaged application with:

```bash
python3 test/run-ui-tests.py --jar build/libs/clammy-all.jar
```

This mode copies only the JAR into each otherwise empty test directory, naming it
`Clammy [release].jar` to cover filenames with spaces and brackets. It then prepares
any recorded data fixtures and launches the app from that directory using the
equivalent of `java -jar "Clammy [release].jar"`. Each restart reuses the copied JAR.
This checks the entry point, packaged classes, console behavior, and saving/loading
without access to project files through the working directory. All inputs, expected
output, and saved-data checks below remain the same. Both modes require Java 25 and
stop at the first failure.

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

**Aim:** Verifies that todo, deadline, and event commands create correctly formatted tasks and that list
displays them in their insertion order.

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

**Expected saved data:**

```text
T	0	borrow book
D	0	return book	Sunday
E	0	project meeting	Mon 2pm	4pm
```

### TC-02: Flexible input and task status changes

**Aim:** Verifies that command keywords are case-insensitive, repeated whitespace is accepted, and valid
task numbers can be marked and unmarked.

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

**Expected saved data:**

```text
T	0	read book
```

### TC-03: All malformed commands remain recoverable

**Aim:** Verifies that missing task fields, invalid and nonexistent task numbers, and unexpected command
arguments produce specific correction messages without terminating Clammy.

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

**Expected saved data:** absent

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

**Expected saved data:** absent

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

**Expected saved data:**

```text
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

**Expected saved data:**

```text
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

**Expected saved data:**

```text
T	1	read book
T	0	write notes
```

### TC-08: Save and reload every task type and status

**Aim:** Verifies automatic saving on first use, order, date fields, marking, unmarking, and three process
starts.

**Input:**

```text
todo read book
deadline return book /by Sunday
event meeting /from Mon 2pm /to 4pm
mark 1
mark 2
mark 3
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
[E][ ] meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] read book
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[D][X] return book (by: Sunday)
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[E][X] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	1	read book
D	1	return book	Sunday
E	1	meeting	Mon 2pm	4pm
```

**Input:**

```text
list
unmark 1
unmark 2
unmark 3
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: Sunday)
3.[E][X] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
OK, I've marked this task as not done yet:
[T][ ] read book
____________________________________________________________

____________________________________________________________
OK, I've marked this task as not done yet:
[D][ ] return book (by: Sunday)
____________________________________________________________

____________________________________________________________
OK, I've marked this task as not done yet:
[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	read book
D	0	return book	Sunday
E	0	meeting	Mon 2pm	4pm
```

**Input:**

```text
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
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	read book
D	0	return book	Sunday
E	0	meeting	Mon 2pm	4pm
```

### TC-09: Missing file inside an existing folder

**Aim:** Verifies first save creates the missing file.

**Setup:**

```json
{
  "directories": [
    "data"
  ]
}
```

**Input:**

```text
list
todo first task
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] first task
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	first task
```

### TC-10: Empty saved file

**Aim:** Verifies an empty file loads an empty list without rewriting it.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": ""
  }
}
```

**Input:**

```text
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
Here are the tasks in your list:
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
```

**Data unchanged:** yes

### TC-11: Blank lines BOM and Windows line endings

**Aim:** Verifies a UTF-8 BOM, blank lines, CRLF, and a final record without a newline.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "﻿\r\n\r\nT\t1\tread book\r\n   \r\nD\t0\treturn book\tSunday"
  }
}
```

**Input:**

```text
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
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-12: Round trip special characters and end of input

**Aim:** Verifies pipes, Unicode, backslashes, and tabs in descriptions and dates, including exit at end of
input.

**Input:**

```text
todo C:\notes | café	计划
deadline pay | bill /by Fri	noon
event meet | team /from Mon	2pm /to 4	pm
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] C:\notes | café	计划
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[D][ ] pay | bill (by: Fri	noon)
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[E][ ] meet | team (from: Mon	2pm to: 4	pm)
Now you have 3 tasks in the list.
____________________________________________________________

```

**Expected saved data:**

```text
T	0	C:\\notes | café\t计划
D	0	pay | bill	Fri\tnoon
E	0	meet | team	Mon\t2pm	4\tpm
```

**Input:**

```text
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
Here are the tasks in your list:
1.[T][ ] C:\notes | café	计划
2.[D][ ] pay | bill (by: Fri	noon)
3.[E][ ] meet | team (from: Mon	2pm to: 4	pm)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	C:\\notes | café\t计划
D	0	pay | bill	Fri\tnoon
E	0	meet | team	Mon\t2pm	4\tpm
```

### TC-13: Preserve duplicate tasks

**Aim:** Verifies duplicate descriptions and independent completion statuses survive saving and restarting.

**Input:**

```text
todo same
todo same
mark 2
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
[T][ ] same
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Got it. I've added this task:
[T][ ] same
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] same
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	same
T	1	same
```

**Input:**

```text
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
Here are the tasks in your list:
1.[T][ ] same
2.[T][X] same
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	same
T	1	same
```

### TC-14: Unknown task type

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nQ\t0\tbad\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-15: Invalid completion status

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nT\t2\tbad\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-16: Missing event field

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nE\t0\tbad\tMonday\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-17: Extra todo field

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nT\t0\tbad\textra\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-18: Blank description

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nT\t0\t   \n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-19: Blank deadline

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nD\t0\tbad\t\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-20: Blank event start

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nE\t0\tbad\t\tTuesday\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-21: Unknown escape

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nT\t0\tbad\\q\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-22: Incomplete escape

**Aim:** Verifies malformed data is reported with its line number and is never overwritten.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\nT\t0\tbad\\\n"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. Invalid saved task on line 2.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-23: Invalid UTF-8

**Aim:** Verifies unreadable encoding is reported without overwriting the original bytes.

**Setup:**

```json
{
  "hex_files": {
    "data/clammy.txt": "fffe00"
  }
}
```

**Input:**

```text
todo must not overwrite
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. The saved task file could not be read.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-24: Data path is a file

**Aim:** Verifies a file blocking the required folder is reported.

**Setup:**

```json
{
  "files": {
    "data": "keep this file\n"
  }
}
```

**Input:**

```text
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. The data path must be a folder.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** absent

**Data unchanged:** yes

### TC-25: Saved task path is a directory

**Aim:** Verifies a directory cannot be mistaken for a saved task file.

**Setup:**

```json
{
  "directories": [
    "data/clammy.txt"
  ]
}
```

**Input:**

```text
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. The saved task path must be a regular file.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-26: Saved task path is a symbolic link

**Aim:** Verifies a symbolic link is rejected and its target remains unchanged.

**Setup:**

```json
{
  "files": {
    "original.txt": "T\t0\tkeep me\n"
  },
  "directories": [
    "data"
  ],
  "symlinks": {
    "data/clammy.txt": "../original.txt"
  }
}
```

**Input:**

```text
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. The saved task path must be a regular file.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-27: Unreadable saved file

**Aim:** Verifies read permission failures are reported and original bytes are preserved.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\n"
  },
  "modes": {
    "data/clammy.txt": "0000"
  }
}
```

**Input:**

```text
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Could not load data/clammy.txt. The saved task file could not be read.
Existing data was left unchanged. Fix the file or folder and restart Clammy.
____________________________________________________________

```

**Expected saved data:** unchanged

**Data unchanged:** yes

### TC-28: Save failure preserves memory and previous data

**Aim:** Verifies failed writes show a warning, preserve the previous file, and keep the current session
usable.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\n"
  },
  "modes": {
    "data": "0555"
  }
}
```

**Input:**

```text
todo unsaved task
list
mark 1
unmark 1
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
[T][ ] unsaved task
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Could not save data/clammy.txt. Changes are in memory only.
Check that the data folder is writable before changing another task.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep me
2.[T][ ] unsaved task
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] keep me
____________________________________________________________

____________________________________________________________
Could not save data/clammy.txt. Changes are in memory only.
Check that the data folder is writable before changing another task.
____________________________________________________________

____________________________________________________________
OK, I've marked this task as not done yet:
[T][ ] keep me
____________________________________________________________

____________________________________________________________
Could not save data/clammy.txt. Changes are in memory only.
Check that the data folder is writable before changing another task.
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	keep me
```

**Data unchanged:** yes

### TC-29: Invalid commands never rewrite saved data

**Aim:** Verifies invalid commands and read-only operations preserve the file byte for byte.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "\nT\t0\tkeep me\n\n"
  }
}
```

**Input:**

```text
todo
mark 2
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
A todo must have a description.
____________________________________________________________

____________________________________________________________
That task number does not exist.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep me
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text

T	0	keep me

```

**Data unchanged:** yes

### TC-30: Save immediately before exit

**Aim:** Verifies that add, mark, unmark, and delete each update the file while Clammy is
still running, before the next command or bye is sent.

**Input:**

```text
todo live save
mark 1
unmark 1
delete 1
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
[T][ ] live save
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Nice! I've marked this task as done:
[T][X] live save
____________________________________________________________

____________________________________________________________
OK, I've marked this task as not done yet:
[T][ ] live save
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[T][ ] live save
Now you have 0 tasks in the list.
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
```

**Save checkpoints:**

```json
[
  {
    "after_command": 1,
    "data": "T\t0\tlive save\n"
  },
  {
    "after_command": 2,
    "data": "T\t1\tlive save\n"
  },
  {
    "after_command": 3,
    "data": "T\t0\tlive save\n"
  },
  {
    "after_command": 4,
    "data": ""
  }
]
```

### TC-31: Deletion survives restarts and an empty list

**Aim:** Verifies deletion of loaded tasks, renumbering after restart, and persistence after the last task is removed.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tread book\nD\t1\treturn book\tSunday\nE\t0\tmeeting\tMon 2pm\t4pm\n"
  }
}
```

**Input:**

```text
delete 2
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
Noted. I've removed this task:
[D][X] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	read book
E	0	meeting	Mon 2pm	4pm
```

**Input:**

```text
list
delete 1
delete 1
bye
```

**Expected output:**

```text
____________________________________________________________
Hello! I'm Clammy.
What can I do for you?
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[T][ ] read book
Now you have 1 task in the list.
____________________________________________________________

____________________________________________________________
Noted. I've removed this task:
[E][ ] meeting (from: Mon 2pm to: 4pm)
Now you have 0 tasks in the list.
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
```

**Input:**

```text
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
Here are the tasks in your list:
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
```

### TC-32: Invalid deletions never rewrite saved data

**Aim:** Verifies malformed and out-of-range deletion commands preserve the existing file byte for byte.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "\nT\t0\tkeep me\n\n"
  }
}
```

**Input:**

```text
delete
delete 0
delete 2
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
Please provide a valid task number.
____________________________________________________________

____________________________________________________________
Please provide a positive task number.
____________________________________________________________

____________________________________________________________
That task number does not exist.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
1.[T][ ] keep me
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text

T	0	keep me

```

**Data unchanged:** yes

### TC-33: Failed deletion save preserves the previous file

**Aim:** Verifies a deletion save failure shows a warning and preserves the previous file; restart restores that saved task.

**Setup:**

```json
{
  "files": {
    "data/clammy.txt": "T\t0\tkeep me\n"
  },
  "modes": {
    "data": "0555"
  }
}
```

**Input:**

```text
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
Noted. I've removed this task:
[T][ ] keep me
Now you have 0 tasks in the list.
____________________________________________________________

____________________________________________________________
Could not save data/clammy.txt. Changes are in memory only.
Check that the data folder is writable before changing another task.
____________________________________________________________

____________________________________________________________
Here are the tasks in your list:
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	keep me
```

**Input:**

```text
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
Here are the tasks in your list:
1.[T][ ] keep me
____________________________________________________________

____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________

```

**Expected saved data:**

```text
T	0	keep me
```

**Data unchanged:** yes
