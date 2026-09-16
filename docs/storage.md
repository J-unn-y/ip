# Saved tasks

Run Clammy from the project root. It loads `data/clammy.txt` at startup and
saves after successful `todo`, `deadline`, `event`, `mark`, and `unmark` commands.
The path is relative to the working directory and built using Java's `Path` API
so it works across operating systems. Personal saved tasks are ignored by Git.

The folder and file are created on the first save. A missing or empty file starts
an empty list. Listing tasks, invalid commands, and exiting do not write the file.

## File format

The file uses UTF-8 with one task per line. Fields are separated by actual tabs:

```text
T	1	read book
D	0	return book	Sunday
E	0	project meeting	Mon 2pm	4pm
```

The first field is the type (`T`, `D`, or `E`). The second is the status (`0` for
incomplete, `1` for complete). Remaining fields contain the description and,
where applicable, deadline or start/end text. Duplicate tasks retain their order.
Within text fields, backslashes are stored as `\\` and tabs as `\t`.
Other characters, including pipes and Unicode, are stored literally.

Blank lines and an optional UTF-8 byte-order mark at the start are accepted.
Unknown types, invalid statuses, wrong field counts, blank text fields, and
invalid escape sequences are rejected. Dates remain plain text, as in Level 5.

## File errors

If existing data cannot be read or a record is malformed, Clammy reports the
problem and stops before accepting commands. The original file stays unchanged;
correct the file or folder and restart. Malformed records include a line number.
The saved task path must be a regular file, not a directory or symbolic link.

Saving writes a temporary file in the same folder before replacing the old file.
Replacement is atomic where supported; otherwise it uses ordinary replacement.
A save error is reported after the command result: changes remain in memory,
but are not guaranteed to survive exit. Fix folder permissions or free disk space,
then change a task again to retry saving the full list. Concurrent Clammy sessions
are not coordinated, so use one session per data file.
