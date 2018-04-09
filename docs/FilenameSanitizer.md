## FilenameSanitizer

[FilenameSanitizer](../src/main/java/com/andrealigios/legendaryutils/FilenameSanitizer.java) performs the sanitization of a filename in order to make it safe to be used for a file creation on every modern File System. 

Unallowed chars are replaced with a safe token (an underscore) according to every modern File System naming convention.

**Standard Sanitization** replaces:
<ul>
<li> the `NUL` (0) character
<li> Control Codes between 1 and 31
<li> `<` (less than)
<li> `>` (greater than)
<li> `:` (colon)
<li> `"` (double quote)
<li> `/` (forward slash)
<li> `\` (backslash)
<li> `|` (vertical bar or pipe)
<li> `?` (question mark)
<li> `*` (asterisk)
<li> leading and trailing spaces
</ul>

In addition, it prepends a safe token to files which correspond to one of the Windows reserved filenames (CON, PRN, AUX, NUL, COM1, COM2, COM3, COM4, COM5, COM6, COM7, COM8, COM9, LPT1, LPT2, LPT3, LPT4, LPT5, LPT6, LPT7, LPT8, and LPT9), with or without extension.
<p>

**Safe Sanitization** also handles invalid inputs, specifically:
<ul>
<li> null file names, by generating a safe, unique filename
<li> empty or whitespace-only file names, by generating a safe, unique filename 
<li> file names too long (higher than 256 charaters, since on Windows the MAX_PATH is 260 characters and includes drive letter, colon, backslash and terminal NUL, like C:\file-256-chars-long<NUL>.
</ul><p>

**Pretty Sanitization** behaves identically to a *Safe Sanitization*, but it also removes characters which (even if allowed) could be dangerous or annoying. Specifically:
<ul>
<li> leading dots (make files semi-hidden on *NIX systems, and dangerous to be removed massively with "rm .*") 
<li> leading hyphens (make tricky to perform "rm -filename" since - is used to express command options)
<li> trailing dots (make Windows angry)
</ul><p>

The logging is performed with SLF4J, which will default to NOP (No OPeration) if no binding will be specified.

Related: [Naming Files, Paths, and Namespaces](https://msdn.microsoft.com/en-us/library/windows/desktop/aa365247(v=vs.85).aspx)

Related: [Fixing Unix/Linux/POSIX Filenames: Control Characters (such as Newline), Leading Dashes, and Other Problems](https://www.dwheeler.com/essays/fixing-unix-linux-filenames.html)
